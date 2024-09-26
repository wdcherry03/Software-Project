/* Student: Winston Bounsavy
 * Date Submitted:	3-27-2024
 * Due Date:		3-27-2024
 * Assignment 5 Description:
 * This assignment is mainly rewriting the script to be more modular by including subclasses. We also add two more sprite subclasses,
 * Pellet and Fruit.
 */

import javax.swing.JPanel;
import java.awt.Graphics;

// Handles the button and drawing the turtle image / background
public class View extends JPanel {

	// MVC Components
	private Model model;

	// Constructor
	public View(Controller c, Model m) {
		model = m;
	}

	// Update function. Runs every frame
	public void update() {
	}

	// Overloads paintComponent. Runs every frame
	public void paintComponent(Graphics g) {
	}
}
