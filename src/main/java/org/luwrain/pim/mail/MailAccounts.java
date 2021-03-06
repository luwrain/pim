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

import org.luwrain.pim.*;

public interface MailAccounts
{
    int getId(MailAccount account) throws PimException;
    MailAccount[] load() throws PimException;
    String getUniRef(MailAccount account) throws PimException;
    MailAccount loadById(int id) throws PimException;
    MailAccount loadByUniRef(String uniRef) throws PimException;
    MailAccount save(MailAccount account) throws PimException;
    void delete(MailAccount account) throws PimException;
    MailAccount getDefault(MailAccount.Type type) throws PimException;
    void sendDirectly(MailAccount account, MailMessage message) throws PimException;
}
