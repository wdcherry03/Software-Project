/*
 * Model component of the program.
 * Handles database and backend information.
 * Handles recieving and sending signals
 */

import java.util.*;
import java.sql.*;

public class Model {

	// MVC
	View view;

	// Variables
	public Connection database;

	// Player Lists
	public ArrayList<Player> redPlayerList;			// Red Players List, contains red players currently in play
	public ArrayList<Player> greenPlayerList;		// Green Players List, contains green players currently in play
	public ArrayList<Player> allPlayersList;		// List of player retrieved from the database. Should be synced with the database. Should not be cleared. 

	// Constructor
	public Model() {

		// Initialize Varables
		redPlayerList = new ArrayList<Player>();
		greenPlayerList = new ArrayList<Player>();
		allPlayersList = new ArrayList<Player>();

		// Sets up connection to database
		String url = "jdbc:postgresql://localhost/photon";

		try {
			// Connects to the database
			database = DriverManager.getConnection(url, "student", "student");

			// Fills player lists with database entries
			fetchPlayersFromDatabase();
		} 
		catch (SQLException e) {
			System.out.println("ERROR connecting to database, skipping connection step");
			System.out.println(e.getMessage());
		}
	}

	// Update function. Runs every frame
	public void update() {}

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

	// Clears player ilsts
	public void clearPlayerLists() {
		redPlayerList.clear();
		greenPlayerList.clear();
	}

	// MVC components
	public void setView(View v) {
		view = v;
	}
}