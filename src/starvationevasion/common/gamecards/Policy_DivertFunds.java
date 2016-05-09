package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.EnumSet;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br><br>
 *
 * Votes Required: Automatic<br><br>
 *
*/

public class Policy_DivertFunds extends GameCard
{
  
  public static final String TITLE = "Divert Funds";
  
  public static final String TEXT = 
      "Discard your current hand. Gain $14 million.";
 
  public static final String FLAVOR_TEXT =
      "No one can come up with a good idea. Good thing we're paid anyways.";
  
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
  public String getFlavorText() {return FLAVOR_TEXT;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }
  
}
