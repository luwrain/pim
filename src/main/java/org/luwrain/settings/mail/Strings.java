/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.mail;

public interface Strings
{
    static final String NAME = "luwrain.settings.mail";

    String accountFormDefaultOutgoing();
    String accountFormEnabled();
    String accountForMessagesAuthorName();
    String accountFormHost();
    String accountFormLeaveMessageOnServer();
    String accountFormLogin();
    String accountFormMessagesAuthorAddress();
    String accountFormName();
    String accountFormPasswd();
    String accountFormPort();
    String accountFormServerType();
    String accountFormTitle();
    String accountFormTrustedHosts();
    String accountFormTypeSelectionPopupName();
    String accountFormUseSsl();
    String accountFormUseTls();
    String accountsSection();
    String addAccountPredefined();
    String addAccountPopupName();
    String addAccountYandex();
    String addMailAccount();
    String addMailFolder();
    String deleteMailFolder();
    String deleteAccount();
    String groupsSection();
    String incomingMailSuffix();
    String mailSection();
    String noStoring();
    String outgoingMailSuffix();
    String portMustBeGreaterZero();
    String portNotNumber();
    String rulesSection();
    String yourFullNameQuestion();
    String yourGoogleAccountQuestion();
    String yourGooglePasswordQuestion(String accountName);
    String yourYandexAccountQuestion();
    String yourYandexPasswordQuestion(String accountName);
    String newMailFolderPopupName();
    String newMailFolderPopupPrefix();
    String folderFormName(String folderName);
    String mailFolderFormTitle();
    String mailFolderFormOrderIndex();
    String mailFolderFormBadOrderIndex(String value);
}
