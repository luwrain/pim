
package org.luwrain.pim;


import org.luwrain.core.*;
import org.luwrain.pim.mail.*;
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
