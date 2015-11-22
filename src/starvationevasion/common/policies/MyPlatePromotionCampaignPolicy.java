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
 * Draft Affects: When drafting this policy, player selects X million dollars.<br><br>
 *
 * Votes Required: Automatic <br><br>
 *
 * Model Effects: Model uses four control points of an ease-in-out cubic Bezier
 * function giving shift in food preference demand verses advertising dollars
 * spent. The effect is largest in the region running the campaign, but also
 * effects world regions in direct proportion to that regions import levels of the
 * effected food categories.<br><br>
*/
public class MyPlatePromotionCampaignPolicy extends Policy
{
  public static PolicyCard CARD = Fall2015PolicyProvider.EnumPolicy.MyPlate_Promotion_Campaign;

  public static final String TITLE =
     "MyPlate Promotion Campaign";

  public static final String TEXT =
     "You spend X million dollars on an advertising campaign within your region promoting " +
     "public awareness of the United States Department of Agricultures MyPlate nutrition guide.";

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

  public MyPlatePromotionCampaignPolicy(EnumRegion region)
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
    // case MyPlatePromotionCampaignPolicy:
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
    Policy myCard = new MyPlatePromotionCampaignPolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
