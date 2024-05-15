
package org.luwrain.pim.mail;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PopularServersTest
{
    @Test public void main() throws Exception
    {
	final var s = PopularServers.getPopularMailServers();
	assertNotNull(s);
	assertFalse(s.isEmpty());
	final var s1 = s.get(0);
	assertNotNull(s1);
	assertNotNull(s1.getSuffixes());
	assertEquals("@yandex.ru", s1.getSuffixes().get(0));
	assertNotNull(s1.getSmtp());
	assertEquals("smtp.yandex.ru", s1.getSmtp().getHost());
	assertNotNull(s1.getPop3());
	assertEquals("pop3.yandex.ru", s1.getPop3().getHost());
    }
}
