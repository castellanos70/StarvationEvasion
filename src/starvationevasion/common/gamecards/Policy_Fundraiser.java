package starvationevasion.common.gamecards;

import java.util.EnumSet;

import starvationevasion.server.model.State;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br>
 *                 {@value #FLAVOR_TEXT_SOURCE}<br><br>
 *
 * Votes Required: Automatic<br><br>
*/

public class Policy_Fundraiser extends GameCard
{

  public static final String TITLE = "Fundraiser";
  
  public static final String TEXT = 
      "You raise 1 million dollars";
  
  public static final String FLAVOR_TEXT =
      "...A small loan of a million dollars.";
  
  public static final String FLAVOR_TEXT_SOURCE =
      "-Donald Trump";
  
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
}
