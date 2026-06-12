// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.io.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable
{
    public enum Type {HOME, WORK};
    private Type type = Type.HOME;
    private String poBox, extendedAddress, street, locality, region, postalCode, country;
}
