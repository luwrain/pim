/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.util.*;
import java.io.*;
import java.util.zip.*;
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
		    StreamUtils.copyAllBytes(is, os);
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
	    org.luwrain.util.StreamUtils.copyAllBytes(is, bytes);
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
