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

	// MVC
	View view;

	// Variables
	public Connection database;
	public udpServer server;
	public udpClient client;
	public int redID; // Odd
	public int greenID; // Even
	public String input;

	// Player Lists
	public ArrayList<Player> redPlayerList;
	public ArrayList<Player> greenPlayerList;
	public ArrayList<Player> allPlayersList;
	
	// Constructor
	public Model() {

		// Initialize Varables
		redID = 11;
		greenID = 12;
		input = "12";

		redPlayerList = new ArrayList<Player>();
		greenPlayerList = new ArrayList<Player>();

		// Sets up connection to database
		String url = "jdbc:postgresql://localhost/photon";

		try {
			// Connects to the database
			database = DriverManager.getConnection(url, "student", "student");

			// Fills player lists with database entries
			fetchPlayersFromDatabase();
			printPlayersPSQL();
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

	// Adds a player to the database table. No checks
	// Returns true on successful insert, false otherwise
	public boolean addPlayerPSQL(int playerId, String codename, int hardwareID) {
		try {

			// Adds the player to the database
			Statement st = database.createStatement();
			String query = "INSERT INTO players (id, codename) VALUES (" + playerId + ", '" + codename + "')";
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

	// Adds a player to the correct team list and to the main players list
	// Returns true on successful addition
	// Returns false if the player already exists.
	public boolean addPlayerToLists(Player player) {
		allPlayersList.add(player);

		int hardwareID = player.hardwareID;
		if (hardwareID % 2 == 0) {			// Even, Green
			greenPlayerList.add(player);
		}
		else if (hardwareID % 2 == 1) {		// Odd, Red
			redPlayerList.add(player);
		}
		return true;
	}

	// Checks if a player with matching id is located in the all player list
	// Returns the index in the players list if the player exists
	// Returns -1 if the player does not exist
	public int checkPlayerListByID(int id) {
		for (int i = 0; i < allPlayersList.size(); ++i) {
			if (allPlayersList.get(i).playerID == id) {
				return i;
			}
		}
		return -1;
	}

	// Checks if a player with a matching id is located in the red or green player lists
	// Returns true if it exists
	// Returns false otherwise
	public boolean checkTeamListsByID(int id) {

		// Check Red
		for (int i = 0; i < redPlayerList.size(); ++i) {
			if (redPlayerList.get(i).playerID == id) {
				return true;
			}
		}

		// Check Green
		for (int i = 0; i < greenPlayerList.size(); ++i) {
			if (greenPlayerList.get(i).playerID == id) {
				return true;
			}
		}

		return false;
	}

	// Queries for the entire database table and prints the player codenames
	// Mainly for testing / debugging
	public void printPlayersPSQL() {
		try {
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM players");

			System.out.println("Got Players, Printing Players: ");
			while (rs.next()) {
				System.out.println("id: " + rs.getString(1) + ", codename: " + rs.getString(2));
			}

			st.close();
		}
		catch (SQLException e) {
			System.out.println("ERROR retrieving list of players from database [printPlayersPSQL()]");
			System.out.println(e.getMessage());
		}
	}

	// Dumps red and green lists in memory and replaces it with ones in the database
	public void fetchPlayersFromDatabase() {
		try {

			// Queries PSQL Database
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM players");
			System.out.println("Got Players, Populating Team Lists");

			// Dumps current team lists
			redPlayerList = new ArrayList<Player>();
			greenPlayerList = new ArrayList<Player>();
			allPlayersList = new ArrayList<Player>();

			// Populates list with database entries
			while (rs.next()) {
				
				// Gets Player information from query
				int playerID = Integer.parseInt(rs.getString(1));
				String playerName = rs.getString(2);
				System.out.println(playerName);

				Player newPlayer = new Player(playerID, playerName, -1);		// Initialized with hardware ID = -1
				allPlayersList.add(newPlayer);
			}

			st.close();
		}
		catch (SQLException e) {
			System.out.println("ERROR retrieving list of players from database [fetchPlayersFromDatabase()]");
			System.out.println(e.getMessage());
		}
	}

	// MVC components
	public void setView(View v) {
		view = v;
	}
}