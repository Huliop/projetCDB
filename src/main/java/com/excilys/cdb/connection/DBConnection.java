package com.excilys.cdb.connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {
	
	private static final String jdbcUrl = "jdbc:mysql://localhost:3307/"
	          + "computer-database-db?"
	          + "useUnicode=true"
	          + "&useJDBCCompliantTimezoneShift=true"
	          + "&useLegacyDatetimeCode=false"
	          + "&serverTimezone=UTC";
	
	private static final String username = "admincdb";
	private static final String password = "qwerty1234";
	
	public static Connection getConnection() {
		try {;
			return DriverManager.getConnection(jdbcUrl, username, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect to DB : " + e.getMessage());
			System.exit(0);
		}
		return null;
	}

}
