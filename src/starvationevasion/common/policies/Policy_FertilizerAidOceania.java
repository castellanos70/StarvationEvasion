package starvationevasion.common.policies;

import java.util.ArrayList;


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

public class Policy_FertilizerAidOceania extends PolicyCard
{

  public static final String TITLE =
    "Fertilizer Aid to Oceania";

  public static final String TEXT =
    "Each region of the United States sends X million dollars in fertilizer to Oceania.";


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
