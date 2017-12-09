
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailRules
{
    StoredMailRule[] getRules() throws PimException;
    void saveRule(MailRule rule) throws PimException;
    void deleteRule(StoredMailRule rule) throws PimException;
}
