/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
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

import java.util.function.*;

import org.luwrain.pim.*;
import org.luwrain.pim.mail2.persistence.model.*;

public interface MailMessages
{
    void save(Folder folder, MailMessage message);
    MailMessage[] load(Folder folder);
    MailMessage[] load(Folder folder, Predicate<MailMessage> cond);
    //    MailMessage[] loadNoDeleted(MailFolder folder);
    void moveToFolder(MailMessage message, Folder folder);
    void update(MailMessage message);
    void delete(MailMessage message);
    //    byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders);
    //    MailMessage fromByteArray(byte[] bytes);
}
