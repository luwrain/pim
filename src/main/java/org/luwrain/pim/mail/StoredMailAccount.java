
package org.luwrain.pim.mail;

public interface StoredMailAccount
{
    int getType() throws Exception;
    void setType(int value) throws Exception;
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    String getHost() throws Exception;
    void setHost(String value) throws Exception;
    int getPort() throws Exception;
    void setPort(int value) throws Exception;
    String getLogin() throws Exception;
    void setLogin(String value) throws Exception;
    String getPasswd() throws Exception;
    void setPasswd(String value) throws Exception;
    int getFlags() throws Exception;
    void setFlags(int value) throws Exception;
    String getSubstName() throws Exception;
    void setSubstName(String value) throws Exception;
    String getSubstAddress() throws Exception;
    void setSubstAddress(String value) throws Exception;
}
