/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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

//LWR_API 1.0

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.pim.PimException;

/**
 * Provides access to the mail account stored in PIM storage. Using the
 * instance of this interface always implies association with some object
 * which already exists in storage, and all operations to modify the data
 * always result in modification in the corresponding stored object. The
 * exact way of storing, as it is accepted in LUWRAIN PIM, is obscured
 * from the user and developer, though the most popular way, of cource,
 * is SQL. 
 * <p>
 * This interface saves the information about accounts both for incoming
 * and outgoing mail. 
 *
 * @see MailAccount
 */
public interface StoredMailAccount
{
    MailAccount.Type getType() throws PimException;
    void setType(MailAccount.Type value) throws PimException;
    String getTitle() throws PimException;
    void setTitle(String value) throws PimException;
    String getHost() throws PimException;
    void setHost(String value) throws PimException;
    int getPort() throws PimException;
    void setPort(int value) throws PimException;
    String getLogin() throws PimException;
    void setLogin(String value) throws PimException;
    String getPasswd() throws PimException;
    void setPasswd(String value) throws PimException;
    String getTrustedHosts() throws PimException;
    void setTrustedHosts(String value) throws PimException;
    Set<MailAccount.Flags> getFlags() throws PimException;
    void setFlags(Set<MailAccount.Flags> value) throws PimException;
    String getSubstName() throws PimException;
    void setSubstName(String value) throws PimException;
    String getSubstAddress() throws PimException;
    void setSubstAddress(String value) throws PimException;
}
