
package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public interface ContactsFolders
{
    StoredContactsFolder getRoot() throws PimException;
    StoredContactsFolder[] load(StoredContactsFolder folder) throws PimException;
    void save(StoredContactsFolder addTo, ContactsFolder folder) throws PimException;
    void delete(StoredContactsFolder folder) throws PimException;
}
