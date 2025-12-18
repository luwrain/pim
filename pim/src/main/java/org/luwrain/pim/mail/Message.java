// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail;

import java.util.*;
import java.io.*;
import java.util.zip.*;
import org.apache.commons.io.*;
import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

import org.luwrain.pim.mail.persistence.model.*;
import org.luwrain.util.*;

@Data
@NoArgsConstructor
public class Message
{
    static public final String
	DATA_DIR = "luwrain.pim.mail",
	MESSAGE_FILE_EXTENSION = ".gz";

    MessageMetadata metadata;
    byte[] rawMessage;
    List<MessageContentItem> contentItems;

    public Message(MessageMetadata metadata)
    {
	this.metadata = metadata;
	this.rawMessage = null;
	this.contentItems = MessageContentItem.fromJson(metadata.getContent());
    }

    void saveRawMessage(Luwrain luwrain) throws IOException
    {
	if (rawMessage == null || rawMessage.length == 0)
	    throw new IllegalStateException("The message doesn't contain the raw version");
	final String id = new Sha1().getSha1(rawMessage);
	final File messageFile = getRawMessageFileName(luwrain.getAppDataDir(DATA_DIR).toFile(), id);
	java.nio.file.Files.createDirectories(messageFile.getParentFile().toPath());
	try (final FileOutputStream fs = new FileOutputStream(messageFile)) {
	    try (final GZIPOutputStream os = new GZIPOutputStream(fs)) {
		try (final ByteArrayInputStream is = new ByteArrayInputStream(rawMessage)){
		    IOUtils.copy(is, os);
		}
		os.flush();
	    }
	    fs.flush();
	}
    }

    byte[] loadRawMessage(Luwrain luwrain) throws IOException
    {
	if (rawMessage == null || rawMessage.length == 0)
	    throw new IllegalStateException("The message doesn't contain the raw version");
	final String id = new Sha1().getSha1(rawMessage);
	try (final InputStream is = new GZIPInputStream(new FileInputStream(getRawMessageFileName(luwrain.getAppDataDir(DATA_DIR).toFile(), id) ))){
	    final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    IOUtils.copy(is, bytes);
	    return bytes.toByteArray();
	}
    }

    File getRawMessageFileName(File parent, String id)
    {
	if (id.length() < 4)
	    throw new IllegalArgumentException("id (" + id + ") can't be shorter than 4");
	File f = new File(parent, id.substring(0, 1));
	f = new File(f, id.substring(0, 2));
	f = new File(f, id.substring(0, 3));
	f = new File(f, id.substring(0, 4));
	return new File(f, id + MESSAGE_FILE_EXTENSION);
    }
}
