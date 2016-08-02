
package org.luwrain.settings.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;

public class Factory implements org.luwrain.cpanel.Factory
{


    private Luwrain luwrain;
    private Strings strings;
    private MailStoring storing = null;
    private SimpleElement mailElement = null;

    private SimpleElement accountsElement = null;
    private SimpleElement rulesElement = null;
    private SimpleElement groupsElement = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    @Override public Element[] getElements()
    {
	if (!init())
	    return new Element[0];
	return new Element[]{
	    mailElement,
	    groupsElement,
	    accountsElement,
	    rulesElement,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	NullCheck.notNull(parent, "parent");
	if (!initStoring())
	return new Element[0];
	if (parent.equals(accountsElement))
	{
	try {
	    final StoredMailAccount[] accounts = storing.loadAccounts();
	    System.out.println("loaded " + accounts.length + " accounts");
	    final Element[] res = new Element[accounts.length];
	    for(int i = 0;i < accounts.length;++i)
		res[i] = new AccountElement(parent, accounts[i].getId(), accounts[i].getTitle());
	    return res;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	}
	}

	return new Element[0];

    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	if (el.equals(mailElement))
	    return new SimpleSection(mailElement, strings.mailSection());

	if (el.equals(accountsElement))
	    return new SimpleSection(accountsElement, strings.accountsSection(), null,
				     new Action[]{
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-yandex", "Добавить учётную запись для Яндекс.Почты"),
				     }, (controlPanel, event)->onAccountsActionEvent(controlPanel, event, -1));

	if (el.equals(groupsElement))
	    return new SimpleSection(groupsElement, strings.groupsSection());
	if (el.equals(rulesElement))
	    return new SimpleSection(rulesElement, strings.rulesSection());

	if (el instanceof AccountElement)
	    return new SimpleSection(el, ((AccountElement)el).title(), (controlPanel)->Account.create(controlPanel, storing, ((AccountElement)el).id()),
				     new Action[]{
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-yandex", "Добавить учётную запись для Яндекс.Почты"),
					 new Action("delete-mail-account", "Удалить учётную запись", new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     }, (controlPanel, event)->onAccountsActionEvent(controlPanel, event, ((AccountElement)el).id()));

	return null;
    }

    private boolean onAccountsActionEvent(ControlPanel controlPanel, ActionEvent event, long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	if (!initStoring())
	{
	    luwrain.message(strings.noStoring(), Luwrain.MESSAGE_ERROR);
	    return true;
	}

	//adding
	if (ActionEvent.isAction(event, "add-mail-account"))
	{
	    try {
		final MailAccount account = new MailAccount();
		account.title = "Новая";
		account.flags = MailAccount.FLAG_ENABLED;
		storing.saveAccount(account);
		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}

	//deleting
	if (ActionEvent.isAction(event, "delete-mail-account"))
	{
	    if (id < 0)
		return false;
	    try {
		final StoredMailAccount account = storing.loadAccountById(id);
		if (account == null)
		    return false;
		if (Popups.confirmDefaultNo(luwrain, "Удаление почтовой учётной записи", "Вы действительно хотите удалить почтовую запись \"" + account.getTitle() + "\"?"))
		{
		    storing.deleteAccount(account);
		    controlPanel.refreshSectionsTree();
		}
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}

	//yandex
	if (ActionEvent.isAction(event, "add-mail-account-yandex"))
	{
	    final String title = Popups.simple(luwrain, "Добавление учётной записи Яндекс.Почты", "Как вы зарегистрированы в Яндекс.Почте (без строки \"@yandex.ru\"):", "");
	    if (title == null)
		return true;
	    final String fullName = Popups.simple(luwrain, "Добавление учётной записи Яндекс.Почты", "Введите ваше полное имя:", "");
	    if (fullName == null)
		return true;
	    final String passwd = Popups.simple(luwrain, "Добавление учётной записи Яндекс.Почты", "Введите пароль для доступа к учётной записи " + title.trim() + "@yandex.ru:", "");
	    if (passwd == null)
		return true;
	    try {
		final MailAccount account = new MailAccount();
		account.title = title.trim() + "@yandex.ru (входящая почта)";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_LEAVE_MESSAGES | MailAccount.FLAG_SSL;
		account.type = MailAccount.POP3;
		account.host = "pop3.yandex.ru";
		account.port = 995;
		account.login = title.trim() + "@yandex.ru";
		account.passwd = passwd;
		account.substAddress = "";
		account.substName = "";
		storing.saveAccount(account);

		account.type = MailAccount.SMTP;
		account.title = title.trim() + "@yandex.ru (исходящая почта)";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_DEFAULT | MailAccount.FLAG_TLS;
		account.host = "smtp.yandex.ru";
		account.port = 587;
		account.substAddress = account.login;
		account.substName = fullName;
		storing.saveAccount(account);

		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}
	return false;
    }
    
    private boolean init()
    {
	if (strings == null)
	{
	    final Object o = luwrain.i18n().getStrings(Strings.NAME);
	    if (o != null && (o instanceof Strings))
		strings = (Strings)o; else
		return false;
	}
	if (mailElement == null)
	    mailElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	if (accountsElement == null)
	    accountsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Accounts");
	if (groupsElement == null)
	    groupsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Groups");
	if (rulesElement == null)
	    rulesElement = new SimpleElement(mailElement, this.getClass().getName() + ":Rules");
	return true;
    }

    private boolean initStoring()
    {
	final Object obj = luwrain.getSharedObject("luwrain.pim.mail");
	if (obj == null || !(obj instanceof org.luwrain.pim.mail.Factory))
	    return false;
	storing = ((org.luwrain.pim.mail.Factory)obj).createMailStoring();
	return storing != null;
	}
}


    /*
    private void constructChildSections()
    {
	if (childSections != null)
	    return;
	try {
	    final StoredMailAccount[] accounts = storing.loadAccounts();
	    if (accounts == null || accounts.length < 1)
	    {
		childSections = new MailAccountSection[0];
		return;
	    }
	    childSections = new MailAccountSection[accounts.length];
	    for(int i = 0;i < accounts.length;++i)
		childSections[i] = new MailAccountSection(storing, accounts[i]);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    childSections = new MailAccountSection[0];
	}
    }

    static void addNew(MailStoring storing, Environment environment)
    {
	final String title = Popups.simple(environment.getLuwrain(), "Новая учётная запись", "Введите имя новой учётной записи:", "");
	if (title == null)
	    return;
	if (title.trim().isEmpty())
	{
	    environment.getLuwrain().message("Введённое имя новой учётной записи не может быть пустым", Luwrain.MESSAGE_ERROR);//FIXME:
	    return;
	}
	final MailAccount account = new MailAccount();
	account.title = title;
	try {
	    storing.saveAccount(account);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("Во время добавления новой учётной записи произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return;
	}
	environment.refreshSectionsTree();
    }
    */

    /*
    @Override public SectionArea getSectionArea(Environment environment)
    {
	try {
	    if (area == null)
		area = new MailAccountArea(environment, storing, account);
	}
	catch (Exception e)
	{
	    environment.getLuwrain().message("Невозможно получить параметры учётной записи", Luwrain.MESSAGE_ERROR);
	    e.printStackTrace();
	    return null;
	}
	return area;
    }

    @Override public boolean canCloseSection(Environment environment)
    {
	if (account == null || area == null)
	    return true;
	try {
	    if (!area.save())
		return false;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("Во время сохранения введённых изменений произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	}
	area = null;
	return true;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	MailAccountsSection.addNew(storing, environment);
	return true;
    }

    @Override public boolean onTreeDelete(Environment environment)
    {
	String title;
	try {
	    title = account.getTitle();
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    title = "";
	}
	final YesNoPopup popup = new YesNoPopup(environment.getLuwrain(),
						"Удаление учётной записи", "Вы действительно хотите удалить учётную запись" + (!title.isEmpty()?" \"" + title + "\"?":"?"),//FIXME:
						false, Popups.DEFAULT_POPUP_FLAGS);
	environment.getLuwrain().popup(popup);
	if (popup.closing.cancelled() || !popup.result())
	    return true;
	try {
	    storing.deleteAccount(account);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("При удалении учётной записи произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return true;
	}
	System.out.println("deleted");
	account = null;
	area = null;
	environment.refreshSectionsTree();
	return true;
    }
    */

    /*
    private void constructChildSections()
    {
	if (childSections != null)
	    return;
	try {
	    final StoredMailRule[] rules = storing.getRules();
	    if (rules == null || rules.length < 1)
	    {
		childSections = new MailRuleSection[0];
		return;
	    }
	    childSections = new MailRuleSection[rules.length];
	    for(int i = 0;i < rules.length;++i)
		childSections[i] = new MailRuleSection(storing, rules[i]);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    childSections = new MailRuleSection[0];
	}
    }

    static boolean addNew(MailStoring storing)
    {
	final MailRule rule = new MailRule();
	rule.action = MailRule.ACTION_MOVE_TO_FOLDER;
	rule.headerRegex = "^From:.*";
	rule.destFolderUniRef = "";
	try {
	    storing.saveRule(rule);
	    return true;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return false;
	}
    }
    */

    /*
    @Override public SectionArea getSectionArea(Environment environment)
    {
	try {
	    if (area == null)
		area = new MailRuleArea(environment, storing, rule);
	}
	catch (Exception e)
	{
	    environment.getLuwrain().message("Невозможно получить параметры правила фильтрации", Luwrain.MESSAGE_ERROR);//FIXME:
	    e.printStackTrace();
	    return null;
	}
	return area;
    }

    */

    /*
    @Override public boolean onTreeDelete(Environment environment)
    {
	final YesNoPopup popup = new YesNoPopup(environment.getLuwrain(),
						"Удаление правила фильтрации", "Вы действительно хотите удалить выделенное правило фильтрации?",//FIXME:
						false, Popups.DEFAULT_POPUP_FLAGS);
	environment.getLuwrain().popup(popup);
	if (popup.closing.cancelled() || !popup.result())
	    return true;
	try {
	    storing.deleteRule(rule);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("При удалении правила фильтрации произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return true;
	}
	rule = null;
	area = null;
	environment.refreshSectionsTree();
	return true;
    }

    */


