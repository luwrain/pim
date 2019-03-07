
//LWR_API 1.0

package org.luwrain.pim.fetching;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.script.*;

public class Pop3 extends Base implements MailServerConversations.Listener
{
    private final MailStoring storing;
    private final Rule[] rules;
    private final StoredMailFolder inbox;

    public Pop3(Control control, Strings strings) throws FetchingException, PimException, InterruptedException
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new FetchingException("Отсутствует соединение");
	final org.luwrain.pim.Settings.MailFolders sett = org.luwrain.pim.Settings.createMailFolders(registry);
	final String inboxUniRef = sett.getFolderInbox("");
	if (inboxUniRef.trim().isEmpty())
	    throw new FetchingException("Ошибка конфигурации электронной почты: не указаны базовые группы для хранения (исходящие, отправленные и т. д.)");//FIXME:
	inbox = storing.getFolders().loadByUniRef(inboxUniRef);
	final StoredMailRule[] storedRules = storing.getRules().load();
	this.rules = new Rule[storedRules.length];
	for(int i = 0;i < storedRules.length;++i)
	{
	    rules[i] = initRule(storedRules[i]);
	    if (rules[i] == null)
		throw new FetchingException("Ошибочное почтовое правило");
	}
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
	final MailServerConversations conversation = new MailServerConversations(createMailServerParams(account), true);
	Log.debug(LOG_COMPONENT, "connecting to POP3 server:" + account.getHost() + ":" + account.getPort());
	control.message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	control.message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	conversation.fetchPop3("inbox", this, !account.getFlags().contains(MailAccount.Flags.LEAVE_MESSAGES)/*, (text)->{
													      return org.luwrain.util.MlTagStrip.run(text);
													      }*/);
    }

    @Override public void numberOfNewMessages(int count, boolean haveMore)
    {
	//			Log.debug("fetch", "" + count + (haveMore?"+":"") + " new messages in " + title);
	if (count <= 0)
	{
	    //			    control.message(strings.noNewMailInAccount(title));
	    return;
	}
	//			control.message(strings.numberOfNewMessages("" + count, title));
	if (haveMore)
	    control.message(strings.noAllMessagesToBeFetched());
    }

    @Override public boolean newMessage(/*MailMessage*/byte[] message, int num, int total)
    {
	/*
			final String folderTitle = saveIncomingMessage(message);
			if (folderTitle != null)
			{
			    control.message(strings.fetchedMessageSaved("" + (num + 1), "" + total, folderTitle));
			    return true;
			}
			control.message(strings.errorSavingFetchedMessage()); 
			Log.error("fetch", "unable to save the message");
			*/
	return false;
    }

    private void saveIncomingMessage(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	final MessageHookObject hookObj = new MessageHookObject(message);
	try {
	    luwrain.xRunHooks("luwrain.message.new.save", new Object[]{null, hookObj}, Luwrain.HookStrategy.CHAIN_OF_RESPONSIBILITY);
	}
	catch(RuntimeException e)
	{
	    Log.error(LOG_COMPONENT, "unable to save a message:" + e.getClass().getName() + ":" + e.getMessage());
	}
    }

    private Rule initRule(StoredMailRule storedRule) throws PimException, InterruptedException
    {
	control.checkInterrupted();
	final String regex = storedRule.getHeaderRegex();
	if (regex == null || regex.trim().isEmpty())
	{
	    control.message("Обнаружено почтовое правило с пустым условием, доставка почты отменена");
	    return null;
	}
	final Pattern pattern;
	try {
	    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}
	catch(PatternSyntaxException e)
	{
	    e.printStackTrace();
	    control.message("Регулярное выражение в правиле \"" + regex + "\" не является правильным регулярным выражением");
	    return null;
	}
	final String uniRef = storedRule.getDestFolderUniRef();
	if (uniRef == null || uniRef.trim().isEmpty())
	{
	    control.message("Почтовое правило с условием \"" + regex + "\" не имеет заданной целевой почтовой группы, доставка почты отменена");
	    return null;
	}
	final StoredMailFolder folder = storing.getFolders().loadByUniRef(uniRef);
	if (folder == null)
	{control.message("Невозможно получить доступ к группе сообщений по ссылке: " + uniRef);
	    return null;
	}
	return new Rule(regex, folder, pattern);
    }

    static private String[] extractHeaders(byte[] raw)
    {
	String str;
	try {
	    str = new String(raw, "US-ASCII");
	}
	catch(java.io.UnsupportedEncodingException e)
	{
	    e.printStackTrace();
	    return new String[0];
	}
	final String[] lines = str.split("\n");
	final LinkedList<String> res = new LinkedList<String>();
	for(String s: lines)
	{
	    if (s.trim().isEmpty())
		break;
	    res.add(s);
	}
	return res.toArray(new String[res.size()]);
    }

    static private boolean regexMatch(Pattern pattern, String[] lines)
    {
	for(int i = 0;i < lines.length;++i)
	{
	    final Matcher matcher = pattern.matcher(lines[i]);
	    if (matcher.find())
		return true;
	}
	return false;
    }

    static private class Rule
    {
	final String regex;
	final StoredMailFolder destFolder;
	final Pattern pattern;

	Rule(String regex, StoredMailFolder destFolder,
	     Pattern pattern)
	{
	    NullCheck.notNull(regex, "regex");
	    NullCheck.notNull(destFolder, "destFolder");
	    NullCheck.notNull(pattern, "pattern");
	    this.regex = regex;
	    this.destFolder = destFolder;
	    this.pattern = pattern;
	}
    }
}
