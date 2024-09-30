/*
 * Model component of the program.
 * Handles database and backend information.
 * Handles recieving and sending signals
 */

import java.sql.*;
import java.util.Properties;

public class Model {

	// Variables
	Connection database;

	
	// Constructor
	public Model() {

		// Sets up connection to database
		String url = "jdbc:postgresql://localhost/photon";

		try {
			// Connects to the database
			database = DriverManager.getConnection(url, "student", "student");

			// Attempts to add two players to the database
			// addPlayerPSQL(database, 14, "jem");
			// addPlayerPSQL(database, 15, "gemma");

			// Prints players as a test
			printPlayersPSQL();
		} 
		catch (SQLException e) {
			System.out.println("ERROR connecting to database, skipping connection step");
			System.out.println(e.getMessage());
		}
	}

	// Update function. Runs every frame
	public void update() {
	}

	// Adds a player to the database table
	// Returns true on successful insert, false otherwise
	public boolean addPlayerPSQL(int playerId, String codename) {
		try {
			Statement st = database.createStatement();
			String query = "INSERT INTO players (id, codename) VALUES (" + playerId + ", '" + codename + "')";
			System.out.println(query);

			st.executeUpdate(query);
			st.close();
			return true;
		}
		catch (SQLException e) {
			System.out.println("ERROR inserting player into database");
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Queries for the entire database table and prints the player codenames
	// Mainly for testing / debugging
	public void printPlayersPSQL() {
		try {
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM players");

			System.out.println("Got Players: ");
			while (rs.next()) {
				System.out.println(rs.getString(2));
			}

			st.close();
		}
		catch (SQLException e) {
			System.out.println("ERROR retrieving list of players from database");
			System.out.println(e.getMessage());
		}
	}
}