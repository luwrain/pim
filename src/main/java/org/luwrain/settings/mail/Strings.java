
package org.luwrain.settings.mail;

public interface Strings
{
    static final String NAME = "luwrain.settings.mail";

    String mailSection();
    String accountsSection();
    String rulesSection();
    String groupsSection();
    String addMailAccount();
    String noStoring();
    String accountFormName();
    String accountFormTitle();
    String accountFormEnabled();
    String accountFormServerType();
    String accountFormTypeSelectionPopupName();
    String accountFormHost();
    String accountFormPort();
    String accountFormLogin();
    String accountFormPasswd();
    String accountFormTrustedHosts();
    String accountFormDefaultOutgoing();
    String accountFormLeaveMessageOnServer();
    String accountFormUseSsl();
    String accountFormUseTls();
    String accountForMessagesAuthorName();
    String accountFormMessagesAuthorAddress();
    String portNotNumber();
    String portMustBeGreaterZero();

    String outgoingMailSuffix();
    String incomingMailSuffix();
    String addAccountYandex();
    String addAccountGoogle();
    String deleteAccount();
    String addAccountPopupName();
    String yourYandexAccountQuestion();
    String yourFullNameQuestion();
    String yourYandexPasswordQuestion(String accountName);
    String yourGoogleAccountQuestion();
    String yourGooglePasswordQuestion(String accountName);
}
