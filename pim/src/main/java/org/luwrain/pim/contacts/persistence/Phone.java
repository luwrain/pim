// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.io.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone implements Serializable
{
    public enum Type {CELL, HOME, WORK, FAX, PAGER, VOICE, VIDEO, TEXT};
    private Type type = Type.CELL;
    private String number;
}
