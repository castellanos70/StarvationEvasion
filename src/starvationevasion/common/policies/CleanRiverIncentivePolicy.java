
package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a percentage Y
 * [1% through 100%] and Z% [1% through 100%].<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The sim estimates the number and location of farms
 * within the region and the amount spent by those farms on improved irrigation.
 * The model will need a current amount of pesticides and fertilizers in use
 * per square km, the current percentage that goes into rivers, and, the four
 * control points of each ease-in-out cubic Bezier function giving pesticide
 * and fertilizer use verses yield for each of the 12 farm products and
 * pesticide and fertilizer use verses cost of alternative methods including
 * more expensive seeds,  natural pest controls, and landscape for reduced run-off.<br><br>
 * The Y% tax break is applied to the regions tax revenue on the next turn.
 <br><br>
*/
public class CleanRiverIncentivePolicy extends Policy
{
  public static final String TITLE =
      "Clean River Incentive";

  public static final String TEXT =
      "X% tax break for farmers in my region who reduce by Y% the outflow of "+
      "pesticides and fertilizers from their farms into the rivers.";

  /* The number of votes required for this policy.  A value of 0 means that
   * the policy is automatic.
  */
  public final static int VOTES_REQUIRED = 0;

  /* Combined with 0 required votes, this Indicates that this policy is automatic.
  */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  public CleanRiverIncentivePolicy(EnumRegion region)
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
    String msg = validatePercentValue(varX);
    if (msg != null) return getPolicyName() + msg;

   return null;
  }

  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Policy myCard = new CleanRiverIncentivePolicy(EnumRegion.MOUNTAIN);

    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
