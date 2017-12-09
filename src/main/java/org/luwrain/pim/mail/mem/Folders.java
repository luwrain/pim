
package org.luwrain.pim.mail.mem;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folders implements MailFolders
{
    	    @Override public int getId(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return 0;
    }

    @Override public StoredMailFolder loadById(int id)
    {
	return null;
    }

    @Override public void save(StoredMailFolder parentFolder, MailFolder newFolder)
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(parentFolder, "parentFolder");
    }

    @Override public StoredMailFolder getRoot()
    {
	return null;
    }

    @Override public StoredMailFolder[] load(StoredMailFolder folder)
    {
	return null;
    }

    @Override public String getUniRef(StoredMailFolder folder)
    {
	return null;
    }

    @Override public StoredMailFolder loadByUniRef(String uniRef)
    {
	return null;
    }
}
