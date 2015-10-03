/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;

public interface StoredMailMessage
{
    int getState();
    void setState(int state) throws SQLException;
    String getMessageId() throws Exception; 
    void setMessageId(String messageId) throws Exception;
    String getSubject() throws Exception;
    void setSubject(String subject) throws Exception;
    String getFrom() throws Exception;
    void setFrom(String from) throws Exception;
    String[] getTo() throws Exception;
    void setTo(String[] to) throws Exception;
    String[] getCc() throws Exception;
    void setCc(String[] cc) throws Exception;
    String[] getBcc() throws Exception;
    void setBcc(String[] bcc) throws Exception;
    String[] getAttachments() throws Exception;
    void setAttachments(String[] value) throws Exception;
    Date getSentDate() throws Exception;
    void setSentDate(Date sentDate) throws Exception;
    Date getReceivedDate() throws Exception;
    void setReceivedDate(Date receivedDate) throws Exception;
    String getBaseContent() throws Exception;
    void setBaseContent(String baseContent) throws Exception;
    String getMimeContentType() throws Exception;
    void setMimeContentType(String mimeContentType) throws Exception;
    byte[] getRawMail() throws SQLException;
    void setRawMail(byte[] rawMail) throws SQLException;
}
