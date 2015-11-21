package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies the draw pile must
 * be set at the start of the game rather than cards being randomly picked at draw
 * time.<br><br>
*/
public class CovertIntelligencePolicy extends Policy
{
  public static final String TITLE =
      "Covert Intelligence";

  public static final String TEXT =
      "You get to covertly examine target player's hand and the top two cards " +
      "of that player's deck. You may target yourself. " +
      "During the voting phase, other players will see that you have " +
      "played this card, but not know its target. Bonus: If you can " +
      "correctly answer a hidden research question, you examine the top seven " +
      "cards of the target player's deck.";

  /* The number of votes required for this policy.  A value of 0 means that
   * the policy is automatic.
  */
  public final static int VOTES_REQUIRED = 0;

  /* Combined with 0 required votes, this Indicates that this policy is automatic.
  */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  public CovertIntelligencePolicy(EnumRegion region)
  {
    super(region);
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public int votesRequired() {return VOTES_REQUIRED;}

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean voteWaitForAll() {return false;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getTitle(){ return TITLE;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getGameText(){ return TEXT;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String validate()
  {
    String msg;

    if (varX != 2 && varX != 7)
    {
      return getPolicyName() + "["+varX +"]: Cards revealed must be 2 or 7.";
    }

    return null;
  }

  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Policy myCard = new CovertIntelligencePolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
