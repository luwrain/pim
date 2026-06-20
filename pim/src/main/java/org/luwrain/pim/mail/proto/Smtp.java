// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.proto;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.persistence.*;

import static org.luwrain.core.NullCheck.*;

public final class Smtp
{
    static private final String
	LOG_COMPONENT = "smtp";

    private final MailConnections con;

    public Smtp(Account account)
    {
	notNull(account, "account");
	Log.debug(LOG_COMPONENT, "connecting to the SMTP server: " + account.getHost() + ":" + account.getPort());
	this.con = new MailConnections(createMailServerParams(account), false);
	Log.debug(LOG_COMPONENT, "connection established");
    }

    /**
     * Sends a raw RFC822 message via the SMTP transport.
     *
     * @param rawMessage The raw RFC822 message bytes
     * @throws PimException If sending fails
     */
    public void send(byte[] rawMessage)
    {
	notNull(rawMessage, "rawMessage");
	con.send(rawMessage);
    }

    static MailConnections.Params createMailServerParams(Account account)
    {
	final var params = new MailConnections.Params();
	params.doAuth = account.getLogin() != null && !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.isSsl();
	params.tls = account.isTls();
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
	return params;
    }
}
