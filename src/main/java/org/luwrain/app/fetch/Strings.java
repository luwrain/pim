/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.fetch;

public interface Strings
{
    String actionFetchAll();
    String actionFetchIncomingMail();
    String actionFetchMail();
    String actionFetchNews();
    String actionFetchOutgoingMail();
    String appName();
    String connectingTo(String hostPort);
    String connectionEstablished(String hostPort);
    String errorFetchingMessagesFromAccount(String accountTitle, String errormsg);
    String errorLoadingMailAccounts(String errorMsg);
    String errorLoadingMailAccount(String uniRef);
    String errorSavingFetchedMessage();
    String fetchedMessageSaved(String num, String total, String folderName);
    String fetchingCompleted();
    String fetchingMailFromAccount(String accountTitle);
    String interrupted();
    String introductionAll();
    String introductionIncomingMail();
    String introductionMail();
    String introductionNews();
    String introductionOutgoingMail();
    String mailAccountDisabled(String accountName);
    String messagesInQueueForAccount(String accountName, String num);
    String newsFetchingError(String groupName);
    String noAllMessagesToBeFetched();
    String noMailAccountsForFetching();
    String noNewMailInAccount(String accountTitle);
    String noNewsGroups();
    String noNewsGroupsData();
    String numberOfNewMessages(String num , String accountTitle);
    String sendingMessage(String num, String total);
    String skippingFetchingFromDisabledAccount(String title);
    String startingNewsFetching();
}
