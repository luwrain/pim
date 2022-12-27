/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail;

import org.junit.*;
import static org.luwrain.pim.mail.AddressUtils.*;

public class AddressUtilsTest extends Assert
{
    @Test public void personal() throws Exception
    {
	assertEquals("", getPersonal(""));
	assertEquals("", getPersonal("test@test.com"));
	assertEquals("", AddressUtils.getPersonal("<test@test.com>"));
	assertEquals("TEST", getPersonal("TEST <test@test.com>"));
	assertEquals("TEST USER", getPersonal(" TEST USER <test@test.com>"));
	assertEquals("", getPersonal("TEST USER"));
	assertEquals("", getPersonal("TEST USER"));
    }

    @Test public void qp()
    {
	assertEquals("Редактор новостей Радио РАНСиС", getPersonal("=?koi8-r?B?8sXEwcvUz9Igzs/Xz9PUxcog8sHEyc8g8uHu88nz?= <news@ransis.org>"));
	assertEquals("news@ransis.org", getAddress("=?koi8-r?B?8sXEwcvUz9Igzs/Xz9PUxcog8sHEyc8g8uHu88nz?= <news@ransis.org>"));
    }

    @Test public void address()
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
