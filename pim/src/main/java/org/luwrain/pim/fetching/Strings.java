// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

public interface Strings
{
    static public final String NAME = "luwrain.fetching";

    String connectingTo(String hostPort);
    String connectionEstablished(String hostPort);
    String errorLoadingMailAccounts(String errorMsg);
    String errorLoadingMailAccount(String uniRef);
    String fetchingMailFromAccount(String accountTitle);
    String mailAccountDisabled(String accountName);
    String messagesInQueueForAccount(String accountName, String num);
    String newsFetchingError(String groupName);
    String noAllMessagesToBeFetched();
    String noMailAccountsForFetching();
    String noNewsGroups();
    String noNewsGroupsData();
    String sendingMessage(String num, String total);
    String skippingFetchingFromDisabledAccount(String title);
}
