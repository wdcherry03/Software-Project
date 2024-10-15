/*
 * Model component of the program.
 * Handles database and backend information.
 * Handles recieving and sending signals
 */

import java.util.*;
import java.sql.*;
import java.util.Properties;
import java.net.SocketException;

public class Model {

	// Variables
	public Connection database;
	public udpServer server;
	public udpClient client;
	public int redID; // Odd
	public int greenID; // Even
	public Strgin input;

	// Player Lists
	public ArrayList<Player> redPlayerList;
	public ArrayList<Player> greenPlayerList;


	
	// Constructor
	public Model() {

		// Initialize Varables
		redID = 11;
		greenID = 12;
		input = "12";

		redPlayerList = new ArrayList<Player>;
		greenPlayerList = new ArrayList<Player>;

		// Sets up connection to database
		String url = "jdbc:postgresql://localhost/photon";

		try {
			// Connects to the database
			database = DriverManager.getConnection(url, "student", "student");

			// Fills player lists with database entries
			fillTeamLists();
		} 
		catch (SQLException e) {
			System.out.println("ERROR connecting to database, skipping connection step");
			System.out.println(e.getMessage());
		}

		// UDP stuff
		try{
			server = new udpServer(); // Create server socket
		}
		catch (SocketException e){
			System.out.println("ERROR creating socket");
			System.out.println(e.getMessage());
		}
		client = new udpClient(); // Create client socket
	}

	// Update function. Runs every frame
	public void update() {
		if (input != null)
		{
			client.update(input);
			server.update();
			input = null;
		}
	}

	// Adds a player to the database table
	// Returns true on successful insert, false otherwise
	public boolean addPlayerPSQL(int playerId, String codename) {
		try {
			Statement st = database.createStatement();
			String query = "INSERT INTO players (id, codename) VALUES (" + playerId + ", '" + codename + "')";
			System.out.println(query);

			input = Integer.toString(redID++);

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
			System.out.println("ERROR retrieving list of players from database [printPlayersPSQL()]");
			System.out.println(e.getMessage());
		}
	}

	// Dumps red and green lists in memory and replaces it with ones in the database
	public void fillTeamLists() {
		try {

			// Queries PSQL Database
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM players");
			System.out.println("Got Players, Populating Team Lists");

			// Dumps current team lists
			redPlayerList = new ArrayList<Player>();
			greenPlayerList = new ArrayList<Player>();

			// Populates list with database entries
			while (rs.next()) {
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println("");
			}

			st.close();
		}
		catch (SQLException e) {
			System.out.println("ERROR retrieving list of players from database [fillTeamLists()]");
			System.out.println(e.getMessage());
		}
	}
}