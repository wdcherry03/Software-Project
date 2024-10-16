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

	// Constructor
	public View(Model m, Controller c) {

		// MVC
		model = m;
		controller = c;

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
		this.runEntry();
	}

	// Update function. Runs every frame
	public void update() {
        this.repaint();
	}

	// Dumps anything on the screen
	public void dumpScreen() {

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

        JTextField playerEntryDialogue = new JTextField("");
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

        playerEntryPanel.add(playerEntryButtons);
        playerEntryPanel.add(playerEntryDialogue);

        // Adds a player when the player add button is called 
        playerAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int id = Integer.parseInt(playerAddIdField.getText());
                
                // Checks if the id already exists
                boolean playerFound = false;
                int foundPlayerId = 0;

                if (id % 2 == 0) {           // Even, Green
                    for (int i = 0; i < model.greenPlayerList.size(); ++i) {
                        if (id == model.greenPlayerList.get(i).playerID) {
                            playerFound = true;
                            foundPlayerId = i;
                            break;
                        }
                    }
                }

                else if (id % 2 == 0) {      // Odd, Red
                    for (int i = 0; i < model.greenPlayerList.size(); ++i) {
                        if (id == model.greenPlayerList.get(i).playerID) {
                            playerFound = true;
                            foundPlayerId = i;
                            break;
                        }
                    }
                }

                // If the player exists, uses already existing player data and adds hardware id
                if (playerFound) {
                    playerEntryDialogue.setText("Player already Found. Using existing information...");

                    // Updates player list entry with the hardware ID and adds the player panel to the panel list
                    if (id % 2 == 0) {           // Green
                        PlayerPanel newPanel = new PlayerPanel(model.greenPlayerList.get(foundPlayerId));
                        model.greenPlayerList.get(foundPlayerId).hardwareID = Integer.parseInt(playerAddHardwareId.getText());
                        greenTeamPanel.add(newPanel);
                    }

                    else if (id % 2 == 1) {      // Red
                    PlayerPanel newPanel = new PlayerPanel(model.redPlayerList.get(foundPlayerId));
                        model.redPlayerList.get(foundPlayerId).hardwareID = Integer.parseInt(playerAddHardwareId.getText());
                        redTeamPanel.add(newPanel);
                    }
                }

                // If the player doesn't exist, adds them to the correct player list and the database
                else {

                    // Adds to the database
                    playerEntryDialogue.setText("Player not found. Adding player to the database...");
                    String codename = playerAddNameField.getText();
                    int hardwareId = Integer.parseInt(playerAddHardwareId.getText());
                    model.addPlayerPSQL(id, codename, hardwareId);

                    // Adds to the player list and adds a new panel to the panel list
                    Player newPlayer = new Player(id, codename, hardwareId);
                    PlayerPanel newPanel = new PlayerPanel(newPlayer);

                    if (id % 2 == 0) {           // Green
                        model.greenPlayerList.add(newPlayer);
                        greenTeamPanel.add(newPanel);
                    }
                    else if (id % 2 == 1) {      // Red
                        model.redPlayerList.add(newPlayer);
                        redTeamPanel.add(newPanel);
                    }
                }
                update();
            }
        });
        
        // Add panels to frame
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(playerEntryPanel, BorderLayout.SOUTH);

        this.setVisible(true);
        this.repaint();
	}
}
