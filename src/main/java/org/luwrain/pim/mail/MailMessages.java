
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailMessages
{
    void save(StoredMailFolder folder, MailMessage message) throws PimException;
    StoredMailMessage[] load(StoredMailFolder folder) throws PimException;
    void moveToFolder(StoredMailMessage message, StoredMailFolder folder) throws PimException;
    void delete(StoredMailMessage message) throws PimException;
}
