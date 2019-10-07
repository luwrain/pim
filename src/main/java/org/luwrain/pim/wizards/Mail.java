
package org.luwrain.pim.wizards;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class Mail
{
    private final Luwrain luwrain;
    private final MailStoring storing;
    private final String title;

    public Mail(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, true);
	this.title = "Подключение к почтовому серверу";//FIXME:
    }

    public boolean start()
    {
	return true;
    }
}
