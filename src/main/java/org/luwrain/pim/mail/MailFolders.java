
//LWR_API 1.0

package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailFolders
{
    MailFolder getRoot() throws PimException;
    MailFolder[] load(MailFolder folder) throws PimException;
    MailFolder loadByUniRef(String uniRef) throws PimException;
    MailFolder loadById(int id) throws PimException;
    MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException;
    String getUniRef(MailFolder folder) throws PimException;
    int getId(MailFolder folder) throws PimException;
    MailFolder findFirstByProperty(String propName, String propValue) throws PimException;
}
