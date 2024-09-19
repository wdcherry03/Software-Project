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
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
    public void paint(Graphics g)
    {
        g.drawImage(this.logo, 500, 500, null);
    }
    public static void main(String[] args)
    {
        JFrame f = new JFrame();
        f.setSize(500,500);
        splash spl = new splash();
        spl.loadImage();
        spl.repaint();
        System.out.println("Hello  world");
    }
}