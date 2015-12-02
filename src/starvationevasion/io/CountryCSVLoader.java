package starvationevasion.io;


import starvationevasion.common.Constant;
import starvationevasion.sim.Territory;
import starvationevasion.common.EnumRegion;
import starvationevasion.io.CSVReader.CSVRecord;
import starvationevasion.sim.Region;
import starvationevasion.common.EnumFood;
import starvationevasion.sim.EnumGrowMethod;

import java.io.*;
import java.util.*;
import java.lang.Integer;
import java.util.logging.Level;
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
  private static final String PATH = "data/sim/WorldData/TerritoryFarmAreaAndIncome-2014.csv";

  private Collection<Territory> countries;        // collection populated by parsing csv
  private Collection<Territory> masterUnits; // collection passed in (i.e., after parsing xml)
  private File csvFile;
  private List<CSVRecord> records;

  private enum EnumHeader
  { territory, region, population1981, population1990, population2000,
    population2010, population2014, population2025, population2050,
    averageAge, undernourished, births, migration, mortality,
    landArea, organic, gmo, farmLand, incomeCitrus, produceCitrus,
    incomeNonCitrus, produceNonCitrus, incomeNuts, produceNuts, incomeGrains,
    produceGrains, incomeSeedOil, produceSeedOil, incomeVeg, produceVeg,
    incomeSpecial, produceSpecial, incomeFeed, produceFeed, incomeFish,
    produceFish, incomeMeat, produceMeat, incomePoultry, producePoultry,
    incomeDairy, produceDairy;

    public static final int SIZE = values().length;
  };



  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public CountryCSVLoader(Collection<Territory> territoryList)
  {
    //this.masterUnits = territoryList;
    countries = new ArrayList<>();


    CSVReader fileReader = new CSVReader(PATH, 0);

    //Check header
    String field[] = fileReader.readRecord(EnumHeader.SIZE);
    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(field[i]))
      {
        LOGGER.severe("**ERROR** Reading " + PATH+
          "Expected header["+i+"]="+header + ", Found: "+ field[i]);
        return;
      }
    }
    fileReader.trashRecord();

    for (Territory territory : territoryList)
    {
      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        int value = 0;
        if (i > 1) value = Integer.parseInt(field[i]);
        switch (header)
        {
          case territory:
            if (!territory.getName().equals(field[i]))
            {
              LOGGER.severe("**ERROR** Reading " + PATH+
                "Expected Territory="+territory.getName() + ", Found: "+ field[i]);
              return;
            }
            break;
          case region:
            for (EnumRegion enumRegion : EnumRegion.values())
            {
              if (enumRegion.name().equals(field[i]))
              {
                territory.setGameRegion(enumRegion);
                break;
              }
            }
            LOGGER.severe("**ERROR** Reading " + PATH+
              "Game Region not recognized: "+ field[i]);
            return;

          case population1981:  case population1990:  case population2000:
          case population2010:  case population2014:  case population2025:
          case population2050:
            int year = Integer.valueOf(header.name().substring(10));
            territory.setPopulation(year, value);
            break;

          case averageAge:
            territory.setMedianAge(value);
            break;

          case births:
            territory.setBirths(value);
            break;

          case mortality:
            territory.setMortality(Constant.FIRST_YEAR, value);
            break;

          case migration:
            territory.setMigration(value);
            break;

          case undernourished:
            territory.setUndernourished(value);
            break;

          case landArea:
            territory.setLandTotal(value);
            break;

          case farmLand:
            territory.setTotalFarmLand(value);
            break;

          case organic:
            territory.setMethodPercentage(EnumGrowMethod.ORGANIC,value);
            break;

          case gmo:
            territory.setMethodPercentage(EnumGrowMethod.GMO,value);
            break;

          case incomeCitrus: case incomeNonCitrus: case incomeNuts:case incomeGrains:
          case incomeSeedOil:case incomeVeg:case incomeSpecial: case incomeFeed:
          case incomeFish:case incomeMeat:case incomePoultry:case incomeDairy:
            territory.setCropIncome(EnumFood.values()[(i-18)/2], value);
            break;

          case produceCitrus: case produceNonCitrus: case produceNuts:case produceGrains:
          case produceSeedOil:case produceVeg:case produceSpecial: case produceFeed:
          case produceFish:case produceMeat:case producePoultry:case produceDairy:
            territory.setCropProduction(EnumFood.values()[(i-19)/2], value);
            break;


        }
        int conventional = 100 - (territory.getMethodPercentage(EnumGrowMethod.GMO) +
          territory.getMethodPercentage(EnumGrowMethod.ORGANIC));
        territory.setMethodPercentage(EnumGrowMethod.CONVENTIONAL, conventional);

        interpolatePopulation(territory, 1981, 1990);
        interpolatePopulation(territory, 1990, 2000);
        interpolatePopulation(territory, 2000, 2010);
        interpolatePopulation(territory, 2010, 2014);
        interpolatePopulation(territory, 2014, 2050);
      }
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
      int imports = agriculturalUnitTemp.getCropImport(crop);
      int exports = agriculturalUnitTemp.getCropExport(crop);
      int production = agriculturalUnitTemp.getCropProduction(crop);
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
