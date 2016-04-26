package starvationevasion.common;

/**
 * Shea Polansky
 * Simple generic 2-Tuple class
 */
public class Tuple<T, U>
{
  public final T a;
  public final U b;

  public Tuple(T a, U b)
  {
    this.a = a;
    this.b = b;
  }
}
