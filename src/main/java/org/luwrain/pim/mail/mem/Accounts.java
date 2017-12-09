/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.mem;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Accounts implements MailAccounts
{
    @Override public StoredMailAccount[] load() throws PimException
    {
	return null;
    }

	@Override public StoredMailAccount loadById(long id) throws PimException
    {
	return null;
    }

    @Override public void save(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
    }

    @Override public void delete(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
    }

    @Override public String getUniRef(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	return "";
    }

    @Override public StoredMailAccount loadByUniRef(String uniRef)
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	return null;
    }

    @Override public int getId(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	return 0;
    }
}
