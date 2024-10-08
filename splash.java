//Splash Screen File
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class splash extends JPanel{
    
    private BufferedImage logo;
    // private boolean check = true;
    // private int count = 0;

    // TEMP MVC, gives this to entry screen
    Model model;

    public splash() {
        loadImage();
    }

    // Loading logo image
    void loadImage()
    {
        try {
            this.logo = ImageIO.read(new File("./assets/logo.jpg"));
            // System.out.println("Success");
            // System.out.println("Yes");
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    // Overrides base repaint() function
    public void paintComponent(Graphics g) {
        // System.out.println("Yep");
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 800, 600);
        g.drawImage(this.logo, 0, -75, 800, 600, null);
    }
}