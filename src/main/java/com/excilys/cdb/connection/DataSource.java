package com.excilys.cdb.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

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

    static {
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return ds.getConnection();
    }
}
