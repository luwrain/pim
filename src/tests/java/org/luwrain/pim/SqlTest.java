
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
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	final InputStream is = getClass().getResourceAsStream("/org/luwrain/pim/news.sqlite");
	assertNotNull(is);
	try {
	    final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder b = new StringBuilder();
	    String line = reader.readLine();
	    while (line != null)
	    {
		if (line.trim().isEmpty())
		{
		    execSql(con, new String(b));
		    b = new StringBuilder();
		} else
		    b.append(line + " ");
		line = reader.readLine();
	    }
	    execSql(con, new String(b));
	}
	finally {
	    is.close();
	}
	con.close();
    }

    private void execSql(Connection con, String query) throws SQLException
    {
	if (query.trim().isEmpty())
	    return;
	final Statement st = con.createStatement();
	st.executeUpdate(query);
    }

    @After @Before public void deleteTestDb()
    {
	if (testDb.exists())
   	    testDb.delete();
    }
}
