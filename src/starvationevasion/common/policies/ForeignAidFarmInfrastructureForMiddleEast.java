package starvationevasion.common.policies;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dayloki on 4/1/2016.
 */
public class ForeignAidFarmInfrastructureForMiddleEast extends PolicyCard implements Serializable
{

  public static final String TITLE =
          "Foreign Aid for Farm Infrastructure in the Middle East";

  public static final String TEXT =
          "The US sends millions of dollars in foreign aid for capital development " +
                  "of farming infrastructure in the Middle East";

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
  public EnumRegion getValidTargetRegion()
  {
    return EnumRegion.MIDDLE_EAST;
  }

  /**
   * Millions of dollars that can be spent on this card
   * {@inheritDoc}
   */
  public ArrayList<Integer> getOptionsOfVariable()
  {
    ArrayList<Integer> options=new ArrayList<>();
    options.add(7);
    options.add(28);
    options.add(49);
    return options;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PolicyCard.EnumVariableUnit getRequiredVariables(PolicyCard.EnumVariable variable)
  {
    if (variable == PolicyCard.EnumVariable.X) return PolicyCard.EnumVariableUnit.MILLION_DOLLAR;
    return null;
  }
}
