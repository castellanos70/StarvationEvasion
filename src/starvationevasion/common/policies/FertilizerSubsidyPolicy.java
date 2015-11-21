package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target food product and percentage Y.<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: This policy makes conventional farming of target crop more
 * economic and therefore shifts farmer plantings. It also affects the use of
 * the fertilizer on existing crops causing a change in yield and a change in
 * fertilizer run off.<br><br>
*/
public class FertilizerSubsidyPolicy extends Policy
{
  public static final String TITLE =
      "Fertilizer or Feed Subsidy";

  public static final String TEXT =
      "This policy offers a subsidy of X% rebate to farmers in your region purchasing " +
      "commercial fertilizer for target crop or feed supplements for target live stock.";

  /* The number of votes required for this policy.  A value of 0 means that
   * the policy is automatic.
  */
  public final static int VOTES_REQUIRED = 0;

  /* Combined with 0 required votes, this Indicates that this policy is automatic.
  */
  public final static boolean VOTE_WAIT_FOR_ALL = false;

  public FertilizerSubsidyPolicy (EnumRegion region)
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
    // case FertilizerSubsidyPolicy:
    String msg = validatePercentValue(varX);
    if (msg != null) return getPolicyName() + msg;
    if (targetFood == null)
    {
      return getPolicyName() + "Must have target Food";
    }

    return null;
  }


  /**
   * Used only for testing this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Policy myCard = new FertilizerSubsidyPolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
