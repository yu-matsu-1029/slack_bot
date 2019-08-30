package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnector {
	private static String driverName = "com.mysql.cj.jdbc.Driver";
	private static String url =ResourceBundle.getBundle("dbconnector").getString("URL");

	private static String user =ResourceBundle.getBundle("dbconnector").getString("USER");
	private static String password = ResourceBundle.getBundle("dbconnector").getString("PASSWORD");

	public Connection getConnection() {
		Connection con = null;

		try {
			Class.forName(driverName);
			con =(Connection)DriverManager.getConnection(url,user,password);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return con;
		}

}
