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
	public View view;
	public Model model;
	public boolean remove;

	// Constructor
	public Controller(Model m) 
	{
		model = m;
		remove = false;
	}

	// Sets the view
	public void setView(View v) {
		view = v;
	}

	// Sets model
	public void setModel(Model m) {
		model = m;
	}


	// Update Function. Runs Every Frame	
	public void update() 
	{
		if(remove)
		{
			view.clearEntries();
			view.runEntry();
			System.out.println("All good");
			remove = false;
		}
	}

	// Overrides some methods so that the program compiles correctly.
	public void actionPerformed(ActionEvent e) {    }
	public void keyTyped(KeyEvent e) {    }
	public void keyPressed(KeyEvent e) {    }
	public void keyReleased(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_F12: remove = true; break; // Clear player entries
			case KeyEvent.VK_F5:
				view.resetGameState(); // Sets 'gameStart' and 'gameEnd' to false
				view.runGame(); break; // Go to gameplay screen
			case KeyEvent.VK_F7: view.runEntry(); break; // Return to entry screen
			//case KeyEvent.VK_0: model.server.send(String.valueOf(12)); break;
		}
	}
	public void mousePressed(MouseEvent e) {    }
	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }
}
