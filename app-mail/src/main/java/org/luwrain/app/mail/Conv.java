// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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
