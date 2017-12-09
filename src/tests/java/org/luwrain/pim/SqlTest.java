
package org.luwrain.pim;

import java.sql.*;
import java.io.*;

import org.junit.*;

import org.luwrain.core.*;

public class SqlTest extends Assert
{
    static private final File testDb = new File("/tmp/lwr-pim-test.sqlite");
    
    @Test public void sqliteCreating() throws Exception
    {

	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "", "sqlite-wal");
	assertNotNull(con);
    }

    @After @Before public void deleteTestDb()
    {
	if (testDb.exists())
	    testDb.delete();
    }
}
