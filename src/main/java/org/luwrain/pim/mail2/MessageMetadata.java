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

package org.luwrain.pim.mail2;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
public class MessageMetadata
{
    public enum State {NEW, READ, MARKED, DELETED};
    private State state = State.NEW;

    private String
	messageId = "",
	subject = "",
	from = "",
	text = "",
	contentType = "",
	extInfo = "";

    private String[]
	to = new String[0],
	cc = new String[0],
	bcc = new String[0],
	attachments = new String[0];

private java.util.Date
    sentDate = new java.util.Date();
    private java.util.Date
	receivedDate = new java.util.Date();
}
