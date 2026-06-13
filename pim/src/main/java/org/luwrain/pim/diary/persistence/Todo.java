// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * To-do item model corresponding to the VTODO component of the
 * iCalendar standard
 * (<a href="https://tools.ietf.org/html/rfc5545">RFC 5545</a>).
 * Each to-do has a unique {@link #id} assigned upon saving. All time
 * fields except {@link #timestamp} are stored as {@link Long} — the
 * number of milliseconds since the Unix epoch — and may be {@code null}
 * when the value is not set.
 *
 * <p>Equality comparison via {@link #equals(Object)} and
 * {@link #hashCode()} is based solely on {@link #id}.</p>
 *
 * @see Event
 * @see TodoDAO
 * @see DiaryPersistence
 */
@Data
public class Todo implements Serializable
{
    /**
     * Internal numeric identifier of the to-do item.
     * Assigned automatically when added to storage via
     * {@link TodoDAO#add(Todo)}.
     */
    private long id;

    // VTODO core identification

    /**
     * Globally unique component identifier (UID property of the
     * iCalendar standard). Must be unique within the entire calendar
     * system.
     */
    private String uid;

    /**
     * Date-time when the iCalendar representation of the to-do was
     * created (DTSTAMP property). In milliseconds since the Unix epoch.
     */
    private long timestamp;

    /**
     * Component revision number (SEQUENCE property). Starts at 0 and
     * increments on each significant change.
     */
    private int seq;

    // VTODO content

    /**
     * Short title of the to-do (SUMMARY property).
     */
    private String title;

    /**
     * Full description of the to-do (DESCRIPTION property).
     * May contain multiline text.
     */
    private String comment;

    // VTODO timing

    /**
     * Start date and time of the to-do (DTSTART property).
     * In milliseconds since the Unix epoch. May be {@code null}
     * if the start time is not set.
     */
    private Long startTimestamp;

    /**
     * Due date of the to-do (DUE property).
     * In milliseconds since the Unix epoch. May be {@code null}
     * if no due date is set.
     */
    private Long due;

    /**
     * Duration of the to-do in minutes (DURATION property).
     * May be {@code null} if the duration is not set.
     */
    private Integer durationMin;

    /**
     * Date and time when the to-do was completed (COMPLETED property).
     * In milliseconds since the Unix epoch. May be {@code null}
     * if the to-do is not yet completed.
     */
    private Long completedTimestamp;

    /**
     * Date and time when the to-do was created in the calendar system
     * (CREATED property). In milliseconds since the Unix epoch.
     * May be {@code null}.
     */
    private Long createdTimestamp;

    /**
     * Date and time of the last modification (LAST-MODIFIED property).
     * In milliseconds since the Unix epoch. May be {@code null}.
     */
    private Long modificationTimestamp;

    // VTODO progress

    /**
     * Completion percentage of the to-do (PERCENT-COMPLETE property).
     * An integer from 0 to 100. May be {@code null} if not set.
     */
    private Integer percentComplete;

    // VTODO location and geo

    /**
     * Textual description of the to-do location (LOCATION property).
     */
    private String location;

    /**
     * Geographic coordinates as a {@code "latitude;longitude"} string
     * (GEO property). Latitude and longitude are specified in degrees.
     */
    private String geo;

    // VTODO classification

    /**
     * Access classification of the to-do (CLASS property).
     * Allowed values: {@code "PUBLIC"}, {@code "PRIVATE"},
     * {@code "CONFIDENTIAL"}.
     */
    private String clazz;

    /**
     * To-do status (STATUS property). Allowed values:
     * {@code "NEEDS-ACTION"} (requires action),
     * {@code "COMPLETED"} (finished),
     * {@code "IN-PROCESS"} (in progress),
     * {@code "CANCELLED"} (cancelled).
     */
    private String status;

    /**
     * To-do priority (PRIORITY property). Value from 1 (highest)
     * to 9 (lowest); 0 indicates undefined priority.
     * {@code null} — priority is not set.
     */
    private Integer priority;

    // VTODO organizer and attendees

    /**
     * To-do organizer (ORGANIZER property). Typically specified as a
     * {@code mailto:user@example.com} URI or as a common name (CN).
     */
    private String organizer;

    /**
     * Contact information associated with the to-do (CONTACT property).
     */
    private String contact;

    // VTODO URL and resources

    /**
     * URL associated with the to-do (URL property).
     */
    private String url;

    /**
     * List of attachment references (ATTACH property). Each element is
     * an identifier or path to a linked resource.
     */
    private List<String> references;

    /**
     * List of to-do categories (CATEGORIES property).
     */
    private List<String> categories;

    /**
     * List of resources involved in the to-do (RESOURCES property).
     * For example, equipment, rooms, etc.
     */
    private List<String> resources;

    /**
     * To-do recurrence rule (RRULE property).
     * A string in RRULE format as defined by RFC 5545.
     */
    private String rrule;

    /**
     * Identifier of a related parent component (RELATED-TO property).
     * Used to build a hierarchy of to-do items.
     */
    private String relatedTo;

    /**
     * Compares two to-do items by {@link #id}.
     *
     * @param o object to compare with
     * @return {@code true} if {@code o} is a to-do with the same id
     */
    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Todo t)
	    return id == t.id;
	return false;
    }

    /**
     * Returns a hash code based on {@link #id}.
     *
     * @return hash code of the to-do
     */
    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    /**
     * Returns a string representation of the to-do — its title,
     * or an empty string if the title is not set.
     *
     * @return to-do title or an empty string
     */
    @Override public String toString()
    {
	return title != null?title:"";
    }
}
