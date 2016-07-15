
package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Account extends FormArea implements SectionArea
{
    final String smtpTitle = "SMTP";//FIXME:
    final String pop3Title = "POP3";//FIXME:

    private ControlPanel controlPanel;
    private Luwrain luwrain;
    private MailStoring storing;
    private StoredMailAccount account;
    private Strings strings;

    Account(ControlPanel controlPanel, Strings strings,
	    MailStoring storing, StoredMailAccount account) throws PimException
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()), strings.accountFormName());
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
	final int flags = account.getFlags();
	addEdit("title", strings.accountFormTitle(), account.getTitle(), null, true);
	addCheckbox("enabled", strings.accountFormEnabled(), (flags & MailAccount.FLAG_ENABLED) > 0, null, true);
	String selected = null;
	switch(account.getType())
	{
	case MailAccount.SMTP:
	    selected = smtpTitle;
	    break;
	case MailAccount.POP3:
	    selected = pop3Title;
	    break;
	}
	addList("type", strings.accountFormServerType(), selected,
		new FixedFormListChoosing(luwrain, strings.accountFormTypeSelectionPopupName(), new String[]{pop3Title, smtpTitle}, Popups.DEFAULT_POPUP_FLAGS), null, true);
	addEdit("host", strings.accountFormHost(), account.getHost(), null, true);
	addEdit("port", strings.accountFormPort(), "" + account.getPort());
	addEdit("login", strings.accountFormLogin(), account.getLogin());
	addEdit("passwd", strings.accountFormPasswd(), account.getPasswd());
	addEdit("trusted-hosts", strings.accountFormTrustedHosts(), account.getTrustedHosts(), null, true);
	addCheckbox("default", strings.accountFormDefaultOutgoing(), (flags & MailAccount.FLAG_DEFAULT) > 0, null, true);
	addCheckbox("leave-messages", strings.accountFormLeaveMessageOnServer(), (flags & MailAccount.FLAG_LEAVE_MESSAGES) > 0, null, true);
	addCheckbox("ssl", strings.accountFormUseSsl(), (flags & MailAccount.FLAG_SSL) > 0, null, true);
	addCheckbox("tls", strings.accountFormUseTls(), (flags & MailAccount.FLAG_TLS) > 0, null, true);
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
	    luwrain.message(strings.portNotNumber(), Luwrain.MESSAGE_ERROR);
	    return false;
	}
	if (port <= 0)
	{
	    luwrain.message(strings.portMustBeGreaterZero(), Luwrain.MESSAGE_ERROR);
	    return false;
	}
	try {
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
		account.setType(MailAccount.POP3);
	    if (selected.equals(smtpTitle))
		account.setType(MailAccount.SMTP);
	    int flags = 0;
	    if (getCheckboxState("ssl"))
		flags |= MailAccount.FLAG_SSL;
	    if (getCheckboxState("tls"))
		flags |= MailAccount.FLAG_TLS;
	    if (getCheckboxState("default"))
		flags |= MailAccount.FLAG_DEFAULT;
	    if (getCheckboxState("enabled"))
		flags |= MailAccount.FLAG_ENABLED;
	    if (getCheckboxState("leave-messages"))
		flags |= MailAccount.FLAG_LEAVE_MESSAGES;
	    account.setFlags(flags);
	    return true;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return false;
	}
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onKeyboardEvent(event))
	    return true;
	return super.onKeyboardEvent(event);
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onEnvironmentEvent(event))
	    return true;
	return super.onEnvironmentEvent(event);
    }

    static Account create(ControlPanel controlPanel, MailStoring storing,
			  long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(storing, "storing");
	final Luwrain luwrain = controlPanel.getCoreInterface();
	final Strings strings = (Strings)luwrain.i18n().getStrings(Strings.NAME);
	try {
	    return new Account(controlPanel, strings, storing, storing.loadAccountById(id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
    }
}
