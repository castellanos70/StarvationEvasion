package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;

public class Policy_SharetheKnowledge extends GameCard
{

  public static String TITLE = "Share the Knowledge";
  
  public static String TEXT = 
      "You may look at target US Region's hand and " +
      "you may play one card from their hand.  Pay that region " +
      "5 million dollars";
  
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
