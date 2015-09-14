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

public interface StoredMailAccount
{
    int getType() throws Exception;
    void setType(int value) throws Exception;
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    String getHost() throws Exception;
    void setHost(String value) throws Exception;
    int getPort() throws Exception;
    void setPort(int value) throws Exception;
    String getLogin() throws Exception;
    void setLogin(String value) throws Exception;
    String getPasswd() throws Exception;
    void setPasswd(String value) throws Exception;
    int getFlags() throws Exception;
    void setFlags(int value) throws Exception;
    String getSubstName() throws Exception;
    void setSubstName(String value) throws Exception;
    String getSubstAddress() throws Exception;
    void setSubstAddress(String value) throws Exception;
}
