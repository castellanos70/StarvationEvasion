package starvationevasion.sim.io;


import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.sim.Region;

import java.io.FileNotFoundException;
import java.lang.Integer;
import java.util.logging.Logger;

/**
 * Parses the WorldFoodProduction.csv file
 */
public class ProductionCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(FertilizerCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldFoodProductionFake.csv";


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
    consumption, //Domestic consumption: Including all possible uses of the commodity:
                 //           food, feed, seed, waste, and industrial processing.
                 //           Does NOT include Ending stocks.
    yield;       //Yield (MT/HA)

    public static final int SIZE = values().length;
  };

  public static void load(Region[] regionList)
  {
    CSVReader fileReader = new CSVReader(PATH, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);

    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(fieldList[i]))
      {
        LOGGER.severe("**ERROR** Reading " + PATH +
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
      long exports = 0;
      long imports = 0;
      long production = 0;
      long consumption = 0;
      double yield = 0.0;

      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        if (fieldList[i].equals("")) continue;

        switch (header)
        {
          case year:
            year = Integer.parseInt(fieldList[i]);
            //TODO: for now, data is 1981, but this should be changed
            if (year < Constant.FIRST_DATA_YEAR) year = Constant.FIRST_DATA_YEAR;
            break;
          case category:
            food = EnumFood.valueOf(fieldList[i]);
            break;
          case region:
            //TODO: divide US data into states
            if (fieldList[i].equals("UNITED_STATES"))
            {
              region = EnumRegion.USA_CALIFORNIA;
            }
            else
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

          case consumption:
            consumption = Long.parseLong(fieldList[i]);
            break;

          case yield:
            yield = Double.parseDouble(fieldList[i]);
            break;
        }
      }

      //Usually, our game will start in 2010. Thus, we only want to load pre-game data
      //  of productions up through 2009.
      if (year < Constant.FIRST_GAME_YEAR)
      { int idx = region.ordinal();
        //System.out.println("regionList["+idx+"]="+regionList[idx]);
        regionList[idx].addProduction(year, food, imports, exports, production, consumption,yield);
      }
    }
  }
}
