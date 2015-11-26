
package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a crop and an
 * amount X to be paid by EACH player who approves the policy. <br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED}<br><br>
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
public class GMOSeedInsectResistanceResearchPolicy extends PolicyCard implements Serializable
{

  public static final String TITLE =
     "GMO Seed Insect Resistance Research";

  public static final String TEXT =
    "Each participating region spends X million dollars to fund GMO seed research " +
    "for increasing insect resistance of target crop.";

  /**
   * The number of votes required for this policy.  A value of 1 means that
   * only one player must vote to enact this policy.
  */
  public final static int VOTES_REQUIRED = 1;



  /**
   * {@inheritDoc}
  */
  @Override
  public int votesRequired() {return VOTES_REQUIRED;}

  /**
   * {@inheritDoc}
  */
  @Override
  public boolean voteWaitForAll() {return true;}

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
  public EnumFood[] getValidTargetFoods()
  { return EnumFood.CROP_FOODS;
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
