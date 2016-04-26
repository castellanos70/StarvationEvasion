package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;

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
public class Policy_CovertIntelligence extends PolicyCard
{
  public static final String TITLE =
      "Covert Intelligence";

  public static final String TEXT =
      "You get to covertly examine target player's hand and the top seven cards " +
      "of that player's deck. You may target yourself. " +
      "During the voting phase, other players will see that you have " +
      "played this card, but not know its target.";

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
}
