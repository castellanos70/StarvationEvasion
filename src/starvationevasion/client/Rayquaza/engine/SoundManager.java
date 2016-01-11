/**
 * Created by MohammadR on 11/21/2015.
 * ImageManager stores all images that are used as the textures.
 * Current iteration of ImageManager works with Image and BufferedImage classes.
 * <p>
 * All audio files must be located in the "res/audio" directory or any subdirectory thereof.
 * Usage SoundManager.getAudioClip(String "relative path to root folder");
 */
package starvationevasion.client.Rayquaza.engine;

import javafx.scene.media.AudioClip;

import javax.swing.*;
import java.util.HashMap;

public class SoundManager
{
  final static private String ASSET_ROOT_STRING = "res/audio/";
  final static private HashMap<String, AudioClip> audioClips = new HashMap<>();

  /**
   * Retrieves an AudioClip. If the clip is not in memory attempts to load it.
   *
   * @param audioPath The relative path to the image rooted in "res".
   */
  public static AudioClip getAudioClip(String audioPath)
  {
    if (audioClips.containsKey(audioPath)) return audioClips.get(audioPath);
    else
    {
      audioClips.put(audioPath, new AudioClip(SoundManager.class.getClassLoader().getResource(ASSET_ROOT_STRING +
          audioPath).toString()));
      return audioClips.get(audioPath);
    }
  }

  /**
   * FOR TESTING ONLY
   */
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(new JLabel("waterDrop.mp3"));
    frame.pack();
    frame.setVisible(true);
    AudioClip ac = getAudioClip("waterDrop.mp3");
    System.out.println(ac.toString());
    ac.setBalance(0.0);
    ac.setVolume(1.0);
    ac.play();
    frame.dispose();
  }
}
