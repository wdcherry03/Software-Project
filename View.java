/* 
 * View Component
 * Handles UI, messages, toasts, and other visual components.
 * Also handles the starting splash screen and other screen overlays
 * Calls other components to handle signals and database stuff.
 */

 import java.util.*;
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

    // Booleans for game states
    public boolean gameStart = false;
    public boolean gameEnd = false;

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

                // Checks if the id already exists
                int foundPlayerIndex = model.checkPlayerListByID(playerId);

                // If the player exists, uses already existing player data and adds hardware id
                if (foundPlayerIndex >= 0) {
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
                    // Check if codename entry box is empty (only if player ID doesn't already exist in DB)
                    if (playerAddNameField.getText().isEmpty()) {
                        playerEntryDialogue.setText("Codename was empty. Not adding the requested player.");
                        return;
                    }

                    // Adds to the database and player lists
                    playerEntryDialogue.setText("Player not found. Adding player to the database...");

                    String codename = playerAddNameField.getText();
                    Player newPlayer = new Player(playerId, codename, hardwareId);
                    // model.addPlayerPSQL(playerId, codename, hardwareId);
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

    // Runs the game display
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
        greenTeamHeader.add(new JLabel("PLAYER ID", JLabel.LEFT));
        greenTeamHeader.add(new JLabel("SCORE", JLabel.LEFT));
        greenTeamHeader.setBackground(Color.GREEN);
    
        greenTeamPanel.add(greenTeamHeader);
        greenTeamPanel.setBackground(Color.GREEN);
    
        // Initialize total scores for both teams
        int redTotalScore = 0;
        int greenTotalScore = 0;
    
        // Populate red team players with scores and accumulate the total score
        for (Player redPlayer : model.redPlayerList) {
            JPanel playerRow = new JPanel(new GridLayout(1, 4)); // Updated to 4 columns
            playerRow.add(new JLabel(redPlayer.codename, JLabel.LEFT));
            playerRow.add(new JLabel(String.valueOf(redPlayer.playerID), JLabel.CENTER));
            playerRow.add(new JLabel(String.valueOf(redPlayer.score), JLabel.RIGHT)); // Assuming Player class has 'score'

            // Check if the player is at base and add a stylized "B" if true
            if (redPlayer.atBase) {
                JLabel baseIcon = new JLabel("B", JLabel.CENTER);
                baseIcon.setFont(new Font("Arial", Font.BOLD, 16));
                baseIcon.setForeground(Color.BLUE);
                playerRow.add(baseIcon);
            } else {
                playerRow.add(new JLabel()); // Placeholder for alignment
            }

            redTeamPanel.add(playerRow);
            redTotalScore += redPlayer.score; // Add player's score to the cumulative score
        }

        // Populate green team players with scores and accumulate the total score
        for (Player greenPlayer : model.greenPlayerList) {
            JPanel playerRow = new JPanel(new GridLayout(1, 4)); // Updated to 4 columns
            playerRow.add(new JLabel(greenPlayer.codename, JLabel.LEFT));
            playerRow.add(new JLabel(String.valueOf(greenPlayer.playerID), JLabel.CENTER));
            //playerRow.add(new JLabel(String.valueOf(greenPlayer.score), JLabel.RIGHT)); // Assuming Player class has 'score'
            playerRow.add(new JLabel(String.format("%d",greenPlayer.score), JLabel.RIGHT));

            // Check if the player is at base and add a stylized "B" if true
            if (greenPlayer.atBase) {
                JLabel baseIcon = new JLabel("B", JLabel.CENTER);
                baseIcon.setFont(new Font("Arial", Font.BOLD, 16));
                baseIcon.setForeground(Color.BLUE);
                playerRow.add(baseIcon);
            } else {
                playerRow.add(new JLabel()); // Placeholder for alignment
            }

            greenTeamPanel.add(playerRow);
            greenTotalScore += greenPlayer.score; // Add player's score to the cumulative score
        }
    
        // Add cumulative score row for the RED team
        JPanel redTotalScoreRow = new JPanel(new GridLayout(1, 3));
        redTotalScoreRow.add(new JLabel("TOTAL", JLabel.LEFT));
        redTotalScoreRow.add(new JLabel("", JLabel.LEFT)); // Empty label for the Player ID column
        redTotalScoreRow.add(new JLabel(String.valueOf(redTotalScore), JLabel.LEFT));
        redTeamPanel.add(redTotalScoreRow);
    
        // Add cumulative score row for the GREEN team
        JPanel greenTotalScoreRow = new JPanel(new GridLayout(1, 3));
        greenTotalScoreRow.add(new JLabel("TOTAL", JLabel.LEFT));
        greenTotalScoreRow.add(new JLabel("", JLabel.LEFT)); // Empty label for the Player ID column
        greenTotalScoreRow.add(new JLabel(String.valueOf(greenTotalScore), JLabel.LEFT));
        greenTeamPanel.add(greenTotalScoreRow);
    
        // Create panel to hold both team panels side by side
        JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
        teamsPanel.add(redTeamPanel);
        teamsPanel.add(greenTeamPanel);
    
        // Game log panel
        JPanel gameLogPanel = new JPanel(new BorderLayout());
        JTextArea gameLogArea = new JTextArea(10, 50);
        gameLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameLogArea);
        gameLogPanel.add(scrollPane, BorderLayout.CENTER);
        gameLogPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
    
        // Timer Panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel timerLabel = new JLabel("00:30"); // Startup timer starts at 30 seconds
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerPanel.add(timerLabel);
        timerPanel.setBorder(BorderFactory.createTitledBorder("Startup Timer"));

        // Startup and Game Timer logic
        final int[] secondsRemaining = {30}; // Initial 30 seconds for startup
        gameTimer = new javax.swing.Timer(1000, new ActionListener() {
            boolean isStartupPhase = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStartupPhase) {
                    // Countdown for the startup phase
                    secondsRemaining[0]--;
                    int minutes = secondsRemaining[0] / 60;
                    int remainingSeconds = secondsRemaining[0] % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes, remainingSeconds));

                    if (secondsRemaining[0] <= 0) {
                        // Transition from startup to game timer
                        isStartupPhase = false;
                        secondsRemaining[0] = 10; // 6 minutes in seconds
                        timerPanel.setBorder(BorderFactory.createTitledBorder("Game Timer"));
                        timerLabel.setText("06:00");
                        
                        // Send "Game Start" signal 202 after countdown timer finishes
                        if(!gameStart) {
                            //Separate Thread to run UDP server
                            new Thread(() -> 
                            {
                                server.run();
                            }).start();
                            server.send("202");
                            gameStart = true;
                        }
                    }
                    //Separate Thread to run tracks without interfering with game timer
                    new Thread(() -> 
                    {
                        if(minutes == 0 && remainingSeconds == 18)
                        {
                            audio.run();
                        }
                    }).start();
                } else {
                    // Countdown for the game phase
                    secondsRemaining[0]--;
                    int minutes = secondsRemaining[0] / 60;
                    int remainingSeconds = secondsRemaining[0] % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes, remainingSeconds));

                    if (secondsRemaining[0] <= 0) {
                        // End of the game timer
                        gameTimer.stop();
                        timerLabel.setText("00:00");

                        // Send "End Game" signal 221 three times
                        if(!gameEnd)
                        {
                            for (int i = 0; i < 3; i++)
                                server.send("221");
                            gameEnd = true;
                        }
                    }
                }
            }
        });
        gameTimer.start(); // Start the timer with startup countdown

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
}