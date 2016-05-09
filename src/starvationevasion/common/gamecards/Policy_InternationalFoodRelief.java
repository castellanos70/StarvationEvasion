package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;

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
 * in the most efficient manner discovered by the sim where
 * efficiency calculation is based on the type of commodity
 * available in each participating region, each
 * country's nutritional need and each country's import penalty function.
 * Note: depending on yields, different regions may contribute different
 *    mixes of commodities.<br><br>
 * Note: Not all foods sent will necessarily go to the same region.<br><br>
 *
 * 10% bonus to effectiveness of total dollars spent per participating region.<br><br>
 *
 * Food purchased for relief inflates the global sell foodPrice of the food type by a
 * direct reduction of supply without effect on demand (since those to whom the
 * relief is delivered are presumed to lack the resources to have been part of the demand).
*/
public class Policy_InternationalFoodRelief extends GameCard
{

  public static final String TITLE =
    "International Food Relief Program";

  public static final String TEXT =
    "Each participating region spends X million dollars to purchase " +
    "from its local farmers surplus commodity food for redistribution to where it is" +
      "most needed.";
  
  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);

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
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }

  /**
   * Millions of dollars spent by each participating region.
   * {@inheritDoc}
   */
  @Override
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(5);
    options.add(10);
    options.add(15);
    return options;
  }
}
