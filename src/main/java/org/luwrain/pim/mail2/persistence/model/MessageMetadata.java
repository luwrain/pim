/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail2.persistence.model;

import java.util.*;
import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table (name = "message")
public class MessageMetadata
{
    public enum State {NEW, READ, MARKED, DELETED};

            @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private State state;
    private int folderId;

    private String
	messageId, subject, fromAddr;

        @Column(columnDefinition="TEXT")
    private String content;

    private long sentTimestamp;

    @ElementCollection
    @CollectionTable(name="to_addr", joinColumns=@JoinColumn(name="message_id"))
    private List<String> toAddr;

        @ElementCollection
    @CollectionTable(name="cc_addr", joinColumns=@JoinColumn(name="message_id"))
    private List<String> ccAddr;

    

    private transient String title;
    /*
    private String[]
	to = new String[0],
	cc = new String[0],
	bcc = new String[0],
	attachments = new String[0];

private java.util.Date
    sentDate = new java.util.Date();
    private java.util.Date
	receivedDate = new java.util.Date();
    */
}
