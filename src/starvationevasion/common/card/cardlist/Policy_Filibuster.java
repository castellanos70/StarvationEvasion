package starvationevasion.common.card.cardlist;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.card.AbstractPolicy;

public class Policy_Filibuster extends AbstractPolicy
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
