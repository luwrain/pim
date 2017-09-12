/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.popups.pim;

import org.luwrain.core.*;

public interface Strings
{
    static public final String NAME = "luwrain.pim.popups";

    String chooseMailPopupName(String folderName);
    String ccEditPopupName();
    String contactDoesntHaveMail(String contactTitle);
    String chooseMailForContactPopupName(String contactTitle);

    static public Strings create(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object obj = luwrain.i18n().getStrings(NAME);
	if (obj == null || !(obj instanceof Strings))
	    return null;
	return (Strings)obj;
    }
}
