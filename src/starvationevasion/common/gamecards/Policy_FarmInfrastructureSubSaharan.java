package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;


public class Policy_FarmInfrastructureSubSaharan extends GameCard
{

  public static final String TITLE =
          "Aid for Farm Infrastructure in Sub-Saharan Africa";

  public static final String TEXT =
          "Each region of the United States sends X million dollars in foreign aid for capital development " +
          "of farming infrastructure to Sub-Saharan Africa.";
  
  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);

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
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EnumSet<State> getUsableStates()
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
