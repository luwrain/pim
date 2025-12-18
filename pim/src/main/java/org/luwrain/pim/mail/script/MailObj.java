// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.script;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import org.luwrain.pim.mail.persistence.*;
import org.luwrain.pim.mail.persistence.dao.*;

import static java.util.Objects.*;
import static org.luwrain.core.NullCheck.*;
import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;

public final class MailObj
{
    	    static final Session session = Session.getDefaultInstance(new Properties(), null);
    static final Logger log = LogManager.getLogger();

    final FolderDAO folderDAO;
    final MessageDAO messageDAO;

    public MailObj(Luwrain luwrain)
    {
	requireNonNull(luwrain, "luwrain can't be null");
	final var persist = luwrain.createInstance(MailPersistence.class);
	this.folderDAO = persist.getFolderDAO();
	this.messageDAO = persist.getMessageDAO();
    }

    @HostAccess.Export
    public final ProxyExecutable getFolders = (ProxyExecutable)this::getFoldersImpl;
    private Object getFoldersImpl(Value[] args) { return new FoldersObj(this); };

        @HostAccess.Export
    public final ProxyExecutable getAddressPersonalName = (ProxyExecutable)this::getAddressPersonalNameImpl;
    private Object getAddressPersonalNameImpl(Value[] args)
    {
	if (!notNullAndLen(args, 1) || !args[0].isString() || args[0].asString().trim().isEmpty())
	    throw new IllegalArgumentException("Mail.getAddressPersonalName() takes exactly one non-empty string argument");
		try {
		    final var inetAddr = new javax.mail.internet.InternetAddress(args[0].asString().trim(), false);
	    final String personal = inetAddr.getPersonal();
	    return personal != null?personal.trim():"";
	}
	catch (javax.mail.internet.AddressException ex)
	{
	    throw new PimException(ex);
	}
    }

            @HostAccess.Export
    public final ProxyExecutable getAddressWithoutPersonalName = (ProxyExecutable)this::getAddressWithoutPersonalNameImpl;
    private Object getAddressWithoutPersonalNameImpl(Value[] args)
    {
	if (!notNullAndLen(args, 1) || !args[0].isString())
	    throw new IllegalArgumentException("Mail.getAddressWithoutPersonalName() takes exactly one string argument");
		try {
		    final var inetAddr = new javax.mail.internet.InternetAddress(args[0].asString().trim(), false);
	    final String personal = inetAddr.getAddress();
	    return personal != null?personal.trim():"";
	}
	catch (javax.mail.internet.AddressException ex)
	{
	    throw new PimException(ex);
	}
    }

}
