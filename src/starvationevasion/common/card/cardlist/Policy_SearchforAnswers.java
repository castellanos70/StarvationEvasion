package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.common.GameState;

import java.util.EnumSet;

//Not functional, and not in EnumPolicy. Uncomment it there to re-enable this card.
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

public class Policy_SearchforAnswers extends AbstractPolicy
{
  public static final String TITLE = "Search for Answers";
  
  public static final String TEXT = 
      "Pay $50 million. Search your deck for a card and " +
      "add that card to your hand. Your deck is then shuffled.";
  
  public static final EnumSet<GameState> PLAY_STATES = //when the card can be used
      EnumSet.of(GameState.DRAFTING);
  
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
  @Override
  public int getActionPointCost(EnumPolicy policy) {return 1;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<GameState> getUsableStates()
  {
    return PLAY_STATES;
  }
  
  //TODO Have some Collection to hold each region's deck, that can be iterated over, 
  //and have an accessor method here
  
}
