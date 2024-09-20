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
    private boolean check = true;
    private int count = 0;


    void loadImage()
    {
        try
        {
            this.logo = ImageIO.read(new File("logo.jpg"));
            // System.out.println("Success");
            // System.out.println("Yes");
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
    public void paintComponent(Graphics g)
    {
        // System.out.println("Yep");
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 500, 500);
        if(this.check == true)
        {
            g.drawImage(this.logo, 0, -75, 500, 500, null);
        }
        if(this.count == 1)
        {
            try
            {
                Thread.sleep(3000);
            }
            catch(InterruptedException ie)
            {
                Thread.currentThread().interrupt();
            }
        }
        this.count++;
        this.check = false;
    }
    public static void main(String[] args)
    {
        splash spl = new splash();
        spl.loadImage();
        JFrame f = new JFrame();
        f.setTitle("Laser Tag");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500,500);
        f.getContentPane().add(spl);
        f.setVisible(true);
        // System.out.println("Hello   world");
        while (true) {
            spl.repaint();
            try {
                Thread.sleep(40);
            }
            catch(Exception e) {
                System.out.println("Uh oh");
            }
        }
    }
}