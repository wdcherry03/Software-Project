//Splash Screen File
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class splash{
    
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
    public static void main(String[] args)
    {
        System.out.println("Hello  world");
    }
}