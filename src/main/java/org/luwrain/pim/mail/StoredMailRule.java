/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.pim.*;

public interface StoredMailRule
{
    MailRule.Actions getAction() throws PimException;
    void setAction(MailRule.Actions value) throws PimException;
    String getHeaderRegex() throws PimException;
    void setHeaderRegex(String value) throws PimException;
    String getDestFolderUniRef() throws PimException;
    void setDestFolderUniRef(String value) throws PimException;
}
