package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
*/

public class Policy_Fundraiser extends GameCard
{

  public static String TITLE = "Fundraiser";
  
  public static String TEXT = 
      "You raise 1 million dollars";
  
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
}
