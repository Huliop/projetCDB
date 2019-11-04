package com.excilys.cdb.connection;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class DBConnection {
	@Autowired
	private DataSource ds;

	private DBConnection() { }

	private static Connection instanceConnection;

	public Connection getConnection() {
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
