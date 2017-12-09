
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
