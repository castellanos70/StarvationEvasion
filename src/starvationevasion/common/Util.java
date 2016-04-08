package starvationevasion.common;

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

  public static boolean likeliness (float likeliness)
  {
    return rand.nextFloat() <= likeliness;
  }


  public static float linearInterpolate(float x1, float x2, float x3, float y1, float y3)
  {
    if (x2 <= x1) return y1;
    if (x2 >= x3) return y3;

    float w = (x2-x1)/(x3-x1);
    float y2 = y1*(1-w) + y3*w;

    return y2;
  }
}
