/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

//LWR_API 1.0

package org.luwrain.pim.mail;

import java.util.*;
import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class MailMessage
{
    //    public enum State {NEW, READ, MARKED, DELETED};
    //    private State state = State.NEW;

    private String
	messageId = "",
	subject = "",
	from = "",
	text = "",
	contentType = "",
	extInfo = "";

    //    public List<String> toList = new ArrayList<>();

    private String[]
	to = new String[0],
	cc = new String[0],
	bcc = new String[0],
	attachments = new String[0];

    /*

    public String[] getTo() { return toList.toArray(new String[toList.size()]);}
    public void setTo(String[] value) {toList = Arrays.asList(value);}

    public String[] getCc() {return new String[0];}
    public void setCc(String[] value) {}

        public String[] getBcc() {return new String[0];}
    public void setBcc(String[] value) {}


    public String[] getAttachments() {return new String[0];}
    public void setAttachments(String[] value) {}
    */


    
    

    transient private java.util.Date
	sentDate = new java.util.Date(),
	receivedDate = new java.util.Date();

    transient private byte[] rawMessage = new byte[0];

    public final void copyValues(MailMessage message)
    {
	this.messageId = message.messageId;
	this.subject = message.subject;
	this.from = message.from;
	//	this.to = message.to.clone();
	//	this.cc = message.cc.clone();
	//	this.bcc = message.bcc.clone();
	//	this.state = message.state;
	this.sentDate = message.sentDate;
	this.receivedDate = message.receivedDate;
	this.text = message.text;
	this.contentType = message.contentType;
	//	this.attachments = message.attachments.clone();
	this.rawMessage = message.rawMessage.clone();
	this.extInfo = message.extInfo;
    }

    public void save() throws PimException
    {
    }
}
