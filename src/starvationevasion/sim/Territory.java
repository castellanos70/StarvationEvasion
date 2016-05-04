package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.sim.io.CSVReader;

import java.util.ArrayList;

public class Territory
{
  /**
   * A Territory is the base political unit in the simulator.
   * Each modeled country and each US state is a Territory.
   * A Region is the base political unit exposed to the player.
   * A region a set of territories. Each territory is assigned to one region.
   *<br><br>
   * Note: Values below that are, in the countryData.cvs file, defined as type int
   * should be displayed to the user as type int. However, as some of these
   * values may be adjusted from year-to-year by continuous functions, it
   * is useful to maintain their internal representations as doubles.
   *<br><br>
   * Note: As the start of milestone II, births and migration are assumed constant
   * for each year of the model and medianAge is not used at all.
   * However, it might be that before the end of milestone II, we are given simple functions
   * for these quantities: such as a constant rate of change of birth rate replacing the
   * constant birth rate or a data lookup table that has predefined values for each year.
   * The development team should expect and plan for such mid-milestone updates.
   */


  private static final String TERRITORY_DATA_PATH = "/sim/TerritoryPopulationAndLandUse.csv";

  private String name;


  /**
   * Projected population read from data file for selected years.
   * For the first year of the model, the simulation population is defined as the projected population for
   * that year. For future years, the simulation population is a function of the projected population and
   * the state of the model.
   */
  private int[] populationProjection = new int[Model.YEARS_OF_DATA];       // in 1,000 people
  private int[] population = new int[Model.YEARS_OF_DATA];       // in 1,000 people
  private int[] undernourished = new int[Model.YEARS_OF_DATA];  // number of undernourished x 1,000 per year.

  // The Fall 2015 spec on pg19 defines Human Development Index (HDI) as a function
  // of undernourishment in two categories; Protien Energy and Micronutrient.
  //
  private float humanDevelopmentIndex;


  /*
   * Unit: square kilometers.<br>
   * The total land area of the Earth is 148,940,000 km2. Thus, and int is sufficient.
   */
  private int landTotal;

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
   * cultivated crops.<br>
   * Unit: square kilometers.
   */
  private int[] farmLandTotal = new int[Model.YEARS_OF_DATA]; // in square kilometers

  private int[] cultivationMethod = new int[EnumFarmMethod.SIZE]; //percentage [0,100]
  private double[] cropNeedPerCapita = new double[EnumFood.SIZE]; //metric tons per person per year.

  private double penaltyValue = -1;

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
  { territory,	region,	population2000,
    population2010,	population2014,	population2025,	population2050,
    undernourished, landArea,	farmLand2000,	farmLand2009,	organic,	gmo;

    public static final int SIZE = values().length;
  };


  private GeographicArea geographicArea;
  private ArrayList<LandTile> landTiles  = new ArrayList<>();

  /*
   * The region to which this country belongs.
   */
  private EnumRegion region;
  private long[] cropBudget = new long[EnumFood.SIZE];

  /**
   * Territory constructor
   *
   * @param name country name
   */
  public Territory(String name)
  {
    this.name = name;
    geographicArea = new GeographicArea(name);
  }


  private void init()
  {
    int numYearsBeforeModel = Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR;

    //System.out.println("*******Territory.init(): " + name);
    cultivationMethod[EnumFarmMethod.CONVENTIONAL.ordinal()] =
      100 - (cultivationMethod[EnumFarmMethod.GMO.ordinal()]
          +  cultivationMethod[EnumFarmMethod.ORGANIC.ordinal()]);

    //At the start of the game, populations for each territory for 1981, 1990, 2000, 2010, 2014, 2025, and 2050
    //  are obtained from 3rd party historical or simulation results.
    //Also at the start of the game, the in between years are estimated using linear interpolation (this code is done here).


    int lastIdx = 0;
    int i = 0;
    while(i< populationProjection.length)
    {
      if (populationProjection[i] > 0)
      {
        population[i] = populationProjection[i];
        lastIdx = i;
        i++;
        continue;
      }

      int nextIdx = i+1;
      while (nextIdx< populationProjection.length)
      {
        if (populationProjection[nextIdx] > 0)
        {
          break;
        }
        nextIdx++;
      }
      for (int k=lastIdx+1; k<nextIdx; k++)
      {
        float lastPop = populationProjection[lastIdx];
        float nextPop = populationProjection[nextIdx];
        populationProjection[k] = (int)Util.linearInterpolate(lastIdx, k, nextIdx, lastPop, nextPop);

        if (k+Constant.FIRST_DATA_YEAR < Constant.FIRST_GAME_YEAR)
        {
          population[k] = populationProjection[k];
        }
      }
      i = nextIdx;
    }



    //Assume the first year undernourished data is for 2000 (which is false as it is for 1990).
    //Assume the percentage of undernourished in each country remains unchanged from 2000 through 2019 (also false).
    double undernourishedPercent = (double)undernourished[0]/(double)population[0];

    for (i=1; i<=numYearsBeforeModel; i++)
    { undernourished[i] = (int)(population[i]*undernourishedPercent);
    }


    for (int yearIdx=1; yearIdx<9; yearIdx++)
    {
      farmLandTotal[yearIdx] =
        (int) Util.linearInterpolate(0, yearIdx, 9, farmLandTotal[0], farmLandTotal[9]);
    }

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
  public int getPopulation(int year)
  {
    return population[year - Constant.FIRST_DATA_YEAR];
  }



  /**
   * @return % undernourished at end of the current year of the simulation.
   */
  final public int getUndernourished(int year)
  {
    return undernourished[year - Constant.FIRST_DATA_YEAR];
  }

  /**
   * @return % human development index at end of the current year of the simulation.
   */
  final public float getHumanDevelopmentIndex()
  {
    return humanDevelopmentIndex;
  }



  final public void setUndernourished(int year, int people)
  {
    undernourished[year - Constant.FIRST_DATA_YEAR] = people;
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
    double population = getPopulation(Constant.FIRST_DATA_YEAR);
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
   * Returns how many tons of crop country needs for specified year
   *
   * @param year year in question
   * @param crop crop in question
   * @return total tons needed to meet population's need
   */
  public double getTotalCropNeed(int year, EnumFood crop)
  {
    //double tonsPerPerson = getCropNeedPerCapita(crop);
    //int population = getPopulation(year);
    //return tonsPerPerson * population;
    return 0;
  }

  /**
   * Calculates net crop available using formula from p. 15 of spec 1.7
   *
   * @param crop crop in question
   * @return metric tons available
   */
  final public double getNetCropAvailable(EnumFood crop)
  {
    //double available = getCropProduction(crop) + getCropImport(crop) - getCropExport(crop);
    //return available;
    return 0;
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
    population[year - Constant.FIRST_DATA_YEAR] = n;
  }


  ///**
  // * @return country's collection of 100km2 tiles
  // */
  //public Area getArea()
  //{
  //  return totalArea;
  //}


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
    /*
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
    */
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



  public int getLandTotal()
  {
    return landTotal;
  }
  public void setLandTotal(int squareKm)
  {
    landTotal = squareKm;
  }

  public int getFarmLand(int year)
  {
    return farmLandTotal[year-Constant.FIRST_DATA_YEAR];
  }
  public void setFarmLand(int year, int squareKm)
  {
    farmLandTotal[year-Constant.FIRST_DATA_YEAR] = squareKm;
  }





  /**
   * @return country's collection of 100km2 tiles
   */
  public ArrayList<LandTile> getLandTiles()
  {
    return landTiles;
  }

  /**
   * @param tile LandTile to add to country
   */
  public void addLandTile(LandTile tile)
  {
    landTiles.add(tile);
  }




  /**
   * Used to link land tiles to a country.
   *
   * @param mapPoint mapPoint that is being testing for inclusing
   * @return true is mapPoint is found inside country.
   */
  public boolean contains(MapPoint mapPoint)
  {
    return geographicArea.contains(mapPoint);
  }


  public boolean contains(double latitude, double longitude)
  {
    return geographicArea.contains(latitude, longitude);
  }



  public GeographicArea getGeographicArea()
  {
    return geographicArea;
  }

  public String toString()
  {
    return getClass().getSimpleName() + " " + name;
  }

  public int compareTo(String territoryName)
  {
    return territoryName.compareTo(name);
  }


  public MapPoint getCenter()
  {
    return null;
  }





  //====================================================================
  /**
   * Constructor takes list of country objects that need data from csv file
   */

  public static ArrayList<Territory> territoryLoader()
  {
    CSVReader fileReader = new CSVReader(TERRITORY_DATA_PATH, 0);
    ArrayList<Territory> territoryList = new ArrayList<>(243);

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
        int value = 0;
        if ((i > 1) && (i < fieldList.length))
        {
          try
          {
            value = Integer.parseInt(fieldList[i]);
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

          case population2000:  case population2010:  case population2014:
          case population2025:  case population2050:
            int yearIdx = Integer.valueOf(header.name().substring(10)) - Constant.FIRST_DATA_YEAR;
            if (yearIdx >= Model.YEARS_OF_DATA) yearIdx = Model.YEARS_OF_DATA-1;
            territory.populationProjection[yearIdx] = value;
            break;

          case undernourished:
            territory.undernourished[0] = value;  break;
          case landArea: territory.landTotal = value; break;

          case farmLand2000:
            //Convert input percentage value to square km.
            //Note: multiplying by value before division could cause integer overflow.
            territory.setFarmLand(2000, (int)(territory.landTotal * (value/100.0)));
            break;
          case farmLand2009:
            territory.setFarmLand(2009, (int)(territory.landTotal * (value/100.0)));
            break;

          case organic: territory.setMethod(EnumFarmMethod.ORGANIC, (int) value); break;
          case gmo: territory.setMethod(EnumFarmMethod.GMO, (int) value); break;
        }
      }
      territory.init();

      //Read next record
      fieldList = fileReader.readRecord(EnumHeader.SIZE);
    }
    return territoryList;
  }
}

