/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;
//import org.luwrain.util.*;
import org.luwrain.pim.*;

class StoredMailAccountRegistry extends MailAccount implements StoredMailAccount
{
    private final org.luwrain.pim.RegistryKeys registryKeys = new org.luwrain.pim.RegistryKeys();
private Registry registry;
    private Settings.Account settings;
    private int id;

    StoredMailAccountRegistry(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.settings = Settings.createAccount(registry, Registry.join(registryKeys.mailAccounts(), "" + id));
    }

    @Override public long getId()
    {
	return id;
    }

    @Override public Type getType() throws PimException
    {
	return type;
    }

    @Override public void setType(Type value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setType(getTypeStr(value));
	type = value;
    }

    @Override public String getTitle() throws PimException
    {
	return title;
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setTitle(value);
	title = value;
    }

    @Override public String getHost() throws PimException
    {
	return host;
    }

    @Override public void setHost(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setHost(value);
	host = value;
    }

    @Override public int getPort() throws PimException
    {
	return port;
    }

    @Override public void setPort(int value) throws PimException
    {
	settings.setPort(value);
	port = value;
    }

    @Override public String getLogin() throws PimException
    {
	return login;
    }

    @Override public void setLogin(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setLogin(value);
	login = value;
    }

    @Override public String getPasswd() throws PimException
    {
	return passwd;
    }

    @Override public void setPasswd(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setPasswd(value);
	passwd = value;
    }

    @Override public String getTrustedHosts() throws PimException
    {
	return trustedHosts;
    }

    @Override public void setTrustedHosts(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setTrustedHosts(value);
	trustedHosts = value;
    }

    @Override public Set<Flags> getFlags() throws PimException
    {
	return flags;
    } 

    @Override public void setFlags(Set<Flags> value) throws PimException
    {
	NullCheck.notNull(value, "value");
	final boolean enabled = value.contains(Flags.ENABLED);
	final boolean ssl =  value.contains(Flags.SSL);
	final boolean tls =  value.contains(Flags.TLS);
	final boolean def =  value.contains(Flags.DEFAULT);
	final boolean leaveMessages =  value.contains(Flags.LEAVE_MESSAGES);
	settings.setEnabled(enabled);
	settings.setSsl(ssl);
	settings.setTls(tls);
	settings.setDefault(def);
	settings.setLeaveMessages(leaveMessages);
	flags = value;
    }

    @Override public String getSubstName() throws PimException
    {
	return substName;
    }

    @Override public void setSubstName(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setSubstName(value);
	substName = value;
    }

    @Override public String getSubstAddress() throws PimException
    {
	return substAddress;
    }

    @Override public void setSubstAddress(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	settings.setSubstAddress(value);
	substAddress = value;
    }

    boolean load()
    {
	final String typeStr = settings.getType("").trim().toLowerCase();
	title = settings.getTitle("");
	host = settings.getHost("").trim();
	port = settings.getPort(port);
	login = settings.getLogin("");
	passwd = settings.getPasswd("");
	trustedHosts = settings.getTrustedHosts("");
	substName = settings.getSubstName("");
	substAddress = settings.getSubstAddress("");
	final LinkedList<Flags> f = new LinkedList<Flags>();
	if (settings.getEnabled(true))
	    f.add(Flags.ENABLED);
	if (settings.getSsl(false))
	    f.add(Flags.SSL);
	if (settings.getTls(false))
	    f.add(Flags.TLS);
	if (settings.getDefault(true))
	    f.add(Flags.DEFAULT);
	if (settings.getLeaveMessages(true))
	    f.add(Flags.LEAVE_MESSAGES);
	flags = EnumSet.copyOf(f);
	switch(typeStr)
	{
	case "pop3":
	    type = Type.POP3;
	    break;
	case "smtp":
	    type = Type.SMTP;
	    break;
	default:
	    return false;
	}
	return true;
    }

    /*
    private String getPath()
    {
	return Registry.join(registryKeys.mailAccounts(), "" + id);
    }
    */

    static String getTypeStr(Type type)
    {
	NullCheck.notNull(type, "type");
	switch(type)
	{
	case POP3:
	    return "pop3";
	case SMTP:
	    return "smtp";
	default:
	    return "";
	}
    }

    /*
    void updateError(String param) throws PimException
    {
	throw new PimException("Unable to update in the registry " + getPath() + "/" + param);
    }
    */
}
