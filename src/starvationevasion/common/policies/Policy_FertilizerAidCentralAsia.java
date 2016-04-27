package starvationevasion.common.policies;

import java.util.ArrayList;

public class Policy_FertilizerAidCentralAsia extends PolicyCard
{

  public static final String TITLE =
    "Fertilizer Aid to Central Asia";

  public static final String TEXT =
    "Each region of the United States sends X million dollars in fertilizer to Central Asia.";


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
