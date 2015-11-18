package starvationevasion.sim;

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
   * The states total income.
   */
  private int totalIncome = 0;

  /**
   * The states total farmland.
   */
  private int totalFarmLand = 0;

  /**
   * Average conversion factor, this is set by the Simulator.
   */
  private float averageConversionFactor;

  /**
   * Amount of fertilizer in Kg the states uses in a year.
   */
  private float kgPerAcreFertilizer;

  /**
   * The states income as it corresponds to category.
   */
  private int[]   incomePerCategory;

  /**
   * The states adjustment factors, twelve in total, one per category
   */
  private float[] adjustmentFactors;

  /**
   * The states ratios of it's category income to total income.
   */
  private float[] incomeToCategoryPercentages;

  /**
   * The states percentages of land dedicated to each category
   */
  private float[] landPerCategory;

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
    landPerCategory             = new float[Constant.TOTAL_AGRO_CATEGORIES];
    adjustmentFactors           = new float[Constant.TOTAL_AGRO_CATEGORIES];

    String[] stateData = data.split(",");
    int numDataFields = stateData.length;
    //System.out.println("NUM DATA FIELDS "+numDataFields);

    name = stateData[Constant.STATE_NAME_IDX];
    totalFarmLand = Integer.parseInt(stateData[Constant.STATE_TOTAL_FARM_LAND_IDX]);

    //1. Tally total income
    for (int i = Constant.STATE_CITRUS_IDX; i < numDataFields; i++)
    {
      //System.out.println(stateData[i]);
      totalIncome += Integer.parseInt(stateData[i]);
    }

    for (int i = 0; i < Constant.TOTAL_AGRO_CATEGORIES; i++)
    {
      incomePerCategory[i]           = Integer.parseInt(stateData[i+3]);
      incomeToCategoryPercentages[i] = (float) incomePerCategory[i] / (float) totalIncome;
      landPerCategory[i]             = incomeToCategoryPercentages[i] * (float)totalFarmLand;
    }
  }

  /**
   * Getter
   */
  public float[] getPercentages()
  {
    return incomeToCategoryPercentages;
  }

  /**
   * This Method calculates the initial category adjustment factors <br></>
   * along with setting the average conversion factor.
   * @param acf
   */
  public void setAverageConversionFactor(float acf)
  {
    averageConversionFactor = acf;
    for (int i = 0; i < Constant.TOTAL_AGRO_CATEGORIES; i++)
    {
      adjustmentFactors[i] = incomeToCategoryPercentages[i] - averageConversionFactor;
      System.out.println(name + " Adj Factors "+adjustmentFactors[i]);
    }
  }
}
