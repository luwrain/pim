// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.binder;


public interface StoredCase
{
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    int getStatus() throws Exception;
    void setStatus(int value) throws Exception;
    int getPriority() throws Exception;
    void setPriority(int value) throws Exception;
}
