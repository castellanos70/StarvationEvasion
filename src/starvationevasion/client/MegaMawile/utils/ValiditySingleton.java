package starvationevasion.client.MegaMawile.utils;

/**
 * Used to reference the {@link Validity} object in a <code>static</code> manner.
 *
 * @author Chris Wu
 * @see Validity
 */
public class ValiditySingleton
{
  private static Validity ourInstance = new Validity();

  /**
   * Returns our singleton instance of the Validity object.
   *
   * @return the Validity object.
   */
  public static Validity getInstance() { return ourInstance; }

  private ValiditySingleton() { }
}
