package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;


/**
  * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target player
 * from whom to attempt to borrow and the amount X.<br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED}<br>
 * Eligible Regions: Target player only <br><br>
 *
 * Model Effects: Before resolving any other model effects,
 * 25 million dollars are transferred from target player to the card owner.
 * Then, December 31st of each year for the next 10 years,
 * 2.5 million + 10% of remaining balance are transferred from card owner
 * back to the target player.
 */
public class Policy_Loan extends GameCard
{
    public static final String TITLE = "Loan";

    public static final String TEXT =
      "Target player region lends you $25 million at 10% interest for 10 years. "+
        "Annual loan payments are automatically paid on December 31st of each year.";

    /**
     * The number of votes required for this policy.  A value of 0 means that
     * the policy is automatic.
    */
    public final static int VOTES_REQUIRED = 2;



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
  public int votesRequired() {return VOTES_REQUIRED;}

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEligibleToVote(EnumRegion playerRegion)
  {
    //Note: enums can use == to test value.
    if (playerRegion == getOwner()) return true;
    if (getTargetRegion() == null) return false;;

    if (playerRegion != getTargetRegion()) return false;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  { return EnumRegion.US_REGIONS;
  }




}
