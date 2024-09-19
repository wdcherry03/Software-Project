//Splash Screen File
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class splash extends JPanel{
    
    private BufferedImage logo;

    void loadImage()
    {
        try
        {
            this.logo = ImageIO.read(new File("logo.jpg"));
            System.out.println("Success");
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(this.logo, logo.getWidth(), logo.getHeight(), null);
        System.out.println("Yep");
    }
    public static void main(String[] args)
    {
        splash spl = new splash();
        spl.loadImage();
        spl.repaint();
        JFrame f = new JFrame();
        f.setTitle("Laser Tag");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500,500);
        f.setVisible(true);
        System.out.println("Hello  world");
    }
}