// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.io.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail implements Serializable
{
    public enum Type {INTERNET};
    private Type type = Type.INTERNET;
    private String address;
}
