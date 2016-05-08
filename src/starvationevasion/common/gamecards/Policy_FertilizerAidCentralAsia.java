package starvationevasion.common.gamecards;

import starvationevasion.server.model.State;

import java.util.ArrayList;
import java.util.EnumSet;

public class Policy_FertilizerAidCentralAsia extends GameCard
{

  public static final String TITLE =
    "Fertilizer Aid to Central Asia";

  public static final String TEXT =
    "Each region of the United States sends X million dollars in fertilizer to Central Asia.";

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
  public EnumSet<State> getUsableStates()
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
