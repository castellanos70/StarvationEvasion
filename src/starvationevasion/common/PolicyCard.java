package starvationevasion.common;

import java.util.*;

/**
 * PolicyCard is the structure that the Simulator maintains for each card in the deck.<br><br>
 * When a card is played it becomes a Policy for voting and implementation purposes.
 *
 * The game client's might want to implement a class adding,
 * for example, an image, a status (in deck, in hand, proposed, voting, enacted, rejected)
 * and other data. <br>
 * If the client does extend this class, be sure to downcast to Policy before sending
 * across the network to the server.<br><br>
 *
 * Use the validate() method to verify all needed parameters of the policy are defined and
 * in range.
 */
public interface PolicyCard
{
  /**
   * @return the policy card name.
  */
  public String name();

  /**
   * @return the policy card title text.
  */
  public String title();

  /**
   * @return the policy card game text.
  */
  public String gameText();

  /**
   * @return an id number unique to each card type.
   */
  public int cardTypeId();

  /**
   * @return 0 if the policy is automatic. Otherwise, returns the number of
   * votes required for the policy to be enacted.
   */
  public int votesRequired();

  /**
   * @return true if voting should continue until all eligible players
   * have voted on this policy. Return false if voting should stop as soon as
   * the required number of votes have been reached.
   */
  public boolean voteWaitForAll();

  /**
   * @return A collection of applicable target regions if this card requires a target region,
   * or null if no regions apply.
   */
  public Collection<EnumRegion> validTargetRegions();

  /**
   * @return The target food types required by this card, or null if no types apply.
   */
  public Collection<EnumFood> validTargetFoods();
}
