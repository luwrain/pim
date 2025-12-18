// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.obsolete;

import java.util.*;
import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
class MailMessage
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

    transient private byte[] rawMessage = new byte[0];

    public final void copyValues(MailMessage message)
    {
	this.messageId = message.messageId;
	this.subject = message.subject;
	this.from = message.from;
		this.to = message.to.clone();
	this.cc = message.cc.clone();
		this.bcc = message.bcc.clone();
		this.state = message.state;
	this.sentDate = message.sentDate;
	this.receivedDate = message.receivedDate;
	this.text = message.text;
	this.contentType = message.contentType;
		this.attachments = message.attachments.clone();
	this.rawMessage = message.rawMessage.clone();
	this.extInfo = message.extInfo;
    }

    public void save() throws PimException
    {
    }
}
