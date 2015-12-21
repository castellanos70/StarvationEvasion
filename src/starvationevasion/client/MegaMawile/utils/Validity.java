package starvationevasion.client.MegaMawile.utils;

import starvationevasion.common.PolicyCard;

/**
 * The Validity object is used to validate the legality of moves per the game rules before they are
 * submitted.
 *
 * @author Chris Wu
 * @since 11/18/2015
 */
public class Validity
{
  private int actions;
  private int requiresVotes;
  private int discard;

  //Initializes the validator with the appropriate limits for each action.
  Validity()
  {
    actions = 2;
    requiresVotes = 1;
    discard = 1;
  }

  /**
   * Allows drafting when there is up to 1 card that requires votes and actions available.
   *
   * @param policyCard the {@link PolicyCard} to validate.
   * @return <code>true</code> when actions are available and no more than 1 policy needs votes.
   */
  public boolean canDraft(PolicyCard policyCard)
  {
    if (actions > 1)
    {
      if(policyCard.votesRequired() > 0)
      {
        if (requiresVotes > 0) requiresVotes--;
        else return false;//can't play more than 1 policy card that requires voting
      }
      actions--;
      return true;
    }
    return false;//not enough actions
  }

  /**
   * Allows a discard up to 3 cards if there is an action available.
   *
   * @param policyCards the {@link PolicyCard} to validate.
   * @return <code>true</code> when there are at most 3 cards to discard and an action available.
   */
  public boolean canDiscardDraw(PolicyCard[] policyCards)
  {
    if (policyCards.length > 3) return false;
    if (actions > 1)
    {//there are actions to take
      actions--;
      return true;
    }
    return true;
  }

  /**
   * Boolean check for discarding a card.
   *
   * @param policyCard the {@link PolicyCard} we're trying to discard.
   * @return <code>true</code> if there is a discard allowed.
   */
  public boolean canDiscard(PolicyCard policyCard)
  {
    if(discard > 0)
    {
      discard--;
      return true;
    }
    return false;
  }

  /**
   * Resets the counters for the next drafting phase.
   */
  public void reset()
  {
    actions = 2;
    requiresVotes = 1;
    discard = 1;
  }
}
