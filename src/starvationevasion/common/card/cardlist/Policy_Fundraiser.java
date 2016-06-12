package starvationevasion.common.card.cardlist;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.common.card.EnumPolicy;

public class Policy_Fundraiser extends AbstractPolicy
{

  public static String TITLE = "Fundraiser";
  
  public static String TEXT = 
      "You raise 1 million dollars";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() {return TITLE;}

  @Override
  public int getActionPointCost(EnumPolicy policy) {return 1;}
  /**
   * {@inheritDoc}
   */
  @Override
  public String getGameText() {return TEXT;}
}
