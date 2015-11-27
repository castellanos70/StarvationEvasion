package starvationevasion.sim;


import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;


/**
 * PlayerData is maintained by the simulator. A copy of this data can be requested by
 * the Server using Simulator.getPlayerData().<br><br>
 * This structure contains the player's current revenue, crop income, crop production,
 * crop land allocations, cards in deck, discard and hand, and other player information.
 */
public class PlayerData extends RegionData
{
  /**
   * Total player revenue in millions of dollars for the current simulation year.
   */
  private int revenue;

  private CardDeck deck;

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
  private int[] incomePerCategory;

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
   * Class constructor
   *
   * @param playerRegion the player's region.
   */
  public PlayerData(EnumRegion playerRegion)
  {
    super(playerRegion);
    deck = new CardDeck(playerRegion);
  }


  /**
   * Data string of comma separated values, which indicate
   * the income from a category of Crop. These values will
   * help to determine how much land will be dedicated to a
   * certain Crop.
   *
   * @param data crop data.
   */
  public void setData(String data)
  {
    incomePerCategory = new int[EnumFood.SIZE];
    incomeToCategoryPercentages = new float[EnumFood.SIZE];
    landPerCategory = new float[EnumFood.SIZE];
    adjustmentFactors = new float[EnumFood.SIZE];

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

    for (int i = 0; i < EnumFood.SIZE; i++)
    {
      incomePerCategory[i] = Integer.parseInt(stateData[i + 3]);
      incomeToCategoryPercentages[i] = (float) incomePerCategory[i] / (float) totalIncome;
      landPerCategory[i] = incomeToCategoryPercentages[i] * (float) totalFarmLand;
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
   * This Method calculates the initial category adjustment factors
   * along with setting the average conversion factor.
   *
   * @param acf
   */
  public void setAverageConversionFactor(float acf)
  {
    averageConversionFactor = acf;
    for (int i = 0; i < EnumFood.SIZE; i++)
    {
      adjustmentFactors[i] = incomeToCategoryPercentages[i] - averageConversionFactor;
      //System.out.println(name + " Adj Factors "+adjustmentFactors[i]);
    }
  }
}
