// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.binder;

public interface BinderStoring
{
    StoredCase[] getCases() throws Exception;
    void saveCase(Case c) throws Exception;
    void deleteCase(StoredCase c) throws Exception;
}
