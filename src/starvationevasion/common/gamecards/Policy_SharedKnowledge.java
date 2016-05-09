package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.State;

import java.util.EnumSet;

//Not functional, and not in EnumPolicy. Uncomment it there to re-enable this card.
/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies that every player's
 * hands must, in theory, be able to be viewed during any part of the game.
 */

public class Policy_SharedKnowledge extends GameCard
{

  public static final String TITLE = "Shared Knowledge";

  public static final String TEXT = 
      "Look at target player\'s hand. " +
      "You may play one card from the revealed hand as though it is in your hand. " +
      "Pay that player $10 million.";
  
  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);

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
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  {
    return EnumRegion.US_REGIONS;
  }
}
