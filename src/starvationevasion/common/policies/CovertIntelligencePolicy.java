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
 * Model Effects: The fact that such cards as this exist, implies the draw pile must
 * be set at the start of the game rather than cards being randomly picked at draw
 * time.
*/
public class CovertIntelligencePolicy extends PolicyCard implements Serializable
{
  public static final String TITLE =
      "Covert Intelligence";

  public static final String TEXT =
      "You get to covertly examine target player's hand and the top two cards " +
      "of that player's deck. You may target yourself. " +
      "During the voting phase, other players will see that you have " +
      "played this card, but not know its target. Bonus: If you can " +
      "correctly answer a hidden research question, you examine the top seven " +
      "cards of the target player's deck.";

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
  public EnumRegion[] getValidTargetRegions()
  { return EnumRegion.US_REGIONS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumVariableUnit getRequiredVariables(EnumVariable variable)
  {
    if (variable == EnumVariable.X) return EnumVariableUnit.UNIT;
    return null;
  }

  /**
   * {@inheritDoc}
  */
  @Override
  public String validate()
  {
    String msg = super.validate();
    if (msg != null) return msg;

    if (varX != 2 && varX != 7)
    {
      return getPolicyName() + "["+varX +"]: Cards revealed must be 2 or 7.";
    }

    return null;
  }
}
