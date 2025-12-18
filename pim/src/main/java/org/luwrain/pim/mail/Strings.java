// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "ru" })
public interface Strings
{
    String mailFoldersRoot();
    String inboxFolder();
    String mailingListsFolder();
    String outgoingFolder();
    String sentFolder();
    String draftsFolder();
}
