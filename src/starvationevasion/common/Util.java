package starvationevasion.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * public static methods that might be useful in as utilities.
 */
public class Util
{
  /**
   * There need only be one instance of a random number generator used
   * by all the parts of the program. Let this be it.
   */
  public static Random rand = new Random();

  public static int randInt (int min, int max)
  {
    return rand.nextInt((max - min) + 1) + min;
  }


  public static float linearInterpolate(float x1, float x2, float x3, float y1, float y3)
  {
    if (x2 <= x1) return y1;
    if (x2 >= x3) return y3;

    float w = (x2-x1)/(x3-x1);
    float y2 = y1*(1-w) + y3*w;

    return y2;
  }




  /**
   * Given a file name returns the absolute path of that file in URL format.
   * taken from:
   * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
   *
   * @param filename within the search path.
   * @return Absolute path of that file in URL format
   */
  public static String convertToFileURL(String filename)
  {
    String path = new File(filename).getAbsolutePath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    return "file:" + path;
  }




  public static ArrayList<String> getFilesInDir(String dirName)
  {
    File folder = new File(dirName);

    if (!folder.isDirectory()) return null;

    ArrayList<String> files = new ArrayList<>();

    for (File f : folder.listFiles())
    {
      if (!f.isHidden()) files.add(f.getPath());
    }
    return files;
  }
}
