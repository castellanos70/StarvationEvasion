package starvationevasion.common.gamecards;

import java.util.EnumSet;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.State;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br>
 *                 {@value #FLAVOR_TEXT_SOURCE}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies the draw pile must
 * be set at the start of the game rather than cards being randomly picked at draw
 * time.
*/
public class Policy_CovertIntelligence extends GameCard
{
  public static final String TITLE =
      "Covert Intelligence";

  public static final String TEXT =
      "You get to covertly examine target player's hand and the top seven cards " +
      "of that player's deck. You may target yourself. " +
      "During the voting phase, other players will see that you have " +
      "played this card, but not know its target.";
  
  public static final String FLAVOR_TEXT =
      "'Spy' is such a short ugly word. I prefer 'espionage.' Those extra three " +
      "syllables really say something.";
  
  public static final String FLAVOR_TEXT_SOURCE =
      "-Howard Tayler, Emperor Pius Dei";

  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);
  
  /**
   * {@inheritDoc}
  */
  @Override
  public String getTitle(){ return TITLE;}

  /**
   * {@inheritDoc}
  */
  @Override
  public String getGameText(){ return TEXT;}

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
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  { return EnumRegion.US_REGIONS;
  }
}
