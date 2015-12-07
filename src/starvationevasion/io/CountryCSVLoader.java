package starvationevasion.io;


import starvationevasion.common.Constant;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.EnumFood;
import starvationevasion.sim.EnumFarmMethod;

import java.io.FileNotFoundException;
import java.util.*;
import java.lang.Integer;
import java.util.logging.Logger;

/**
 * CountryCSVLoader contains methods for parsing country data in csv file, creating list of
 * ag unit objects.
 * @author  jessica
 * @version Mar-21-2015
 */
public class CountryCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(CountryCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldData/TerritoryFarmAreaAndIncome-2014.csv";

  private enum EnumHeader
  { territory, region, population1981, population1990, population2000,
    population2010, population2014, population2025, population2050,
    averageAge, undernourished, births, migration, mortality,
    landArea, farmLand1981, farmLand2014,
    incomeCitrus, incomeNonCitrus, incomeNuts, incomeGrains,
    incomeSeedOil, incomeVeg, incomeSpecial, incomeFeed, incomeFish,
    incomeMeat, incomePoultry, incomeDairy,
    organic, gmo, convert2014to1981;

    public static final int SIZE = values().length;
  };



  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  //public CountryCSVLoader(Collection<Territory> territoryList)
  public CountryCSVLoader(Territory[] territoryList, Region[] regionList) throws FileNotFoundException
  {
    CSVReader fileReader = new CSVReader(PATH, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);
    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(fieldList[i]))
      {
        LOGGER.severe("**ERROR** Reading " + PATH+
          "Expected header["+i+"]="+header + ", Found: "+ fieldList[i]);
        return;
      }
    }
    fileReader.trashRecord();

    // Implementation notes : The CSV file contains 2014 numbers for production, etc. Each row
    // includes a column at the end that converts 2014 production and farm income to 1981.
    // It is an int percentage to be multiplied onto the 2014 production and income by to get
    // the corrosponding value for 1981.  For example CA is 61, so in 1981 they had 61% of
    // their current production and income.
    //
    for (int k=0; k<territoryList.length; k++)
    {
      Territory territory = null;
      fieldList = fileReader.readRecord(EnumHeader.SIZE);
      if (fieldList == null) break;

      double foodFactor = 1.;
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
            Territory tmp = new Territory(fieldList[i]);
            int idx = Arrays.binarySearch(territoryList, tmp);
            if (idx < 0)
            {
              LOGGER.severe("**ERROR** Reading " + PATH +
                  "Territory=" + fieldList[i] + " not found in territory list.  Check the XML.");
              return;
            }

            territory = territoryList[idx];
            break;

          case region:
            for (EnumRegion enumRegion : EnumRegion.values())
            {
              if (enumRegion.name().equals(fieldList[i]))
              {
                // System.out.println("Territory " + territory.getName() + " region " + enumRegion);

                territory.setGameRegion(enumRegion);
                regionList[enumRegion.ordinal()].addTerritory(territory);
                break;
              }
            }

            if (territory.getGameRegion() == null)
            { // Handle special case book-keeping regions.
              //
              int r;
              for (r = EnumRegion.SIZE ; r < regionList.length ; r += 1)
              {
                if (regionList[r].getName().equals(fieldList[i]))
                {
                  regionList[r].addTerritory(territory);
                  break;
                }
              }

              if (r == regionList.length)
              {
                LOGGER.severe("**ERROR** Reading " + PATH + "Game Region not recognized: " + fieldList[i]);
                return;
              }
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

          case incomeCitrus: territory.setCropIncome(EnumFood.CITRUS, value); break;
          case incomeNonCitrus: territory.setCropIncome(EnumFood.FRUIT, value); break;
          case incomeNuts: territory.setCropIncome(EnumFood.NUT, value); break;
          case incomeGrains: territory.setCropIncome(EnumFood.GRAIN, value); break;
          case incomeSeedOil: territory.setCropIncome(EnumFood.OIL, value); break;
          case incomeVeg: territory.setCropIncome(EnumFood.VEGGIES, value); break;
          case incomeSpecial: territory.setCropIncome(EnumFood.SPECIAL, value); break;
          case incomeFeed: territory.setCropIncome(EnumFood.FEED, value); break;
          case incomeFish: territory.setCropIncome(EnumFood.FISH, value); break;
          case incomeMeat: territory.setCropIncome(EnumFood.MEAT, value); break;
          case incomePoultry: territory.setCropIncome(EnumFood.POULTRY, value); break;
          case incomeDairy: territory.setCropIncome(EnumFood.DAIRY, value); break;

          case convert2014to1981: foodFactor = (double) value / 100; break;
        }
      }

      int conventional = 100 -
        (territory.getMethod(EnumFarmMethod.GMO) + territory.getMethod(EnumFarmMethod.ORGANIC));
      territory.setMethod(EnumFarmMethod.CONVENTIONAL, conventional);

      // Scale the crop income and production by the 2014 to 1981 conversion factor.
      //
      territory.scaleCropData (foodFactor);

      interpolatePopulation(territory, 1981, 1990);
      interpolatePopulation(territory, 1990, 2000);
      interpolatePopulation(territory, 2000, 2010);
      interpolatePopulation(territory, 2010, 2014);
      interpolatePopulation(territory, 2014, 2050);
    }
  }

  /**
   * Linear interpolate population.
  */
  private void interpolatePopulation(Territory territory, int year0, int year1)
  {
    int y0 = territory.getPopulation(year0);
    int y1 = territory.getPopulation(year1);

    for (int i = year0 + 1 ; i < year1 ; i += 1)
    {
      double y = y0 + (y1 - y0) * (((double) i - year0) / (year1 - year0));
      territory.setPopulation(i, (int) y);
    }
  }
  
  private void copyCropValues(Territory countryFinal, Territory agriculturalUnitTemp)
  {
    for (EnumFood crop:EnumFood.values())
    {
      long imports = agriculturalUnitTemp.getCropImport(crop);
      long exports = agriculturalUnitTemp.getCropExport(crop);
      long production = agriculturalUnitTemp.getCropProduction(crop);
      int land = agriculturalUnitTemp.getCropLand(crop);
      double yield = agriculturalUnitTemp.getCropYield(crop);
      double need = agriculturalUnitTemp.getCropNeedPerCapita(crop);

      countryFinal.setCropImport(crop, imports);
      countryFinal.setCropExport(crop, exports);
      countryFinal.setCropProduction(crop, production);
      countryFinal.setCropLand(crop, land);
      countryFinal.setCropYield(crop, yield);
      countryFinal.setCropNeedPerCapita(crop, need);
    }
  }
}
