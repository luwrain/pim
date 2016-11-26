
package org.luwrain.pim.mail;

public class MailRule
{
    public enum Actions {MOVE_TO_FOLDER};

    public Actions action = Actions.MOVE_TO_FOLDER;
    public String headerRegex = "";
    public String destFolderUniRef = "";

    @Override public String toString()
    {
	return headerRegex;
    }
}
