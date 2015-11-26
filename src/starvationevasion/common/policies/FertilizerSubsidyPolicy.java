package starvationevasion.common.policies;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target food product and percentage Y.<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: This policy makes conventional farming of target crop more
 * economic and therefore shifts farmer plantings. It also affects the use of
 * the fertilizer on existing crops causing a change in yield and a change in
 * fertilizer run off.
*/
public class FertilizerSubsidyPolicy extends PolicyCard implements Serializable
{

  public static final String TITLE =
      "Fertilizer or Feed Subsidy";

  public static final String TEXT =
      "This policy offers a subsidy of X% rebate to farmers in your region purchasing " +
      "commercial fertilizer for target crop or feed supplements for target live stock.";


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
  { return EnumFood.values();
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
