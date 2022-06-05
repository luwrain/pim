
package org.luwrain.pim.mail;

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
	final String messageId = "message123";
	tmpFile.delete();
	final Nitrite db = Nitrite.builder()
        .compressed()
        .filePath(tmpFile)
        .openOrCreate("luwrain", "passwd");
	final ObjectRepository<MailMessage> repo = db.getRepository(MailMessage.class);
	MailMessage m = new MailMessage();
	m.setMessageId(messageId);
	m.setCc(new String[]{"0", "1", "2"});
	m.setRawMessage(new byte[]{0, 1, 2});
	repo.insert(m);
	final Cursor<MailMessage> c = repo.find();
	final List<MailMessage> res = c.toList();
	assertNotNull(res);
	assertEquals(1, res.size());
	m = res.get(0);
	assertNotNull(m);
	assertEquals(messageId, m.getMessageId());
	String[] a = m.getCc();
	assertNotNull(a);
	assertEquals(3, a.length);
	for(int i = 0;i < a.length;i++)
	{
	    assertNotNull(a[i]);
	    assertEquals(String.valueOf(i), a[i]);
	}
	assertNotNull(m.getRawMessage());
	assertEquals(0, m.getRawMessage().length);
    }
}
