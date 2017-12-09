
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailAccounts
{
    int getId(StoredMailAccount account) throws PimException;
    StoredMailAccount[] load() throws PimException;
    String getUniRef(StoredMailAccount account) throws PimException;
    StoredMailAccount loadById(long id) throws PimException;
    StoredMailAccount loadByUniRef(String uniRef) throws PimException;
    void save(MailAccount account) throws PimException;
    void delete(StoredMailAccount account) throws PimException;
}
