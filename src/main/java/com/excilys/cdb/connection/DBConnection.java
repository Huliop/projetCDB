package com.excilys.cdb.connection;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

public class DBConnection {
	
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

	private static String jdbcUrl = "jdbc:mysql://localhost:3306/"
	          + "computer-database-db?"
	          + "useUnicode=true"
	          + "&useJDBCCompliantTimezoneShift=true"
	          + "&useLegacyDatetimeCode=false"
	          + "&serverTimezone=UTC";

	private static final String USERNAME = "admincdb";
	private static final String PASSWORD = "qwerty1234";

	public static DBConnection instance;
	
	static {
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds = new HikariDataSource( config );
    }
	
	private DBConnection() { }

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}

		return instance;
	}

	public static Connection getConnection() {
		try {
			return ds.getConnection();
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
