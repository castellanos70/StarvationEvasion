package starvationevasion.common.gamecards;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies that
 * each region's deck must be made prior to drawing any single card, but rather
 * at the beginning of the game.
*/

public class Policy_SearchforAnswers extends GameCard
{
  public static final String TITLE = "Search for Answers";
  
  public static final String TEXT = 
      "The owner of this card pays 50 million dollars " +
      "and may search their deck for a card and " +
      "add that card to their hand.";
  
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
  public int actionPointCost() {return 3;}
  
  //TODO Have some Collection to hold each region's deck, that can be iterated over, 
  //and have an accessor method here
  
}
