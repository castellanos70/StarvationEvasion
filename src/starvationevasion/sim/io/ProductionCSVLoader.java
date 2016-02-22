package starvationevasion.sim.io;


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
  private static final String PATH = "/sim/WorldFoodProduction_1981.csv";


  private enum EnumHeader
  {
    food, category, country, region, year, exports, imports, production;

    public static final int SIZE = values().length;
  };

  public ProductionCSVLoader(Region[] regions)
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
      System.out.println("ProductionCSVLoader(): record="+fieldList[0]+", "+fieldList[2]+", len="+fieldList.length);
      EnumFood food = null;
      EnumRegion region = null;
      int year = 0;
      long exports = 0;
      long imports = 0;
      long production = 0;

      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        if (fieldList[i].equals("")) continue;

        switch (header)
        {
          case year:
            year = Integer.parseInt(fieldList[i]);
            break;
          case category:
            food = EnumFood.valueOf(fieldList[i]);
            break;
          case region:
            //TODO: divide US data into states
            if (!fieldList[i].equals("UNITED_STATES"))
            {
              region = EnumRegion.valueOf(fieldList[i]);
            }
            break;
          case exports:
            exports = Long.parseLong(fieldList[i]);
            break;
          case imports:
            imports = Long.parseLong(fieldList[i]);
            break;
          case production:
            production = Long.parseLong(fieldList[i]);
            break;
        }
      }

      Region r;
      if (region != null)
      {

        r = regions[region.ordinal()];

      r.setInitialProduction(food, r.getInitialProduction(food, year) + production, year);
      r.setInitialImports(food, r.getInitialImports(food, year) + imports, year);
      r.setInitialExports(food, r.getInitialExports(food, year) + exports, year);
      }
    }
  }
}
