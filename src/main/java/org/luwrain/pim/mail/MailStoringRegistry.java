
package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

abstract class MailStoringRegistry implements MailStoring
{
    private final Registry registry;

    MailStoringRegistry(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }
}
