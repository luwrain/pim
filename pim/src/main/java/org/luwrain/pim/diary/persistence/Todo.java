// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
public class Todo implements Serializable
{
    private long id;

    // VTODO core identification
    private String uid;
    private long dtStamp;
    private int seq;

    // VTODO content
    private String title;       // corresponds to SUMMARY
    private String comment;     // corresponds to DESCRIPTION

    // VTODO timing
    private Long dtStart;       // corresponds to DTSTART, nullable
    private Long due;           // corresponds to DUE, nullable
    private Integer durationMin; // corresponds to DURATION (in minutes), nullable
    private Long completed;     // corresponds to COMPLETED, nullable
    private Long created;       // corresponds to CREATED
    private Long lastModified;  // corresponds to LAST-MODIFIED

    // VTODO progress
    private Integer percentComplete; // corresponds to PERCENT-COMPLETE (0-100)

    // VTODO location and geo
    private String location;    // corresponds to LOCATION
    private String geo;         // corresponds to GEO ("lat;lon")

    // VTODO classification
    private String clazz;       // corresponds to CLASS (PUBLIC/PRIVATE/CONFIDENTIAL)
    private String status;      // corresponds to STATUS (NEEDS-ACTION/COMPLETED/IN-PROCESS/CANCELLED)
    private Integer priority;   // corresponds to PRIORITY (0=undefined, 1=highest, 9=lowest)

    // VTODO organizer and attendees
    private String organizer;   // corresponds to ORGANIZER (mailto URI or CN)
    private String contact;     // corresponds to CONTACT

    // VTODO URL and resources
    private String url;         // corresponds to URL
    private List<String> references; // corresponds to ATTACH (references to attachments)
    private List<String> categories; // corresponds to CATEGORIES
    private List<String> resources;  // corresponds to RESOURCES
    private String rrule;       // corresponds to RRULE (recurrence rule)
    private String relatedTo;   // corresponds to RELATED-TO

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Todo t)
	    return id == t.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
