/*
 * Main script for intializing, coordinating, and running MVC components
 */

import javax.swing.JFrame;
import java.awt.Toolkit;

public class Main extends JFrame {

	// Private variables
	private Model model;
	private Controller controller;
	private View view;

	// Config
	public static final int gameHeight = 1500;
	public static final int gameWidth = 750;
	public static final int gameWindowHeight = 750;
	public static final int gameWindowWidth = 750;

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}

	// Constructor, runs on initialization
	public Main() {

		// Sets up MVC components
		model = new Model();
		controller = new Controller(model);
		view = new View();
		view.addMouseListener(controller);
		this.addKeyListener(controller);
		controller.setView(view);

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

		// Switch to view control
		this.setVisible(false);
		this.getContentPane().remove(splashScreen);
        this.getContentPane().add(view);
		this.setVisible(true);
		
	}

	// Core functional loop
	public void run() {

		while(true) {

			// Updates each component
			controller.update();
			model.update();
			view.update();

			// view.repaint(); 					// This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Sleeps until the next frame. 1000 / 40 = 25 fps
			try {
				Thread.sleep(40);
			} 
			catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
