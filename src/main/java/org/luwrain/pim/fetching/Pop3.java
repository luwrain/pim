
//LWR_API 1.0

package org.luwrain.pim.fetching;

import java.util.*;
import java.io.*;
//import java.util.regex.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.script.*;

public class Pop3 extends Base implements MailServerConversations.Listener
{
    private final MailStoring storing;
    private final MailHookObject mailHookObject;
    private final StoredMailFolder inbox;

    public Pop3(Control control, Strings strings) throws FetchingException, PimException, InterruptedException
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new FetchingException("Отсутствует соединение");
	this.mailHookObject = new MailHookObject(storing);
	final org.luwrain.pim.Settings.MailFolders sett = org.luwrain.pim.Settings.createMailFolders(registry);
	final String inboxUniRef = sett.getFolderInbox("");
	if (inboxUniRef.trim().isEmpty())
	    throw new FetchingException("Ошибка конфигурации электронной почты: не указаны базовые группы для хранения (исходящие, отправленные и т. д.)");//FIXME:
	inbox = storing.getFolders().loadByUniRef(inboxUniRef);
    }

    public void fetch() throws PimException, InterruptedException
    {
	final StoredMailAccount[] accounts;
	try {
	    accounts = storing.getAccounts().load();
	}
	catch(PimException e)
	{
	    throw new FetchingException(strings.errorLoadingMailAccounts(e.getClass().getName() + ":" + e.getMessage()));
	}
	Log.debug(LOG_COMPONENT, "loaded " + accounts.length + " accounts for fetching mail");
	int used = 0;
	for(StoredMailAccount a: accounts)
	{
	    control.checkInterrupted();
	    final MailAccount.Type type;
	    try {
		type = a.getType();
	    }
	    catch(PimException e)
	    {
		control.luwrain().crash(e);
		continue;
	    }
	    if (type == MailAccount.Type.POP3)
	    {
		try {
		    processAccount(a);
		}
		catch(PimException | IOException e)
		{
		    //FIXME:
		}
		control.checkInterrupted();
		++used;
	    }
	}
	if (used <= 0)
	    control.message(strings.noMailAccountsForFetching());
    }

    private void processAccount(StoredMailAccount account) throws IOException, PimException, InterruptedException
    {
	NullCheck.notNull(account, "account");
	final String title = account.getTitle();
	Log.debug(LOG_COMPONENT, "fetching POP3 mail from account \"" + account.getTitle() + "\", flags " + account.getFlags());
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    control.message(strings.skippingFetchingFromDisabledAccount(title));
	    return;
	}
	control.message(strings.fetchingMailFromAccount(title));
	Log.debug(LOG_COMPONENT, "connecting to POP3 server:" + account.getHost() + ":" + account.getPort());
	control.message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	final MailServerConversations conversation = new MailServerConversations(createMailServerParams(account), true);
	Log.debug(LOG_COMPONENT, "connection established");
	control.message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	conversation.fetchPop3("inbox", this, !account.getFlags().contains(MailAccount.Flags.LEAVE_MESSAGES));
	Log.debug(LOG_COMPONENT, "fetching from the account finished");
    }

    @Override public void numberOfNewMessages(int count, boolean haveMore)
    {
	Log.debug(LOG_COMPONENT, String.valueOf(count) + " messages");
	if (count <= 0)
	    return;
	//			control.message(strings.numberOfNewMessages("" + count, title));
	if (haveMore)
	    control.message(strings.noAllMessagesToBeFetched());
    }

    @Override public void newMessage(byte[] bytes, int num, int total)
    {
		NullCheck.notNull(bytes, "bytes");
	final MailMessage message;
	try {
	    message = BinaryMessage.fromByteArray(bytes);
	}
	catch(PimException | IOException e)
	{
	    Log.error(LOG_COMPONENT, "unable to create a message object:" + e.getMessage());
	    return;
	}
	final MessageHookObject hookObj = new MessageHookObject(message);
	try {
	    luwrain.xRunHooks("luwrain.pim.message.new.save", new Object[]{mailHookObject, hookObj}, Luwrain.HookStrategy.CHAIN_OF_RESPONSIBILITY);
	}
	catch(RuntimeException e)
	{
	    Log.error(LOG_COMPONENT, "unable to save a message:" + e.getClass().getName() + ":" + e.getMessage());
	}
    }
}
