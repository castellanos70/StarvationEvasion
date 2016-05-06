package starvationevasion.common.gamecards;

import java.util.EnumSet;

import starvationevasion.server.model.State;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: The fact that such cards as this exist, implies that there
 * should be some way to have separate pools of money that separate available
 * funds into what those funds may be used for.
*/

public class Policy_SpecialInterests extends GameCard
{
  public static final String TITLE = "Special Interests";
  
  public static final String TEXT = 
      "For this voting phase, the owner of this card " +
      "gains $100 million that they may spend " +
      "only to support policies drafted this turn.";
  
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
}
