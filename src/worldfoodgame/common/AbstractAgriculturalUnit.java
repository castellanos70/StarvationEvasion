package worldfoodgame.common;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.geography.GeographicArea;

import java.awt.image.BufferedImage;

public abstract class AbstractAgriculturalUnit
{
  private static final boolean VERBOSE = false;
  private static final int START_YEAR = Constant.FIRST_YEAR;

  protected String name;
  protected BufferedImage flag;
  protected double shippingLongitude;
  protected double shippingLatitude;
  
  protected static final int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;

  protected GeographicArea border;
  protected AbstractCropLocations cropLocationsCurrentYear;
  protected AbstractCropLocations cropLocationsPreviousYear;

  //Note: Since there are only about 200 countries, it does not take much
  //      space to maintain all annual country data values from the start of the
  //      model through the current year of the model.
  //      This information may be useful in milestone III for
  //      a) displaying graphs
  //      b) improved prediction algorithms that use multi-year trends.


  //Note: This class does not include fields for unused arable land, annual
  //      crop consumption for each country, annual unhappy people for each
  //      country and other quantities which can be easily calculated
  //      on demand form the included fields.

  //Note: Values below that are, in the countryData.cvs file, defined as type int
  //      should be displayed to the user as type int. However, as some of these
  //      values may be adjusted from year-to-year by continuous functions, it
  //      is useful to maintain their internal representations as doubles.

  //Note: As the start of milestone II, birthRate and migrationRate are assumed constant
  //      for each year of the model and medianAge is not used at all.
  //      However, it might be that before the end of milestone II, we are given simple functions
  //      for these quantities: such as a constant rate of change of birth rate replacing the
  //      constant birth rate or a data lookup table that has predefined values for each year.
  //      The development team should expect and plan for such mid-milestone updates.
  protected int[] population = new int[YEARS_OF_SIM];       //in people
  protected double[] medianAge = new double[YEARS_OF_SIM];  //in years
  protected double[] birthRate = new double[YEARS_OF_SIM];  //number of live births per 1,000 individuals per year.
  protected double[] mortalityRate = new double[YEARS_OF_SIM];   //number of deaths per 1,000 individuals per year.
  protected double[] migrationRate = new double[YEARS_OF_SIM];   //immigration - emigration per 1,000 individuals.
  protected double[] undernourished = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.

  protected double[][] cropProduction = new double[EnumFood.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumFood.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumFood.SIZE][YEARS_OF_SIM]; //in metric tons.
  
  protected double[] landTotal  = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumFood.SIZE][YEARS_OF_SIM];  //in square kilometers
  
  //Note: in milestone II, the model does nothing with the cultivation method.
  protected double[][] cultivationMethod = new double[EnumGrowMethod.SIZE][YEARS_OF_SIM]; //percentage
  
  //In Milestone II, crop yield and per capita need are defined in the first year and assumed constant 
  //    throughout each year of the simulation.
  //    This will NOT be changed before the end of milestone II as it would require redefining the core of
  //    the model's calculations.
  protected double[] cropYield = new double[EnumFood.SIZE]; //metric tons per square kilometer
  protected double[] cropNeedPerCapita = new double[EnumFood.SIZE]; //metric tons per person per year.

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
   * @param year year in question
   * @return median age
   */
  final public double getMedianAge(int year)
  {
    return medianAge[year - START_YEAR];
  }

  /**
   * @param year year in question
   * @return birth rate
   */
  final public double getBirthRate(int year)
  {
    return birthRate[year - START_YEAR];
  }

  final public double getMortalityRate(int year)
  {
    return mortalityRate[year - START_YEAR];
  }


  final public double getMigrationRate(int year)
  {
    return migrationRate[year - START_YEAR];
  }

  /**
   * @param year year in question
   * @return % undernourished
   */
  final public double getUndernourished(int year)
  {
    return undernourished[year - START_YEAR];
  }

  /**
   * @param year year in question
   * @param crop crop in question
   * @return tons produced
   */
  final public double getCropProduction(int year, EnumFood crop)
  {
    return cropProduction[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year year in question
   * @param crop crop in question
   * @return tons exported
   */
  final public double getCropExport(int year, EnumFood crop)
  {
    return cropExport[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year year in question
   * @param crop crop in question
   * @return tons imported
   */
  final public double getCropImport(int year, EnumFood crop)
  {
    return cropImport[crop.ordinal()][year - START_YEAR];
  }

  final public double getLandTotal(int year)
  {
    return landTotal[year - START_YEAR];
  }

  /**
   * @param year year in question
   * @return area of arable land
   */
  final public double getArableLand(int year)
  {
    return landArable[year - START_YEAR];
  }

  /**
   * Returns area available for planting: arable land - sum(area used for each crop)
   *
   * @param year year to check
   * @return arable area unused
   */
  final public double getArableLandUnused(int year)
  {
    double used = 0;
    for (EnumFood crop : EnumFood.values())
    {
      used += getCropLand(year, crop);
    }
    double unused = getArableLand(year) - used;
    return unused;
  }

  /**
   * @param year year in question
   * @param crop crop in question
   * @return square km planted with crop
   */
  final public double getCropLand(int year, EnumFood crop)
  {
    return landCrop[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year   year in question
   * @param method cultivation method
   * @return % land cultivated by method
   */
  final public double getMethodPercentage(int year, EnumGrowMethod method)
  {
    return cultivationMethod[method.ordinal()][year - START_YEAR];
  }

  /**
   * @param year (passing year might be useful in the next milestone?)
   * @param crop
   * @return yield for crop
   */
  final public double getCropYield(int year, EnumFood crop)
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
   * Calculates number of unhappy people in country for a given year based on formula in specifications.
   * For START_YEAR, returns undernourished population to avoid null pointer.
   *
   * @param year year in question
   * @return number of unhappy people for that year
   */
  final public double getUnhappyPeople(int year)
  {
    int currentPop = getPopulation(year);
    double formulaResult;
    if (year == START_YEAR)
    {
      return currentPop * getUndernourished(year);
    }
    else
    {
      double numUndernourish = getUndernourished(year) * currentPop;
      double numDeaths = getMortalityRate(year) / 1000 * currentPop;    // mortality is per 1000, so divide to get %
      double changeUndernourish = numUndernourish - (getPopulation(year - 1) * getUndernourished(year - 1));
      double changeDeaths = numDeaths - (getPopulation(year - 1) * getMortalityRate(year - 1) / 1000);
      formulaResult = 5 * numUndernourish + 2 * changeUndernourish + 10 * numDeaths + 5 * changeDeaths;
    }

    if (formulaResult < 0) formulaResult = 0;
    return Math.min(currentPop, formulaResult);
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
    return this.getCropProduction(year, type) - getTotalCropNeed(year, type);
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
   * @param year year in question
   * @param crop crop in question
   * @return tons available
   */
  final public double getNetCropAvailable(int year, EnumFood crop)
  {
    double available = getCropProduction(year, crop) + getCropImport(year, crop) - getCropExport(year, crop);
    return available;
  }
}
