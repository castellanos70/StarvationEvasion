package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.sim.io.CSVReader;
import starvationevasion.sim.util.MapConverter;
import starvationevasion.common.MapPoint;

import java.awt.*;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Territory is the former Country class, and extends AbstractAgriculturalUnit.
 * This class is an abstraction subclassed by the USState and Country classes, but is
 * also directly instantiated by the CSV parser as a data structure used to store
 * temporary data.
 * Created by winston on 3/9/15.
 * Edited by Jessica on 3/14/15: getName, setLandTotal, methodPercentage methods
 * Edited by Peter November 2015 to make the class more abstract.
 *
 * TERRITORY_DATA_PATH defines the territory names and spellings used in the game.
 *
 * @version 22-Mar-2015
 */
public class Territory
{
  private static final String TERRITORY_DATA_PATH = "/sim/TerritoryPopulationAndLandUse.csv";

  protected String name;

  protected static final int YEARS_OF_SIM = 1+Constant.LAST_YEAR - Constant.FIRST_YEAR;

  protected GeographicArea border;

  //AbstractTerritory is extended by Territory and by Region.
  //A Territory is the base political unit in the simulator.
  //   Each modeled country and each US state is a Territory.
  //A Region is the base political unit exposed to the player.
  //   A region a set of territories. Each territory is assigned to one region.

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
  protected int undernourished;  // number of undernourished x 1,000 per year.

  // The Fall 2015 spec on pg19 defines Human Development Index (HDI) as a function
  // of undernourishment in two categories; Protien Energy and Micronutrient.
  //
  protected float humanDevelopmentIndex;

  /**
   * The territory's crop income is the gross farm income less
   * farm expenses. <br><br>
   *
   * The Total income includes farm income from food consumed within the
   * territory as well as food exported. Food imported is not a farm expense.
   */
  protected long[] cropIncome = new long[EnumFood.SIZE]; // in $1000s
  protected long[] cropProduction = new long[EnumFood.SIZE]; //in metric tons.

  protected int landTotal;  //in square kilometers

  /**
   * Agricultural land refers to the share of land area that is arable,
   * under permanent crops, and under permanent pastures. Arable land includes
   * land defined by the FAO as land under temporary crops (double-cropped
   * areas are counted once), temporary meadows for mowing or for pasture, land
   * under market or kitchen gardens, and land temporarily fallow. land abandoned
   * as a result of shifting cultivation is excluded. land under permanent crops
   * is land cultivated with crops that occupy the land for long periods and need
   * not be replanted after each harvest, such as cocoa, coffee, and rubber.
   * This category includes land under flowering shrubs, fruit trees, nut trees,
   * and vines, but excludes land under trees grown for wood or timber. Permanent
   * pasture is land used for five or more years for forage, including natural and
   * cultivated crops.
   */

  protected int farmLand1981, farmLand2014, totalFarmLand;
  protected int[] landCrop = new int[EnumFood.SIZE];  // in square kilometers
  protected long[] cropImport = new long[EnumFood.SIZE];  //in metric tons.
  protected long[] cropExport = new long[EnumFood.SIZE];  //in metric tons.

  protected int[] cultivationMethod = new int[EnumFarmMethod.SIZE]; //percentage [0,100]

  //In Milestone II, crop yield and per capita need are defined in the first year and assumed constant
  //    throughout each year of the simulation.
  //    This will NOT be changed before the end of milestone II as it would require redefining the core of
  //    the model's calculations.
  protected double[] cropYield = new double[EnumFood.SIZE]; //metric tons per square kilometer
  protected double[] cropNeedPerCapita = new double[EnumFood.SIZE]; //metric tons per person per year.

  protected double penaltyValue = -1;

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






  private enum EnumHeader
  { territory,	region,	population1981,	population1990,	population2000,
    population2010,	population2014,	population2025,	population2050,
    averageAge,	undernourished,	births,	migration,	mortality,
    landArea,	farmLand1981,	farmLand2014,	organic,	gmo;

    public static final int SIZE = values().length;
  };


  final public static MapConverter converter = new MapConverter();

  private final Area totalArea = new Area();
  private final ArrayList<GeographicArea> geographicAreaList = new ArrayList<>();
  private Collection<LandTile> landTiles  = new ArrayList<>();;
  private MapPoint capitolLocation;

  /* The region to which this country belongs.
   */
  private EnumRegion region;

  private double[] land1981 = new double[EnumFood.SIZE];
  private double[] yield1981 = new double[EnumFood.SIZE];
  private long[] cropBudget = new long[EnumFood.SIZE];

  /**
   * Territory constructor
   *
   * @param name country name
   */
  public Territory(String name)
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
    return population[year - Constant.FIRST_YEAR];
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
   * @return % human development index at end of the current year of the simulation.
   */
  final public float getHumanDevelopmentIndex()
  {
    return humanDevelopmentIndex;
  }

  final public double getPenaltyValue()
  {
    return penaltyValue;
  }

  final public void setPenaltyValue(double penaltyValue)
  {
    this.penaltyValue = penaltyValue;
  }

  /**
   * Populate medianAge array with given age; assumes median age remains constant.
   *
   */
  final public void setMedianAge(int age)
  {
    medianAge = age;
  }

  /**
   * Populate births array with given rate; assumes rate remains constant.
   *
   * @param numberOfBirths in people.
   */
  final public void setBirths(int numberOfBirths)
  {
    births = numberOfBirths;
  }




  final public void setMortality(int year, int deaths)
  {
    mortality = deaths;
  }



  final public void setMigration(int people)
  {
    migration = people;
  }




  final public void setUndernourished(int people)
  {
    undernourished = people;
  }

  final public void setTotalFarmLand(int squareKm) { totalFarmLand = squareKm;}

  final public void setFarmLand1981(int squareKm)
  {
    farmLand1981 = squareKm;
    totalFarmLand = farmLand1981;
  }

  final public void setFarmLand2014(int squareKm) { farmLand2014 = squareKm;}

  /**
   * @param crop    crop in question
   * @param metTons tons produced
   */
  public void setCropProduction(EnumFood crop, long metTons)
  {
    cropProduction[crop.ordinal()] = metTons;
  }

  /**
   * @param crop    crop in question
   * @param value Income in $1,000
   */
  final public void setCropIncome(EnumFood crop, long value)
  {
    cropIncome[crop.ordinal()] = value;
  }

  /**
   * @param crop    crop in question
   * @param metTons tons exported
   */

  final public void setCropExport(EnumFood crop, long metTons)
  {
    cropExport[crop.ordinal()] = metTons;
  }

  /**
   * @param crop    crop in question
   * @param metTons tons imported
   */
  final public void setCropImport(EnumFood crop, long metTons)
  {
    cropImport[crop.ordinal()] = metTons;
  }


  final public void setLandTotal(int kilomsq)
  {
    landTotal = kilomsq;
  }



  /**
   * Set crop land value; use this method when initializing
   *
   * @param crop    crop in question
   * @param kilomsq area to set
   */
  final public void setCropLand(EnumFood crop, int kilomsq)
  {
    landCrop[crop.ordinal()] = Math.min(kilomsq, getLandTotal());
  }


  /**
   * @param method     cultivation method (Conventional, Organic or GMO)
   * @param percentage percentage [0, 100] of land cultivated by the given method.
   */
  final public void setMethod(EnumFarmMethod method, int percentage)
  {
    cultivationMethod[method.ordinal()] = percentage;
  }
  /**
   * Method for calculating and setting crop need
   *
   * @param crop                  EnumFood
   * @param tonsConsumed          2014 production + imports - exports
   * @param percentUndernourished 2014 % of population undernourished
   */
  public void setCropNeedPerCapita(EnumFood crop, double tonsConsumed, double percentUndernourished)
  {
    double population = getPopulation(Constant.FIRST_YEAR);
    double tonPerPerson = tonsConsumed / (population - 0.5 * percentUndernourished * population);
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Method for setting crop need when already known (e.g., when copying).
   *
   * @param crop         EnumFood
   * @param tonPerPerson 2014 ton/person
   */
  public void setCropNeedPerCapita(EnumFood crop, double tonPerPerson)
  {
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * @param crop
   * @param tonPerSqKilom yield for crop
   */
  final public void setCropYield(EnumFood crop, double tonPerSqKilom)
  {
    cropYield[crop.ordinal()] = tonPerSqKilom;
  }

  /**
   * @param crop crop in question
   * @return tons produced  at end of the current year of the simulation.
   */
  final public long getCropProduction(EnumFood crop)
  {
    return cropProduction[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons produced at end of the current year of the simulation.
   */
  final public long getCropIncome(EnumFood crop)
  {
    return cropIncome[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons exported
   */
  final public long getCropExport(EnumFood crop)
  {
    return cropExport[crop.ordinal()];
  }

  /**
   * @param crop crop in question
   * @return tons imported
   */
  final public long getCropImport(EnumFood crop)
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
   * @param method cultivation method (Conventional, Organic or GMO)
   * @return percentage [0, 100] of land cultivated by the given method.
   */
  final public int getMethod(EnumFarmMethod method)
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




  /**
   * @param year year in question
   * @param n    population in that year
   */
  public void setPopulation(int year, int n)
  {
    population[year - Constant.FIRST_YEAR] = n;
  }


  /**
   * @return country's collection of 100km2 tiles
   */
  final public Area getArea()
  {
    return totalArea;
  }


  /**
   * @return Sets the game region for this unit.
   */
  public EnumRegion getGameRegion()
  {
    return region;
  }

  /**
   * Estimates the initial land used per food type, and the yield.
   */
  public void updateYield()
  {
    int income = 0;
    int production = 0;
    int land = 0;

    for (EnumFood crop : EnumFood.values())
    {
      land += landCrop[crop.ordinal()];
      production += cropProduction[crop.ordinal()];
      income += cropIncome[crop.ordinal()];
    }

    if (production == 0.)
    {
      for (EnumFood crop : EnumFood.values()) {
        // The current version of the CSV file doesn't have any production values.
        // Use the income values (if available) to estimate land per crop.
        //

        // Estimate production from the yield.
        //TODO: read data
        //cropProduction[crop.ordinal()] = (cropIncome[crop.ordinal()] / income) * landTotal;
        production += cropProduction[crop.ordinal()];
      }
    }

    for (EnumFood crop : EnumFood.values())
    {
      //TODO: read data
      //cropYield[crop.ordinal()] = cropProduction[crop.ordinal()] / landTotal;

      // Use the crop production to estimate land per crop.
      //
      //double p = cropProduction[crop.ordinal()] / production;

      // This is an initial naive estimate.  Per Joel there will eventually be a multiplier
      // applied that gives a more realistic estimate.
      //
      //landCrop[crop.ordinal()] =(int)( landTotal * p );// multiplier[food]

      land += landCrop[crop.ordinal()];

      if (landCrop[crop.ordinal()] != 0)
      { cropYield[crop.ordinal()] = cropProduction[crop.ordinal()] / landCrop[crop.ordinal()];
      }
      else cropYield[crop.ordinal()] = 0;
    }
  }

  public void setCropBudget(EnumFood food, long budget)
  {
    cropBudget[food.ordinal()] = budget;
  }

  /**
   * Get the budget for the type of food
   *
   * @param food EnumFood
   * @return current budget of the food
   */
  public long getCropBudget(EnumFood food)
  {
    return cropBudget[food.ordinal()];
  }

  /**
   * Get the total budget for all crops
   *
   * @return total budget of all crops for the territory
   */
  public long getCropBudget()
  {
    long budget = 0;
    for (int i = 0; i < cropBudget.length; i++)
    {
      budget += cropBudget[i];
    }
    return budget;
  }

  public void setLand1981(EnumFood food, double land)
  {
    land1981[food.ordinal()] = land;
  }

  /**
   * Get the land area for the type of food
   *
   * @param food EnumFood
   * @return current land area of the food
   */
  public double getLand1981(EnumFood food)
  {
    return land1981[food.ordinal()];
  }

  /**
   * Get the total land area for all crops
   *
   * @return total land area of all crops for the territory
   */
  public double getLand1981()
  {
    double land = 0;
    for (int i = 0; i < land1981.length; i++)
    {
      land += land1981[i];
    }
    return land;
  }

  public void setYield1981(EnumFood food, double yield)
  {
    yield1981[food.ordinal()] = yield;
  }

  /**
   * Get the yield for the type of food
   *
   * @param food EnumFood
   * @return current yield of the food
   */
  public double getYield1981(EnumFood food)
  {
    return yield1981[food.ordinal()];
  }

  /**
   * Get the total yield for all crops
   *
   * @return total yield of all crops for the territory
   */
  public double getYield1981()
  {
    double yield = 0;
    for (int i = 0; i < yield1981.length; i++)
    {
      yield += yield1981[i];
    }
    return yield;
  }

  /**
   * The loader loads 2014 data.  This function scales the data for 1981 given the scale factor.
   * @param factor The scaling factor.
   */
  public void scaleCropData(double factor)
  {
    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropIncome[i] *= factor;
      cropProduction[i] *= factor;
    }
  }

  /**
   * Copies initial data from another territory to this territory.  This is primarily only
   * done when unifying XML and CSV data.
   *
   * @param fromTerritory The territory from which to copy data.
   */
  public void copyInitialValuesFrom(final Territory fromTerritory)
  {
    int index = 0;
    medianAge = fromTerritory.medianAge;
    births = fromTerritory.births;
    mortality = fromTerritory.mortality;
    migration = fromTerritory.migration;
    undernourished = fromTerritory.undernourished;

    landTotal = fromTerritory.landTotal;

    for (EnumFood food : EnumFood.values())
    {
      cropYield[food.ordinal()] = fromTerritory.cropYield[food.ordinal()];
      cropNeedPerCapita[food.ordinal()] = fromTerritory.cropNeedPerCapita[food.ordinal()];
      cropIncome[food.ordinal()] = fromTerritory.cropIncome[food.ordinal()];
      cropProduction[food.ordinal()] = fromTerritory.cropProduction[food.ordinal()];
      landCrop[food.ordinal()] = fromTerritory.landCrop[food.ordinal()];
    }

    for (EnumFarmMethod method : EnumFarmMethod.values())
    {
      cultivationMethod[method.ordinal()] = fromTerritory.cultivationMethod[method.ordinal()];
    }

    while (index < Constant.LAST_YEAR - Constant.FIRST_YEAR)
    {
      population[index] = fromTerritory.population[index];
      index += 1;
    }
  }

  public void updatePopulation(int year)
  {
    int index = year - Constant.FIRST_YEAR;

    // Population data is stored in a fixed array.
    //
    int netChange = 0;
    if (index > 0) netChange = population[index] - population[index - 1];

    // TODO: We need a way to take the net change in population and back that
    // number out to birth rate, mortality rate, and undernourishment. This is
    // the Spring code to update undernourishment, based on the Spring 2015
    // spec. :
    //
    double numUndernourished;
    double population = getPopulation(year);
    double[] netCropsAvail = new double[EnumFood.SIZE];
    int numCropsAvail = 0;
    for (EnumFood crop : EnumFood.values())
    {
      double netAvail = getNetCropAvailable(crop);
      netCropsAvail[crop.ordinal()] = netAvail;
      if (netAvail >= 0) numCropsAvail++;
    }

    if (numCropsAvail == EnumFood.SIZE)
    {
      numUndernourished = 0;
    }
    else
    {
      double maxResult = 0;
      for (EnumFood crop : EnumFood.values())
      {
        double need = getCropNeedPerCapita(crop);
        double result = (netCropsAvail[crop.ordinal()]) / (0.5 * need * population);
        if (result > maxResult) maxResult = result;
      }

      numUndernourished = Math.min(population, maxResult);
    }

    setUndernourished((int) numUndernourished);
  }

  /**
   * @return country's collection of 100km2 tiles
   */
  final public Collection<LandTile> getLandTiles()
  {
    return landTiles;
  }

  /**
   * @param tile LandTile to add to country
   */
  final public void addLandTile(LandTile tile)
  {
    landTiles.add(tile);
  }

  // generate the capital by finding the center of the largest landmass.
  // this method can only be called after the Country's regions have been set.
  //
  private MapPoint calCapitolLocation()
  {
    if (geographicAreaList== null) throw new RuntimeException("(!) regions not set!");
    if (geographicAreaList.isEmpty()) throw new RuntimeException("(!) no regions !");

    int maxArea = 0;
    Polygon largest = null;

    for (GeographicArea area : geographicAreaList)
    {
      Polygon poly = converter.regionToPolygon(area);
      int landArea = (int) (poly.getBounds().getWidth() * poly.getBounds().getHeight());
      if (landArea >= maxArea)
      {
        largest = poly;
        maxArea = landArea;
      }
    }

    int x = (int) largest.getBounds().getCenterX();
    int y = (int) largest.getBounds().getCenterY();

    return converter.pointToMapPoint(new Point(x, y));
  }

  /**
   * returns the point representing the shipping location of that country.<br>
   *
   * (!) note: this method can only be called after the Country's regions have
   * been set.
   *
   * @return map point representing the lat and lon location of the Country's
   * capitol.
   */
  public MapPoint getCapitolLocation()
  {
    if (capitolLocation == null)  capitolLocation = calCapitolLocation();

    return capitolLocation;
  }

  /**
   * Used to link land tiles to a country.
   *
   * @param mapPoint mapPoint that is being testing for inclusing
   * @return true is mapPoint is found inside country.
   */
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (geographicAreaList == null)
    {
      throw new RuntimeException("(!)REGIONS NOT SET YET");
    }

    for (GeographicArea area : geographicAreaList)
    {
      if (area.containsMapPoint(mapPoint)) return true;
    }
    return false;
  }


  public void addGeographicArea(GeographicArea area)
  {
    geographicAreaList.add(area);
    totalArea.add(new Area(converter.regionToPolygon(area)));
  }

  /**
   * @return regions
   */
  final public Collection<GeographicArea> getRegions()
  {
    return geographicAreaList;
  }

  public String toString()
  {
    return getClass().getSimpleName() + " " + name;
  }

  public int compareTo(String territoryName)
  {
    return territoryName.compareTo(name);
  }








  //====================================================================
  /**
   * Constructor takes list of country objects that need data from csv file
   */

  public static ArrayList<Territory> territoryLoader()
  {
    CSVReader fileReader = new CSVReader(TERRITORY_DATA_PATH, 0);
    ArrayList<Territory> territoryList = new ArrayList<>();

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);
    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(fieldList[i]))
      {
        System.out.println("**ERROR** Reading " + TERRITORY_DATA_PATH +
          "Expected header[" + i + "]=" + header + ", Found: " + fieldList[i]);
        System.exit(0);
      }
    }
    fileReader.trashRecord();

    // Implementation notes : The CSV file contains 2014 numbers for production, etc. Each row
    // includes a column at the end that converts 2014 production and farm income to 1981.
    // It is an int percentage to be multiplied onto the 2014 production and income by to get
    // the corrosponding value for 1981.  For example CA is 61, so in 1981 they had 61% of
    // their current production and income.

    fieldList = fileReader.readRecord(EnumHeader.SIZE);
    while (fieldList != null)
    //for (int k=0; k<territoryList.size(); k++)
    {
      Territory territory = new Territory(fieldList[EnumHeader.territory.ordinal()]);
      territoryList.add(territory);

      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        long value = 0;
        if ((i > 1) && (i < fieldList.length))
        {
          try
          {
            value = Long.parseLong(fieldList[i]);
          }
          catch (Exception e) {} //Default empty cell, and text to 0
        }


        switch (header)
        {
          case territory:
            break;

          case region:
            for (EnumRegion enumRegion : EnumRegion.values())
            {
              if (enumRegion.name().equals(fieldList[i]))
              {
                territory.region = enumRegion;
                break;
              }
            }

            if (territory.region == null)
            {
              System.out.println("**ERROR** Reading " + TERRITORY_DATA_PATH + " in Territory.territoryLoader");
              System.out.println("          Reading Record#" + territoryList.size() + " Territory="+territory.name);

              System.out.println("          Game Region not recognized: " + fieldList[i]);
              System.exit(0);
            }
            break;

          case population1981:  case population1990:  case population2000:
          case population2010:  case population2014:  case population2025:
          case population2050:
            int year = Integer.valueOf(header.name().substring(10));
            territory.setPopulation(year, (int) value);
            break;

          case averageAge: territory.setMedianAge((int) value); break;
          case births: territory.setBirths((int) value); break;
          case mortality: territory.setMortality(Constant.FIRST_YEAR, (int) value); break;
          case migration: territory.setMigration((int) value); break;
          case undernourished: territory.setUndernourished((int) value); break;
          case landArea: territory.setLandTotal((int) value); break;

          case farmLand1981: territory.setFarmLand1981((int) value); break;
          case farmLand2014: territory.setFarmLand2014((int) value); break;

          case organic: territory.setMethod(EnumFarmMethod.ORGANIC, (int) value); break;
          case gmo: territory.setMethod(EnumFarmMethod.GMO, (int) value); break;
        }
      }

      int conventional = 100 -
        (territory.getMethod(EnumFarmMethod.GMO) + territory.getMethod(EnumFarmMethod.ORGANIC));
      territory.setMethod(EnumFarmMethod.CONVENTIONAL, conventional);

      //Read next record
      fieldList = fileReader.readRecord(EnumHeader.SIZE);
    }
    return territoryList;
  }
}

