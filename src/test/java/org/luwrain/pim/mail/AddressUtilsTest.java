
package org.luwrain.pim.mail;

import java.io.*;

import org.junit.*;

import org.luwrain.core.*;

public class AddressUtilsTest extends Assert
{
    @Ignore @Test public void getPersonal() throws Exception
    {
	assertTrue(AddressUtils.getPersonal("").equals(""));
	assertTrue(AddressUtils.getPersonal("test@test.com").equals(""));
	assertTrue(AddressUtils.getPersonal("<test@test.com>").equals(""));
	assertTrue(AddressUtils.getPersonal("TEST <test@test.com>").equals("TEST"));
	assertTrue(AddressUtils.getPersonal(" TEST USER <test@test.com>").equals("TEST USER"));
	assertTrue(AddressUtils.getPersonal("TEST USER <>").equals(""));
	assertTrue(AddressUtils.getPersonal("TEST USER").equals(""));
    }

    @Ignore @Test public void getAddress()
    {
	assertTrue(AddressUtils.getAddress("").equals(""));
	assertTrue(AddressUtils.getAddress("test@test.com").equals("test@test.com"));
	assertTrue(AddressUtils.getAddress("<test@test.com>").equals("test@test.com"));
	assertTrue(AddressUtils.getAddress("TEST <test@test.com>").equals("test@test.com"));
	assertTrue(AddressUtils.getAddress(" TEST USER <test@test.com>").equals("test@test.com"));
	assertTrue(AddressUtils.getAddress("TEST USER <>").equals("TEST USER <>"));
	assertTrue(AddressUtils.getAddress("TEST USER").equals("TEST USER"));
    }
}
