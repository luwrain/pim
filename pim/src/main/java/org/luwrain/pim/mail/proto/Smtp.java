// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.proto;

import java.util.*;
import java.io.*;
import java.util.zip.*;
import org.apache.logging.log4j.*;

import org.apache.commons.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.*;

//import static org.luwrain.core.NullCheck.*;
import static java.util.Objects.*;

public final class Smtp
{
    static private final String
	LOG_COMPONENT = "smtp";

    static public final class Result
    {
	public final int total;
	public final int sent;
	public final Map<Integer, Throwable> errors;

	Result()
	{
	    this.total = 0;
	    this.sent = 0;
	    this.errors = new HashMap<>();
	}

	Result(int total, int sent, Map<Integer, Throwable> errors)
	{
	    requireNonNull(errors, "errors can't be null");
	    if (total < 0)
		throw new IllegalArgumentException("total (" + total + ") may not be negative");
	    if (sent < 0)
		throw new IllegalArgumentException("sent (" + sent + ") may not be negative");
	    this.total = total;
	    this.sent = sent;
	    this.errors = errors;
	}
    }

    private final Folder pending, sent;
    private final MailConnections con;
    private final MessageDAO messageDAO;
    private final Luwrain luwrain;

    public Smtp(Account account, MailPersistence persistence, Luwrain luwrain)
    {
	requireNonNull(account, "account can't be null");
	requireNonNull(persistence, "persistence can't be null");
	requireNonNull(luwrain, "luwrain can't be null");
	this.luwrain = luwrain;
	this.messageDAO = persistence.getMessageDAO();
	final var folderDAO = persistence.getFolderDAO();
	this.pending = folderDAO.findFirstByProperty(FolderProperties.DEFAULT_OUTGOING, "true");
	if (this.pending == null)
	    throw new PimException("No default outgoing folder (pending) found");
	this.sent = folderDAO.findFirstByProperty(FolderProperties.DEFAULT_SENT, "true");
	if (this.sent == null)
	    throw new PimException("No default sent folder found");
	Log.debug(LOG_COMPONENT, "connecting to the SMTP server: " + account.getHost() + ":" + account.getPort());
	this.con = new MailConnections(createMailServerParams(account), false);
	Log.debug(LOG_COMPONENT, "connection established");
    }

    /**
     * Reads all pending messages from the outgoing folder,
     * sends each via SMTP, and moves successfully sent messages
     * to the sent folder.
     *
     * @return A {@link Result} describing how many messages were processed,
     * how many were sent, and any errors encountered
     * @throws InterruptedException If the thread is interrupted during execution
     */
    public Result send() throws InterruptedException
    {
	final var messages = messageDAO.getByFolderId(pending.getId());
	Log.debug(LOG_COMPONENT, "loading " + messages.size() + " message(s) from pending folder");
	if (messages.isEmpty())
	{
	    Log.debug(LOG_COMPONENT, "no messages to send");
	    return new Result();
	}
	final Map<Integer, Throwable> errors = new HashMap<>();
	int sentCount = 0;
	for (final var metadata : messages)
	{
	    if (Thread.currentThread().interrupted())
		throw new InterruptedException();
	    try {
		final byte[] rawMessage = loadRawMessage(metadata);
		con.send(rawMessage);
		//		metadata.setState(MessageMetadata.State.READ);
		metadata.setFolderId(sent.getId());
		messageDAO.update(metadata);
		++sentCount;
		Log.debug(LOG_COMPONENT, "message " + metadata.getId() + " sent and moved to sent folder");
	    }
	    catch (Throwable e)
	    {
		Log.error(LOG_COMPONENT, "unable to send message " + metadata.getId() + ": " + e.getClass().getName() + ": " + e.getMessage());
		errors.put(Integer.valueOf((int)metadata.getId()), e);
	    }
	}
	return new Result(messages.size(), sentCount, errors);
    }

    /**
     * Sends a raw RFC822 message via the SMTP transport.
     *
     * @param rawMessage The raw RFC822 message bytes
     * @throws PimException If sending fails
     */
    public void send(byte[] rawMessage)
    {
	requireNonNull(rawMessage, "rawMessage can't be null");
	con.send(rawMessage);
    }

    static MailConnections.Params createMailServerParams(Account account)
    {
	final var params = new MailConnections.Params();
	params.doAuth = account.getLogin() != null && !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.isSsl();
	params.tls = account.isTls();
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
	return params;
    }

    private byte[] loadRawMessage(MessageMetadata metadata) throws IOException
    {
	final String messageId = metadata.getMessageId();
	if (messageId == null || messageId.isEmpty())
	    throw new IOException("Message " + metadata.getId() + " has no messageId, cannot load raw bytes");
	final java.io.File messageFile = getRawMessageFileName(
	    luwrain.getAppDataDir(Message.DATA_DIR).toFile(), messageId);
	try (final InputStream is = new GZIPInputStream(new FileInputStream(messageFile)))
	{
	    final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    IOUtils.copy(is, bytes);
	    return bytes.toByteArray();
	}
    }

    static java.io.File getRawMessageFileName(java.io.File parent, String id)
    {
	if (id.length() < 4)
	    throw new IllegalArgumentException("id (" + id + ") can't be shorter than 4");
	java.io.File f = new java.io.File(parent, id.substring(0, 1));
	f = new java.io.File(f, id.substring(0, 2));
	f = new java.io.File(f, id.substring(0, 3));
	f = new java.io.File(f, id.substring(0, 4));
	return new java.io.File(f, id + Message.MESSAGE_FILE_EXTENSION);
    }
}
