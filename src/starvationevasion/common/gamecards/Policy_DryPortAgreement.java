
package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;

//TODO: Figure out a way to include when the card can be played on the Javadoc
/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br><br>
 *
 * Draft Affects: 
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: 
*/
public class Policy_DryPortAgreement extends GameCard
{
  public static final String TITLE =
      "Dry Port Agreement";

  public static final String TEXT =
      "Gives neighboring regions a large yield of a food type you could produce";
  
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
  public String getGameText(){ return TEXT; }
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }


}
