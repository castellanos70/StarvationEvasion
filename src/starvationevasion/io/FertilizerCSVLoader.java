package starvationevasion.io;


import starvationevasion.sim.FertilizerData;
import starvationevasion.common.EnumFood;

import java.io.FileNotFoundException;
import java.lang.Integer;
import java.util.logging.Logger;

/**
 * FertilizerCSVLoader contains methods for parsing fertilizer data in csv file.
 */
public class FertilizerCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(FertilizerCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldData/world_fertilizers.csv";

  private FertilizerData data;

  private enum EnumHeader
  {
    country, fertilizerType, total, citrus, nonCitrus, nuts,
    grains, oilCrops, vegetables, specialty, feedCrop;

    public static final int SIZE = values().length;
  };

  public FertilizerCSVLoader() throws FileNotFoundException
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
          ": Expected header["+i+"]="+header + ", Found: " + fieldList[i]);
        return;
      }
    }
    fileReader.trashRecord();

    data = new FertilizerData();

    // read until end of file is found
    while (true)
    {
      String country = "";
      // read three lines of fertilizer data
      for (int j = 0; j < 3; j++)
      {
        fieldList = fileReader.readRecord(EnumHeader.SIZE);
        if (fieldList == null)
        {
          // end of file, stop reading
          return;
        }

        if (j == 0)
        {
          country = fieldList[0];
        }

        for (EnumHeader header : EnumHeader.values())
        {
          int i = header.ordinal();
          if (fieldList[i].equals("")) continue;

          int value;
          int oldValue;
          switch (header)
          {
            case citrus:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.CITRUS);
              data.setFertilizerData(country, EnumFood.CITRUS, oldValue+value);
              break;
            case nonCitrus:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.FRUIT);
              data.setFertilizerData(country, EnumFood.FRUIT, oldValue+value);
              break;
            case nuts:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.NUT);
              data.setFertilizerData(country, EnumFood.NUT, oldValue+value);
              break;
            case grains:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.GRAIN);
              data.setFertilizerData(country, EnumFood.GRAIN, oldValue+value);
              break;
            case oilCrops:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.OIL);
              data.setFertilizerData(country, EnumFood.OIL, oldValue+value);
              break;
            case vegetables:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.VEGGIES);
              data.setFertilizerData(country, EnumFood.VEGGIES, oldValue+value);
              break;
            case specialty:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.SPECIAL);
              data.setFertilizerData(country, EnumFood.SPECIAL, oldValue+value);
              break;
            case feedCrop:
              value = Integer.parseInt(fieldList[i]);
              oldValue = data.getFertilizerData(country, EnumFood.FEED);
              data.setFertilizerData(country, EnumFood.FEED, oldValue+value);
              break;
          }
        }
      }
      // ignore the N+P+K row
      fileReader.trashRecord();
    }
  }

  /**
   * Get the fertilizer data read in by the loader
   *
   * @return FertilizerData
   */
  public FertilizerData getReadData()
  {
    return data;
  }

  public static void main(String[] args)
  {
    try
    {
      FertilizerCSVLoader fertilizerData = new FertilizerCSVLoader();
      System.out.println(fertilizerData.getReadData());
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
