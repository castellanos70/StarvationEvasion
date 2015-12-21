package starvationevasion.client.MegaMawile.ai.brain;

import starvationevasion.common.PolicyCard;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Outlines the fundamental decision/update methods for any AI. Their actual logic varies depending
 * on AI flavor.
 *
 * @author Evan King
 */
public interface Ai
{
  /**
   * Updates the AI in the drafting phase
   */
  void draftUpdate(float deltaSeconds);

  /**
   * Updates the AI in the voting phase
   */
  void voteUpdate(float deltaSeconds);

  /**
   * Choose a policy from the passed hand, used during the drafting phase.
   *
   * @param hand  a PriorityQueue of policies sorted based on desirability
   * @return the chosen policy, based on the Ai's decision mechanism
   */
  PolicyCard choosePolicy(PriorityQueue<PolicyCard> hand);

  /**
   * Sort the passed hand, giving priority to certain cards.
   *
   * @param hand  an ArrayList of policies to sort
   * @return the sorted hand
   */
  PriorityQueue<PolicyCard> sortHand(Iterator<PolicyCard> hand);

  /**
   * Signals the AI to fill out its ballot.
   */
  void placeVotes();
}
