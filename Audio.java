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
    public int track = rand.nextInt(7);
 }