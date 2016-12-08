
package org.luwrain.pim.mail;

import org.luwrain.core.*;

interface Settings
{
    static final String STORING_PATH = "/org/luwrain/pim/mail/storing";
    static final String FOLDERS_PATH = "/org/luwrain/pim/mail/folders";
    static final String RULES_PATH = "/org/luwrain/pim/mail/rules";


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

    interface Account
    {
	String getType(String defValue);
	String getTitle(String defValue);
	String getHost(String defValue);
	int getPort(int defValue);
	String getLogin(String defValue);
	String getPasswd(String defValue);
	String getTrustedHosts(String defValue);
	String getSubstName(String defValue);
	String getSubstAddress(String defValue);
	boolean getEnabled(boolean defValue);
	boolean getSsl(boolean defValue);
	boolean getTls(boolean defValue);
	boolean getDefault(boolean defValue);
	boolean getLeaveMessages(boolean defValue);
	void setType(String value);
	void setTitle(String value);
	void setHost(String value);
	void setPort(int value);
	void setLogin(String value);
	void setPasswd(String value);
	void setTrustedHosts(String value);
	void setSubstName(String value);
	void setSubstAddress(String value);
	void setEnabled(boolean value);
	void setSsl(boolean value);
	void setTls(boolean value);
	void setDefault(boolean value);
	void setLeaveMessages(boolean value);
    }

interface Folder
{
    String getTitle(String defValue);
    int getOrderIndex(int defValue);
    int getParentId(int defValue);
	void setTitle(String value);
    void setOrderIndex(int defValue);
    void setParentId(int defValue);
}

interface Rule
{
    String getAction(String defValue);
    String getHeaderRegex(String defValue);
    String getDestFolderUniref(String defValue);
    void setAction(String value);
    void setHeaderRegex(String value);
    void setDestFolderUniref(String value);
}

    static Account createAccount(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Account.class);
    }

    static Folder createFolder(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Folder.class);
    }

    static Rule createRule(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Rule.class);
    }


    static Storing createStoring(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, STORING_PATH, Storing.class);
    }
}
