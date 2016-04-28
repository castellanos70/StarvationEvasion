package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.GameCard;

public class Policy_Filibuster extends GameCard
{

  public static final String TITLE = "Filibuster";
  
  public static final String TEXT = 
      "Play this card before the voting phase to return a target " +
      "region's policy card to their hand.";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() {return TITLE;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getGameText() {return TEXT;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  {
	return EnumRegion.US_REGIONS;
  }
}
