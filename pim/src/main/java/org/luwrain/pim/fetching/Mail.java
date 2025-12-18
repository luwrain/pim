// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public class Mail extends Base
{
    //    private MailStoring storing;

    public Mail(Luwrain luwrain, Control control, Strings strings)
    {
	super(control, strings);
    }

    void work(boolean incoming, boolean outgoing) throws InterruptedException
    {
	/*
	if (!init())
	    return;
	control.checkInterrupted();
	if (outgoing)
	    sendMessages();
	control.checkInterrupted();
	*/
    }


    private boolean init() throws InterruptedException
    {
	/*
	storing = org.luwrain.pim.Connections.getMailStoring(control.luwrain(), false);
	if (storing == null)
	{
message("Отсутствует расширение для работы с электронной почтой");
	    return false;
	}
	if (!initBasicFolders())
	    return false;
	return true;
	*/
	return false;
    }

    private boolean initBasicFolders() throws InterruptedException
    {
	/*
	final org.luwrain.pim.Settings.MailFolders sett = org.luwrain.pim.Settings.createMailFolders(registry);
	final String pendingUniRef =sett.getFolderPending("");
	final String sentUniRef = sett.getFolderSent("");
	if (pendingUniRef.trim().isEmpty() ||
	    sentUniRef.trim().isEmpty())
	{
message("Ошибка конфигурации электронной почты: не указаны базовые группы для хранения (исходящие, отправленные и т. д.)");//FIXME:
	    return false;
	}
	try {
	    pending = storing.getFolders().loadByUniRef(pendingUniRef);
	    sent = storing.getFolders().loadByUniRef(sentUniRef);
	}
	catch (PimException e)
	{
	    e.printStackTrace();
	    control.message("Не удалось подготовить почтовые группы для доставки сообщений");
	    return false;
	}
	if (pending == null || sent == null)
	{
	    control.message("Не удалось подготовить почтовые группы, доставка сообщений отменена");
	    return false;
	}
	return true;
	*/
	return false;
   }

}
