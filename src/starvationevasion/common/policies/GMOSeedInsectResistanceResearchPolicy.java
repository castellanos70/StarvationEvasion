
package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;
import starvationevasion.common.PolicyCard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a crop and an
 * amount X to be paid by EACH player who approves the policy. <br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED} through all<br>
 * Eligible Regions: All U.S.<br><br>
 *
 * Model Effects: 5% bonus to effectiveness of total dollars spent per
 * participating region.<br><br>
 *
 * Starting 10 years after research, permanent reduction in kg/km<sup>2</sup>
 * of pesticides for target crop. All player and non-player regions in world
 * who permit GMO in their region receive this benefit.<br>
 * Benefit yield is an ease-in-out cubic bezier function of effective
 * dollars spent and target crop.
*/
public class GMOSeedInsectResistanceResearchPolicy extends Policy
{
  public static PolicyCard CARD = Fall2015PolicyProvider.EnumPolicy.GMO_Seed_Insect_Resistance_Research;

  public static final String TITLE =
     "GMO Seed Insect Resistance Research";

  public static final String TEXT =
    "Each participating region spends X million dollars to fund GMO seed research " +
    "for increasing insect resistance of target crop.";

  /* The number of votes required for this policy.  A value of 1 means that
   * only one player must vote to enact this policy.
  */
  public final static int VOTES_REQUIRED = 1;

  /**
   * Indicates if voting voting should continue until all eligible players
   * have voted on this policy. A value of true indicates that voting should
   * continue until all players have voted.
   */
  public final static boolean VOTE_WAIT_FOR_ALL = true;

  /* The crop types applicable to this policy.
  */
  public final static Collection<EnumFood> TARGET_FOOD;

  /* The target regions applicable to this policy.
  */
  public final static Collection<EnumRegion> TARGET_REGIONS = null;

  static 
  {
    TARGET_FOOD = new ArrayList<>();
	for (EnumFood food : EnumFood.values())
	{
	  if (food.isCrop()) TARGET_FOOD.add(food);
	}
  }

  public GMOSeedInsectResistanceResearchPolicy(EnumRegion owner)
  {
    super(owner);
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
  public boolean voteWaitForAll() {return VOTE_WAIT_FOR_ALL;}

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
    // case GMOSeedInsectResistanceResearchPolicy:
    if ((targetFood == null) || (!targetFood.isCrop()))
    {
      return getPolicyName() + ": must have a target food that is a crop (not livestock).";
    }
    String msg = validateDollarValue(varX);
    if (msg != null) return getPolicyName() + msg;

    return null;
  }


  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Policy myCard = new GMOSeedInsectResistanceResearchPolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
