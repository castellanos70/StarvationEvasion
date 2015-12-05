package starvationevasion.io;


import starvationevasion.sim.PenaltyData;
import starvationevasion.sim.Territory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;


/**
 * Loads Trade and Food penalty function into a map
 */
public class PenaltyCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(FertilizerCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldData/Food_Trade_Penalty_Function.csv";

  private PenaltyData data;

  public PenaltyCSVLoader(List<Territory> territories) throws FileNotFoundException
  {
    CSVReader fileReader = new CSVReader(PATH, 2);
    data = new PenaltyData(territories);

    String[] fieldList;
    while ((fieldList = fileReader.readRecord(2)) != null)
    {
      String territory = fieldList[0];
      double value = Double.parseDouble(fieldList[1]);
      data.setPenaltyData(territory, value);
    }
  }

  /**
   * Get the penalty function data read in by the loader
   *
   * @return PenaltyData
   */
  public PenaltyData getReadData()
  {
    return data;
  }
}
