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
        runEntry();
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
        JPanel playerEntryPanel = new JPanel(new GridLayout(1, 3));
        JButton playerAddButton = new JButton("Add Player");
        JTextField playerAddIdField = new JTextField("Enter ID Here");
        JTextField playerAddNameField = new JTextField("Enter Player Codename Here");
        playerEntryPanel.add(playerAddButton);
        playerEntryPanel.add(playerAddNameField);
        playerEntryPanel.add(playerAddIdField);

        // Adds a player when the player add button is called 
        playerAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String codename = playerAddNameField.getText();
                int id = Integer.parseInt(playerAddIdField.getText());
                model.addPlayerPSQL(id, codename);
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
