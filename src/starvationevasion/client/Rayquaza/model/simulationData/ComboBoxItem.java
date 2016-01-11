/**
 * @author Mohammad R. Yousefi
 * Wrapper for Tuple t use in ComboBoxes.
 */
package starvationevasion.client.Rayquaza.model.simulationData;

import starvationevasion.common.Tuple;

public class ComboBoxItem<T, U> extends Tuple<T, U>
{
  public ComboBoxItem(T a, U b)
  {
    super(a, b);
  }

  @Override
  public String toString()
  {
    return a.toString();
  }
}