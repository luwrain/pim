
package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class MailAccountArea extends FormArea implements SectionArea
{
    final String smtpTitle = "SMTP";//FIXME:
    final String pop3Title = "POP3";//FIXME:

    private Environment environment;
    private MailStoring storing;
    private StoredMailAccount account;

    MailAccountArea(Environment environment,
		    MailStoring storing, 
		    StoredMailAccount account) throws Exception
    {
	super(new DefaultControlEnvironment(environment.getLuwrain()));
	this.storing = storing;
	this.account = account;
	this.environment = environment;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(account, "account");
	NullCheck.notNull(environment, "environment");

	//FIXME:Translation;
	final int flags = account.getFlags();
	addEdit("title", "Имя:", account.getTitle(), null, true);
	addCheckbox("enabled", "Учётная запись включена:", (flags & MailAccount.FLAG_ENABLED) > 0, null, true);
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
	addList("type", "Тип сервера:", selected,
		new FixedFormListChoosing(environment.getLuwrain(), "Выберите тип сервера почтовой учётной записи", new String[]{
			pop3Title, 
			smtpTitle,
		    }, Popup.WEAK), null, true);
	addEdit("host", "Хост:", account.getHost(), null, true);
	addEdit("port", "Порт:", "" + account.getPort(), null, true);
	addEdit("login", "Логин:", account.getLogin(), null, true);
	addEdit("passwd", "Пароль:", account.getPasswd(), null, true);
	addEdit("trusted-hosts", "Доверенные серверы самоподписанных сертификатов:", account.getTrustedHosts(), null, true);
	addCheckbox("default", "Учётная запись по умолчанию (для исходящей почты):", (flags & MailAccount.FLAG_DEFAULT) > 0, null, true);
	addCheckbox("leave-messages", "Оставлять письма на сервере:", (flags & MailAccount.FLAG_LEAVE_MESSAGES) > 0, null, true);
	addCheckbox("ssl", "Использовать SSL:", (flags & MailAccount.FLAG_SSL) > 0, null, true);
	addCheckbox("tls", "Использовать TLS:", (flags & MailAccount.FLAG_TLS) > 0, null, true);
	addEdit("subst-name", "Отправлять письма от имени:", account.getSubstName(), null, true);
	addEdit("subst-address", "Отправлять письма с адреса:", account.getSubstAddress(), null, true);
    }

    //Returns false if there are errors which the user is able to fix;
    boolean save() throws Exception
    {
	int port;
	try {
	    port = Integer.parseInt(getEnteredText("port"));
	}
	catch(NumberFormatException e)
	{
	    environment.getLuwrain().message("Введённое значение порта не является допустимым числом", Luwrain.MESSAGE_ERROR);
	    return false;
	}
	if (port <= 0)
	{
	    environment.getLuwrain().message("Введённое значение порта должно быть больше нуля", Luwrain.MESSAGE_ERROR);
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

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (event.isSpecial() && !event.isModified())
	    switch(event.getSpecial())
	    {
	    case TAB:
		environment.gotoSectionsTree();
		return true;
	    }
	return super.onKeyboardEvent(event);
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	switch(event.getCode())
	{
	case SAVE:
	    try {
		if (save())
		    environment.getLuwrain().message("Все параметры сохранены", Luwrain.MESSAGE_OK);
	    }
	    catch(Exception e)
	    {
		e.printStackTrace();
		environment.getLuwrain().message("Во время сохранения параметров произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    }
	    return true;
	case CLOSE:
	    environment.close();
	default:
	    return super.onEnvironmentEvent(event);
	}
    }

    @Override public String getAreaName()
    {
	try {
	    return "Настройка учётной записи \"" + account.getTitle() + "\"";
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return "Настройка учётной записи";
	}
    }
}