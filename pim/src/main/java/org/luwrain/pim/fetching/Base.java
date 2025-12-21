// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.fetching;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.*;

public class Base
{
    static final String LOG_COMPONENT = "pim";

    protected final Control control;
    protected final Luwrain luwrain;
    //    protected final Registry registry;
    protected final Strings strings;

    public Base(Control control, Strings strings)
    {
	NullCheck.notNull(control, "control");
	NullCheck.notNull(strings, "strings");
	this.control = control;
	this.strings = strings;
	this.luwrain = control.luwrain();
	//FIXME:newreg 	this.registry = luwrain.getRegistry();
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

    protected MailConnections.Params createMailServerParams(Account account)
    {
	final MailConnections.Params params = new MailConnections.Params();
	params.doAuth = !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.isSsl();
	params.tls = account.isTls();
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
		params.extProps.put( "mail.pop3.ssl.trust", account.getTrustedHosts());
	return params;
    }
}
