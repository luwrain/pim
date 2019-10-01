/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.pim.*;

public interface MailMessages
{
    void save(MailFolder folder, MailMessage message) throws PimException;
    MailMessage[] load(MailFolder folder) throws PimException;
    MailMessage[] loadNoDeleted(MailFolder folder) throws PimException;
    void moveToFolder(MailMessage message, MailFolder folder) throws PimException;
    void delete(MailMessage message) throws PimException;
    byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders) throws PimException;
    MailMessage fromByteArray(byte[] bytes) throws PimException;
}
