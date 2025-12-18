// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class Contact
{
    private String title = "";
    private ContactValue[] values = new ContactValue[0];
    private String[] tags = new String[0]; 
    private String[] uniRefs = new String[0];
    private String notes = "";

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
