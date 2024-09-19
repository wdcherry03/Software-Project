//Splash Screen File
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
    public void paintComponent(Graphics g)
    {
        g.drawImage(this.logo, 500, 500, null);
    }
    public static void main(String[] args)
    {
        splash spl = new splash();
        spl.loadImage();
        System.out.println("Hello  world");
    }
}