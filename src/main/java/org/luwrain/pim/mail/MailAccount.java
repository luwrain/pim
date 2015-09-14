
package org.luwrain.pim.mail;

public class MailAccount
{
    static public final int POP3 = 0;
    static public final int SMTP = 1;

    static public final int FLAG_SSL = 1;
    static public final int FLAG_TLS = 2;

    public int type;
    public String title = "";
    public String host = "";
    public int port;
    public String login = "";
    public String passwd = "";
    public int flags = 0;
    public String substName = "";
    public String substAddress = "";
}
