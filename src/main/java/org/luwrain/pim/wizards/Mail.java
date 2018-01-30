
package org.luwrain.pim.wizards;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

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
					      if (!text.trim().isEmpty())
						  return true;
					      luwrain.message("Указанный адрес не может быть пустым", Luwrain.MessageType.ERROR);
					      return false;
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
	
		
		
    return true;
}
}
