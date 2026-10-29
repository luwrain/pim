// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

/**
 * Persistent storage for news groups and articles.
 *
 * <p>This class provides access to the underlying H2 MVStore maps that hold
 * {@link Group} and {@link Article} instances, as well as the map used to
 * generate unique identifiers. All data access is performed through the
 * {@link #getGroupDAO()} and {@link #getArticleDAO()} methods, which return
 * data access objects whose operations are executed on the configured
 * {@link ExecQueues} runner to preserve thread safety.</p>
 */
public final class NewsPersistence
{
    static private final Logger log = LogManager.getLogger();

    /** The execution queues used to run data access operations. */
    final ExecQueues queues;

    /** The priority used when scheduling data access operations. */
    private Priority priority = Priority.MEDIUM;

    /** The runner that executes data access operations on the queues. */
    private Runner runner;

    /** The map storing groups, keyed by group identifier. */
    private final MVMap<Integer, Group> groupsMap;

    /** The map storing articles, keyed by article identifier. */
    private final MVMap<Long, Article> articlesMap;

    /** The map storing the last assigned identifiers, keyed by class name. */
    private final MVMap<String, Long> keysMap;

    /**
     * Creates a new persistence instance backed by the specified maps.
     *
     * @param queues the execution queues used to run data access operations
     * @param groupsMap the map storing groups
     * @param articlesMap the map storing articles
     * @param keysMap the map storing the last assigned identifiers
     * @throws NullPointerException if any argument is {@code null}
     */
    public NewsPersistence(ExecQueues queues,
			   MVMap<Integer, Group> groupsMap,
			   MVMap<Long, Article> articlesMap,
			   MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.groupsMap = requireNonNull(groupsMap, "groupsMap can't be null");
	this.articlesMap = requireNonNull(articlesMap, "articlesMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    /**
     * Returns the data access object for {@link Group} instances.
     *
     * @return the group data access object
     */
    public GroupDAO getGroupDAO()
    {
	return new GroupDAO(){
	    	    @Override public List<Group> load()
	    {
		return runner.run(() -> groupsMap.entrySet().stream()
						    .map( e -> e.getValue() )
						    .toList());
	    }

	    @Override public int add(Group group)
	    {
		requireNonNull(group, "group can't be null");
		return runner.run(() -> {
			    final int newId = getNewKey(Group.class).intValue();
			    group.setId(newId);
			    		log.trace("Adding " + group);
			    groupsMap.put(Integer.valueOf(newId), group);
			    return Integer.valueOf(newId);
		}).intValue();
	    }

	    @Override public void delete(Group group)
	    {
				requireNonNull(group, "group can't be null");
		if (group.getId() < 0)
		    throw new IllegalArgumentException("A group can't have negative ID");
					    		log.trace("Removing " + group);
		runner.run(new FutureTask<Object>( () -> groupsMap.remove(Integer.valueOf(group.getId())) , null));
	    }


	    @Override public void update(Group group)
	    {
		requireNonNull(group, "group can't be null");
		if (group.getId() < 0)
		    throw new IllegalArgumentException("A group can't have negative ID");
					    		log.trace("Updating " + group);
		runner.run(new FutureTask<Object>( () -> groupsMap.put(Integer.valueOf(group.getId()), group) , null));
	    }
	};
    }

    /**
     * Returns the data access object for {@link Article} instances.
     *
     * @return the article data access object
     */
    public ArticleDAO getArticleDAO()
    {
	return new ArticleDAO()
	{
    @Override public List<Article> load(Group group)
    {
	requireNonNull(group, "group");
			return runner.run(() -> articlesMap.entrySet().stream()
					  .filter(e -> e.getValue().getGroupId() == group.getId())
						    .map( e -> e.getValue() )
						    .toList());
    }

	        @Override public List<Article> load(Group group, Predicate<Article> filter)
	    {
			requireNonNull(group, "group");
			if (filter == null)
			    return load(group);
			return runner.run(() -> articlesMap.entrySet().stream()
					  .filter(e -> e.getValue().getGroupId() == group.getId())
					  .filter(e -> filter.test(e.getValue()))
						    .map( e -> e.getValue() )
						    .toList());
	    }

    @Override public void update(Article article)
    {
			requireNonNull(article, "article can't be null");
		if (article.getId() < 0)
		    throw new IllegalArgumentException("An article can't have negative ID");
					    		log.trace("Updating " + article);
		runner.run(new FutureTask<Object>( () -> articlesMap.put(Long.valueOf(article.getId()), article) , null));
    }
    
    @Override public long add(Group group, Article article)
    {
			requireNonNull(group, "group can't be null");
			requireNonNull(article, "article");
		return runner.run(() -> {
			    final long newId = getNewKey(Article.class).intValue();
			    article.setId(newId);
			    article.setGroupId(group.getId());
			    articlesMap.put(Long.valueOf(newId), article);
			    return Long.valueOf(newId);
		}).intValue();
    }
    
    @Override public void delete(Group group, Article article)
    {
					requireNonNull(article, "article can't be null");
		if (article.getId() < 0)
		    throw new IllegalArgumentException("An article can't have negative ID");
					    		log.trace("Removing " + article);
		runner.run(new FutureTask<Object>( () -> articlesMap.remove(Long.valueOf(article.getId())) , null));

    }
    
        @Override public Set<String> loadUrisInGroup(Group group)
	{
	    return new HashSet<>(runner.run(() -> articlesMap.entrySet().parallelStream()
	.map(e -> e.getValue().getUri())
				     .toList()));
	}
	
    @Override public List<Integer> countMarkedInGroups(List<Group> groups)
    {
	requireNonNull(groups, "groups can't be null");
	return runner.run(() -> {
	    final var counts = new HashMap<Integer, Integer>();
	    for (final var e : articlesMap.entrySet())
	    {
		final var a = e.getValue();
		if (a.getStatus() != Article.Status.MARKED)
		    continue;
		counts.merge(Integer.valueOf(a.getGroupId()), Integer.valueOf(1), Integer::sum);
	    }
	    final var res = new ArrayList<Integer>(groups.size());
	    for (final var g : groups)
		res.add(counts.getOrDefault(Integer.valueOf(g.getId()), Integer.valueOf(0)));
	    return res;
	});
    }
    
    @Override public List<Integer> countNewInGroups(List<Group> groups)
    {
	requireNonNull(groups, "groups can't be null");
	return runner.run(() -> {
	    final var counts = new HashMap<Integer, Integer>();
	    for (final var e : articlesMap.entrySet())
	    {
		final var a = e.getValue();
		if (a.getStatus() != Article.Status.NEW)
		    continue;
		counts.merge(Integer.valueOf(a.getGroupId()), Integer.valueOf(1), Integer::sum);
	    }
	    final var res = new ArrayList<Integer>(groups.size());
	    for (final var g : groups)
		res.add(counts.getOrDefault(Integer.valueOf(g.getId()), Integer.valueOf(0)));
	    return res;
	});
    }
    
    @Override public int countNewInGroup(Group group)
    {
	requireNonNull(group, "group can't be null");
	return runner.run(() -> {
	    int count = 0;
	    for (final var e : articlesMap.entrySet())
	    {
		final var a = e.getValue();
		if (a.getGroupId() == group.getId() && a.getStatus() == Article.Status.NEW)
		    count++;
	    }
	    return Integer.valueOf(count);
	}).intValue();
    }
    
    @Override public int countByUriInGroup(Group group, String uri)
    {
	requireNonNull(group, "group can't be null");
	requireNonNull(uri, "uri can't be null");
	return runner.run(() -> {
	    int count = 0;
	    for (final var e : articlesMap.entrySet())
	    {
		final var a = e.getValue();
		if (a.getGroupId() == group.getId() && uri.equals(a.getUri()))
		    count++;
	    }
	    return Integer.valueOf(count);
	}).intValue();
    }
	};
	    }

    /**
     * Generates and returns the next unique identifier for the given class.
     *
     * @param c the class for which an identifier is requested
     * @return the newly assigned identifier
     */
    Long getNewKey(Class c)
    {
	final var res = keysMap.get(c.getName());
	if (res == null)
	{
	    keysMap.put(c.getName(), Long.valueOf(0));
	    return Long.valueOf(0);
	}
	final var newVal = Long.valueOf(res.longValue() + 1);
	keysMap.put(c.getName(), newVal);
	return newVal;
    }

    /**
     * Sets the priority used when scheduling data access operations.
     *
     * @param priority the new priority
     * @throws NullPointerException if {@code priority} is {@code null}
     */
    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
