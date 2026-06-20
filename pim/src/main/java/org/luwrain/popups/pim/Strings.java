// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.popups.pim;

import org.luwrain.core.*;

import static java.util.Objects.*;

public interface Strings
{
    static public final String NAME = "luwrain.pim.popups";

    String chooseMailPopupName(String folderName);
    String ccEditPopupName();
    String contactDoesntHaveMail(String contactTitle);
    String chooseMailForContactPopupName(String contactTitle);

    static public Strings create(Luwrain luwrain)
    {
	requireNonNull(luwrain, "luwrain can't be null");
	final Object obj = luwrain.i18n().getStrings(NAME);
	if (obj == null || !(obj instanceof Strings))
	    return null;
	return (Strings)obj;
    }
}
