// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.pim.mail.persistence.*;
import org.luwrain.app.base.*;
import org.luwrain.app.mail.*;

import static java.util.Objects.*;

final class Pop3AccountLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	HOST = "host",
	PASSWD = "passwd",
	PORT = "port",
	SSL = "ssl",
	LEAVE_MESSAGES = "leave-messages";

    private final App app;
    final FormArea formArea;
    
    public Pop3AccountLayout(App app, Account account, ActionHandler closing)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	this.formArea = new FormArea(getControlContext(), s.pop3AccountAreaName(account.getName())) ;
	formArea.addEdit(NAME, s.accountPropertiesName(), requireNonNullElse(account.getName(), "").trim());
	formArea.addEdit(HOST, s.accountPropertiesHost(), requireNonNullElse(account.getHost(), "").trim());
	formArea.addEdit(PORT, s.accountPropertiesPort(), String.valueOf(account.getPort()));
	formArea.addPasswd(PASSWD, s.accountPropertiesPasswd(), requireNonNullElse(account.getPasswd(), ""), null, true);
	formArea.addCheckbox(SSL, s.accountPropertiesSsl(), account.isSsl());
					formArea.addCheckbox(LEAVE_MESSAGES, s.accountPropertiesLeaveMessages(), account.isLeaveMessages());
	setCloseHandler(closing);
	setOkHandler(() -> {
		if (!save(account))
		    return true;
		return closing.onAction();
	    });
	setAreaLayout(formArea, actions());
    }

    private boolean save(Account account)
    {
	final String
	name = formArea.getEnteredText(NAME).trim(),
	host = formArea.getEnteredText(HOST).trim(),
	passwd = formArea.getEnteredText(PASSWD),
	port = formArea.getEnteredText(PORT).trim();
	if (name.isEmpty())
	{
	    getLuwrain().message(app.getStrings().accountPropertiesNameCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return false;
	}
	final int portValue;
	try {
	    portValue = Integer.parseInt(port);
	}
	catch(NumberFormatException ex)
	{
	    getLuwrain().message(app.getStrings().accountPropertiesInvalidPortValue(), Luwrain.MessageType.ERROR );
	    return false;
	}
	if (portValue <= 0 || portValue > 65536)
	{
	    	    getLuwrain().message(app.getStrings().accountPropertiesInvalidPortValue(), Luwrain.MessageType.ERROR );
		    return false;
	}
	account.setName(name);
	account.setHost(host);
	account.setPort(portValue);
	account.setPasswd(passwd);
	account.setSsl(formArea.getCheckboxState(SSL));
		account.setLeaveMessages(formArea.getCheckboxState(LEAVE_MESSAGES));
	app.getData().accountDAO.update(account);
	return true;
    }
}
