package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

/**
 * Shea Polansky
 * A message from server to client informing them of the current
 * vote tallies.
 */
public class VoteStatus
{
  public final PolicyCard[] currentCards;

  public VoteStatus(PolicyCard[] currentCards)
  {
    this.currentCards = currentCards;
  }
}
