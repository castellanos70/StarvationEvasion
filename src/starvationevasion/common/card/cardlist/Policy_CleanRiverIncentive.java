
package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;

/**
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
public class Policy_CleanRiverIncentive extends AbstractPolicy
{
  public static final String TITLE =
      "Clean River Incentive";

  public static final String TEXT =
      "X% tax break for farmers in my region who reduce by 20% the outflow of "+
      "pesticides and fertilizers from their farms into the rivers.";
  
  public static final String FLAVOR_TEXT =
      "We should do something before the city council realizes that the 'Kool-Aid' actually " +
      "came straight out of the tap.";
  
  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);
  
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
  public String getFlavorText(){ return FLAVOR_TEXT;}

  @Override
  public int getActionPointCost(EnumPolicy policy) {return 1;}

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }


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
