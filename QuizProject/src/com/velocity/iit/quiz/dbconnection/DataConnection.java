package com.velocity.iit.quiz.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
	public static Connection connect() {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String url = "jdbc:mysql://localhost:3306/db_quiz";
		String username = "root";
		String password = "1234";
		
		Connection connection = null;
		try {
		connection = DriverManager.getConnection(url, username, password);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
