
package starvationevasion.common.policies;

import starvationevasion.common.*;

import java.io.Serializable;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects a percentage X
 * [1% through 100%] and Y% [1% through 100%].<br><br>
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
 * The Y% tax break is applied to the regions tax revenue on the next turn.
*/
public class CleanRiverIncentivePolicy extends PolicyCard implements Serializable
{
  public static final String TITLE =
      "Clean River Incentive";

  public static final String TEXT =
      "X% tax break for farmers in my region who reduce by Y% the outflow of "+
      "pesticides and fertilizers from their farms into the rivers.";

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
  public EnumVariableUnit getRequiredVariables(EnumVariable variable)
  {
    if (variable == EnumVariable.X) return EnumVariableUnit.PERCENT;
    if (variable == EnumVariable.Y) return EnumVariableUnit.PERCENT;
    return null;
  }
}
