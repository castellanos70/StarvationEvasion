package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

/**
 * Shea Polansky
 * Simple generic 2-Tuple class
 */
public class Tuple<T, U> implements Sendable
{
  public final T a;
  public final U b;

  public Tuple(T a, U b)
  {
    this.a = a;
    this.b = b;
  }

  @Override
  public Type getType ()
  {
    return Type.SUCCESS;
  }

  @Override
  public JSONDocument toJSON ()
  {
    return null;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }
}
