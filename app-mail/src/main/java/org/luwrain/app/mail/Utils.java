// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.reader.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.*;
//import org.luwrain.util.*;

import static org.luwrain.util.TextUtils.*;

final class Utils
{
    static Document createDocForMessage(Message message, Strings strings) throws PimException
    {
	final NodeBuilder builder = new NodeBuilder();
	/*
	builder.addParagraph(strings.messageAreaFrom() + " " + message.getFrom());
	builder.addParagraph(strings.messageAreaTo() + " " + listToString(message.getTo()));
	builder.addParagraph(strings.messageAreaCc() + " " + listToString(message.getCc()));
	builder.addParagraph(strings.messageAreaSubject() + " " + message.getSubject());
	builder.addParagraph(strings.messageAreaDate() + " " + message.getSentDate());
	builder.addParagraph(strings.messageAreaContentType() + " " + message.getContentType());
	if (message.getAttachments().length > 0)
	{
	    builder.addEmptyLine();
	    for(String a: message.getAttachments())
		builder.addParagraph(strings.messageAreaAttachment() + " " + a);
	}
	builder.addEmptyLine();
	for(String line: splitLines(message.getText()))
	    if (!line.trim().isEmpty())
		builder.addParagraph(line); else
		builder.addEmptyLine();
	*/
	final Document doc = new Document(builder.newRoot());
	doc.commit();
	return doc;
    }

    static private String listToString(String[] items)
    {
	if (items.length == 0)
	    return "";
	final StringBuilder b = new StringBuilder();
	b.append(items[0]);
	for(int i = 1;i < items.length;++i)
	    b.append(", ").append(items[i]);
	return new String(b);
    }

    static private String[] splitLines(String str)
    {
	if (str.isEmpty())
	    return new String[0];
	return splitLines(str);
    }
}
