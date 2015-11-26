package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player
 * selects an amount X to be paid by EACH player who approves the policy. <br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED} through all<br>
 * Eligible Regions: All U.S.<br><br>
 *
 * Model Effects: Commodity food is distributed to relief world hunger
 * in the most efficient manor discovered by the sim where
 * efficiency calculation is based on the type of commodity
 * available in each participating region, each
 * country's nutritional need and each country's import penalty function.
 * Note: depending on yields, different regions may contribute different
 *    mixes of commodities.<br><br>
 *
 * 10% bonus to effectiveness of total dollars spent per participating region.<br><br>
 *
 * Food purchased for relief inflates the global sell price of the food type by a
 * direct reduction of supply without effect on demand (since those to whom the
 * relief is delivered are presumed to lack the resources to have been part of the demand).
*/
public class InternationalFoodReliefProgramPolicy extends PolicyCard implements Serializable
{

  public static final String TITLE =
    "International Food Relief Program";

  public static final String TEXT =
    "Each participating region spends X million dollars to purchase " +
    "their own regions commodity food for relief of world hunger.";

  /* The number of votes required for this policy.  A value of 1 means that
   * only one player must vote to enact this policy.
  */
  public final static int VOTES_REQUIRED = 1;

  /**
   *  {@inheritDoc}
  */
  @Override
  public int votesRequired() {return VOTES_REQUIRED;}

  /**
   *  {@inheritDoc}
  */
  @Override
  public boolean voteWaitForAll() {return true;}

  /**
   *  {@inheritDoc}
  */
  @Override
  public String getTitle(){ return TITLE;}

  /**
   *  {@inheritDoc}
  */
  @Override
  public String getGameText(){ return TEXT;}


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
