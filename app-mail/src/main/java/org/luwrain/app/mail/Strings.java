// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail;

import java.util.Date;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();

    String accountsAreaName();
    String actionAccounts();
    String actionNewAccount();
    String newAccountTypePopupName();
    String newAccountTypePop3();
    String newAccountTypeSmtp();
    String newAccountName();

    String foldersAreaName();
    String actionNewFolder();
    String newFolderNamePopupName();
    String newFolderNamePopupPrefix();
    
    String actionRemoveFolder();
    String removeFolderPopupName();
    String removeFolderPopupText();
        String actionFetchIncomingBkg();

        String summaryAreaName();
    String actionReply();
    String actionReplyAll();
    String actionDeleteMessage();
    String actionDeleteMessageForever();
    String deleteMessageForeverPopupName();
    String deleteMessageForeverPopupText();
    String actionMarkMessage();
    String messageMarked();
    String actionUnmarkMessage();
    String messageUnmarked();
    String actionDeletedShow();
    String actionDeletedHide();



    String messageAreaAttachment();
    String messageAreaCc();
    String messageAreaContentType();
    String messageAreaDate();
    String messageAreaFrom();
    String messageAreaName();
    String messageAreaSubject();
    String messageAreaTo();



    String wizardIntro();
    String wizardContinue();
    String wizardSkip();
    String wizardMailAddr();
    String wizardMailAddrIsEmpty();
    String wizardMailAddrIsInvalid();
    String wizardPasswordAnnouncement();
    String wizardPasswordIntro();
    String wizardPassword();
    String wizardPasswordIsEmpty();

    String pop3AccountAreaName(String accountName);
    String accountPropertiesName();
    String accountPropertiesHost();
    String accountPropertiesPasswd();
    String accountPropertiesPort();
    String accountPropertiesSsl();
    String accountPropertiesTls();
    String accountPropertiesLeaveMessages();
    String accountPropertiesNameCannotBeEmpty();
    String accountPropertiesInvalidPortValue();
}
