
package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;
import starvationevasion.common.PolicyCard;

import java.util.Collection;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a percentage X [1% through 100%].<br><br>
 *
 * Votes Required: Automatic<br>
 *
 * Model Effects: The sim estimates the number and location of farms within
 * the region and the amount spent by those farms on improved irrigation. The
 * model will need a current distribution of irrigation efficiency levels, the
 * four control points of an ease-in-out cubic Bezier function giving efficiency
 * increase as a function of dollars spent and a distribution of current water
 * sources being used by farms.<br><br>
 * Depending on the region and the area within the region, the effects of lower
 * water usage might include a permanent (through endgame) reduction of farming
 * costs, aquifer depletion, and/or river flow depletion. Increased irrigation
 * efficiency also reduces pesticides, fertilizer, and turbidity levels in
 * outflowing rivers.<br><br>
 *
 * X% of the money that the sim estimates is spent for improved irrigation
 * is deducted from the regions tax revenue at the start of the next turn.
*/
public class EfficientIrrigationIncentivePolicy extends Policy
{
  public static PolicyCard CARD = Fall2015PolicyProvider.EnumPolicy.Efficient_Irrigation_Incentive;

  public static final String TITLE =
      "Efficient Irrigation Incentive";

  public static final String TEXT =
      "From now through the start of the next turn, X% of money spent by farmers " +
      "in players region for improved irrigation efficiency is tax deductible.";

  /* The number of votes required for this policy.  A value of 0 means that
   * the policy is automatic.
  */
  public final static int VOTES_REQUIRED = 0;

  /* Combined with 0 required votes, this Indicates that this policy is automatic.
  */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  /* The crop types applicable to this policy.
  */
  public final static Collection<EnumFood> TARGET_FOOD = null;

  /* The target regions applicable to this policy. A v
  */
  public final static Collection<EnumRegion> TARGET_REGIONS = null;

  public EfficientIrrigationIncentivePolicy(EnumRegion region)
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
  public PolicyCard getCardType() { return CARD; }

  /**
   * {@inheritDoc}
  */
  @Override
  public String validate()
  {
    // case EfficientIrrigationIncentivePolicy:
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

    Policy myCard = new EfficientIrrigationIncentivePolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
