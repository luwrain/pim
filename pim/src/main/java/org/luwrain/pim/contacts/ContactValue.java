// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import lombok.*;

import org.luwrain.core.*;

@Data
@NoArgsConstructor
public final class ContactValue
{
    public enum Type { MAIL, ADDRESS, PHONE, BIRTHDAY, URL, SKYPE };

    private Type type = null;
    private String value = "";
    private boolean preferable = false;
}
