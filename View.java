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

	// Variables
	private entry entryScreen;

	// Constructor
	public View() {

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
		// this.getContentPane().remove(splashScreen);
		this.runEntry();
	}

	// Update function. Runs every frame
	public void update() {
	}

	// Dumps anything on the screen
	public void dumpScreen() {
	}

	// Runs the entry screen
	public void runEntry() {

		// Clears JfRame
		this.getContentPane().removeAll();
		this.repaint();

		// Create panels for teams
        JPanel redTeamPanel = new JPanel(new GridLayout(20, 1));
        JPanel greenTeamPanel = new JPanel(new GridLayout(20, 1));
		JPanel buttonPanel;

        // Add checkboxes for each player for each team
        redTeamPanel.add(new JLabel("   PLAYER NAME", JLabel.LEFT));
        redTeamPanel.add(new JLabel("PLAYER ID", JLabel.LEFT));
        redTeamPanel.add(new JLabel("HARDWARE ID", JLabel.LEFT));
        redTeamPanel.setBackground(Color.RED);
        greenTeamPanel.add(new JLabel("   PLAYER NAME", JLabel.LEFT));
        greenTeamPanel.add(new JLabel("PLAYER ID", JLabel.LEFT));
        greenTeamPanel.add(new JLabel("HARDWARE ID", JLabel.LEFT));
        greenTeamPanel.setBackground(Color.GREEN);

        // Add checkboxes
        for (int i = 1; i <= 19; i++) {
            redTeamPanel.add(new JTextField());
            redTeamPanel.add(new JTextField());
            redTeamPanel.add(new JTextField());
            greenTeamPanel.add(new JTextField());
            greenTeamPanel.add(new JTextField());
            greenTeamPanel.add(new JTextField());
        }

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
	}
}
