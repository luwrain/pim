
package org.luwrain.network.fetching;

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
