package starvationevasion.common.policies;

import starvationevasion.common.gamecards.GameCard;

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
