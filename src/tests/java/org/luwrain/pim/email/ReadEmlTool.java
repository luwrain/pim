
package org.luwrain.pim.email;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;

public class ReadEmlTool
{
    public static void main(String[] args) throws Exception
    {
	try {
	EmailEssentialJavamail mail = new EmailEssentialJavamail();
	EmailMessage message = mail.LoadEmailFromFile(new FileInputStream(args[0]));
	final String fileName = constructFileName(message.from, message.sentDate.toString());
	Path path = Paths.get(fileName);
	try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
	    {
		writer.write(args[0]);
		writer.newLine();
		writer.write("From: " + message.from);
		writer.newLine();
		writer.write("Subject: " + message.subject);
		writer.newLine();
	writer.write("Date: " + message.sentDate);
	writer.newLine();
	writer.newLine();
	writer.write(message.baseContent);
	writer.newLine();
	    }
	}
	catch (Exception e)
	{
	    System.out.println("failed " + args[0]);
	}
    }

    private static String constructFileName(String from, String date)
    {
	StringBuilder b = new StringBuilder();
	for(int i = 0;i < from.length();++i)
	{
	    final char c = from.charAt(i);
	    b.append((Character.isDigit(c) || Character.isLetter(c))?c:'_');
	}
	b.append("-");
	for(int i = 0;i < date.length();++i)
	{
	    final char c = date.charAt(i);
	    b.append((Character.isDigit(c) || Character.isLetter(c))?c:'_');
	}
	return b.toString();


    }
}
