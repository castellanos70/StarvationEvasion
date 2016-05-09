package starvationevasion.common.policies;

import starvationevasion.common.gamecards.GameCard;

public class Policy_DiverttheFunds extends GameCard
{

  public static String TITLE = "Divert the Funds";
  
  public static String TEXT = 
      "You discard all cards in your current hand " +
      "and gain 14 million dollars";
  
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
