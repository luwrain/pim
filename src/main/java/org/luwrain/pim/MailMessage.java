/*
   Copyright 2012-2014 Michael Pozhidaev <msp@altlinux.org>

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

package org.luwrain.pim;

public class MailMessage
{
    public static final int READ = 0;
    public static final int NEW = 1;
    public static final int MARKED = 2;

    public int state = READ;
    public String fromAddr = "";
    public String[] fromAddrs = new String[0];
    public String toAddr = "";
    public String[] toAddrs = new String[0];
    public String subject = "";
    public java.util.Date date = new java.util.Date();
    public String rawMsg = "";
    public String contentText = "";
    public String extInfo = "";
    public String[] attachments = new String[0];
}
