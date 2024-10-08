/* 
 * View Component
 * Handles UI, messages, toasts, and other visual components.
 * Also handles the starting splash screen and other screen overlays
 * Calls other components to handle signals and database stuff.
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class View extends JPanel {

	// MVC Components
	public Model model;
	public Controller controller;

	// Variables
	private entry entryScreen;

	// Update function. Runs every frame
	public void update() {
	}
}
