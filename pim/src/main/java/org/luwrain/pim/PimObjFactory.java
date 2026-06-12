// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.pim.news.*;
import org.luwrain.pim.news.persist.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.*;
import org.luwrain.pim.diary.*;
import org.luwrain.pim.diary.persistence.*;

import static java.util.Objects.*;

public final class PimObjFactory implements ObjFactory, AutoCloseable
{
    static private final Logger log = LogManager.getLogger();
    
    private final Path baseDir;
    private NewsFactory newsFactory = null;
    private MailFactory mailFactory = null;
    private DiaryFactory diaryFactory = null;

    PimObjFactory(Path baseDir)
    {
	this.baseDir = requireNonNull(baseDir, "baseDir can't be null");
    }

    @Override public String getExtObjName()
    {
	return "luwrain.pim.factory";
    }

    @Override public Object newObject(String name)
    {
	requireNonNull(name, "name can't be null");
	try {
	    if (name.equals(NewsPersistence.class.getName()))
	    {
		if (newsFactory == null)
		    newsFactory = new NewsFactory(baseDir.resolve("news"));
		return newsFactory.newInstance();
	    }
	    if (name.equals(MailPersistence.class.getName()))
	    {
		if (mailFactory == null)
		    mailFactory = new MailFactory(baseDir.resolve("mail"));
		return mailFactory.newInstance();
	    }
	    if (name.equals(DiaryPersistence.class.getName()))
	    {
		if (diaryFactory == null)
		    diaryFactory = new DiaryFactory(baseDir.resolve("diary"));
		return diaryFactory.newInstance();
	    }
	    return null;
	}
	catch(IOException ex)
	{
	    log.error("Unable to create an object " + name, ex);
	}
	return null;
    }

    @Override public void close()
    {
	if (newsFactory != null)
	    newsFactory.close();
	newsFactory = null;
	if (mailFactory != null)
	    mailFactory.close();
	mailFactory = null;
	if (diaryFactory != null)
	    diaryFactory.close();
	diaryFactory = null;
    }
}
