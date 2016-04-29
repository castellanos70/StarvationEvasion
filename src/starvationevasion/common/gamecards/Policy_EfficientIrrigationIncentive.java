
package starvationevasion.common.gamecards;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a percentage X [1% through 100%].<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The sim estimates the number and location of farms within
 * the region and the amount spent by those farms on improved irrigation. The
 * model will need a current distribution of irrigation efficiency levels, the
 * four control points of an ease-in-out cubic Bezier function giving efficiency
 * increase as a function of dollars spent and a distribution of current water
 * sources being used by farms.<br><br>
 * Depending on the region and the area within the region, the effects of lower
 * water usage might include a permanent (through endgame) reduction of farming
 * costs, aquifer depletion, and/or river flow depletion. Increased irrigation
 * efficiency also reduces pesticides, fertilizer, and turbidity levels in
 * outflowing rivers.<br><br>
 *
 * X% of the money that the sim estimates is spent for improved irrigation
 * is deducted from the regions tax revenue at the start of the next turn.
*/
public class Policy_EfficientIrrigationIncentive extends GameCard
{
  public static final String TITLE =
      "Efficient Irrigation Incentive";

  public static final String TEXT =
      "From now through the start of the next turn, X% of money spent by farmers " +
      "in player\'s region for improved irrigation efficiency is tax deductible.";

  public static final EnumSet<EnumGameState> PLAY_STATES = //when the card can be used
      EnumSet.of(EnumGameState.PLANNING_STATE);
  
  public Policy_EfficientIrrigationIncentive()
  {
    this.setUsableStates(PLAY_STATES);
  }


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
   * Percentage tax break.
   * {@inheritDoc}
   */
  @Override
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(10);
    options.add(25);
    options.add(50);
    return options;
  }

}
