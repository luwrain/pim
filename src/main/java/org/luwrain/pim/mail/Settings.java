
package org.luwrain.pim.mail;

import org.luwrain.core.*;

interface Settings
{
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

    static Account createAccount(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Account.class);
    }
}
