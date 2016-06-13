package starvationevasion.common.card.cardlist;

import starvationevasion.common.EnumFood;
import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.common.GameState;

import java.util.EnumSet;

/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target food product.<br><br>
 *
 * Votes Required: Automatic<br><br>
 *
 * Model Effects: This policy makes conventional farming of target crop more
 * economic and therefore shifts farmer plantings. It also affects the use of
 * the fertilizer on existing crops causing a change in yield and a change in
 * fertilizer run off.
*/
public class Policy_FertilizerSubsidy extends AbstractPolicy
{

  public static final String TITLE =
      "Fertilizer or Feed Subsidy";

  public static final String TEXT =
      "This policy offers a subsidy of 20% rebate to farmers in your region purchasing " +
      "commercial fertilizer or feed supplements for target crop or live stock.";

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
   * {@inheritDoc}
   */
  @Override
  public EnumFood[] getValidTargetFoods()
  { return EnumFood.values();
  }

}
