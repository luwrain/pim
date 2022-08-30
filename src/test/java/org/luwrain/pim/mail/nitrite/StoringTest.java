
package org.luwrain.pim.mail.nitrite;

import java.io.*;
import java.util.*;
import org.junit.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

@Ignore
public final class StoringTest extends Assert
{
    @Test public void main() throws Exception
    {
	final File tmpFile = File.createTempFile(".lwr-junit-pim-mail.", ".nitrite");
	tmpFile.delete();
	final Nitrite db = Nitrite.builder()
        .compressed()
        .filePath(tmpFile)
        .openOrCreate("luwrain", "passwd");
	final ObjectRepository<Message> repo = db.getRepository(Message.class);
	Message m = new Message();
	m.id = "123";
	m.folderId = 32;
	repo.insert(m);
	final Cursor<Message> c = repo.find();
	final List<Message> res = c.toList();
	assertNotNull(res);
	assertEquals(1, res.size());
	m = res.get(0);
	assertNotNull(m.id);
	assertEquals("123", m.id);
	assertEquals(32, m.folderId);
    }
}
