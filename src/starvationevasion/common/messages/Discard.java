package starvationevasion.common.messages;

import starvationevasion.common.EnumPolicy;

import java.io.Serializable;

/**
 * Shea Polansky
 * A message from client to server indicating that it wishes to discard card(s).
 * May be used to represent either a free discard action (which uses the single-
 * argument constructor and may be performed once per turn independent of other actions)
 * or a non-free discard action (which uses the three argument constructor and may be used
 * in place of a draft action each draft phase).
 * Arguments in the three-argument constructor may be null (e.g. new Discard(policyA, policyB, null))
 * to indicate fewer than three cards are intended to be discarded.
 */
public class Discard implements Serializable
{
  public final boolean isFreeDiscard;
  public final EnumPolicy[] discards;

  /**
   * Create a Discard representing a free discard action.
   * @param discard a non-null EnumPolicy to discard.
   */
  public Discard(EnumPolicy discard)
  {
    if (discard == null) throw new IllegalArgumentException();
    isFreeDiscard = true;
    discards = new EnumPolicy[] {discard};
  }

  /**
   * Creates a new Discard action that represents a non-free discard action.
   * At least one argument must be non-null.
   * @param discard1 the first card to discard
   * @param discard2 the second card to discard
   * @param discard3 the third card to discard
   */
  public Discard(EnumPolicy discard1, EnumPolicy discard2, EnumPolicy discard3)
  {
    if (discard1 == null && discard2 == null && discard3 == null)
    {
      throw new IllegalArgumentException();
    }
    isFreeDiscard = false;
    discards = new EnumPolicy[] {discard1, discard2, discard3};
  }
}
