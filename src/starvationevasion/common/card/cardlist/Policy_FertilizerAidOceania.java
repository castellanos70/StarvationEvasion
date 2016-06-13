package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.common.GameState;

import java.util.ArrayList;
import java.util.EnumSet;


/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects X million dollars.<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: model needs four control points of each ease-in-out cubic Bezier
 * function giving investment verses food trade penalty function reduction. This one
 * time spending permanently reduces the regions penalty function.<br><br>.
 */

public class Policy_FertilizerAidOceania extends AbstractPolicy
{

  public static final String TITLE =
    "Fertilizer Aid to Oceania";

  public static final String TEXT =
    "Each region of the United States sends X million dollars in fertilizer to Oceania.";

  public static final EnumSet<GameState> PLAY_STATES = //when the card can be used
      EnumSet.of(GameState.DRAFTING);

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

  @Override
  public int getActionPointCost(EnumPolicy policy) {return 2;}
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<GameState> getUsableStates()
  {
    return PLAY_STATES;
  }

  /**
   * Millions of dollars spent by each participating region.
   * {@inheritDoc}
   */
  @Override
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(5);
    options.add(10);
    options.add(25);
    return options;
  }

}
