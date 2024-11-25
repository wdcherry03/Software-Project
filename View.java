/* 
 * View Component
 * Handles UI, messages, toasts, and other visual components.
 * Also handles the starting splash screen and other screen overlays
 * Calls other components to handle signals and database stuff.
 */

 import java.util.*;
import java.util.List;
import java.awt.*;
 import javax.swing.*;
 import java.awt.event.*;

public class View extends JFrame {

	// MVC Components
	public Model model;
	public Controller controller;
	public JFrame frame;
    public String state;
    public javax.swing.Timer gameTimer; 
    public ArrayList<PlayerPanel> entries;
    public Audio audio; //Initial Audio object
    public udpServer server; // UDP server socket
    public JPanel gameLogArea;

    // Booleans for game states
    public boolean gameStart = false;
    public boolean gameEnd = false;

    // Maps for player list
    public Map<Integer, JPanel> redPlayerRows = new HashMap<>();
    public Map<Integer, JPanel> greenPlayerRows = new HashMap<>();

    // Constructor
	public View(Model m, Controller c) {

		// MVC
		model = m;
		controller = c;
        entries = new ArrayList<PlayerPanel>();
        this.addKeyListener(c);
        audio = new Audio();
        server = new udpServer(7501, m); // Initialize UDP server at port 7501

		// Set JFrame data
		this.setTitle("Laser Tag");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);

		// Loads splash screen
		splash splashScreen = new splash();
		this.getContentPane().add(splashScreen);
		this.setVisible(true);

		// Sleeps 3 seconds
		try {
            Thread.sleep(3000);
        }
        catch(InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

		// Switch to entry screen
        state = "entry";
		this.runEntry();
	}

	// Update function. Runs every frame
	public void update() {
        this.repaint();
        this.setVisible(true);
	}

	// Dumps anything on the screen
	public void dumpScreen() {
        this.removeAll();
	}

    public void resetGameState() {
        gameStart = false;
        gameEnd = false;
    }

	// Runs the entry screen
	public void runEntry() {

		// Clears JFRame
		this.getContentPane().removeAll();

		// Create panels for teams
        JPanel redTeamPanel = new JPanel(new GridLayout(20, 1));
        JPanel greenTeamPanel = new JPanel(new GridLayout(20, 1));
		JPanel buttonPanel;

        // Add table headers for RED
		JPanel redTeamHeader = new JPanel(new GridLayout(1, 3));
        redTeamHeader.add(new JLabel("PLAYER NAME", JLabel.LEFT));
        redTeamHeader.add(new JLabel("PLAYER ID", JLabel.LEFT));
        redTeamHeader.add(new JLabel("HARDWARE ID", JLabel.LEFT));
		redTeamHeader.setBackground(Color.RED);

		redTeamPanel.add(redTeamHeader);
        redTeamPanel.setBackground(Color.RED);

        // Add table headers for GREEN
        JPanel greenTeamHeader = new JPanel(new GridLayout(1, 3));
        greenTeamHeader.add(new JLabel("PLAYER NAME", JLabel.LEFT));
        greenTeamHeader.add(new JLabel("PLAYER ID", JLabel.LEFT));
        greenTeamHeader.add(new JLabel("HARDWARE ID", JLabel.LEFT));
        greenTeamHeader.setBackground(Color.GREEN);

        greenTeamPanel.add(greenTeamHeader);
        greenTeamPanel.setBackground(Color.GREEN);

        // Create panel to hold both team panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(redTeamPanel);
        mainPanel.add(greenTeamPanel);

        // Setting buttons
        buttonPanel = new JPanel(new GridLayout(1, 8));
        buttonPanel.add(new JButton("Edit Game"));
        buttonPanel.add(new JButton("Game Parameters"));
        buttonPanel.add(new JButton("Start Game"));
        buttonPanel.add(new JButton("Preferred Games"));
        buttonPanel.add(new JButton("View Game"));
        buttonPanel.add(new JButton("Flick Sync"));
        buttonPanel.add(new JButton("Clear Game"));

        // Player Add buttons
        JPanel playerEntryPanel = new JPanel(new GridLayout(2, 1));

        JTextField playerEntryDialogue = new JTextField("Welcome to the player entry screen! Input information in the boxes above to get started.");
        playerEntryDialogue.setEditable(false);

        JPanel playerEntryButtons = new JPanel(new GridLayout(1, 4));
        JButton playerAddButton = new JButton("Add Player");
        JTextField playerAddIdField = new JTextField("Enter ID Here");
        JTextField playerAddNameField = new JTextField("Enter Player Codename Here");
        JTextField playerAddHardwareId = new JTextField("Enter Hardware Id Here");
        playerEntryButtons.add(playerAddButton);
        playerEntryButtons.add(playerAddNameField);
        playerEntryButtons.add(playerAddIdField);
        playerEntryButtons.add(playerAddHardwareId);

        // Add key listeners
        playerAddButton.addKeyListener(controller);
        playerAddNameField.addKeyListener(controller);
        playerAddIdField.addKeyListener(controller);
        playerAddHardwareId.addKeyListener(controller);

        // Add buttons to the panel
        playerEntryPanel.add(playerEntryButtons);
        playerEntryPanel.add(playerEntryDialogue);

        // Adds a player when the player add button is called 
        playerAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int hardwareId = 0;
                int playerId = 0;
                // Handle errors if hardware/player ID isn't an int
                try {
                    hardwareId = Integer.parseInt(playerAddHardwareId.getText());
                    playerId = Integer.parseInt(playerAddIdField.getText());
                }
                catch (NumberFormatException e) {
                    playerEntryDialogue.setText("Hardware/Player ID was empty/not a number. Not adding the requested player.");
                    return;
                }

                // Check to see if 202/221/53/43 (important codes) are used as hardware ID
                if (hardwareId == 202 || hardwareId == 221 || hardwareId == 53 || hardwareId == 43) {
                    playerEntryDialogue.setText("Invalid hardware ID entered. Not adding the requested player.");
                    return;
                }

                // Check if codename entry box is empty
                if (playerAddNameField.getText().isEmpty()) {
                    playerEntryDialogue.setText("Codename was empty. Not adding the requested player.");
                    return;
                }

                // Checks if the id already exists
                int foundPlayerIndex = model.checkPlayerListByID(playerId);
                int foundPlayerHardware = model.checkPlayerListByHardware(hardwareId);

                // Handle duplicate hardware ID's
                if (foundPlayerHardware != -1) {
                    playerEntryDialogue.setText("Hardware ID already added. Not adding the requested player.");
                    return;
                }

                // If the player exists, uses already existing player data and adds hardware id
                else if (foundPlayerIndex >= 0) {
                    playerEntryDialogue.setText("Player already found. Using existing information...");

                    // Checks if the player is already added
                    if (model.checkTeamListsByID(playerId)) {
                        playerEntryDialogue.setText("Player already added. Not adding the requested player.");
                        return;
                    }

                    // Updates player list entry with the hardware ID
                    Player updatedPlayer = new Player(model.allPlayersList.get(foundPlayerIndex));
                    updatedPlayer.hardwareID = Integer.parseInt(playerAddHardwareId.getText());
                    model.allPlayersList.get(foundPlayerIndex).hardwareID = Integer.parseInt(playerAddHardwareId.getText());

                    // Adds panel to the panel list and player to the correct player lists
                    PlayerPanel newPanel = new PlayerPanel(updatedPlayer);
                    if (hardwareId % 2 == 0) {           // Green
                        greenTeamPanel.add(newPanel);
                        model.greenPlayerList.add(updatedPlayer);
                    }

                    else if (hardwareId % 2 == 1) {      // Red
                        redTeamPanel.add(newPanel);
                        model.redPlayerList.add(updatedPlayer);
                    }
                    entries.add(newPanel);
                }

                // If the player doesn't exist, adds them to the correct player list and the database
                else {
                    // Adds to the database and player lists
                    playerEntryDialogue.setText("Player not found. Adding player to the database...");

                    String codename = playerAddNameField.getText();
                    Player newPlayer = new Player(playerId, codename, hardwareId);
                    model.addPlayerPSQL(playerId, codename, hardwareId);
                    model.addPlayerToLists(newPlayer);

                    // Adds to the player to the correct panel
                    PlayerPanel newPanel = new PlayerPanel(newPlayer);

                    if (hardwareId % 2 == 0) {           // Green
                        greenTeamPanel.add(newPanel);
                    }
                    else if (hardwareId % 2 == 1) {      // Red
                        redTeamPanel.add(newPanel);
                    }
                    entries.add(newPanel);
                }

                // Transmit hardware ID from server after player is added
                server.send(String.valueOf(hardwareId));

                update();
            }
        });

        // Adds any existing players in the player lists to the player panels

        // Red
        for (int i = 0; i < model.redPlayerList.size(); ++i) {
            PlayerPanel newPanel = new PlayerPanel(model.redPlayerList.get(i));
            redTeamPanel.add(newPanel);
            entries.add(newPanel); // Adds panels to arrayList
        }

        // Green
        for (int i = 0; i < model.greenPlayerList.size(); ++i) {
            PlayerPanel newPanel = new PlayerPanel(model.greenPlayerList.get(i));
            redTeamPanel.add(newPanel);
            entries.add(newPanel); // Adds panels to arrayList
        }
        
        // Add panels to frame
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(playerEntryPanel, BorderLayout.SOUTH);

        this.setVisible(true);
        // Attempting to remove all player panels upon keypress
        if(controller.remove)
        {
            for(int i = 0; i < entries.size(); i++)
            {
                entries.get(i).removeAll();
                entries.get(i).setVisible(false);
            }
        }
        this.repaint();
	}

    // Function to clear all arrayLists upon keypress
    public void clearEntries()
    {
        entries.clear();
        model.clearPlayerLists();
    }

    public void runGame() {
        // Clears JFrame
        this.getContentPane().removeAll();
    
        // Panel for red and green teams' scoreboards
        JPanel redTeamPanel = new JPanel(new GridLayout(21, 1)); // 21 rows, 20 players
        JPanel greenTeamPanel = new JPanel(new GridLayout(21, 1)); // 21 rows, 20 players
    
        // Add table headers for RED Team
        JPanel redTeamHeader = new JPanel(new GridLayout(1, 3));
        redTeamHeader.add(new JLabel("PLAYER NAME", JLabel.LEFT));
        redTeamHeader.add(new JLabel("PLAYER ID", JLabel.LEFT));
        redTeamHeader.add(new JLabel("SCORE", JLabel.LEFT));
        redTeamHeader.setBackground(Color.RED);
        redTeamPanel.add(redTeamHeader);
        redTeamPanel.setBackground(Color.RED);
    
        // Add table headers for GREEN Team
        JPanel greenTeamHeader = new JPanel(new GridLayout(1, 3));
        greenTeamHeader.add(new JLabel("PLAYER NAME", JLabel.LEFT));
        greenTeamHeader.add(new JLabel("PLAYER ID", JLabel.CENTER));
        greenTeamHeader.add(new JLabel("SCORE", JLabel.RIGHT));
        greenTeamHeader.setBackground(Color.GREEN);
        greenTeamPanel.add(greenTeamHeader);
        greenTeamPanel.setBackground(Color.GREEN);
    
        // Initialize total scores for both teams
        int redTotalScore = 0;
        int greenTotalScore = 0;
    
        // Populate red team players with scores and accumulate the total score
        for (Player redPlayer : model.redPlayerList) {
            JPanel playerRow = createPlayerRow(redPlayer, true);  // Create player row for red team
            redTeamPanel.add(playerRow);
            redPlayerRows.put(redPlayer.playerID, playerRow); // Store the row in the map
            redTotalScore += redPlayer.score; // Add player's score to the cumulative score
        }
    
        // Populate green team players with scores and accumulate the total score
        for (Player greenPlayer : model.greenPlayerList) {
            JPanel playerRow = createPlayerRow(greenPlayer, false); // Create player row for green team
            greenTeamPanel.add(playerRow);
            greenPlayerRows.put(greenPlayer.playerID, playerRow); // Store the row in the map
            greenTotalScore += greenPlayer.score; // Add player's score to the cumulative score
        }
    
        // Add cumulative score row for the RED team
        JPanel redTotalScoreRow = new JPanel(new GridLayout(1, 3));
        redTotalScoreRow.add(new JLabel("TOTAL", JLabel.LEFT));
        redTotalScoreRow.add(new JLabel("", JLabel.CENTER)); // Empty label for the Player ID column
        redTotalScoreRow.add(new JLabel(String.valueOf(redTotalScore), JLabel.RIGHT));
        redTeamPanel.add(redTotalScoreRow);
    
        // Add cumulative score row for the GREEN team
        JPanel greenTotalScoreRow = new JPanel(new GridLayout(1, 3));
        greenTotalScoreRow.add(new JLabel("TOTAL", JLabel.LEFT));
        greenTotalScoreRow.add(new JLabel("", JLabel.CENTER)); // Empty label for the Player ID column
        greenTotalScoreRow.add(new JLabel(String.valueOf(greenTotalScore), JLabel.RIGHT));
        greenTeamPanel.add(greenTotalScoreRow);
    
        // Create panel to hold both team panels side by side
        JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
        teamsPanel.add(redTeamPanel);
        teamsPanel.add(greenTeamPanel);
    
        // Game log panel
        JPanel gameLogPanel = new JPanel(new BorderLayout());
        gameLogArea = new JPanel(new GridLayout(8, 1));
        JTextField newGameLog = new JTextField("Game Started.");
        newGameLog.setEditable(false);
        gameLogArea.add(newGameLog);
        gameLogPanel.add(gameLogArea, BorderLayout.CENTER);
        gameLogPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
    
        // Timer Panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel timerLabel = new JLabel("00:30"); // Startup timer starts at 30 seconds
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerPanel.add(timerLabel);
        timerPanel.setBorder(BorderFactory.createTitledBorder("Startup Timer"));
    
        // Startup and Game Timer logic
        final int startupDuration = 30; // 30 seconds for startup timer
        final int gameDuration = 360; // 6 minutes for game timer
        startStartupTimer(startupDuration, gameDuration, timerLabel, timerPanel);
    
        // Layout for the main game screen
        JPanel gameScreen = new JPanel(new BorderLayout());
        gameScreen.add(teamsPanel, BorderLayout.NORTH);
        gameScreen.add(gameLogPanel, BorderLayout.CENTER);
        gameScreen.add(timerPanel, BorderLayout.SOUTH);
    
        // Add the entire game screen to the JFrame
        this.add(gameScreen);
        this.revalidate();
        this.repaint();
    }    

    // Helper to handle the startup timer
    private void startStartupTimer(int startupDuration, int gameDuration, JLabel timerLabel, JPanel timerPanel) {
        final int[] secondsRemaining = {startupDuration};
        gameTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining[0]--;
                updateTimerLabel(secondsRemaining[0], timerLabel);

                // Separate Thread to run tracks without interfering with game timer
                new Thread(() -> {
                    if (secondsRemaining[0] == 18) {
                        audio.run();
                    }
                }).start();

                if (secondsRemaining[0] <= 0) {
                    gameTimer.stop();
                    startGameTimer(gameDuration, timerLabel, timerPanel);
                }
            }
        });
        gameTimer.start();
    }

    public JPanel createPlayerRow(Player player, boolean isRedTeam) {
        // Create panel with 4 columns
        JPanel playerRow = new JPanel(new GridLayout(1, 4));
    
        // Add player details
        playerRow.add(new JLabel(player.codename, JLabel.LEFT));
        playerRow.add(new JLabel(String.valueOf(player.playerID), JLabel.CENTER));
        playerRow.add(new JLabel(String.valueOf(player.score), JLabel.RIGHT)); // Assuming Player class has 'score'
    
        // Check if the player is at base and add a stylized "B" if true
        if (player.atBase) {
            JLabel baseIcon = new JLabel("B", JLabel.CENTER);
            baseIcon.setFont(new Font("Arial", Font.BOLD, 16));
            baseIcon.setForeground(Color.BLUE);
            playerRow.add(baseIcon);
        } else {
            // Add empty label for alignment if not at base
            JLabel temp = new JLabel();
            playerRow.add(temp);
        }
    
        // Optional: Add key listener for player row (assuming controller is already defined)
        playerRow.addKeyListener(controller);
    
        return playerRow;
    }    

    public void updatePlayerRow(Player player, boolean isRedTeam) {
        // Find the correct map based on the team
        Map<Integer, JPanel> playerRows = isRedTeam ? redPlayerRows : greenPlayerRows;
    
        // Find the player's row by player ID
        JPanel playerRow = playerRows.get(player.playerID);
        
        if (playerRow != null) {
            // Update the labels with new information (name, ID, score)
            JLabel nameLabel = (JLabel) playerRow.getComponent(0);
            JLabel idLabel = (JLabel) playerRow.getComponent(1);
            JLabel scoreLabel = (JLabel) playerRow.getComponent(2);
            JLabel baseLabel = (JLabel) playerRow.getComponent(3); // The base icon is the 4th component
    
            // Update the player information in the row
            nameLabel.setText(player.codename);
            idLabel.setText(String.valueOf(player.playerID));
            scoreLabel.setText(String.valueOf(player.score));
    
            // Update the "B" base icon
            if (player.atBase) {
                baseLabel.setText("B");
                baseLabel.setForeground(Color.BLUE); // Ensure the base icon is visible and styled
            } else {
                baseLabel.setText(""); // Remove the "B" if the player is not at base
            }
        }
    }
    
    public void updateAllPlayerRows() {
        // Update all red team player rows
        for (Player redPlayer : model.redPlayerList) {
            updatePlayerRow(redPlayer, true);
        }
    
        // Update all green team player rows
        for (Player greenPlayer : model.greenPlayerList) {
            updatePlayerRow(greenPlayer, false);
        }
    }

    // Helper to handle the game timer
    private void startGameTimer(int gameDuration, JLabel timerLabel, JPanel timerPanel) {
        final int[] secondsRemaining = {gameDuration};
        timerPanel.setBorder(BorderFactory.createTitledBorder("Game Timer"));
        timerLabel.setText(formatTime(gameDuration));

        // Send "Game Start" signal 202 after countdown timer finishes
        if (!gameStart) {
            // Separate Thread to run UDP server
            new Thread(() -> {
                server.run();
            }).start();
            server.send("202");
            gameStart = true;
        }

        gameTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining[0]--;
                updateTimerLabel(secondsRemaining[0], timerLabel);

                if (secondsRemaining[0] <= 0) {
                    gameTimer.stop();
                    endGame();
                }
            }
        });
        gameTimer.start();
    }

    // Helper to update the timer label
    private void updateTimerLabel(int secondsRemaining, JLabel timerLabel) {
        int minutes = secondsRemaining / 60;
        int remainingSeconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }

    // Helper to format time as "MM:SS"
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Helper to handle game end logic
    private void endGame() {
        if (!gameEnd) {
            for (int i = 0; i < 3; i++) {
                server.send("221");
            }
            gameEnd = true;
        }
        repaint();
    }

    // Event handling
    public void eventOccured(int hardware1, int hardware2) {

        if (gameEnd)
            return;

        // Transmit codes based on received data (odd red, even green)
        if (hardware2 == 53) {
            // Red base (code 53)
            System.out.println(hardware1 + " hit the red base");

            // Updates the game log area
            JTextField newGameLog = new JTextField(model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).codename + " hit the red base");
            newGameLog.setEditable(false);
            newGameLog.addKeyListener(controller);

            // Removes the first entry before entering new entry if we're at limit
            if (gameLogArea.getComponents().length >= 8) {
                gameLogArea.remove(gameLogArea.getComponents()[0]);
            }
            gameLogArea.add(newGameLog);

            if (hardware1 % 2 == 0) {
                // Green player hit red base, +100 points & stylized B to left of codename
                model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).hitBase();
            }
            server.send(String.valueOf(hardware2));
        }
        else if (hardware2 == 43) { 
            // Green base (code 43)
            System.out.println(hardware1 + " hit the green base");

            // Updates the game log area
            JTextField newGameLog = new JTextField(model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).codename + " hit the green base");
            newGameLog.setEditable(false);
            newGameLog.addKeyListener(controller);

            // Removes the first entry before entering new entry if we're at limit
            if (gameLogArea.getComponents().length >= 8) {
                gameLogArea.remove(gameLogArea.getComponents()[0]);
            }
            gameLogArea.add(newGameLog);

            if (hardware1 % 2 == 1) {
                // Red player hit green base, +100 points & stylized B to left of codename
                model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).hitBase();
            }
            server.send(String.valueOf(hardware2));
        }
        else if (hardware1 % 2 != hardware2 % 2) {
            // Player hit other team, transmit hit player id
            System.out.println(hardware1 + " hit an enemy player");

            // Updates the game log area
            JTextField newGameLog = new JTextField(model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).codename + " hit the enemy player " + model.allPlayersList.get(model.checkPlayerListByHardware(hardware2)).codename);
            newGameLog.setEditable(false);
            newGameLog.addKeyListener(controller);

            // Removes the first entry before entering new entry if we're at limit
            if (gameLogArea.getComponents().length >= 8) {
                gameLogArea.remove(gameLogArea.getComponents()[0]);
            }
            gameLogArea.add(newGameLog);

            model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).hitEnemyPlayer();
            server.send(String.valueOf(hardware2));
            // +10 points for player
        }
        else {
            // Player hit same team, transmit own player id
            System.out.println(hardware1 + " hit a teammate");

            // Updates the game log area
            JTextField newGameLog = new JTextField(model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).codename + " hit an ally player " + model.allPlayersList.get(model.checkPlayerListByHardware(hardware2)).codename);
            newGameLog.setEditable(false);
            newGameLog.addKeyListener(controller);

            // Removes the first entry before entering new entry if we're at limit
            if (gameLogArea.getComponents().length >= 8) {
                gameLogArea.remove(gameLogArea.getComponents()[0]);
            }
            gameLogArea.add(newGameLog);

            model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).hitTeamPlayer();
            server.send(String.valueOf(hardware1));
            // -10 points for player
        }

        model.sortTeamsByScore();
        updateAllPlayerRows();
        this.repaint();
        this.scrollBoxUpdate(hardware1, hardware2);
    }

    public void actionScreenUpdate() {
        // Update play action screen here
    }

    public void scrollBoxUpdate(int hardware1, int hardware2) {
        // Update scroll box here
        String codename1 = model.allPlayersList.get(model.checkPlayerListByHardware(hardware1)).codename;
        if (hardware2 == 43 || hardware2 == 53) {
            System.out.println(codename1 + " hit the base");
        }
        else{
            String codename2 = model.allPlayersList.get(model.checkPlayerListByHardware(hardware2)).codename;
            System.out.println(codename1 + " hit " + codename2);
        }

        this.repaint();
    }
}