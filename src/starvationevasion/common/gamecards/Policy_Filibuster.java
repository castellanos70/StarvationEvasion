package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br>
 *                 {@value #FLAVOR_TEXT_SOURCE}<br><br>
 *
 * Votes Required: Automatic<br><br>
*/

public class Policy_Filibuster extends GameCard
{

  public static final String TITLE = "Filibuster";
  
  public static final String TEXT = 
      "Play this card before the voting phase to return a target " +
      "region's policy card to their hand.";
  
  public static final String FLAVOR_TEXT =
      "I do not like them, Sam-I-Am, I do not like green eggs and ham.";
  
  public static final String FLAVOR_TEXT_SOURCE =
      "-Senator Ted Cruz, reading Green Eggs and Ham";
      //https://www.youtube.com/watch?v=0-4FQAov2xI
  
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
  public String getFlavorText(){ return FLAVOR_TEXT;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getFlavorTextSource(){ return FLAVOR_TEXT_SOURCE;}
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  {
	return EnumRegion.US_REGIONS;
  }
}
