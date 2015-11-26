package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;


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
 * X million dollars are transferred from target player to the card owner.
 * Then, on each subsequent year for the next 10 years,
 * X/10 + 10% of remaining balance million dollars are transferred from card owner
 * back to the target player.
 * Thus, the duration of the loan is 11 years (3 full turns plus the first
 * two years of a 4th turn.)
 */
public class LoanPolicy extends PolicyCard implements Serializable
{
    public static final String TITLE = "Loan";

    public static final String TEXT =
      "Target player region lends you X million dollars at 10% interest. "+
        "Annual loan payments are scheduled from year 2 through year 11 (spanning 4 turns).";

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
    if (playerRegion == owner) return true;
    if (targetRegion == null) return false;;

    if (playerRegion != targetRegion) return false;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  { return EnumRegion.US_REGIONS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumVariableUnit getRequiredVariables(EnumVariable variable)
  {
    if (variable == EnumVariable.X) return EnumVariableUnit.MILLION_DOLLAR;
    return null;
  }
}
