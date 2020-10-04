/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail.nitrite;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Rules implements MailRules
{
    private final Registry registry;

    Rules(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public StoredMailRule[] load() throws PimException
    {
	final String[] dirNames = registry.getDirectories(org.luwrain.pim.mail.Settings.RULES_PATH);
	if (dirNames == null || dirNames.length < 1)
	    return new StoredMailRule[0];
	final List<Rule> res = new LinkedList();
	for(String s: dirNames)
	{
	    if (s == null || s.trim().isEmpty())
		continue;
	    int id;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
		e.printStackTrace();
		continue;
	    }
	    final Rule rule = new Rule(registry, id);
	    if (rule.load())
		res.add(rule);
	}
	return res.toArray(new Rule[res.size()]);
    }

    @Override public void save(MailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
	final int newId = org.luwrain.pim.Util.newFolderId(registry, org.luwrain.pim.mail.Settings.RULES_PATH);
	final String path = Registry.join(org.luwrain.pim.mail.Settings.RULES_PATH, "" + newId);
	if (!registry.addDirectory(path))
	    throw new PimException("Unable to create new registry directory " + path);
	if (!registry.setString(Registry.join(path, "action"), Rule.getActionStr(rule.action)))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "action"));
	if (!registry.setString(Registry.join(path, "header-regex"), rule.headerRegex))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "header-regex"));
	if (!registry.setString(Registry.join(path, "dest-folder-uniref"), rule.destFolderUniRef))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "dest-folder-uniref"));
    }

    @Override public void delete(StoredMailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
	if (!(rule instanceof Rule))
	    throw new IllegalArgumentException("rule is not an instance of StoredMailRuleRegistry");
	final Rule ruleRegistry = (Rule)rule;
	final String path = Registry.join(org.luwrain.pim.mail.Settings.RULES_PATH, "" + ruleRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }
}
