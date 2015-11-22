package starvationevasion.common;

import java.util.Random;

/**
 * public static methods that might be useful in as utilities.
 */
public class Util
{
  /**
   * There need only be one instance of a random number generator used
   * by all the parts of the program. Let this is it.
   */
  public static Random rand = new Random();
}
