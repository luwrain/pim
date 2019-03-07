
//LWR_API 1.0

package org.luwrain.pim.fetching;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.MailAccount;
import org.luwrain.pim.mail.StoredMailAccount;

public class Base
{
    static final String LOG_COMPONENT = "pim";

    protected final Control control;
    protected final Luwrain luwrain;
    protected final Registry registry;
    protected final Strings strings;

    public Base(Control control, Strings strings)
    {
	NullCheck.notNull(control, "control");
	NullCheck.notNull(strings, "strings");
	this.control = control;
	this.strings = strings;
	this.luwrain = control.luwrain();
	this.registry = luwrain.getRegistry();
    }

    protected void message(String text)
    {
	NullCheck.notNull(text, "text");
	control.message(text);
    }

    protected void checkInterrupted() throws InterruptedException
    {
	control.checkInterrupted();
    }

    protected void crash(Exception e)
    {
	NullCheck.notNull(e, "e");
	luwrain.crash(e);
    }

    protected MailServerConversations.Params createMailServerParams(StoredMailAccount account) throws PimException
    {
	final MailServerConversations.Params params = new MailServerConversations.Params();
	params.doAuth = !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.getFlags().contains(MailAccount.Flags.SSL);
	params.tls = account.getFlags().contains(MailAccount.Flags.TLS);
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
		params.extProps.put( "mail.pop3.ssl.trust", account.getTrustedHosts());
	return params;
    }
}
