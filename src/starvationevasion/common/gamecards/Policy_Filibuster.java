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
*/

public class Policy_Filibuster extends GameCard
{

  public static final String TITLE = "Filibuster";
  
  public static final String TEXT = 
      "Play this card before the voting phase to return target " +
      "region\'s policy card owner\'s hand.";
  
  public static final String FLAVOR_TEXT =
      "I do not like them, Sam-I-Am, I do not like green eggs and ham.";
  
  public static final String FLAVOR_TEXT_SOURCE =
      "-Senator Ted Cruz, reading Green Eggs and Ham";
      //https://www.youtube.com/watch?v=0-4FQAov2xI
  
  //TODO: let this be played during the "policy-reveal" phase, when that's implemented
  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(null);
  
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
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  {
	return EnumRegion.US_REGIONS;
  }
}
