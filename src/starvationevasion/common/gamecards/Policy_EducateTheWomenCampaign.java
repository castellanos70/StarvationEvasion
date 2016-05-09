package starvationevasion.common.gamecards;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.State;

import java.util.EnumSet;


/**
 * Title: {@value #TITLE}<br><br>
 * Game Text: {@value #TEXT}<br><br>
 *
 * Draft Affects: When drafting this policy, player selects target world
 * region and X million dollars.<br><br>
 *
 * Votes Required: {@value #VOTES_REQUIRED}<br><br>
 *
 * Model Effects:  model needs four control points of each ease-in-out cubic Bezier
 * function giving investment verses food trade penalty function reduction. This one
 * time spending permanently reduces the regions penalty function.<br><br>
 * If approved, each US region must pay $10 million.
*/
public class Policy_EducateTheWomenCampaign extends GameCard
{

  public static final String TITLE =
      "Educate the Women Campaign";

  public static final String TEXT =
      "The US sends a total of $70 million to educate woman of the target world " +
      "region including reading, basic business and farming techniques.";

  public static final EnumSet<State> PLAY_STATES = //when the card can be used
      EnumSet.of(State.DRAFTING);
  
  /**
   * The number of votes required for this policy to be enacted.
   */
  public final static int VOTES_REQUIRED = 4;


  /**
   * This policy requires {@value #VOTES_REQUIRED} to be enacted.
   * {@inheritDoc}
  */
  @Override
  public int votesRequired() {return VOTES_REQUIRED;}


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
   * {@inheritDoc}
   */
  @Override
  public EnumRegion[] getValidTargetRegions()
  { return EnumRegion.WORLD_REGIONS;
  }
}
