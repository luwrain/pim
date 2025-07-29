/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail.persistence.model;

import java.util.*;
import lombok.*;

@Data
public class MessageMetadata
{
    public enum State {NEW, READ, MARKED, DELETED};

    private long id;
    private State state;
    private int folderId;
    private String messageId, subject, fromAddr, content;
    private long sentTimestamp;
    private List<String> toAddr, ccAddr;
    private transient String title;
}
