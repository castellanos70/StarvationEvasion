package starvationevasion.sim.io;


import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.sim.Region;

import java.util.logging.Logger;

/**
 * Parses the WorldFoodProduction.csv file
 */
public class ProductionCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(ProductionCSVLoader.class.getName());
  private static final String PATH_WORLD_PRODUCTION = "/sim/WorldFoodProduction_2000-2009.csv";
  private static final String PATH_USA_PRODUCTION_BY_STATE = "/sim/Percentage_USA_Production_by_State_2014.csv";


  private enum EnumHeader
  {
    food,     //Name of food product.
    category, //EnumFood.
    country,  //Territory name, except all USA territories are combined.
    region,   //EnumRegion
    year,     //Market Year
    imports,  //Metric Tons
    exports,  //Metric Tons
    production,  //Domestic production. Does NOT include Beginning stocks
    yield,       //Yield (MT/HA)
    source;

    public static final int SIZE = values().length;
  }

  public static void load(Region[] regionList)
  {
    int preGameYears = Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR;
    long[][] usa_exports     = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_imports     = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_production  = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_area        = new long[preGameYears][EnumFood.SIZE];

    CSVReader fileReader = new CSVReader(PATH_WORLD_PRODUCTION, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);

    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(fieldList[i]))
      {
        LOGGER.severe("**ERROR** Reading " + PATH_WORLD_PRODUCTION +
                      ": Expected header[" + i + "]=" + header + ", Found: " + fieldList[i]);
        return;
      }
    }
    fileReader.trashRecord();

    // read until end of file is found
    while ((fieldList = fileReader.readRecord(EnumHeader.SIZE)) != null)
    {
      //System.out.println("ProductionCSVLoader(): record="+fieldList[0]+", "+fieldList[2]+", len="+fieldList.length);
      EnumFood food = null;
      EnumRegion region = null;
      int year = 0;
      long imports = 0;
      long exports = 0;
      long production = 0;
      long area = 0;

      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        if (fieldList[i].equals("")) continue;

        switch (header)
        {
          case year:
            year = Integer.parseInt(fieldList[i]);
            if (year < Constant.FIRST_DATA_YEAR) year = Constant.FIRST_DATA_YEAR;
            break;
          case category:
            food = EnumFood.valueOf(fieldList[i]);
            break;
          case region:
            if (!fieldList[i].equals("UNITED_STATES"))
            {
              region = EnumRegion.valueOf(fieldList[i]);
            }
            break;
          case imports:
            imports = Long.parseLong(fieldList[i]);
            break;
          case exports:
            exports = Long.parseLong(fieldList[i]);
            break;

          case production:
            production = Long.parseLong(fieldList[i]);
            break;

          case yield:
            area = (long)(production/Double.parseDouble(fieldList[i]));
            break;
        }
      }

      //Usually, our game will start in 2010. Thus, we only want to load pre-game data
      //  of productions up through 2009.
      if (year < Constant.FIRST_GAME_YEAR)
      {
        if (region != null)
        { int idx = region.ordinal();
          regionList[idx].addProduction(year, food, imports, exports, production,area);
        }
        else
        {
          int yearIdx = year-Constant.FIRST_DATA_YEAR;
          int cropIdx = food.ordinal();
          usa_imports[yearIdx][cropIdx]     += imports;
          usa_exports[yearIdx][cropIdx]     += exports;
          usa_production[yearIdx][cropIdx]  += production;
          usa_area[yearIdx][cropIdx]        += area;
        }
      }
    }
    fileReader.close();

    fileReader = new CSVReader(PATH_USA_PRODUCTION_BY_STATE, 0);
    fileReader.trashRecord();

    // read until end of file is found
    double[][] productionPercent = new double[EnumRegion.SIZE][EnumFood.SIZE];

    while ((fieldList = fileReader.readRecord(EnumFood.SIZE+2)) != null)
    {
      int regionIdx = EnumRegion.getRegion(fieldList[1]).ordinal();

      for (int i=2; i<fieldList.length; i++)
      {
        int foodIdx = i-2;
        productionPercent[regionIdx][foodIdx] += Double.parseDouble(fieldList[i]);
     }
    }
    fileReader.close();



    for (int year=Constant.FIRST_DATA_YEAR; year< Constant.FIRST_GAME_YEAR; year++)
    {
      int yearIdx = year - Constant.FIRST_DATA_YEAR;
      for (EnumRegion region : EnumRegion.values())
      {
        int regionIdx = region.ordinal();
        for (EnumFood food : EnumFood.values())
        {
          int cropIdx = food.ordinal();

          long imports     = (long)(productionPercent[regionIdx][cropIdx] * usa_imports[yearIdx][cropIdx]);
          long exports     = (long)(productionPercent[regionIdx][cropIdx] * usa_exports[yearIdx][cropIdx]);
          long production  = (long)(productionPercent[regionIdx][cropIdx] * usa_production[yearIdx][cropIdx]);
          long area        = (long)(productionPercent[regionIdx][cropIdx] * usa_area[yearIdx][cropIdx]);

          regionList[regionIdx].addProduction(year, food, imports, exports, production, area);
        }
      }
    }
    fileReader.close();
  }
}
