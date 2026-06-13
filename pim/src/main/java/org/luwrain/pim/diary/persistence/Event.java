// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * Calendar event model corresponding to the VEVENT component of the
 * iCalendar standard
 * (<a href="https://tools.ietf.org/html/rfc5545">RFC 5545</a>).
 * Each event has a unique {@link #id} assigned upon saving. All time
 * fields are stored as {@code long} — the number of milliseconds since
 * the Unix epoch.
 *
 * <p>Equality comparison via {@link #equals(Object)} and
 * {@link #hashCode()} is based solely on {@link #id}.</p>
 *
 * @see Todo
 * @see EventDAO
 * @see DiaryPersistence
 */
@Data
public class Event implements Serializable
{
    /**
     * Internal numeric identifier of the event.
     * Assigned automatically when added to storage via
     * {@link EventDAO#add(Event)}.
     */
    private long id;

    // VEVENT core identification

    /**
     * Globally unique component identifier (UID property of the
     * iCalendar standard). Must be unique within the entire calendar
     * system.
     */
    private String uid;

    /**
     * Date-time when the iCalendar representation of the event was
     * created (DTSTAMP property). In milliseconds since the Unix epoch.
     */
    private long dtStamp;

    /**
     * Component revision number (SEQUENCE property). Starts at 0 and
     * increments on each significant change that requires notifying
     * attendees.
     */
    private int seq;

    // VEVENT timing

    /**
     * Short title of the event (SUMMARY property).
     */
    private String title;

    /**
     * Full description of the event (DESCRIPTION property).
     * May contain multiline text.
     */
    private String comment;

    /**
     * Start date and time of the event (DTSTART property).
     * In milliseconds since the Unix epoch.
     */
    private long dateTime;

    /**
     * Duration of the event in minutes (DURATION property).
     */
    private int durationMin;

    /**
     * End date and time of the event (DTEND property).
     * In milliseconds since the Unix epoch. May be {@code null}
     * if {@link #durationMin} is used instead.
     */
    private Long dtEnd;

    /**
     * Date and time when the event was created in the calendar system
     * (CREATED property). In milliseconds since the Unix epoch.
     * May be {@code null}.
     */
    private Long created;

    /**
     * Date and time of the last modification (LAST-MODIFIED property).
     * In milliseconds since the Unix epoch. May be {@code null}.
     */
    private Long lastModified;

    // VEVENT location and geo

    /**
     * Textual description of the event venue (LOCATION property).
     */
    private String location;

    /**
     * Geographic coordinates as a {@code "latitude;longitude"} string
     * (GEO property). Latitude and longitude are specified in degrees.
     */
    private String geo;

    // VEVENT classification

    /**
     * Access classification of the event (CLASS property).
     * Allowed values: {@code "PUBLIC"}, {@code "PRIVATE"},
     * {@code "CONFIDENTIAL"}.
     */
    private String clazz;

    /**
     * Time transparency indicator for free/busy searches (TRANSP property).
     * {@code "OPAQUE"} means the event blocks time,
     * {@code "TRANSPARENT"} means the time remains available.
     */
    private String transp;

    /**
     * Event status (STATUS property). Allowed values:
     * {@code "TENTATIVE"}, {@code "CONFIRMED"}, {@code "CANCELLED"}.
     */
    private String status;

    /**
     * Event priority (PRIORITY property). Value from 1 (highest)
     * to 9 (lowest); 0 indicates undefined priority.
     * {@code null} — priority is not set.
     */
    private Integer priority;

    // VEVENT organizer and attendees

    /**
     * Event organizer (ORGANIZER property). Typically specified as a
     * {@code mailto:user@example.com} URI or as a common name (CN).
     */
    private String organizer;

    /**
     * Contact information associated with the event (CONTACT property).
     */
    private String contact;

    // VEVENT URL and resources

    /**
     * URL associated with the event (URL property).
     */
    private String url;

    /**
     * List of attachment references (ATTACH property). Each element is
     * an identifier or path to a linked resource.
     */
    private List<String> references;

    /**
     * List of event categories (CATEGORIES property).
     */
    private List<String> categories;

    /**
     * Event recurrence rule (RRULE property).
     * A string in RRULE format as defined by RFC 5545.
     */
    private String rrule;

    /**
     * Compares two events by {@link #id}.
     *
     * @param o object to compare with
     * @return {@code true} if {@code o} is an event with the same id
     */
    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Event e)
	    return id == e.id;
	return false;
    }

    /**
     * Returns a hash code based on {@link #id}.
     *
     * @return hash code of the event
     */
    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    /**
     * Returns a string representation of the event — its title,
     * or an empty string if the title is not set.
     *
     * @return event title or an empty string
     */
    @Override public String toString()
    {
	return title != null?title:"";
    }
}
