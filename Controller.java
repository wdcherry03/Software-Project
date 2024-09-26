/*
 * Controller component.
 * Handles mouse events like clicking, dragging, etc.
 * Handles keyboard inputs
 * Calls "Model" to handle signals and database calls
 * Calls "View" to handle any UI changes
 */

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controller implements ActionListener, MouseListener, KeyListener {

	// MVC Components
	private View view;
	private Model model;

	// Constructor
	public Controller(Model m) {
		model = m;
	}

	// Sets the view
	void setView(View v) {
		view = v;
	}


	// Update Function. Runs Every Frame	
	public void update() {
	}

	// Overrides some methods so that the program compiles correctly.
	public void actionPerformed(ActionEvent e) {    }
	public void keyTyped(KeyEvent e) {    }
	public void keyPressed(KeyEvent e) {    }
	public void keyReleased(KeyEvent e) {    }
	public void mousePressed(MouseEvent e) {    }
	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }
}
