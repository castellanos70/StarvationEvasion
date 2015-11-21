package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.Policy;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target world
 * region and X million dollars.<br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED}<br>
 * Eligible Regions: <br><br>
 *
 * Model Effects:  model needs four control points of each ease-in-out cubic Bezier
 * function giving investment verses food trade penalty function reduction. This one
 * time spending permanently reduces the regions penalty function.<br><br>
 * If approved, each US region must pay X million.
*/
public class EducateTheWomenCampaignPolicy extends Policy
{
  public static final String TITLE =
      "Educate the Women Campaign";

  public static final String TEXT =
      "The US sends 7X million dollars to educate woman of the target world " +
      "region in reading, basic business and farming techniques.";

  /* The number of votes required for this policy.  A value of 4 means that
   * 4 votes are required for the policy to be enacted.
  */
  public final static int VOTES_REQUIRED = 4;

  /**
   * Indicates if voting voting should continue until all eligible players
   * have voted on this policy. A value of false indicates that voting should
   * stop as soon as the required number of votes have been reached.
   */
  public final static boolean VOTE_WAIT_FOR_ALL = false;
  
  public EducateTheWomenCampaignPolicy(EnumRegion region)
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
  public String validate()
  {
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

    Policy myCard = new EducateTheWomenCampaignPolicy(EnumRegion.MOUNTAIN);
    System.out.println(myCard.getTitle());
    System.out.println(myCard.getGameText());
  }
}
