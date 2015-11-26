package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Draft Affects: When drafting this policy, the model must inform
 * the player of Y and require the player selects X.<br><br>
 *
 * Model Effects: <br><br>
 */
public class EthanolTaxCreditChangePolicy extends PolicyCard implements Serializable
{

  public static final String TITLE =
      "Ethanol Production Tax Credit Change";

  public static final String TEXT =
      "Currently, an ethanol producer located in my region is entitled " +
      "to a Y% tax credit to cost of ethanol production, including " +
      "cellulosic ethanol. This policy changes that to X%.";


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
    return null;
  }
}
