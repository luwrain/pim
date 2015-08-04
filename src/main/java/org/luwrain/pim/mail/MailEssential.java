
package org.luwrain.pim.mail;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface MailEssential
{
	// make MimeMessage from class fields
	public void PrepareInternalStore(MailMessage msg) throws Exception;
	// used to fill fields via .eml file stream
	public MailMessage loadMailFromFile(FileInputStream fs) throws Exception;
	// used to save fields to .eml field stream
	public void SaveMailToFile(MailMessage msg,FileOutputStream fs) throws Exception;

}
