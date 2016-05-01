package starvationevasion.common.gamecards;

import starvationevasion.sim.carddeck;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies that
 * the cards played must be kept inside of some Collection as a "Discard"
 * pile that can be iterated over.
*/
public class Policy_Redraft extends GameCard
{
  public static String TITLE = "Redraft";
  
  public static String TEXT = 
      "When played, the owner of this policy may return a card from " +
      "their discard pile to their hand.";
  
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
  //TODO Might need to be changed how this accesses the discard.
  public EnumPolicy[] getValidTargetCards() {return CardDeck.getDiscardPile();}
  
}
