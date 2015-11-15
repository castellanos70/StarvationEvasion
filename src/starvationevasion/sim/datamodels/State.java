package starvationevasion.sim.datamodels;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;

/**
 * Created by Alfred on 11/15/15.
 *
 * The State class describes a state in the United States. <br></>
 * Each is classified by region, total land, total farm land, and <br></>
 * components to calculate how much land is dedicated to a specific <br></>
 * piece of agriculture.
 *
 */

public class State
{
  /**
   * The states name.
   */
  private String name;

  /**
   * The states region.
   */
  private EnumRegion region;

  /**
   * The states income as it corresponds to category.
   */
  private int[]   incomePerCategory;

  /**
   * The states ratios of it's category income to total income.
   */
  private float[] incomeToCategoryPercentages;

  /**
   * The states percentages of land dedicated to each category
   */
  private float[] landCategoryPercentages;

  /**
   * Class constructor creates a new State Object with <br></>
   * a data string of comma separated values, which indicate <br></>
   * the income from a category of Crop. These values will <br></>
   * help to determine how much land will be dedicated to a <br></>
   * certain Crop.
   *
   * @param data
   */
  public State(String data)
  {
    incomePerCategory           = new int[Constant.TOTAL_AGRO_CATEGORIES];
    incomeToCategoryPercentages = new float[Constant.TOTAL_AGRO_CATEGORIES];
    landCategoryPercentages     = new float[Constant.TOTAL_AGRO_CATEGORIES];

    /*
    String[] stateData = data.split(",");
    int numDataFields = stateData.length;
    System.out.println("NUM DATA FIELDS "+numDataFields);
    for (int i = 0; i < numDataFields; i++)
    {
      System.out.println(stateData[i]);
      1. Tally total income
    }

    2. Calculate Category income %'s
    3. Land for Category = Category income % * Total land

    4. set Adjustment factor to Category Income % - NonLocal Conversion Factor
    */
  }
}
