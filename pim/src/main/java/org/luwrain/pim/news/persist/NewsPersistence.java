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

public final class NewsPersistence
{
    static private final Logger log = LogManager.getLogger();
    
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner;
    private final MVMap<Integer, Group> groupsMap;
    private final MVMap<Long, Article> articlesMap;
    private final MVMap<String, Long> keysMap;
    
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
	return null;
    }
    
    @Override public List<Integer> countNewInGroups(List<Group> groups)
    {
	return null;
    }
    
    @Override public int countNewInGroup(Group group)
    {
	return 0;
    }
    
    @Override public int countByUriInGroup(Group group, String uri)
    {
	return 0;
    }
	};
	    }


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

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
