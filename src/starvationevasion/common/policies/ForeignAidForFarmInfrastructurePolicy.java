package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;
import starvationevasion.common.PolicyCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target world
 * region and X million dollars.<br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED}<br>
 * Eligible Regions: All US<br><br>
 *
 * Model Effects: model needs four control points of each ease-in-out cubic Bezier
 * function giving investment verses food trade penalty function reduction. This one
 * time spending permanently reduces the regions penalty function.
 * If approved, each US region must pay X million.
 <br><br>
*/

public class ForeignAidForFarmInfrastructurePolicy extends Policy
{
  public static PolicyCard CARD = Fall2015PolicyProvider.EnumPolicy.Foreign_Aid_for_Farm_Infrastructure;

  public static final String TITLE =
      "Foreign Aid for Farm Infrastructure";

  public static final String TEXT =
      "The US sends 7X million dollars in foreign aid for capital development " +
      "of farming infrastructure of target world region.";

  /* The number of votes required for this policy.  A value of 4 means that
   * 4 players must vote to enact this policy.
  */
  public final static int VOTES_REQUIRED = 4;

  /**
   * Indicates if voting voting should continue until all eligible players
   * have voted on this policy. A value of false indicates that voting should
   * stop as soon as the required number of votes have been reached.
   */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  /* The crop types applicable to this policy.
  */
  public final static Collection<EnumFood> TARGET_FOOD = null;

  /* The target regions applicable to this policy.
  */
  public final static Collection<EnumRegion> TARGET_REGIONS;

  static 
  {
    TARGET_REGIONS = new ArrayList<>();
    TARGET_REGIONS.addAll(Arrays.asList(EnumRegion.WORLD_REGIONS));
  }

  public ForeignAidForFarmInfrastructurePolicy(EnumRegion region)
  {
    super(region);
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public int votesRequired() { return VOTES_REQUIRED; }

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean voteWaitForAll() { return VOTE_WAIT_FOR_ALL; }

  /**
   * {@inheritDoc}
  */
  @Override
  public String getTitle() { return TITLE;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getGameText() { return TEXT;}

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
    // case ForeignAidForFarmInfrastructurePolicy:
    if (getEnactingRegionCount() < votesRequired())
    {
      return getPolicyName() + ": does not have required votes.";
    }

    String msg = validateDollarValue(varX);
    if (msg != null) return getPolicyName() + msg;

    if (targetRegion == null || targetRegion.isUS())
    {
      return getPolicyName() + "["+targetRegion +"]: Must have target world region";
    }

    return null;
  }

  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Policy myCard = new ForeignAidForFarmInfrastructurePolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
