
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailFolders
{
    StoredMailFolder getRoot() throws PimException;
    StoredMailFolder[] load(StoredMailFolder folder) throws PimException;
    StoredMailFolder loadByUniRef(String uniRef) throws PimException;
    StoredMailFolder loadById(int id) throws PimException;
    void save(StoredMailFolder parentFolder, MailFolder newFolder) throws PimException;
    String getUniRef(StoredMailFolder folder) throws PimException;
    int getId(StoredMailFolder folder) throws PimException;
}
