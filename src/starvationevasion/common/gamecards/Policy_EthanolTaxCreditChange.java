package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 * Flavor Text: <i>{@value #FLAVOR_TEXT}</i><br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Draft Affects: When drafting this policy, the model must inform
 * the player of their current tax credit of ethanol production
 *  and require the player selects X.<br><br>
 *
 * Model Effects: <br><br>
 */
public class Policy_EthanolTaxCreditChange extends GameCard
{

  public static final String TITLE =
      "Ethanol Production Tax Credit Change";

  public static final String TEXT =
      "This policy changes an ethanol producer, located in my region, " +
      "to have an X% tax credit to cost of ethanol production, including " +
      "cellulosic ethanol.";
  
  public static final String FLAVOR_TEXT =
      "Any ideas on how to fit 'Honk if you're carbon-neutral' on a license plate?";

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
  public String getFlavorText() {return FLAVOR_TEXT;}
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
  {
    return PLAY_STATES;
  }
  
  /**
   * Percentage tax break.
   * {@inheritDoc}
   */
  @Override
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(0);
    options.add(10);
    options.add(30);
    return options;
  }
}
