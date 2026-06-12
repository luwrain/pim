// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import java.time.*;
import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Event
{
    enum Type { MEETING, CALL, REMINDER, TASK, NOTE, OTHER }

    private long id;
    private Type type;
    private String title;
    private String descr;
    private LocalDateTime dateTime;
    private LocalDateTime endDateTime;
    private String location;
    private List<String> participants;
    private boolean completed;
    private String notes;

    @Override public String toString()
    {
	final var sb = new StringBuilder();
	sb.append(requireNonNullElse(title, ""));
	return sb.toString();
    }

    static private String requireNonNullElse(String s, String def)
    {
	return s != null ? s : def;
    }
}
