package starvationevasion.common.gamecards;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Draft Affects: When drafting this policy, the model must inform
 * the player of Y and require the player selects X.<br><br>
 *
 * Model Effects: <br><br>
 */
public class Policy_EthanolTaxCreditChange extends GameCard
{

  public static final String TITLE =
      "Ethanol Production Tax Credit Change";

  public static final String TEXT =
      "Currently, an ethanol producer located in my region is entitled " +
      "to a Y% tax credit to cost of ethanol production, including " +
      "cellulosic ethanol. This policy changes that to X%.";

  public static final EnumSet<EnumGameState> PLAY_STATES = //when the card can be used
      EnumSet.of(EnumGameState.PLANNING_STATE);
  
  public Policy_EthanolTaxCreditChange()
  {
    this.setUsableStates(PLAY_STATES);
  }
  
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
