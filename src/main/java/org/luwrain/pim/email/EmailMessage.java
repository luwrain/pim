/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.email;

import java.util.*;

public class EmailMessage
{
    public String messageId = "";
	public String subject = "";
    public String from = "";
    public String[] to = new String[0];
    public String[] cc = new String[0];
    public String[] bcc = new String[0];
    public int state = 0;
    public Date sentDate = new Date();
    public Date receivedDate = new Date();
    public String baseContent = "";
    public String mimeContentType = "";
    public String[] attachments = new String[0];
    public byte[] rawEmail = new byte[0];
}
