package starvationevasion.common.card.cardlist;

import java.util.EnumSet;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.common.GameState;

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

public class Policy_SpecialInterests extends AbstractPolicy
{
  public static final String TITLE = "Special Interests";
  
  public static final String TEXT = 
      "For this voting phase, the owner of this card " +
      "gains $100 million that they may spend " +
      "only to support card drafted this turn.";
  
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
}
