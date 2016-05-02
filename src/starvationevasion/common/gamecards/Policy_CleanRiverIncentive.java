
package starvationevasion.common.gamecards;

import java.util.ArrayList;
import java.util.EnumSet;

//TODO: Figure out a way to include when the card can be played on the Javadoc
/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a percentage X
 * [5%, 10%, or 25%].<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The sim estimates the number and location of farms
 * within the region and the amount spent by those farms on improved irrigation.
 * The model will need a current amount of pesticides and fertilizers in use
 * per square km, the current percentage that goes into rivers, and, the four
 * control points of each ease-in-out cubic Bezier function giving pesticide
 * and fertilizer use verses yield for each of the 12 farm products and
 * pesticide and fertilizer use verses cost of alternative methods including
 * more expensive seeds,  natural pest controls, and landscape for reduced run-off.<br><br>
 * The X% tax break is applied to the regions tax revenue on the next turn.
*/
public class Policy_CleanRiverIncentive extends GameCard
{
  public static final String TITLE =
      "Clean River Incentive";

  public static final String TEXT =
      "X% tax break for farmers in my region who reduce by 20% the outflow of "+
      "pesticides and fertilizers from their farms into the rivers.";
  
  public static final EnumSet<EnumGameState> PLAY_STATES = //when the card can be used
      EnumSet.of(EnumGameState.PLANNING_STATE);
  
  public Policy_CleanRiverIncentive()
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
    options.add(5);
    options.add(10);
    options.add(25);
    return options;
  }
}
