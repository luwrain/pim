// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.popups.pim;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.persistence.*;

public class CcEditPopup extends EditableListPopup<Object>
{
    protected final ContactDAO contactDAO;
    protected final Strings strings;

    public CcEditPopup(Luwrain luwrain, Strings strings, ContactDAO contactDAO,
                       String[] initialList) throws PimException
    {
        super(luwrain,
              makeParams(luwrain, strings.ccEditPopupName(), strings, contactDAO, initialList),
              Popups.DEFAULT_POPUP_FLAGS);
        NullCheck.notNull(contactDAO, "contactDAO");
        NullCheck.notNull(strings, "strings");
        this.contactDAO = contactDAO;
        this.strings = strings;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
        NullCheck.notNull(event, "event");
        if (event.isSpecial() && !event.isModified())
            switch(event.getSpecial())
            {
            }
        return super.onInputEvent(event);
    }

    @Override public boolean onOk()
    {
        return true;
    }

    @Override public String[] result()
    {
        final int count = editableListModel.getItemCount();
        if (count < 1)
            return new String[0];
        final List<String> res = new LinkedList<>();
        for (int i = 0; i < count; ++i)
            res.add(editableListModel.getItem(i).toString());
        return res.toArray(new String[res.size()]);
    }

    static protected EditableListArea.Params<Object> makeParams(Luwrain luwrain, String name,
                                                                 Strings strings,
                                                                 ContactDAO contactDAO,
                                                                 String[] initialList)
    {
        NullCheck.notNull(luwrain, "luwrain");
        NullCheck.notNull(name, "name");
        NullCheck.notNull(strings, "strings");
        NullCheck.notNull(contactDAO, "contactDAO");
        NullCheck.notNullItems(initialList, "initialList");
        final EditableListArea.Params<Object> params = new EditableListArea.Params<>();
        params.context = new DefaultControlContext(luwrain);
        params.name = name;
        params.model = new ListUtils.DefaultEditableModel<>(Object.class);
        params.appearance = new ListUtils.DefaultAppearance<>(params.context);
        return params;
    }
}
