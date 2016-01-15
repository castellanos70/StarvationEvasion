package starvationevasion.client.MegaMawile2.utils;

/**
 * Used to reference the {@link starvationevasion.client.MegaMawile2.utils.Validity} object in a <code>static</code> manner.
 *
 * @author Chris Wu
 * @see starvationevasion.client.MegaMawile2.utils.Validity
 */
public class ValiditySingleton
{
  private static starvationevasion.client.MegaMawile2.utils.Validity ourInstance = new starvationevasion.client.MegaMawile2.utils.Validity();

  /**
   * Returns our singleton instance of the Validity object.
   *
   * @return the Validity object.
   */
  public static starvationevasion.client.MegaMawile2.utils.Validity getInstance() { return ourInstance; }

  private ValiditySingleton() { }
}
