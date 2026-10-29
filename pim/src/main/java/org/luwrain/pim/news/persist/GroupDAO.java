// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;

/**
 * Data access object for {@link Group} instances.
 *
 * <p>This interface defines the operations for loading, creating, updating
 * and deleting news groups.</p>
 */
public interface GroupDAO
{
    /**
     * Adds a new group to the storage.
     *
     * @param group the group to add
     * @return the identifier assigned to the newly added group
     * @throws NullPointerException if {@code group} is {@code null}
     */
    int add(Group group);

    /**
     * Deletes the specified group from the storage.
     *
     * @param group the group to delete
     * @throws NullPointerException if {@code group} is {@code null}
     * @throws IllegalArgumentException if the group has a negative identifier
     */
    void delete(Group group);

    /**
     * Loads all groups from the storage.
     *
     * @return the list of all stored groups
     */
    List<Group> load();

    /**
     * Updates the stored representation of the specified group.
     *
     * @param group the group to update
     * @throws NullPointerException if {@code group} is {@code null}
     * @throws IllegalArgumentException if the group has a negative identifier
     */
    void update(Group group);
}
