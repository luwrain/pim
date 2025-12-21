// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence;

import java.util.*;
import org.luwrain.pim.mail.persistence.*;

public interface MessageDAO
{
    long add(MessageMetadata message);
    void delete(MessageMetadata message);
    List<MessageMetadata> getAll();
        List<MessageMetadata> getByFolderId(int folderId);
    void update(MessageMetadata message);
}
