package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;

import java.awt.image.BufferedImage;

public abstract class AbstractAgriculturalUnit
{
  private static final boolean VERBOSE = false;
  private static final int START_YEAR = Constant.FIRST_YEAR;

  protected String name;
  protected BufferedImage flag;
  protected double shippingLongitude;
  protected double shippingLatitude;

  protected static final int YEARS_OF_SIM = 1+Constant.LAST_YEAR - Constant.FIRST_YEAR;

  protected GeographicArea border;


  //Note: Values below that are, in the countryData.cvs file, defined as type int
  //      should be displayed to the user as type int. However, as some of these
  //      values may be adjusted from year-to-year by continuous functions, it
  //      is useful to maintain their internal representations as doubles.

  //Note: As the start of milestone II, births and migration are assumed constant
  //      for each year of the model and medianAge is not used at all.
  //      However, it might be that before the end of milestone II, we are given simple functions
  //      for these quantities: such as a constant rate of change of birth rate replacing the
  //      constant birth rate or a data lookup table that has predefined values for each year.
  //      The development team should expect and plan for such mid-milestone updates.
  protected int[] population = new int[YEARS_OF_SIM];       // in people
  protected int medianAge;  // in years
  protected int births;  // number of live births x 1,000 per year.
  protected int mortality;   // number of deaths x 1,000 per year.
  protected int migration;   // immigration - emigration x 1,000 individuals.
  protected int undernourished;  // percentage of population. 0.50 is 50%.

  protected int[] cropIncome = new int[EnumFood.SIZE]; // x $1000
  protected int[] cropProduction = new int[EnumFood.SIZE]; //in metric tons.

  protected int landTotal;  //in square kilometers
  protected int[] landCrop = new int[EnumFood.SIZE];  //in square kilometers
  protected int[] cropImport = new int[EnumFood.SIZE];  //in metric tons.
  protected int[] cropExport = new int[EnumFood.SIZE];  //in metric tons.
  
  //Note: in milestone II, the model does nothing with the cultivation method.
  protected double[] cultivationMethod = new double[EnumGrowMethod.SIZE]; //percentage
  
  //In Milestone II, crop yield and per capita need are defined in the first year and assumed constant 
  //    throughout each year of the simulation.
  //    This will NOT be changed before the end of milestone II as it would require redefining the core of
  //    the model's calculations.
  protected double[] cropYield = new double[EnumFood.SIZE]; //metric tons per square kilometer
  protected double[] cropNeedPerCapita = new double[EnumFood.SIZE]; //metric tons per person per year.


  /**
   * The states total income.
   */
  private int totalIncome = 0;


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



  protected AbstractAgriculturalUnit(String name)
  {
    this.name = name;
  }

  /**
   * @return country name
   */
  final public String getName()
  {
    return name;
  }

  /**
   * @param year year in question
   * @return population in that year
   */
  final public int getPopulation(int year)
  {
    return population[year - START_YEAR];
  }

  /**
   * @return median age
   */
  final public double getMedianAge()
  {
    return medianAge;
  }

  /**
   * @return birth rate at end of the current year of the simulation.
   */
  final public int getBirths()
  {
    return births;
  }

  final public int getMortality()
  {
    return mortality;
  }

  final public int getMigration()
  {
    return migration;
  }

  /**
   * @return % undernourished at end of the current year of the simulation.
   */
  final public int getUndernourished()
  {
    return undernourished;
  }

  /**
   * @param crop crop in question
   * @return tons produced  at end of the current year of the simulation.
   */
  final public int getCropProduction(EnumFood crop)
  {
    return cropProduction[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons produced at end of the current year of the simulation.
   */
  final public int getCropIncome(EnumFood crop)
  {
    return cropIncome[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons exported
   */
  final public int getCropExport(EnumFood crop)
  {
    return cropExport[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons imported
   */
  final public int getCropImport(EnumFood crop)
  {
    return cropImport[crop.ordinal()];
  }


  final public int getLandTotal()
  {
    return landTotal;
  }


  /**
   * @param crop crop in question
   * @return square km planted with crop
   */
  final public int getCropLand(EnumFood crop)
  {
    return landCrop[crop.ordinal()];
  }

  /**
   * @param method cultivation method
   * @return % land cultivated by method
   */
  final public double getMethodPercentage(EnumGrowMethod method)
  {
    return cultivationMethod[method.ordinal()];
  }

  /**
   * @param crop
   * @return yield for crop
   */
  final public double getCropYield(EnumFood crop)
  {
    return cropYield[crop.ordinal()];
  }

  /**
   * @param crop
   * @return tons of crop needed per person
   */
  final public double getCropNeedPerCapita(EnumFood crop)
  {
    return cropNeedPerCapita[crop.ordinal()];
  }


  /**
   * Returns difference between country's production and need for a crop for the specified year.
   * If a positive value is returned, country has a surplus available for export.
   * If a negative value is returned, country has unmet need to be satisfied by imports.
   *
   * @param year year in question
   * @param type type of crop
   * @return surplus/shortfall of crop
   */
  final public double getSurplus(int year, EnumFood type)
  {
    return this.getCropProduction(type) - getTotalCropNeed(year, type);
  }

  /**
   * Returns how many tons of crop country needs for specified year
   *
   * @param year year in question
   * @param crop crop in question
   * @return total tons needed to meet population's need
   */
  final public double getTotalCropNeed(int year, EnumFood crop)
  {
    double tonsPerPerson = getCropNeedPerCapita(crop);
    int population = getPopulation(year);
    return tonsPerPerson * population;
  }

  /**
   * Calculates net crop available using formula from p. 15 of spec 1.7
   *
   * @param crop crop in question
   * @return metric tons available
   */
  final public double getNetCropAvailable(EnumFood crop)
  {
    double available = getCropProduction(crop) + getCropImport(crop) - getCropExport(crop);
    return available;
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
