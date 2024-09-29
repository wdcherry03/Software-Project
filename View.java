/* 
 * View Component
 * Handles UI, messages, toasts, and other visual components.
 * Also handles the starting splash screen and other screen overlays
 * Calls other components to handle signals and database stuff.
 */

import javax.swing.JPanel;
import java.awt.Graphics;

public class View extends JPanel {

	// MVC Components
	private Model model;

	// Variables
	private splash splashScreen;
	private entry screen;

	// Constructor
	public View(Controller c, Model m) {
		model = m;
		splashScreen = new splash();
		//splashScreen.run();
		screen = new entry();
		screen.run();
	}

	// Update function. Runs every frame
	public void update() {
	}

	// Overloads paintComponent. Runs every frame
	public void paintComponent(Graphics g) {
	}
}
