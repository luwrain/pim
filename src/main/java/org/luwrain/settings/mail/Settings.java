
package org.luwrain.settings.mail;

import org.luwrain.core.*;

public interface Settings
{
    static final String MAIL_FOLDERS_PATH = "/org/luwrain/pim/mail/";

    public interface MailFolders 
    {
	String getFolderPending(String defValue);
	void setFolderPending(String value);
    }

    static public MailFolders createMailFolders(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, MAIL_FOLDERS_PATH, MailFolders.class);
    }
}
