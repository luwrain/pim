/*
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface MailEssential
{
	// make MimeMessage from class fields
	public void PrepareInternalStore(MailMessage msg) throws Exception;
	// used to fill fields via .eml file stream
	public MailMessage loadMailFromFile(FileInputStream fs) throws Exception;
	// used to save fields to .eml field stream
	public void SaveMailToFile(MailMessage msg,FileOutputStream fs) throws Exception;

}
