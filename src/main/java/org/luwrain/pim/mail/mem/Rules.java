
package org.luwrain.pim.mail.mem;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Rules implements MailRules
{
        @Override public StoredMailRule[] getRules() throws PimException
    {
	return null;
    }

    @Override public void saveRule(MailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
    }

    @Override public void deleteRule(StoredMailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
    }
}
