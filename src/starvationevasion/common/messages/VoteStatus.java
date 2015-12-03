package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Shea Polansky
 * A message from server to client informing them of the current
 * vote tallies.
 */
public class VoteStatus implements Serializable
{
  public final PolicyCard[] currentCards;

  public VoteStatus(PolicyCard[] currentCards)
  {
    this.currentCards = currentCards;
  }
}
