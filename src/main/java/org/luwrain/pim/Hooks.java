/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.pim;


import org.luwrain.core.*;
import org.luwrain.pim.mail2.*;
import org.luwrain.pim.mail.script.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.script.Hooks.*;

public final class Hooks
{
    static public final String
	MAIL_INCOMING = "luwrain.mail.incoming";

    static public void mailIncoming(Luwrain luwrain, Message message)
    {
	final var mailObj = new MailObj(luwrain);
	chainOfResponsibilityNoExc(luwrain, MAIL_INCOMING, new Object[]{
		mailObj,
		new MessageObj(mailObj, message) });
    }
}
