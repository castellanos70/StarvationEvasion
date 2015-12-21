package starvationevasion.client.MegaMawile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Class receives data from the game client and then populates this
 * class with all the statistics from the regions of the world.  Any
 * two clients playing the same game should have there own copies of
 * this data but the data should be identical, only the server/simulator
 * should be populating this class otherwise the data is read-only.
 * @author
 */
public class StatisticReadData
{
  public enum USREGION{ CALIFORNIA, HEARTLAND, N_PLAINS, SE,
    N_CRESCENT, S_PLAINS_DS, PACIFIC_NW_MS }
  public enum WORLDREGION{ARTIC_AMERICA, MIDDLE_AMERICA, SOUTH_AMERICA, EUROPE,
    MIDDLE_EAST, SUB_SAHARAN_AFRICA, RUSSIA_CAUCUSES, CENTRAL_ASIA, SOUTH_ASIA,
    EAST_ASIA, SOUTHEAST_ASIA, OCEANIA}
  public enum FARMPRODUCT{ CITRUS, NON_CITRUS, NUTS, GRAINS, OIL_CROPS,
    VEGETABLES, SPECIALTY_CROPS, FEED_CROPS, FISH, MEAT_ANIMALS, POULTRY_EGGS,
    DAIRY}
  public enum STAT_TYPE{HDI, POPULATION, AVG_AGE, MALNOURISHED, BIRTH_RATE, REVENUE,
    NET_MIGRATION, MORTALITY_RATE, LIFE_EXPECTANCY, NET_FARM_INCOME, TOTAL_COST,
    TOTAL_KM, TOTAL_PRODUCTION,TOTAL_CONSUMPTION, TOTAL_IMPORT, TOTAL_EXPORT, PER_GMO, PER_ORG,
    PER_CON}

  private int curYear=1981;

  private ArrayList<Number> populationByYear;
  private Map<Integer,Double>seaLevels = new HashMap<>();

  //Used to retrieve data about select region, will use enum instead of String
  private Map<USREGION, RegionData> regionMap;
  private Map<WORLDREGION, RegionData> worldMap;
  private Map<String, Number> cropPrices;
  public static StatisticReadData srd;

  /**
   * Since there is a single instance of this class any class can grab the
   * static version of the class.  The constructor should be called once and
   * will create this single instance for all to use.
   */
  public StatisticReadData()
  {
    curYear =1981;
    populationByYear = new ArrayList<>();
    regionMap = new ConcurrentHashMap<>();
    worldMap = new ConcurrentHashMap<>();
    cropPrices = new ConcurrentHashMap<>();
    for(USREGION region: USREGION.values())
    {
      regionMap.put(region, new RegionData());
    }
    for(WORLDREGION region: WORLDREGION.values())
    {
      worldMap.put(region, new RegionData());
    }
    srd = this;

  }

  public int getCurYear()
  {
    return curYear;
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this
   * case the world population for a particular year is recorded.
   * @param year The year for which the world population is recorded.
   * @param worldPopulation The global population at the time.
   */
  public void populateStats(int year, Number worldPopulation)
  {
    if(year>curYear) curYear=year;
    populationByYear.add(((year - 1981) / 3), worldPopulation);
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this
   * case the cropprice of a given year is recorded.
   * @param year The year for the crop price.
   * @param product The crop for which the price is recorded.
   * @param price The price of the crop that year.
   */
  public void populateStats(int year, FARMPRODUCT product, double price)
  {
    if(year>curYear) curYear=year;
    cropPrices.put(product+","+year, price);
  }

  public void populateStats(starvationevasion.common.WorldData wd)
  {
    int year = wd.year;
    if(year>curYear)
    {
      curYear = year;
    }
    double[] prices = wd.foodPrice;
    populateStats(year, FARMPRODUCT.CITRUS, prices[0]);
    populateStats(year, FARMPRODUCT.NON_CITRUS, prices[1]);
    populateStats(year, FARMPRODUCT.NUTS, prices[2]);
    populateStats(year, FARMPRODUCT.GRAINS, prices[3]);
    populateStats(year, FARMPRODUCT.OIL_CROPS, prices[4]);
    populateStats(year, FARMPRODUCT.VEGETABLES, prices[5]);
    populateStats(year, FARMPRODUCT.SPECIALTY_CROPS, prices[6]);
    populateStats(year, FARMPRODUCT.FEED_CROPS, prices[7]);
    populateStats(year, FARMPRODUCT.FISH, prices[8]);
    populateStats(year, FARMPRODUCT.MEAT_ANIMALS, prices[9]);
    populateStats(year, FARMPRODUCT.POULTRY_EGGS, prices[10]);
    populateStats(year, FARMPRODUCT.DAIRY, prices[11]);
    seaLevels.put(year, wd.seaLevel);
    for(starvationevasion.common.RegionData region: wd.regionData)
    {
      switch (region.region)
      {
        case CALIFORNIA:
          populateHelper(year, USREGION.CALIFORNIA, region);
          break;
        case HEARTLAND:
          populateHelper(year, USREGION.HEARTLAND, region);
          break;
        case NORTHERN_PLAINS:
          populateHelper(year, USREGION.N_PLAINS, region);
          break;
        case SOUTHEAST:
          populateHelper(year, USREGION.SE, region);
          break;
        case NORTHERN_CRESCENT:
          populateHelper(year, USREGION.N_CRESCENT, region);
          break;
        case SOUTHERN_PLAINS:
          populateHelper(year, USREGION.S_PLAINS_DS, region);
          break;
        case MOUNTAIN:
          populateHelper(year, USREGION.PACIFIC_NW_MS, region);
          break;
        case ARCTIC_AMERICA:
          populateHelper(year, WORLDREGION.ARTIC_AMERICA, region);
          break;
        case MIDDLE_AMERICA:
          populateHelper(year, WORLDREGION.MIDDLE_AMERICA, region);
          break;
        case SOUTH_AMERICA:
          populateHelper(year, WORLDREGION.SOUTH_AMERICA, region);
          break;
        case EUROPE:
          populateHelper(year, WORLDREGION.EUROPE, region);
          break;
        case MIDDLE_EAST:
          populateHelper(year, WORLDREGION.MIDDLE_EAST, region);
          break;
        case SUB_SAHARAN:
          populateHelper(year, WORLDREGION.SUB_SAHARAN_AFRICA, region);
          break;
        case RUSSIA:
          populateHelper(year, WORLDREGION.RUSSIA_CAUCUSES, region);
          break;
        case CENTRAL_ASIA:
          populateHelper(year, WORLDREGION.CENTRAL_ASIA, region);
          break;
        case SOUTH_ASIA:
          populateHelper(year, WORLDREGION.SOUTH_ASIA, region);
          break;
        case EAST_ASIA:
          populateHelper(year, WORLDREGION.EAST_ASIA, region);
          break;
        case SOUTHEAST_ASIA:
          populateHelper(year, WORLDREGION.SOUTHEAST_ASIA, region);
          break;
        case OCEANIA:
          populateHelper(year, WORLDREGION.OCEANIA, region);
          break;
      }
    }
  }

  private void populateHelper(int year, USREGION region, starvationevasion.common.RegionData rd)
  {
    populateStats(region, STAT_TYPE.HDI, year, rd.humanDevelopmentIndex);
    populateStats(region, STAT_TYPE.POPULATION, year, rd.population);
    populateStats(region, STAT_TYPE.REVENUE, year, rd.revenueBalance);
    populateStats(region, STAT_TYPE.MALNOURISHED, year, rd.undernourished);
    int i=0;
    for(FARMPRODUCT product: FARMPRODUCT.values())
    {
      i=getCropEnumIndex(product);
      populateStats(region, product, STAT_TYPE.TOTAL_PRODUCTION,year,rd.foodProduced[i]);
      populateStats(region, product, STAT_TYPE.TOTAL_EXPORT,year,rd.foodExported[i]);
      populateStats(region, product, STAT_TYPE.TOTAL_KM,year,rd.farmArea[i]);
    }
  }

  private void populateHelper(int year, WORLDREGION region, starvationevasion.common.RegionData rd)
  {
    populateStats(region, STAT_TYPE.HDI, year, rd.humanDevelopmentIndex);
    populateStats(region, STAT_TYPE.POPULATION, year, rd.population);
    populateStats(region, STAT_TYPE.REVENUE, year, rd.revenueBalance);
    populateStats(region, STAT_TYPE.MALNOURISHED, year, rd.undernourished);
    int i=0;
    for(FARMPRODUCT product: FARMPRODUCT.values())
    {
      i=getCropEnumIndex(product);
      populateStats(region, product, STAT_TYPE.TOTAL_PRODUCTION,year,rd.foodProduced[i]);
      populateStats(region, product, STAT_TYPE.TOTAL_EXPORT,year,rd.foodExported[i]);
      populateStats(region, product, STAT_TYPE.TOTAL_KM, year, rd.farmArea[i]);
    }
  }

  private int getCropEnumIndex(FARMPRODUCT product)
  {
    switch (product)
    {
      case CITRUS:
        return 0;
      case NON_CITRUS:
        return 1;
      case NUTS:
        return 2;
      case GRAINS:
        return 3;
      case OIL_CROPS:
        return 4;
      case VEGETABLES:
        return 5;
      case SPECIALTY_CROPS:
        return 6;
      case FEED_CROPS:
        return 7;
      case FISH:
        return 8;
      case MEAT_ANIMALS:
        return 9;
      case POULTRY_EGGS:
        return 10;
      case DAIRY:
        return 11;
      default:
        return 0;
    }
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
  public void populateStats(USREGION region, STAT_TYPE stat, int year, Number amount)
  {
    if(year>curYear) curYear=year;
    if(!regionMap.containsKey(region))
    {
      regionMap.put(region, new RegionData());
    }
    regionMap.get(region).addData(year, stat, amount);
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this case
   * statistic about the region that year are recorded.
   * @param region WorldRegion that the data is for.
   * @param stat The type of data being passed.
   * @param year The year that this data is for.
   * @param amount The amount for the particular statistic being passed.
   */
  public void populateStats(WORLDREGION region, STAT_TYPE stat, int year, Number amount)
  {
    if(year>curYear) curYear=year;
    if(!worldMap.containsKey(region))
    {
      worldMap.put(region, new RegionData());
    }
    worldMap.get(region).addData(year, stat, amount);
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
                            STAT_TYPE stat, int year, Number amount)
  {
    if(year>curYear) curYear=year;
    if(!regionMap.containsKey(region))
    {
      regionMap.put(region, new RegionData());
    }
    regionMap.get(region).addData(year, product, stat, amount);
  }

  /**
   * Overloaded Method is used by the game client to pass along information
   * from the server which will populate the data for this class. In this case
   * statistic about the regions farm products that year are recorded.
   * @param region WorldRegion that the data is for.
   * @param product The farm product that the data is for.
   * @param stat The type of data being passed.
   * @param year The year that this data is for.
   * @param amount The amount for the particular statistic being passed.
   */
  public void populateStats(WORLDREGION region, FARMPRODUCT product,
                            STAT_TYPE stat, int year, Number amount)
  {
    if(year>curYear) curYear=year;
    if(!worldMap.containsKey(region))
    {
      worldMap.put(region, new RegionData());
    }
    worldMap.get(region).addData(year, product, stat, amount);
  }

  /**
   * Takes a region for which information is requested about and returns the
   * data associated with that particular region.
   * @param region Region of the US which the information is being requested.
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
   * Takes a region for which information is requested about and returns the
   * data associated with that particular region.
   * @param region Region of the world which the information is being requested.
   * @return Data object for that particular region, stored in a list of years.
   */
  public RegionData getRegionData(WORLDREGION region)
  {
    if(worldMap.containsKey(region))
    {
      return worldMap.get(region);
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
  public Number getGlobalPopulation(int year)
  {
    return populationByYear.get((year-1981)/3);
  }

  /**
   * Method returns the list of all world populations from 1981 to present turn
   * @return ArrayList of Numbers representing the population
   */
  public ArrayList<Number> getGlobalPopulationList()
  {
    return populationByYear;
  }

  /**
   * Method used for getting the annual list of prices for a specific crop.
   * @param product FarmProduct being used for the list of prices.
   * @return Arraylist of prices for the farm product.
   */
  public ArrayList<Number> getCropPriceList(FARMPRODUCT product)
  {
    ArrayList<Number> priceList= new ArrayList<>();
    int i = 1981;
    while(cropPrices.containsKey(product+","+i))
    {
      priceList.add(cropPrices.get(product+","+i));
      i++;
    }
    return priceList;
  }

  /**
   * Method takes a year, a crop product and a boolean that flags if production
   * or consumption is being examined.  Then it returns a list of the top five
   * producer or consumers in the world.
   * @param year Integer representing the year.
   * @param product Enum representing the crop/farm product in question.
   * @param statType Enum used to sort top production and consumption
   * @return Returns a list of the top five regions in the specified category.
   */
  public ArrayList<WORLDREGION> getTopWorldCropData(
          int year, FARMPRODUCT product, STAT_TYPE statType)
  {
    ArrayList<WORLDREGION> topCropList= new ArrayList<>();
    WorldCompare sorter = new WorldCompare();
    sorter.setYear(year);
    sorter.setProduct(product);
    sorter.setStatType(statType);
    PriorityQueue<WORLDREGION> regionQueue = new PriorityQueue<>(10, sorter);
    for(WORLDREGION region: WORLDREGION.values())
    {
      if(worldMap.get(region)!=null)
      {
        if (worldMap.get(region).getAnnualData(year) != null)
        {
          if (worldMap.get(region).getAnnualData(year).getCropData(product) != null)
          {
            regionQueue.add(region);
          }
        }
      }
    }
    for(int i=0;i<5;i++){
      topCropList.add(regionQueue.poll());
    }
    return topCropList;
  }

  /**
   * Method takes a year, a crop product and a boolean that flags if production
   * or consumption is being examined.  Then it returns a list of the top five
   * producer or consumers in the US.
   * @param year Integer representing the year.
   * @param product Enum representing the crop/farm product in question.
   * @param statType Enum used to pick different statistic to be compare.
   * @return Returns a list of the top five regions in the specified category.
   */
  public ArrayList<USREGION> getTopUSCropData(
          int year, FARMPRODUCT product, STAT_TYPE statType)
  {
    ArrayList<USREGION> topCropList= new ArrayList<>();
    USRegionCompare sorter = new USRegionCompare();
    sorter.setYear(year);
    sorter.setProduct(product);
    sorter.setStatType(statType);
    PriorityQueue<USREGION> regionQueue = new PriorityQueue<>(10, sorter);
    for(USREGION region: USREGION.values())
    {
      if(regionMap.get(region)!=null)
      {
        if(regionMap.get(region).getAnnualData(year)!=null)
        {
          if(regionMap.get(region).getAnnualData(year).getCropData(product)!=null)
          {
            regionQueue.add(region);
          }
        }
      }
    }
    for(int i=0;i<5;i++){
      topCropList.add(regionQueue.poll());
    }
    return topCropList;
  }

  public ArrayList<WORLDREGION> getTopWorldData(STAT_TYPE statType){
    ArrayList<WORLDREGION> topList= new ArrayList<>();
    WorldCompare sorter = new WorldCompare();
    sorter.setYear(curYear);
    sorter.setStatType(statType);
    PriorityQueue<WORLDREGION> regionQueue = new PriorityQueue<>(10, sorter);
    for(WORLDREGION region: WORLDREGION.values())
    {
      if(worldMap.get(region)!=null)
      {
        if(worldMap.get(region).getAnnualData(curYear)!=null)
        {
          if(worldMap.get(region).getAnnualData(curYear)!=null)
          {
            regionQueue.add(region);
          }
        }
      }
    }
    while(!regionQueue.isEmpty()){
      topList.add(regionQueue.poll());
    }
    return topList;
  }

  public ArrayList<USREGION> getTopUSData(STAT_TYPE statType){
    ArrayList<USREGION> topList= new ArrayList<>();
    USRegionCompare sorter = new USRegionCompare();
    sorter.setYear(curYear);
    sorter.setStatType(statType);
    PriorityQueue<USREGION> regionQueue = new PriorityQueue<>(10, sorter);
    for(USREGION region: USREGION.values())
    {
      if(regionMap.get(region)!=null)
      {
        if(regionMap.get(region).getAnnualData(curYear)!=null)
        {
          if(regionMap.get(region).getAnnualData(curYear)!=null)
          {
            regionQueue.add(region);
          }
        }
      }
    }
    while(!regionQueue.isEmpty()){
      topList.add(regionQueue.poll());
    }
    return topList;
  }

  /**
   * This abstract class is the common elements for the two comparators for
   * USRegion and WorldRegion production/consumption.
   * @author Mark Mitchell, Chris Wu, Evan King, Kiera Haskins, Javier Chavez
   */
  public abstract class RegionCompare
  {
    protected int year;
    protected FARMPRODUCT product;
    protected STAT_TYPE statType;

    /**
     * The method sets the year that production/consumption is being examined
     * for.
     * @param year
     */
    protected void setYear(int year)
    {
      this.year = year;
    }

    /**
     * This sets the product that will be measured for production/consumption.
     * @param product Crop/FarmProduct that is being measured.
     */
    protected void setProduct(FARMPRODUCT product)
    {
      this.product = product;
    }

    /**
     * Method is used to differentiate between stat needed to compare.
     * @param statType is an enum specifying the desired stat
     */
    protected void setStatType(STAT_TYPE statType)
    {
      this.statType = statType;
    }
  }

  /**
   * This class is the comparator for which world region produces/consumes more.
   * @author Mark Mitchell, Chris Wu, Evan King, Kiera Haskins, Javier Chavez
   */
  public class WorldCompare extends RegionCompare implements Comparator<WORLDREGION>
  {
    @Override
    public int compare(WORLDREGION one, WORLDREGION two)
    {
      int stat1=0,stat2=0;
      switch (statType)
      {
        case HDI:
          stat1 = (int)(worldMap.get(one).getAnnualData(year).getHumanDevelopmentIndex()*100);
          stat2 = (int)(worldMap.get(two).getAnnualData(year).getHumanDevelopmentIndex()*100);
          return stat2 - stat1;
        case POPULATION:
          stat1 = worldMap.get(one).getAnnualData(year).getPopulation();
          stat2 = worldMap.get(two).getAnnualData(year).getPopulation();
          return stat2 - stat1;
        case AVG_AGE:
          stat1 = worldMap.get(one).getAnnualData(year).getAverageAge();
          stat2 = worldMap.get(two).getAnnualData(year).getAverageAge();
          return stat2 - stat1;
        case MALNOURISHED:
          stat1 = (int)(worldMap.get(one).getAnnualData(year).getMalnourishedPPT()*100);
          stat2 = (int)(worldMap.get(two).getAnnualData(year).getMalnourishedPPT()*100);
          return stat2 - stat1;
        case BIRTH_RATE:
          stat1 = worldMap.get(one).getAnnualData(year).getBirthRatePPT();
          stat2 = worldMap.get(two).getAnnualData(year).getBirthRatePPT();
          return stat2 - stat1;
        case REVENUE:
          stat1 = worldMap.get(one).getAnnualData(year).getRevenue();
          stat2 = worldMap.get(two).getAnnualData(year).getRevenue();
          return stat2 - stat1;
        case NET_MIGRATION:
          stat1 = worldMap.get(one).getAnnualData(year).getNetMigrationPPT();
          stat2 = worldMap.get(two).getAnnualData(year).getNetMigrationPPT();
          return stat2 - stat1;
        case MORTALITY_RATE:
          stat1 = worldMap.get(one).getAnnualData(year).getMortalityRatePPT();
          stat2 = worldMap.get(two).getAnnualData(year).getMortalityRatePPT();
          return stat2 - stat1;
        case LIFE_EXPECTANCY:
          stat1 = worldMap.get(one).getAnnualData(year).getLifeExpectancy();
          stat2 = worldMap.get(two).getAnnualData(year).getLifeExpectancy();
          return stat2 - stat1;
        case NET_FARM_INCOME:
          stat1 = worldMap.get(one).getAnnualData(year).
                  getCropData(product).getNetFarmIncome();
          stat2 = worldMap.get(two).getAnnualData(year).
                  getCropData(product).getNetFarmIncome();
          return stat2 - stat1;
        case TOTAL_COST:
          stat1 = worldMap.get(one).getAnnualData(year).
                  getCropData(product).getTotalCost();
          stat2 = worldMap.get(two).getAnnualData(year).
                  getCropData(product).getTotalCost();
          return stat2 - stat1;
        case TOTAL_KM:
          stat1 = worldMap.get(one).getAnnualData(year).
                    getCropData(product).getSquareKilometers();
          stat2 = worldMap.get(two).getAnnualData(year).
                    getCropData(product).getSquareKilometers();
          return stat2 - stat1;
        case TOTAL_PRODUCTION:
          stat1 = worldMap.get(one).getAnnualData(year).
                    getCropData(product).getProductionTons();
          stat2 = worldMap.get(two).getAnnualData(year).
                    getCropData(product).getProductionTons();
          return stat2 - stat1;
        case TOTAL_CONSUMPTION:
          stat1 = worldMap.get(one).getAnnualData(year).
                  getCropData(product).getConsumptionTons();
          stat2 = worldMap.get(two).getAnnualData(year).
                  getCropData(product).getConsumptionTons();
          return stat2 - stat1;
        case TOTAL_IMPORT:
          stat1 = worldMap.get(one).getAnnualData(year).
                  getCropData(product).getImportTons();
          stat2 = worldMap.get(two).getAnnualData(year).
                  getCropData(product).getImportTons();
          return stat2 - stat1;
        case TOTAL_EXPORT:
          stat1 = worldMap.get(one).getAnnualData(year).
                  getCropData(product).getExportTons();
          stat2 = worldMap.get(two).getAnnualData(year).
                  getCropData(product).getExportTons();
          return stat2 - stat1;
        case PER_GMO:
          stat1 = worldMap.get(one).getAnnualData(year).getPercentGMO();
          stat2 = worldMap.get(two).getAnnualData(year).getPercentGMO();
          return stat2 - stat1;
        case PER_ORG:
          stat1 = worldMap.get(one).getAnnualData(year).getPercentOrganic();
          stat2 = worldMap.get(two).getAnnualData(year).getPercentOrganic();
          return stat2 - stat1;
        case PER_CON:
          stat1 = worldMap.get(one).getAnnualData(year).getPercentConventional();
          stat2 = worldMap.get(two).getAnnualData(year).getPercentConventional();
          return stat2 - stat1;
        default:
          return 0;
      }
    }
  }

  /**
   * This class is the comparator for which US region produces/consumes more.
   * @author Mark Mitchell, Chris Wu, Evan King, Kiera Haskins, Javier Chavez
   */
  public class USRegionCompare extends RegionCompare implements Comparator<USREGION>
  {
    @Override
    public int compare(USREGION one, USREGION two)
    {
      int stat1=0,stat2=0;
      switch (statType)
      {
        case HDI:
          stat1 = (int)(regionMap.get(one).getAnnualData(year).getHumanDevelopmentIndex()*100);
          stat2 = (int)(regionMap.get(two).getAnnualData(year).getHumanDevelopmentIndex()*100);
          return stat2 - stat1;
        case POPULATION:
          stat1 = regionMap.get(one).getAnnualData(year).getPopulation();
          stat2 = regionMap.get(two).getAnnualData(year).getPopulation();
          return stat2 - stat1;
        case AVG_AGE:
          stat1 = regionMap.get(one).getAnnualData(year).getAverageAge();
          stat2 = regionMap.get(two).getAnnualData(year).getAverageAge();
          return stat2 - stat1;
        case MALNOURISHED:
          stat1 = (int)(regionMap.get(one).getAnnualData(year).getMalnourishedPPT()*100);
          stat2 = (int)(regionMap.get(two).getAnnualData(year).getMalnourishedPPT()*100);
          return stat2 - stat1;
        case BIRTH_RATE:
          stat1 = regionMap.get(one).getAnnualData(year).getBirthRatePPT();
          stat2 = regionMap.get(two).getAnnualData(year).getBirthRatePPT();
          return stat2 - stat1;
        case REVENUE:
          stat1 = regionMap.get(one).getAnnualData(year).getRevenue();
          stat2 = regionMap.get(two).getAnnualData(year).getRevenue();
          return stat2 - stat1;
        case NET_MIGRATION:
          stat1 = regionMap.get(one).getAnnualData(year).getNetMigrationPPT();
          stat2 = regionMap.get(two).getAnnualData(year).getNetMigrationPPT();
          return stat2 - stat1;
        case MORTALITY_RATE:
          stat1 = regionMap.get(one).getAnnualData(year).getMortalityRatePPT();
          stat2 = regionMap.get(two).getAnnualData(year).getMortalityRatePPT();
          return stat2 - stat1;
        case LIFE_EXPECTANCY:
          stat1 = regionMap.get(one).getAnnualData(year).getLifeExpectancy();
          stat2 = regionMap.get(two).getAnnualData(year).getLifeExpectancy();
          return stat2 - stat1;
        case NET_FARM_INCOME:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getNetFarmIncome();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getNetFarmIncome();
          return stat2 - stat1;
        case TOTAL_COST:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getTotalCost();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getTotalCost();
          return stat2 - stat1;
        case TOTAL_KM:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getSquareKilometers();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getSquareKilometers();
          return stat2 - stat1;
        case TOTAL_PRODUCTION:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getProductionTons();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getProductionTons();
          return stat2 - stat1;
        case TOTAL_CONSUMPTION:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getConsumptionTons();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getConsumptionTons();
          return stat2 - stat1;
        case TOTAL_IMPORT:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getImportTons();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getImportTons();
          return stat2 - stat1;
        case TOTAL_EXPORT:
          stat1 = regionMap.get(one).getAnnualData(year).
                  getCropData(product).getExportTons();
          stat2 = regionMap.get(two).getAnnualData(year).
                  getCropData(product).getExportTons();
          return stat2 - stat1;
        case PER_GMO:
          stat1 = regionMap.get(one).getAnnualData(year).getPercentGMO();
          stat2 = regionMap.get(two).getAnnualData(year).getPercentGMO();
          return stat2 - stat1;
        case PER_ORG:
          stat1 = regionMap.get(one).getAnnualData(year).getPercentOrganic();
          stat2 = regionMap.get(two).getAnnualData(year).getPercentOrganic();
          return stat2 - stat1;
        case PER_CON:
          stat1 = regionMap.get(one).getAnnualData(year).getPercentConventional();
          stat2 = regionMap.get(two).getAnnualData(year).getPercentConventional();
          return stat2 - stat1;
        default:
          return 0;
      }
    }
  }

  /**
   * Nested class that holds all the data for a particular region, this is held
   * in a list that will represent the years 1981-2050.
   * @author Mark Mitchell, Chris Wu, Evan King, Kiera Haskins, Javier Chavez
   */
  public class RegionData
  {
    private Map<Integer, AnnualData> annualDataSet = new ConcurrentHashMap<>();

    /**
     * This method sends the data to the appropriate year that it is for,
     * the data will then be recorded for a specific kind of statistic.
     * @param year Integer representing the year of the statistic.
     * @param type STAT_TYPE enum for the type of statistic that this is.
     * @param amount Integer representing the quantity for this statistic.
     */
    public void addData(int year, STAT_TYPE type, Number amount)
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
    public void addData(int year,FARMPRODUCT product, STAT_TYPE type, Number amount)
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

    /**
     * Method returns a list of annual data for a particular statistic
     * @param type STAT_TYPE enum specifying the statistic in question
     * @return ArrayList of Numbers for the statistic to be examined
     */
    public ArrayList<Number> getStatList(STAT_TYPE type)
    {
      ArrayList<Number> statList = new ArrayList<>();
      int year = 1981;
      AnnualData dataSheet;
      while(annualDataSet.containsKey(year))
      {
        dataSheet = annualDataSet.get(year);
        switch (type)
        {
          case HDI:
            statList.add((year-1981)/3, dataSheet.getHumanDevelopmentIndex());
            break;
          case POPULATION:
            statList.add((year-1981)/3, dataSheet.getPopulation());
            break;
          case REVENUE:
            statList.add((year-1981)/3, dataSheet.getRevenue());
            break;
          case AVG_AGE:
            statList.add((year-1981)/3, dataSheet.getAverageAge());
            break;
          case MALNOURISHED:
            statList.add((year-1981)/3, dataSheet.getMalnourishedPPT());
            break;
          case BIRTH_RATE:
            statList.add((year-1981)/3, dataSheet.getBirthRatePPT());
            break;
          case NET_MIGRATION:
            statList.add((year-1981)/3, dataSheet.getNetMigrationPPT());
            break;
          case MORTALITY_RATE:
            statList.add((year-1981)/3, dataSheet.getMortalityRatePPT());
            break;
          case LIFE_EXPECTANCY:
            statList.add((year-1981)/3, dataSheet.getLifeExpectancy());
            break;
          case PER_GMO:
            statList.add((year-1981)/3, dataSheet.getPercentGMO());
            break;
          case PER_ORG:
            statList.add((year-1981)/3, dataSheet.getPercentOrganic());
            break;
          case PER_CON:
            statList.add((year-1981)/3, dataSheet.getPercentConventional());
            break;
          default:
            System.out.println("Stat type requires crop selection.");
            return null;
        }
        year +=3;
      }
      return statList;
    }

    /**
     * Method returns a list of annual data for a particular statistic for a
     * specified farm product
     * @param type STAT_TYPE enum specifying the statistic in question
     * @param product FARMPRODUCT enum for the crop the statistics are for
     * @return ArrayList of Numbers for the statistic to be examined
     */
    public ArrayList<Number> getStatList(STAT_TYPE type, FARMPRODUCT product)
    {
      ArrayList<Number> statList = new ArrayList<>();
      int year = 1981;
      Number data;
      while(annualDataSet.containsKey(year))
      {
        switch (type)
        {
          case NET_FARM_INCOME:
            data = annualDataSet.get(year).getCropData(product).getNetFarmIncome();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_COST:
            data = annualDataSet.get(year).getCropData(product).getTotalCost();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_KM:
            data = annualDataSet.get(year).getCropData(product).getSquareKilometers();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_PRODUCTION:
            data = annualDataSet.get(year).getCropData(product).getProductionTons();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_CONSUMPTION:
            data = annualDataSet.get(year).getCropData(product).getConsumptionTons();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_IMPORT:
            data = annualDataSet.get(year).getCropData(product).getImportTons();
            statList.add((year-1981)/3, data);
            break;
          case TOTAL_EXPORT:
            data = annualDataSet.get(year).getCropData(product).getExportTons();
            statList.add((year-1981)/3, data);
            break;
          default:
            System.out.println("Stat type not supported with crop selection.");
            return null;
        }
        year +=3;
      }
      return statList;
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
    private double humanDevelopmentIndex;
    private int population;
    private int revenue;
    private int averageAge;
    private double malnourishedPPT;
    private int birthRatePPT;
    private int netMigrationPPT;
    private int mortalityRatePPT;
    private int lifeExpectancy;
    private int percentGMO;
    private int percentOrganic;
    private int percentConventional;
    /*CropData contains net farm income, total cost, total sq km,
    production in metric tons, export in metric tons...
     */
    private Map<FARMPRODUCT, CropData> cropMap = new ConcurrentHashMap<>();

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
     * @param amount Number representing the quantity for a particular statistic.
     */
    public void addData(FARMPRODUCT product, STAT_TYPE type, Number amount)
    {
      if(!cropMap.containsKey(product)){
        cropMap.put(product, new CropData());
      }
      cropMap.get(product).addData(type, amount);
    }

    public int getAnyStat(STAT_TYPE type)
    {
      switch (type){
        case POPULATION:
          return getPopulation();
        case HDI:
          return (int)(getHumanDevelopmentIndex()*100);
        case MALNOURISHED:
          return (int)(getMalnourishedPPT()*100);
        case REVENUE:
          return getRevenue();
        default:
          return 0;
      }
    }

    /**
     * Method takes statistics for this year and records them.
     * @param type STAT_TYPE enum for the kind of statistic that it is.
     * @param amount Integer representing the quantity for a particular statistic.
     */
    public void addData(STAT_TYPE type, Number amount)
    {
      switch (type)
      {
        case HDI:
          humanDevelopmentIndex = (double)amount;
          break;
        case POPULATION:
          population = (int)amount;
          break;
        case REVENUE:
          revenue = (int)amount;
          break;
        case AVG_AGE:
          averageAge = (int)amount;
          break;
        case MALNOURISHED:
          malnourishedPPT = (double)amount;
          break;
        case BIRTH_RATE:
          birthRatePPT = (int)amount;
          break;
        case NET_MIGRATION:
          netMigrationPPT = (int)amount;
          break;
        case MORTALITY_RATE:
          mortalityRatePPT = (int)amount;
          break;
        case LIFE_EXPECTANCY:
          lifeExpectancy = (int)amount;
          break;
        case PER_GMO:
          percentGMO = (int)(amount);
          break;
        case PER_ORG:
          percentOrganic = (int)(amount);
          break;
        case PER_CON:
          percentConventional = (int)(amount);
          break;
        default:
          System.out.println(type+" not supported by AnnualData");
      }
    }

    /**
     * Getter method that returns CropData for a specified product
     * @param product FARMPRODUCT enum for the crop in question
     * @return CropData object for specified product
     */
    public CropData getCropData(FARMPRODUCT product)
    {
      return cropMap.get(product);
    }

    /**
     * Returns the total population for the region at that time.
     * @return Integer representing the total population.
     */
    public int getPopulation()
    {
      return population;
    }


    public int getRevenue()
    {
      return revenue;
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
     * @return Double representing the people per thousand that are malnourished.
     */
    public double getMalnourishedPPT()
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
     * @return Integer representing the percent of land used for GMO crops.
     */
    public int getPercentGMO()
    {
      return percentGMO;
    }

    /**
     * Getter for the percent of total land used for Organic crops.
     * @return integer representing the percent of land used for Organic crops.
     */
    public int getPercentOrganic()
    {
      return percentOrganic;
    }

    /**
     * Getter for the percent of total land used for conventional crops.
     * @return integer representing the percent of land used for conventional crops.
     */
    public int getPercentConventional()
    {
      return percentConventional;
    }

    /**
     * Getter returns the Human development index which is based off a UN rating
     * system which is done in the simulator.  This will tell how well the any
     * region is doing in the game.
     * @return Double representing the HDI or Human Development Index.
     */
    public double getHumanDevelopmentIndex()
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
      private int consumptionTons;
      private int importTons;
      private int exportTons;

      /**
       * Method takes statistics for this particular crop and records them.
       * @param type STAT_TYPE enum for the kind of statistic that it is.
       * @param amount Integer representing the quantity for a particular statistic.
       */
      public void addData(STAT_TYPE type, Number amount)
      {
        switch (type)
        {
          case NET_FARM_INCOME:
            netFarmIncome = (int)amount;
            break;
          case TOTAL_COST:
            totalCost = (int)amount;
            break;
          case TOTAL_KM:
            squareKilometers = (int)amount;
            break;
          case TOTAL_PRODUCTION:
            productionTons = (int)amount;
            break;
          case TOTAL_CONSUMPTION:
            consumptionTons = (int)amount;
            break;
          case TOTAL_IMPORT:
            importTons = (int)amount;
            break;
          case TOTAL_EXPORT:
            exportTons = (int)amount;
            break;
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
       * Getter method returns the number of metric tons consumed at the time of
       * a particular crop.
       * @return Integer representing metric tons of the crop consumed.
       */
      public int getConsumptionTons()
      {
        //return consumptionTons;
        return productionTons-exportTons;
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
