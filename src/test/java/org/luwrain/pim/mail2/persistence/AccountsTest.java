
package org.luwrain.pim.mail2.persistence;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.pim.mail2.persistence.model.*;
import static org.luwrain.pim.mail2.persistence.MailPersistence.*;

public class AccountsTest
{
    @Test public void main() throws Exception
    {
	deleteAllAccounts();
	final var dao = getAccountDAO();
	assertNotNull(dao);
	var ac = new Account();
	ac.setName("LUWRAIN");
	dao.add(ac);
	final var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals("LUWRAIN", ac.getName());
    }

    @Test public void update() throws Exception
    {
	deleteAllAccounts();
	final var dao = getAccountDAO();
	assertNotNull(dao);
	var ac = new Account();
	ac.setName("LUWRAIN");
	dao.add(ac);
	final int id = ac.getId();
	var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals(id, ac.getId());
	assertEquals("LUWRAIN", ac.getName());
	ac.setName("Proba");
	dao.update(ac);
	res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals(id, ac.getId());
	assertEquals("Proba", ac.getName());
    }
}
