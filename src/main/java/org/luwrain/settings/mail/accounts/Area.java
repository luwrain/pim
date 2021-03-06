/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.mail.accounts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;

public class Area extends FormArea implements SectionArea
{
    final String smtpTitle = "SMTP";//FIXME:
    final String pop3Title = "POP3";//FIXME:

    private ControlPanel controlPanel;
    private Luwrain luwrain;
    private MailStoring storing;
    private MailAccount account;
    private Strings strings;

    Area(ControlPanel controlPanel, Strings strings,
	    MailStoring storing, MailAccount account) throws PimException
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), strings.accountFormName());
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(account, "account");
	this.storing = storing;
	this.strings = strings;
	this.account = account;
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm() throws PimException
    {
	final Set<MailAccount.Flags> flags = account.getFlags();
	addEdit("title", strings.accountFormTitle(), account.getTitle(), null, true);
	addCheckbox("enabled", strings.accountFormEnabled(), flags.contains(MailAccount.Flags.ENABLED), null, true);
	String selected = null;
	switch(account.getType())
	{
	case SMTP:
	    selected = smtpTitle;
	    break;
	case POP3:
	    selected = pop3Title;
	    break;
	}
	addList("type", strings.accountFormServerType(), selected,
		new FormUtils.FixedListChoosing(luwrain, strings.accountFormTypeSelectionPopupName(), new String[]{pop3Title, smtpTitle}, Popups.DEFAULT_POPUP_FLAGS), null, true);
	addEdit("host", strings.accountFormHost(), account.getHost(), null, true);
	addEdit("port", strings.accountFormPort(), "" + account.getPort());
	addEdit("login", strings.accountFormLogin(), account.getLogin());
	addEdit("passwd", strings.accountFormPasswd(), account.getPasswd());
	addEdit("trusted-hosts", strings.accountFormTrustedHosts(), account.getTrustedHosts(), null, true);
	addCheckbox("default", strings.accountFormDefaultOutgoing(), flags.contains(MailAccount.Flags.DEFAULT), null, true);
	addCheckbox("leave-messages", strings.accountFormLeaveMessageOnServer(), flags.contains(MailAccount.Flags.LEAVE_MESSAGES), null, true);
	addCheckbox("ssl", strings.accountFormUseSsl(), flags.contains(MailAccount.Flags.SSL), null, true);
	addCheckbox("tls", strings.accountFormUseTls(), flags.contains(MailAccount.Flags.TLS), null, true);
	addEdit("subst-name", strings.accountForMessagesAuthorName(), account.getSubstName());
	addEdit("subst-address", strings.accountFormMessagesAuthorAddress(), account.getSubstAddress(), null, true);
    }

    @Override public boolean saveSectionData()
    {
	int port;
	try {
	    port = Integer.parseInt(getEnteredText("port"));
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
	    account.setTitle(getEnteredText("title"));
	    account.setLogin(getEnteredText("login"));
	    account.setPasswd(getEnteredText("passwd"));
	    account.setTrustedHosts(getEnteredText("trusted-hosts"));
	    account.setHost(getEnteredText("host"));
	    account.setPort(port);
	    account.setSubstName(getEnteredText("subst-name"));
	    account.setSubstAddress(getEnteredText("subst-address"));
	    final Object selected = getSelectedListItem("type");
	    if (selected.equals(pop3Title))
		account.setType(MailAccount.Type.POP3);
	    if (selected.equals(smtpTitle))
		account.setType(MailAccount.Type.SMTP);
	    final Set<MailAccount.Flags> flags = EnumSet.noneOf(MailAccount.Flags.class);
	    if (getCheckboxState("ssl"))
		flags.add(MailAccount.Flags.SSL);
	    if (getCheckboxState("tls"))
		flags.add(MailAccount.Flags.TLS);
	    if (getCheckboxState("default"))
		flags.add(MailAccount.Flags.DEFAULT);
	    if (getCheckboxState("enabled"))
		flags.add(MailAccount.Flags.ENABLED);
	    if (getCheckboxState("leave-messages"))
		flags.add(MailAccount.Flags.LEAVE_MESSAGES);
	    account.setFlags(flags);
	    return true;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }
}
