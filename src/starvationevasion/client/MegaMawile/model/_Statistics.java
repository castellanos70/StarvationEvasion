package starvationevasion.client.MegaMawile.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * TODO Refactor? so that when we see stats come thro the stream we call this
 *
 *
 * this is kind of a combination of both stats model and a stats-serializer
 *
 * I noticed this is a singleton pattern... can prevent that and
 * race conditions by converting this into a
 * Statistics model and having all our clients update it with
 * synchronized methods.
 *
 * we can have a statistics serializer tho if we want that only
 * handles the processing of string to stats? we dont need to tho.
 *
 *
 */



/**
 * Class receives data from the game client and then populates this
 * class with all the statistics from the regions of the world.  Any
 * two clients playing the same game should have there own copies of
 * this data but the data should be identical, only the server/simulator
 * should be populating this class otherwise the data is read-only.
 * @author
 */
public class _Statistics
{
  public enum USREGION{ CALIFORNIA, HEARTLAND, N_PLAINS, SE,
    N_CRESCENT, S_PLAINS_DS, PACIFIC_NW_MS }
  public enum FARMPRODUCT{ CITRUS, NON_CITRUS, NUTS, GRAINS, OIL_CROPS,
    VEGETABLES, SPECIALTY_CROPS, FEED_CROPS, FISH, MEAT_ANIMALS, POULTRY_EGGS,
    DAIRY}
  public enum STAT_TYPE{HDI, POPULATION, AVG_AGE, MALNOURISHED, BIRTH_RATE,
    NET_MIGRATION, MORTALITY_RATE, LIFE_EXPECTANCY, NET_FARM_INCOME, TOTAL_COST,
    TOTAL_KM, TOTAL_PRODUCTION, TOTAL_IMPORT, TOTAL_EXPORT, PER_GMO, PER_ORG,
    PER_CON}

  private ArrayList<Integer> populationByYear;

  //Used to retrieve data about select region, will use enum instead of String
  private Map<USREGION, RegionData> regionMap;
  public static _Statistics grd;

  /**
   * Since there is a single instance of this class any class can grab the
   * static version of the class.  The constructor should be called once and
   * will create this single instance for all to use.
   */
  public _Statistics()
  {
    grd = this;
    populationByYear = new ArrayList<>();
    regionMap = new HashMap<USREGION, RegionData>();
    for(USREGION region: USREGION.values())
    {
      regionMap.put(region, new RegionData());
    }
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this
   * case the world population for a particular year is recorded.
   * @param year The year for which the world population is recorded.
   * @param worldPopulation The global population at the time.
   */
  public void populateStats(int year, int worldPopulation)
  {
    populationByYear.add((year-1980/3),worldPopulation);
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this case
   * statistic about the region that year are recorded.
   * @param region USRegion that the data is for.
   * @param stat The type of data being passed.
   * @param year The year that this data is for.
   * @param amount The amount for the particular statistic being passed.
   */
  public void populateStats(USREGION region, STAT_TYPE stat, int year, int amount)
  {
    if(!regionMap.containsKey(region))
    {
      regionMap.put(region, new RegionData());
    }
    regionMap.get(region).addData(year, stat, amount);
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this case
   * statistic about the regions farm products that year are recorded.
   * @param region USRegion that the data is for.
   * @param product The farm product that the data is for.
   * @param stat The type of data being passed.
   * @param year The year that this data is for.
   * @param amount The amount for the particular statistic being passed.
   */
  public void populateStats(USREGION region, FARMPRODUCT product,
                            STAT_TYPE stat, int year, int amount)
  {
    if(!regionMap.containsKey(region))
    {
      regionMap.put(region, new RegionData());
    }
    regionMap.get(region).addData(year, product, stat, amount);
  }

  /**
   * Takes a region for which information is requested about and returns the
   * data associated with that particular region.
   * @param region Region of the US or world which the information is being requested.
   * @return Data object for that particular region, stored in a list of years.
   */
  public RegionData getRegionData(USREGION region)
  {
    if(regionMap.containsKey(region))
    {
      return regionMap.get(region);
    }
    else
    {
      return null;
    }
  }

  /**
   * Method is called to retrieve the current global population at the current year.
   * @return Integer representing the total world population.
   */
  public int getGlobalPopulation(int year)
  {
    return populationByYear.get((year-1980)/3);
  }

  /**
   * Nested class that holds all the data for a particular region, this is held
   * in a list that will represent the years 1980-2050.
   * @author
   */
  private class RegionData
  {
    private Map<Integer, AnnualData> annualDataSet = new HashMap<>();

    /**
     * This method sends the data to the appropriate year that it is for,
     * the data will then be recorded for a specific kind of statistic.
     * @param year Integer representing the year of the statistic.
     * @param type STAT_TYPE enum for the type of statistic that this is.
     * @param amount Integer representing the quantity for this statistic.
     */
    public void addData(int year, STAT_TYPE type, int amount)
    {
      if(!annualDataSet.containsKey(year)){
        annualDataSet.put(year, new AnnualData());
      }
      annualDataSet.get(year).addData(type, amount);
    }

    /**
     * This method sends the data to the appropriate year and product that it
     * is for, the data will then be recorded for a specific kind of statistic.
     * @param year Integer representing the year of the statistic.
     * @param product FARM_PRODUCT enum for the type of crop the stat is for.
     * @param type STAT_TYPE enumator for the type of statistic that this is.
     * @param amount Integer representing the quantity for this statistic.
     */
    public void addData(int year,FARMPRODUCT product, STAT_TYPE type, int amount)
    {
      if(!annualDataSet.containsKey(year)){
        annualDataSet.put(year, new AnnualData());
      }
      annualDataSet.get(year).addData(product, type, amount);
    }

    /**
     * Getter method retrieves the years data for a particular year.
     * @param year Integer representing the year that is being requested.
     * @return AnnualData object for that particular year.
     */
    public AnnualData getAnnualData(int year)
    {
      if(annualDataSet.containsKey(year))
      {
        return annualDataSet.get(year);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Nested class that stores regional information for a particular year.  This
   * is held by the RegionalData class so that it can organize all the information
   * for the region according to year.
   * @author
   */
  public class AnnualData
  {
    private int humanDevelopmentIndex;
    private int population;
    private int averageAge;
    private int malnourishedPPT;
    private int birthRatePPT;
    private int netMigrationPPT;
    private int mortalityRatePPT;
    private int lifeExpectancy;
    private float percentGMO;
    private float percentOrganic;
    private float percentConventional;
    /*CropData contains net farm income, total cost, total sq km,
    production in metric tons, export in metric tons...
     */
    private Map<FARMPRODUCT, CropData> cropMap = new HashMap<>();

    /**
     * Constructor method populates the hashmap with all 12 farm products.
     */
    public AnnualData()
    {
      for(FARMPRODUCT farmproduct: FARMPRODUCT.values())
      {
        cropMap.put(farmproduct, new CropData());
      }
    }

    /**
     * Method takes statistics for a specific farm products and records them in
     * their specific data object.
     * @param product FARMPRODUCT enum which is the key for a specific data object.
     * @param type STAT_TYPE enum for the kind of statistic that it is.
     * @param amount Integer representing the quantity for a particular statistic.
     */
    public void addData(FARMPRODUCT product, STAT_TYPE type, int amount)
    {
      if(!cropMap.containsKey(product)){
        cropMap.put(product, new CropData());
      }
      cropMap.get(product).addData(type, amount);
    }

    /**
     * Method takes statistics for this year and records them.
     * @param type STAT_TYPE enum for the kind of statistic that it is.
     * @param amount Integer representing the quantity for a particular statistic.
     */
    public void addData(STAT_TYPE type, int amount)
    {
      switch (type)
      {
        case HDI:
          humanDevelopmentIndex = amount;
        case POPULATION:
          population = amount;
        case AVG_AGE:
          averageAge = amount;
        case MALNOURISHED:
          malnourishedPPT = amount;
        case BIRTH_RATE:
          birthRatePPT = amount;
        case NET_MIGRATION:
          netMigrationPPT = amount;
        case MORTALITY_RATE:
          mortalityRatePPT = amount;
        case LIFE_EXPECTANCY:
          lifeExpectancy = amount;
        case PER_GMO:
          percentGMO = amount;
        case PER_ORG:
          percentOrganic = amount;
        case PER_CON:
          percentConventional = amount;
        default:
          System.out.println(type+" not supported by AnnualData");
      }
    }

    /**
     * Returns the total population for the region at that time.
     * @return Integer representing the total population.
     */
    public int getPopulation()
    {
      return population;
    }

    /**
     * Getter method is called to retrieve the regions average age.
     * @return Integer representing the average age in regions population.
     */
    public int getAverageAge()
    {
      return averageAge;
    }

    /**
     * Getter for the number of people per thousand that are malnourished in
     * a particular region at this time.
     * @return Integer representing the people per thousand that are malnourished.
     */
    public int getMalnourishedPPT()
    {
      return malnourishedPPT;
    }

    /**
     * Getter method that returns the birth rate per thousand people.
     * @return Integer representing the birth rate.
     */
    public int getBirthRatePPT()
    {
      return birthRatePPT;
    }

    /**
     * Getter for the net migration (immigration- emigration) per thousand people.
     * @return Integer representing the net migration.
     */
    public int getNetMigrationPPT()
    {
      return netMigrationPPT;
    }

    /**
     * Getter for the rate of deaths per one thousand people.
     * @return Integer representing the mortality rate.
     */
    public int getMortalityRatePPT()
    {
      return mortalityRatePPT;
    }

    /**
     * Getter method for the life expectancy.
     * @return Integer representing the life expectancy in years.
     */
    public int getLifeExpectancy()
    {
      return lifeExpectancy;
    }

    /**
     * Getter for the percent of total land used for GMO crops.
     * @return Float representing the percent of land used for GMO crops.
     */
    public float getPercentGMO()
    {
      return percentGMO;
    }

    /**
     * Getter for the percent of total land used for Organic crops.
     * @return Float representing the percent of land used for Organic crops.
     */
    public float getPercentOrganic()
    {
      return percentOrganic;
    }

    /**
     * Getter for the percent of total land used for conventional crops.
     * @return Float representing the percent of land used for conventional crops.
     */
    public float getPercentConventional()
    {
      return percentConventional;
    }

    /**
     * Getter returns the Human development index which is based off a UN rating
     * system which is done in the simulator.  This will tell how well the any
     * region is doing in the game.
     * @return Integer representing the HDI or Human Development Index.
     */
    public int getHumanDevelopmentIndex()
    {
      return humanDevelopmentIndex;
    }

    /**
     * Nested class used to store information about a specific type of crop.
     * @author
     */
    public class CropData
    {
      private int netFarmIncome;
      private int totalCost;
      private int squareKilometers;
      private int productionTons;
      private int importTons;
      private int exportTons;

      /**
       * Method takes statistics for this particular crop and records them.
       * @param type STAT_TYPE enum for the kind of statistic that it is.
       * @param amount Integer representing the quantity for a particular statistic.
       */
      public void addData(STAT_TYPE type, int amount)
      {
        switch (type)
        {
          case NET_FARM_INCOME:
            netFarmIncome = amount;
          case TOTAL_COST:
            totalCost = amount;
          case TOTAL_KM:
            squareKilometers = amount;
          case TOTAL_PRODUCTION:
            productionTons = amount;
          case TOTAL_IMPORT:
            importTons = amount;
          case TOTAL_EXPORT:
            exportTons = amount;
          default:
            System.out.println(type + " not supported by CropData.");
        }
      }

      /**
       * Getter returns the revenue - the cost of a particular crop this turn.
       * @return Integer representing the net income from that crop in millions
       * of dollars.
       */
      public int getNetFarmIncome()
      {
        return netFarmIncome;
      }

      /**
       * Getter returns the cost of a particular crop this turn.
       * @return Returns the cost of producing that crop in millions of dollars.
       */
      public int getTotalCost()
      {
        return totalCost;
      }

      /**
       * Getter method returns the number of square kilometers used to grow this
       * particular crop at the time.
       * @return Integer representing the square kilometers used for this crop.
       */
      public int getSquareKilometers()
      {
        return squareKilometers;
      }

      /**
       * Getter method returns the number of metric tons produced at the time of
       * a particular crop.
       * @return Integer representing metric tons of the crop produced.
       */
      public int getProductionTons()
      {
        return productionTons;
      }

      /**
       * Getter method for the metric tons of this crop that are imported.
       * @return Integer representing the metric tons of imported crops of this type.
       */
      public int getImportTons()
      {
        return importTons;
      }

      /**
       * Getter method for the metric tons of this crop that are exported.
       * @return Integer representing the metric tons of exported crops of this type.
       */
      public int getExportTons()
      {
        return exportTons;
      }
    }
  }
}

