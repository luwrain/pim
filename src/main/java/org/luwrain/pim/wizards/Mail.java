
package org.luwrain.pim.wizards;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.util.*;

public final class Mail
{
    private final Luwrain luwrain;
    private final MailStoring storing;
    private final String title;

    public Mail(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, true);
	this.title = "Подключение к почтовому серверу";//FIXME:
    }

    public boolean start()
    {
	if (storing == null)
	{
	    luwrain.message("Отсутствует подключение к хранилищу электронной почты", Luwrain.MessageType.ERROR);
	    return false;
	}
	final String name = Popups.simple(luwrain, title, "Ваше имя:", "",
					  (text)->{
					      if (!text.trim().isEmpty())
						  return true;
					      luwrain.message("Указанное имя не может быть пустым", Luwrain.MessageType.ERROR);
					      return false;
					  });//FIXME:
	if (name == null)
	    return false;
	final String addr = Popups.simple(luwrain, title, "Адрес электронной почты:", "",
					  (text)->{
					      if (text.trim().isEmpty())
					      {
						  luwrain.message("Указанный адрес не может быть пустым", Luwrain.MessageType.ERROR);
						  return false;
					      }
					      if (!text.matches(".*@.*\\..*") || text.trim().matches(".* .*"))
					      {
						  luwrain.message("Введённое значение не является адресом электронной почты", Luwrain.MessageType.ERROR);//FIXME:
						  return false;
					      }
					      return true;
					  });//FIXME:
	if (addr == null)
	    return false;
	final String passwd = Popups.simple(luwrain, title, "Пароль:", "",
					    (text)->{
						if (!text.trim().isEmpty())
						    return true;
						luwrain.message("Пароль не может быть пустым", Luwrain.MessageType.ERROR);
						return false;
					    });//FIXME:
	if (passwd == null)
	    return false;
	final MailAccountsPresets presets = new MailAccountsPresets();
	final Map<String, MailAccountsPresets.Smtp> services;
	try {
	    services = presets.load();
	}
	catch(IOException e)
	{
	    luwrain.crash(e);
	    return false;
	}
	MailAccountsPresets.Smtp service = null;
	for(Map.Entry<String, MailAccountsPresets.Smtp> e: services.entrySet())
	{
	    final String[] suffixes = e.getValue().suffixes;
	    for (String s: suffixes)
		if (addr.trim().toLowerCase().endsWith(s.toLowerCase().trim()))
		{
		    service = e.getValue();
		    break;
		}
	    if (service != null)
		break;
	}
	if (service == null)
	{
	    luwrain.message("В системе не зарегистрировано службы, обслуживающей ваш адрес электронной почты", Luwrain.MessageType.ERROR);
	    return false;
	}
	luwrain.message(service.title);
	try {
	final MailAccount account = new MailAccount();
	account.setType(MailAccount.Type.SMTP);
	account.setTitle(addr.trim() + " (исходящая почта через " + service.title + ")");
	account.setHost(service.host);
	account.setPort(service.port);
	account.setLogin(addr.trim());
	account.setPasswd(passwd);
	//trusted hosts
	final Set<MailAccount.Flags> flags = EnumSet.noneOf(MailAccount.Flags.class);
	flags.add(MailAccount.Flags.ENABLED);
	flags.add(MailAccount.Flags.DEFAULT);
	if (service.ssl)
	    flags.add(MailAccount.Flags.SSL);
	if (service.tls)
	    flags.add(MailAccount.Flags.TLS);
	account.setFlags(flags);
	account.setSubstName(name.trim());
	account.setSubstAddress(addr.trim());
	    storing.getAccounts().save(account);
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return false;
	}
	return true;
    }
}
