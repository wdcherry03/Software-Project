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

   public Audio()
   {
      System.out.println("Track: " + (track + 1));
   }

   public void run()
   {
      File soundFile = new File("./assets/Track0" + (track + 1) + ".wav");
      // System.out.println(soundFile.getName());


      try
      {
         AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
         Clip clip = AudioSystem.getClip();
         clip.addLineListener(new LineListener() 
         {
            @Override
            public void update(LineEvent event)
            {
               if(event.getType() == LineEvent.Type.STOP)
               {
                  clip.close();
               }
            }
         });

         clip.open(audio);
         clip.start();
         while(!clip.isRunning())
         {

         }
         while(clip.isRunning())
         {
               
         }
      }

      catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
      {
         System.out.println("Audio issue");
         e.printStackTrace();
      }
   }
}