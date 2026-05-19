// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.mail.accounts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;
import org.luwrain.pim.mail.persistence.*;
//import org.luwrain.pim.mail.persistence.dao.*;

public final class Area extends FormArea implements SectionArea
{
    static private final int
	LEN_LIMIT = 256;

    static private final String
	TITLE = "title",
	HOST = "host",
	PORT = "port",
	LOGIN = "login",
	PASSWD = "passwd",
	DEFAULT = "default",
	ENABLED = "enabled";

    final ControlPanel controlPanel;
    final Luwrain luwrain;
    final AccountDAO dao;
    final Account account;
    final org.luwrain.settings.mail.Strings strings;

    Area(ControlPanel controlPanel, org.luwrain.settings.mail.Strings strings, AccountDAO dao, Account account)
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), strings.accountFormName(), LEN_LIMIT);
	this.dao = dao;
	this.strings = strings;
	this.account = account;
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm()
    {
	//	final Set<MailAccount.Flags> flags = account.getFlags();
	addStatic(strings.accountFormServerType() + account.getType().toString());
	addEdit(TITLE, strings.accountFormTitle(), account.getName());
	addCheckbox(ENABLED, strings.accountFormEnabled(), account.isEnabled());
	addEdit(HOST, strings.accountFormHost(), account.getHost(), null, true);
	addEdit(PORT, strings.accountFormPort(), "" + account.getPort());
	addEdit(LOGIN, strings.accountFormLogin(), account.getLogin());
	addEdit(PASSWD, strings.accountFormPasswd(), account.getPasswd());
	switch(account.getType())
	{
	case SMTP:
	    addCheckbox(DEFAULT, strings.accountFormDefaultOutgoing(), account.isDefaultAccount(), null, true);
	    addCheckbox("tls", strings.accountFormUseTls(), account.isTls(), null, true);
	    addEdit("subst-name", strings.accountForMessagesAuthorName(), account.getSubstName());
	    addEdit("subst-address", strings.accountFormMessagesAuthorAddress(), account.getSubstAddress(), null, true);
	    break;
	case POP3:
	    addEdit("trusted-hosts", strings.accountFormTrustedHosts(), account.getTrustedHosts(), null, true);
	    addCheckbox("leave-messages", strings.accountFormLeaveMessageOnServer(), account.isLeaveMessages(), null, true);
	    addCheckbox("ssl", strings.accountFormUseSsl(), account.isSsl(), null, true);
	    break;
	}
    }

    @Override public boolean saveSectionData()
    {
	final int port;
	try {
	    port = Integer.parseInt(getEnteredText(PORT));
	}
	catch(NumberFormatException e)
	{
	    luwrain.message(strings.portNotNumber(), Luwrain.MessageType.ERROR);
	    return false;
	}
	if (port <= 0)
	{
	    luwrain.message(strings.portMustBeGreaterZero(), Luwrain.MessageType.ERROR);
	    return false;
	}
	account.setName(getEnteredText(TITLE));
	account.setLogin(getEnteredText(LOGIN));
	account.setPasswd(getEnteredText(PASSWD));
	account.setHost(getEnteredText(HOST));
	account.setPort(port);
//	final Set<MailAccount.Flags> flags = EnumSet.noneOf(MailAccount.Flags.class);
	switch(account.getType())
	{
	case SMTP:
	    account.setSubstName(getEnteredText("subst-name"));
	    account.setSubstAddress(getEnteredText("subst-address"));
account.setTls(getCheckboxState("tls"));
break;
	case POP3:
	    account.setTrustedHosts(getEnteredText("trusted-hosts"));
	account.setSsl(getCheckboxState("ssl"));
account.setLeaveMessages(getCheckboxState("leave-messages"));
	    break;
	}
account.setDefaultAccount(getCheckboxState(DEFAULT));
account.setEnabled(getCheckboxState(ENABLED));
	dao.update(account);
	return true;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	if (controlPanel.onInputEvent(this, event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(this, event))
	    return true;
	return super.onSystemEvent(event);
    }
}
