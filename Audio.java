/* Audio playing file
 * Will be used to play all audio files
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Audio
{
   public Random rand = new Random();
   int track = rand.nextInt(7);

   public void run()
   {
      File soundFile = new File("/assets/Track0" + (track + 1) + ".wav");

      try
      {
         AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
         Clip clip = AudioSystem.getClip();
         clip.open(audio);
         clip.start();

         while (!clip.isRunning())
         {
            Thread.sleep(10); //Wait for clip to start playing
         }
         while(clip.isRunning())
         {
            Thread.sleep(10); //Wait for clip to stop playing
         }
         clip.close();
      }

      catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e)
      {
         System.out.println("Audio issue");
         e.printStackTrace();
      }
   }
}