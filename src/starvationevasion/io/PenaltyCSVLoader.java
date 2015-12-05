package starvationevasion.io;


import starvationevasion.sim.PenaltyData;

import java.io.FileNotFoundException;
import java.util.logging.Logger;


/**
 * Loads Trade and Food penalty function into a map
 */
public class PenaltyCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(FertilizerCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldData/Food_Trade_Penalty_Function.csv";

  private PenaltyData data;

  public PenaltyCSVLoader() throws FileNotFoundException
  {
    CSVReader fileReader = new CSVReader(PATH, 2);
    data = new PenaltyData();

    String[] fieldList;
    while ((fieldList = fileReader.readRecord(2)) != null)
    {
      String territory = fieldList[0];
      double value;
      if (fieldList.length > 1)
      {
        value = Double.parseDouble(fieldList[1]);
      }
      else
      {
        value = 0;
      }
      data.setPenaltyData(territory, value);
    }
  }

  public PenaltyData getReadData()
  {
    return data;
  }

  public static void main(String[] args)
  {
    try
    {
      PenaltyCSVLoader loader = new PenaltyCSVLoader();
      System.out.println(loader.getReadData());
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
