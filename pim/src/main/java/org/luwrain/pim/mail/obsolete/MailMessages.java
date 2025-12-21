// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.obsolete;

import java.util.function.*;

import org.luwrain.pim.*;
import org.luwrain.pim.mail.persistence.*;

public interface MailMessages
{
    void save(Folder folder, MailMessage message);
    MailMessage[] load(Folder folder);
    MailMessage[] load(Folder folder, Predicate<MailMessage> cond);
    //    MailMessage[] loadNoDeleted(MailFolder folder);
    void moveToFolder(MailMessage message, Folder folder);
    void update(MailMessage message);
    void delete(MailMessage message);
    //    byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders);
    //    MailMessage fromByteArray(byte[] bytes);
}
