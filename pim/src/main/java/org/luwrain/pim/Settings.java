
package org.luwrain.pim;

import org.luwrain.core.*;

public interface Settings
{
    static public final String MAIL_PATH = "/org/luwrain/pim/mail";

    public interface MailFolders
    {
	String getFolderPending(String defValue);
	String getFolderSent(String defValue);
    }

    static public MailFolders createMailFolders(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, MAIL_PATH, MailFolders.class);
    }
}
