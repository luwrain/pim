// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;
import java.util.function.*;

/**
 * Data access object for {@link Article} instances.
 *
 * <p>This interface defines the operations for loading, creating, updating
 * and deleting news articles, as well as for querying various aggregate
 * information about the articles stored in a {@link Group}.</p>
 */
public interface ArticleDAO
{
    /**
     * Loads all articles belonging to the specified group.
     *
     * @param group the group whose articles should be loaded
     * @return the list of articles belonging to the group
     * @throws NullPointerException if {@code group} is {@code null}
     */
    List<Article> load(Group group);

    /**
     * Loads the articles belonging to the specified group that match the
     * given filter.
     *
     * @param group the group whose articles should be loaded
     * @param filter the predicate used to select articles; if {@code null},
     *               all articles of the group are returned
     * @return the list of matching articles
     * @throws NullPointerException if {@code group} is {@code null}
     */
    List<Article> load(Group group, Predicate<Article> filter);

    /**
     * Updates the stored representation of the specified article.
     *
     * @param article the article to update
     * @throws NullPointerException if {@code article} is {@code null}
     * @throws IllegalArgumentException if the article has a negative identifier
     */
    void update(Article article);

    /**
     * Adds a new article to the specified group.
     *
     * @param group the group the article should be added to
     * @param article the article to add
     * @return the identifier assigned to the newly added article
     * @throws NullPointerException if {@code group} or {@code article} is {@code null}
     */
    long add(Group group, Article article);

    /**
     * Deletes the specified article from the specified group.
     *
     * @param group the group the article belongs to
     * @param article the article to delete
     * @throws NullPointerException if {@code article} is {@code null}
     * @throws IllegalArgumentException if the article has a negative identifier
     */
    void delete(Group group, Article article);

    /**
     * Loads the set of URIs of all articles belonging to the specified group.
     *
     * @param group the group whose article URIs should be loaded
     * @return the set of article URIs
     * @throws NullPointerException if {@code group} is {@code null}
     */
    Set<String> loadUrisInGroup(Group group);

    /**
     * Counts the articles with the {@link Article.Status#MARKED} status in
     * each of the specified groups.
     *
     * @param groups the groups to count marked articles for
     * @return a list where the element at index {@code i} is the number of
     *         marked articles in the group at index {@code i} of the input
     * @throws NullPointerException if {@code groups} is {@code null}
     */
    List<Integer> countMarkedInGroups(List<Group> groups);

    /**
     * Counts the articles with the {@link Article.Status#NEW} status in each
     * of the specified groups.
     *
     * @param groups the groups to count new articles for
     * @return a list where the element at index {@code i} is the number of
     *         new articles in the group at index {@code i} of the input
     * @throws NullPointerException if {@code groups} is {@code null}
     */
    List<Integer> countNewInGroups(List<Group> groups);

    /**
     * Counts the articles with the {@link Article.Status#NEW} status in the
     * specified group.
     *
     * @param group the group to count new articles for
     * @return the number of new articles in the group
     * @throws NullPointerException if {@code group} is {@code null}
     */
    int countNewInGroup(Group group);

    /**
     * Counts the articles with the specified URI in the specified group.
     *
     * @param group the group to search in
     * @param uri the URI to count articles for
     * @return the number of articles with the given URI in the group
     * @throws NullPointerException if {@code group} or {@code uri} is {@code null}
     */
    int countByUriInGroup(Group group, String uri);
}
