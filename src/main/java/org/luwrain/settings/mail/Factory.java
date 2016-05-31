
package org.luwrain.settings.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;

public class Factory implements org.luwrain.cpanel.Factory
{
    static private final String STRINGS_NAME = "luwrain.settings.mail";

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
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o != null && (o instanceof Strings))
	{
	    strings = (Strings)o;
	}
	    mailElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	    accountsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Accounts");
	    groupsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Groups");
	    rulesElement = new SimpleElement(mailElement, this.getClass().getName() + ":Rules");
    }

    @Override public Element[] getElements()
    {
	return new Element[]{
	    mailElement,
	    groupsElement,
	    accountsElement,
	    rulesElement,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	return new Element[0];
    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	if (el.equals(mailElement))
	    return new SimpleSection(mailElement, "Электронная почта");

	if (el.equals(accountsElement))
	    return new SimpleSection(accountsElement, "Учётные записи");
	if (el.equals(groupsElement))
	    return new SimpleSection(groupsElement, "Группы");
	if (el.equals(rulesElement))
	    return new SimpleSection(rulesElement, "Правила");
	return null;
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


