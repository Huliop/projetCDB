package com.excilys.cdb.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

public class DBConnection {

	private static String jdbcUrl = "jdbc:mysql://localhost:3306/"
	          + "computer-database-db?"
	          + "useUnicode=true"
	          + "&useJDBCCompliantTimezoneShift=true"
	          + "&useLegacyDatetimeCode=false"
	          + "&serverTimezone=UTC";

	private static final String USERNAME = "admincdb";
	private static final String PASSWORD = "qwerty1234";

	public static DBConnection instance;

	private DBConnection() { }

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}

		return instance;
	}

	public static Connection getConnection() {
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Unable to connect to DB : " + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public void changeURLToTest() {
		jdbcUrl = "jdbc:h2:mem:mydb;INIT=RUNSCRIPT FROM '~/Téléchargements/cdb-sprint-1/src/test/resources/init_mydb.sql'";
	}

	public String getJdbc() {
		return jdbcUrl;
	}
}
