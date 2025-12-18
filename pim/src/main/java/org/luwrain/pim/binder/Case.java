// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.binder;

public class Case
{
    public static final int ACTUAL = 0;

    public String title;
    public int status = ACTUAL;
    public int priority = 0;
}
