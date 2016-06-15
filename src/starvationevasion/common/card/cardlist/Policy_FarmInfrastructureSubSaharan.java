package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;
import starvationevasion.common.GameState;

import java.util.ArrayList;
import java.util.EnumSet;


public class Policy_FarmInfrastructureSubSaharan extends AbstractPolicy
{

  public static final String TITLE =
          "Aid for Farm Infrastructure in Sub-Saharan Africa";

  public static final String TEXT =
          "Each region of the United States sends X million dollars in foreign aid for capital development " +
          "of farming infrastructure to Sub-Saharan Africa.";
  
  public static final EnumSet<GameState> PLAY_STATES = //when the card can be used
      EnumSet.of(GameState.DRAFTING);

  /**
   * The number of votes required for this policy to be enacted.
   */
  public final static int VOTES_REQUIRED = 4;


  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() { return TITLE;}

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGameText() { return TEXT;}

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
   * Millions of dollars that can be spent on this card by each player region
   * {@inheritDoc}
   */
  @Override
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(5);
    options.add(10);
    options.add(20);
    return options;
  }
}