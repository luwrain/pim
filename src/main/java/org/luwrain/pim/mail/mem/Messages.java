
package org.luwrain.pim.mail.mem;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Messages implements MailMessages
{
    @Override public void save(StoredMailFolder folder, MailMessage message)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public StoredMailMessage[] load(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return null;
    }

    @Override public void moveToFolder(StoredMailMessage message, StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public void delete(StoredMailMessage message)
    {
	NullCheck.notNull(message, "message");
    }
}
