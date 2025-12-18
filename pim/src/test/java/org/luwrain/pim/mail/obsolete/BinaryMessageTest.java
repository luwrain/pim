
package org.luwrain.pim.mail.obsolete;

import java.util.*;
import java.io.*;
import org.apache.commons.codec.binary.Base64;
import javax.mail.internet.MimeUtility;
import org.apache.commons.io.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import org.luwrain.io.json.*;

import org.luwrain.pim.mail.obsolete.*;
import static org.luwrain.util.StreamUtils.*;
import static org.luwrain.pim.mail.obsolete.BinaryMessage.*;

public class BinaryMessageTest
{
    private byte[] rawMessage = null;

    @Test public void parsing() throws IOException
    {
	final MailMessage m = fromByteArray(rawMessage);
	assertNotNull(m);
	assertNotNull(m.getFrom());
	assertEquals("Редактор новостей Радио РАНСиС <news@ransis.org>", m.getFrom());
	assertNotNull(m.getSubject());
	assertEquals("[RANSIS:8682] Инфо. \"Спецхран\" (анонс)", m.getSubject());
	assertNotNull(m.getTo());
	assertEquals(1, m.getTo().length);
	assertNotNull(m.getTo()[0]);
	assertEquals("ransis@googlegroups.com", m.getTo()[0]);

	assertNotNull(m.getContentType());
	assertEquals("text/plain; charset=\"UTF-8\"", m.getContentType());
	final String text = m.getText();
	assertNotNull(text);
	assertEquals(1580, text.length());
	final String[] lines = text.split("\n", -1);
	assertEquals(42, lines.length);
	assertEquals("   Уважаемые подписчики,\\r", lines[0].replaceAll("\r", "\\\\r"));

	assertNotNull(m.getMessageId());
	assertEquals("", m.getMessageId());//FIXME:

	final Date d = m.getSentDate();
	assertNotNull(m);
	assertEquals(122, d.getYear());
	assertEquals(9, d.getMonth());
	assertEquals(29, d.getDate());
    }

    @Test public void contentPlainEn() throws Exception
    {
	final MailMessage m = new MailMessage();
	m.setFrom("test1@luwrain.org");
	m.setTo(new String[]{ "test2@luwrain.org" });
	m.setSubject("Testing subject");
	m.setText("Testing text");
	final MessageContentType t = new MessageContentType();
	t.setType(MessageContentType.PLAIN);
	t.setCharset("UTF-8");
	t.setEncoding("");
	m.setContentType(t.toString());

	final Map<String, String> headers = new HashMap<>();
	final javax.mail.internet.MimeMessage mm = convertToMimeMessage(m, headers);
	assertNotNull(mm);
	assertNotNull(mm.getSubject());
	assertEquals("Testing subject", mm.getSubject());
	final Object contentObj = mm.getContent();
	assertNotNull(contentObj);
	assertTrue(contentObj instanceof String);

	final String content = (String)contentObj;
	assertEquals("Testing text", content);
	    }

        @Disabled @Test public void contentPlainRu() throws Exception
    {
	final MailMessage m = new MailMessage();
	m.setFrom("test1@luwrain.org");
	m.setTo(new String[]{ "test2@luwrain.org" });
	m.setSubject("Проверочная тема");
	m.setText("Проверочный текст");
	final MessageContentType t = new MessageContentType();
	t.setType(MessageContentType.PLAIN);
	t.setCharset("UTF-8");
	t.setEncoding("");
	m.setContentType(t.toString());

	final Map<String, String> headers = new HashMap<>();
	final javax.mail.internet.MimeMessage mm = convertToMimeMessage(m, headers);
	assertNotNull(mm);
	assertNotNull(mm.getSubject());
	assertEquals("Проверочная тема", mm.getSubject());
	final Object contentObj = mm.getContent();
	assertNotNull(contentObj);
	assertTrue(contentObj instanceof String);

	final String content = (String)contentObj;
	assertEquals("Testing text", content);
	    }

@Test public void contentBase64Ru() throws Exception
    {
	final String
	text = "Проверочный текст",
	subject = "Проверочная тема";
	final MailMessage m = new MailMessage();
	m.setFrom("test1@luwrain.org");
	m.setTo(new String[]{ "test2@luwrain.org" });
	m.setSubject(subject);
	m.setText(text);
	final MessageContentType t = new MessageContentType();
	t.setType(MessageContentType.PLAIN);
	t.setCharset("UTF-8");
	t.setEncoding(MessageContentType.BASE64);
	m.setContentType(t.toString());

	final Map<String, String> headers = new HashMap<>();
	final javax.mail.internet.MimeMessage mm = convertToMimeMessage(m, headers);
	assertNotNull(mm);
	assertNotNull(mm.getSubject());
	assertEquals(subject, mm.getSubject());
	assertNotNull(mm.getHeader(TRANSFER_ENCODING));
	assertEquals(1, mm.getHeader(TRANSFER_ENCODING).length);
		assertNotNull(mm.getHeader(TRANSFER_ENCODING)[0]);
	assertEquals(MessageContentType.BASE64, mm.getHeader(TRANSFER_ENCODING)[0]);

	final Object contentObj = mm.getContent();
	assertNotNull(contentObj);
	assertTrue(contentObj instanceof String);

	final String content = (String)contentObj;
	assertEquals(text, content);

		final byte[] bytes = mimeToByteArray(mm);
	assertNotNull(bytes);
	final String raw[] = new String(bytes, "UTF-8").replaceAll("\r", "").split("\n", -1);
	assertEquals("", raw[raw.length - 2]);
	assertEquals(new String(Base64.encodeBase64(text.getBytes("UTF-8")), "US-ASCII"), raw[raw.length - 1]);
	    }

    @Test public void contentQuotedPrintableRu() throws Exception
    {
final String
text = "Проверочный текст",
subject = "Проверочная тема";
	final MailMessage m = new MailMessage();
	m.setFrom("test1@luwrain.org");
	m.setTo(new String[]{ "test2@luwrain.org" });
	m.setSubject("Проверочная тема");
	m.setText("Проверочный текст");
	final MessageContentType t = new MessageContentType();
	t.setType(MessageContentType.PLAIN);
	t.setCharset("UTF-8");
	t.setEncoding(MessageContentType.QUOTED_PRINTABLE);
	m.setContentType(t.toString());

	final Map<String, String> headers = new HashMap<>();
	final javax.mail.internet.MimeMessage mm = convertToMimeMessage(m, headers);
	assertNotNull(mm);
	assertNotNull(mm.getSubject());
	assertEquals("Проверочная тема", mm.getSubject());
	assertNotNull(mm.getHeader(TRANSFER_ENCODING));
	assertEquals(1, mm.getHeader(TRANSFER_ENCODING).length);
		assertNotNull(mm.getHeader(TRANSFER_ENCODING)[0]);
	assertEquals(MessageContentType.QUOTED_PRINTABLE, mm.getHeader(TRANSFER_ENCODING)[0]);

	final Object contentObj = mm.getContent();
	assertNotNull(contentObj);
	assertTrue(contentObj instanceof String);

	final String content = (String)contentObj;
	assertEquals("Проверочный текст", content);

	final byte[] bytes = mimeToByteArray(mm);
	assertNotNull(bytes);
	final String raw[] = new String(bytes, "UTF-8").replaceAll("\r", "").split("\n", -1);
assertEquals("", raw[raw.length - 3]);
	final String encoded = MimeUtility.encodeText(text, "UTF-8", "Q").replaceAll("=\\?UTF-8\\?Q\\?", "");
	final int checkFirstNChars = 40;
	assertEquals(raw[raw.length - 2].substring(0, checkFirstNChars), encoded.substring(0, checkFirstNChars));
    }

    @BeforeEach public void readRawMessage() throws IOException
    {
	final ByteArrayOutputStream os = new ByteArrayOutputStream();
	try (final InputStream is = getClass().getResourceAsStream("test.eml")) {
	    IOUtils.copy(is, os);
	}
	this.rawMessage = os.toByteArray();
    }
}
