package com.excilys.cdb.connection;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

public class DBConnection {
	
	private static HikariConfig config = new HikariConfig("/DBProperties");
    private static HikariDataSource ds = new HikariDataSource(config);

	public static DBConnection instance;
	
	private DBConnection() { }
	
	private static Connection instanceConnection; 

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}

		return instance;
	}

	public static Connection getConnection() {
		if (instanceConnection == null) {
			try {
				instanceConnection = ds.getConnection();
			} catch (SQLException e) {
				System.out.println("Unable to connect to DB : " + e.getMessage());
				System.exit(0);
			}
		}
		return instanceConnection;
	}	
	
	public static Connection closeConnection() {
		if (instanceConnection != null) {
			try {
				instanceConnection.close();
				instanceConnection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return instanceConnection;
	}
}
