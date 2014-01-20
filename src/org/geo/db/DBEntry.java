package org.geo.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DBEntry {
	public static Connection getConnection(){
		Connection c = null;
		c = LocalDB.getConnection();
//		try {
//			c = DB.getPool().getConnection();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return c;
	}
}
