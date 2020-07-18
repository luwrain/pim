/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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
    //    String addAccountPopupName();
        String addAccountPredefined();
    //    String addAccountYandex();
        String addMailAccount();
        String addMailFolder();
        String deleteAccount();
        String deleteMailFolder();
        String folderFormName(String folderName);
        String groupsSection();
    //    String incomingMailSuffix();
        String mailFolderFormBadOrderIndex(String value);
    //    String mailFolderFormOrderIndex();
    //    String mailFolderFormTitle();
        String mailSection();
        String newMailFolderPopupName();
        String newMailFolderPopupPrefix();
    //    String noStoring();
    //    String outgoingMailSuffix();
        String portMustBeGreaterZero();
        String portNotNumber();
        String rulesSection();
    //    String yourFullNameQuestion();
    //    String yourGoogleAccountQuestion();
    //    String yourGooglePasswordQuestion(String accountName);
    //    String yourYandexAccountQuestion();
    //    String yourYandexPasswordQuestion(String accountName);
}
