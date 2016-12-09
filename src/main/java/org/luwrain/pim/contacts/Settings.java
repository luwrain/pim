
package org.luwrain.pim.contacts;

import org.luwrain.core.*;

interface Settings
{
    static final String STORING_PATH = "/org/luwrain/pim/contacts/storing";
    static final String FOLDERS_PATH = "/org/luwrain/pim/contacts/folders";

interface Folder
{
    String getTitle(String defValue);
    int getOrderIndex(int defValue);
    int getParentId(int defValue );
    void setTitle(String value);
    void setOrderIndex(int value);
    void setParentId(int value);
}

    interface Storing
    {
	String getType(String defValue);
	String getDriver(String defValue);
	String getUrl(String defValue);
	String getLogin(String defValue);
	String getPasswd(String defValue);
	boolean getSharedConnection(boolean defValue);
	String getInitProc(String defValue);
	void setType(String value);
	void setDriver(String value);
	void setUrl(String value);
	void setLogin(String value);
	void setPasswd(String value);
	void setSharedConnection(boolean value);
	void setInitProc(String value);
    }

    static Storing createStoring(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, STORING_PATH, Storing.class);
    }

    static Folder createFolder(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notEmpty(path, "path");
	return RegistryProxy.create(registry, path, Folder.class);
    }
}
