// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail;

import java.util.function.*;

public interface MessageProvider<E>
{
    void getMessages(BiConsumer<Message, E> c);
}
