
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface StoredMailRule
{
    MailRule.Actions getAction() throws PimException;
    void setAction(MailRule.Actions value) throws PimException;
    String getHeaderRegex() throws PimException;
    void setHeaderRegex(String value) throws PimException;
    String getDestFolderUniRef() throws PimException;
    void setDestFolderUniRef(String value) throws PimException;
}
