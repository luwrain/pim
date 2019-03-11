/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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
import java.io.*;

import org.luwrain.pim.*;

public interface StoredMailMessage
{
    MailMessage.State getState() throws PimException;
    void setState(MailMessage.State state) throws PimException;
    String getMessageId() throws PimException; 
    void setMessageId(String messageId) throws PimException;
    String getSubject() throws PimException;
    void setSubject(String subject) throws PimException;
    String getFrom() throws PimException;
    void setFrom(String from) throws PimException;
    String[] getTo() throws PimException;
    void setTo(String[] to) throws PimException;
    String[] getCc() throws PimException;
    void setCc(String[] cc) throws PimException;
    String[] getBcc() throws PimException;
    void setBcc(String[] bcc) throws PimException;
    String[] getAttachments() throws PimException;
    void setAttachments(String[] value) throws PimException;
    Date getSentDate() throws PimException;
    void setSentDate(Date sentDate) throws PimException;
    Date getReceivedDate() throws PimException;
    void setReceivedDate(Date receivedDate) throws PimException;
    String getText() throws PimException;
    void setText(String baseContent) throws PimException;
    String getMimeContentType() throws PimException;
    void setMimeContentType(String mimeContentType) throws PimException;
    byte[] getRawMessage() throws PimException;
    void setRawMessage(byte[] raw) throws PimException;
    String getExtInfo() throws PimException;
    void setExtInfo(String value) throws PimException;
}
