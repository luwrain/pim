
package org.luwrain.network;

import java.io.*;
import java.nio.file.*;
import org.luwrain.core.NullCheck;

class MailAttachmentSaving extends MimePartCollector
{
    static final int SUCCESS = 0;
    static final int NOT_FOUND = 1;

    private String fileName;
    private File destFile;
    private int result = NOT_FOUND;

    MailAttachmentSaving(String fileName, File destFile)
    {
	this.fileName = fileName;
	this.destFile = destFile;
	NullCheck.notNull(fileName, "fileName");
	NullCheck.notNull(destFile, "destFile");
	if (fileName.isEmpty())
	    throw new IllegalArgumentException("fileName may not be null");
    }

    @Override protected void onAttachment(String fileName, Object obj) throws IOException
    {
	if (fileName == null || !fileName.equals(this.fileName))
	    return;
	final FilterInputStream s = (FilterInputStream)obj;
	Files.copy(s, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	result = SUCCESS;
    }

int result()
    {
	return result;
    }
}
