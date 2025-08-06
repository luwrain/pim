/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.mail;

import org.luwrain.core.*;
import static org.luwrain.popups.Popups.*;

public final class Conv
{
    public enum NewAccountType { POP3, SMTP};

    final Luwrain luwrain;
    final Strings s;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.s = app.getStrings();
    }

    String newFolderName() { return textNotEmpty(luwrain, s.newFolderNamePopupName(), s.newFolderNamePopupPrefix(), ""); }
    boolean removeFolder() { return confirmDefaultNo(luwrain, s.removeFolderPopupName(), s.removeFolderPopupText()); }
    boolean deleteMessageForever() { return confirmDefaultYes(luwrain, s.deleteMessageForeverPopupName(), s.deleteMessageForeverPopupText()); }

    public NewAccountType newAccountType()
    {
	final var res = (String)fixedList(luwrain, s.newAccountTypePopupName(), new String[] { s.newAccountTypePop3(), s.newAccountTypeSmtp() });
	if (res == null)
	    return null;
	if (res.equals(s.newAccountTypePop3()))
	    return NewAccountType.POP3;
	if (res.equals(s.newAccountTypeSmtp()))
	    return NewAccountType.SMTP;
	return null;
    }
}
