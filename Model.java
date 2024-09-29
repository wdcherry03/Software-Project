/*
 * Model component of the program.
 * Handles database and backend information.
 * Handles recieving and sending signals
 */

import java.sql.*;
import java.util.Properties;

public class Model {

	
	// Constructor
	public Model() {

		// Sets up connection to database
		String url = "jdbc:postgresql://localhost/players";
		Properties props = new Properties();
		props.setProperty("user", "student");
		props.setProperty("password", "student");
		Connection database;

		try {
			database = DriverManager.getConnection(url, props);
		} 
		catch (SQLException e) {
			System.out.println("Error connecting to database, skipping connection step");
		}
	}

	// Update function. Runs every frame
	public void update() {
	}


}