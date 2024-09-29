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
		String url = "jdbc:postgresql://localhost/photon";
		Connection database;

		try {
			// Connects to the database
			database = DriverManager.getConnection(url, "student", "student");

			// Attempts to add two players to the database
			Statement st = database.createStatement();
			st.executeUpdate("INSERT INTO players (id, codename) VALUES (5, 'wolfgang')");

			st = database.createStatement();
			st.executeUpdate("INSERT INTO players (id, codename) VALUES (6, 'griffin')");

			// Attempts to retrieve players from the database
			st = database.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM players");

			// Prints players as a test
			System.out.println("Database Retrieved Players: ");
			while (rs.next()) {
					System.out.println(rs.getString(2));
			}
			rs.close();
			st.close();
		} 
		catch (SQLException e) {
			System.out.println("Error connecting to database, skipping connection step");
			System.out.println(e.getMessage());
		}
	}

	// Update function. Runs every frame
	public void update() {
	}


}