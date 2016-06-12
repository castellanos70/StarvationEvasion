
package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.server.model.State;

import java.util.EnumSet;

//TODO: Figure out a way to include when the card can be played on the Javadoc
/**
 * Draft Affects: 
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: 
*/
public class Policy_DryPortAgreement extends AbstractPolicy
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


  @Override
  public int getActionPointCost(EnumPolicy policy) {return 1;}
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }


}
