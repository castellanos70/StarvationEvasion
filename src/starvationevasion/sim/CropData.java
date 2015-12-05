package starvationevasion.sim;

import starvationevasion.common.EnumFood;
import starvationevasion.io.CSVReader;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class CropData
{
  private final static Logger LOGGER = Logger.getLogger(CropData.class.getName());


  /**
   * Price in Dollars per Metric Ton for the first year of the model.
   */
  public double[] foodPriceStart = new double[EnumFood.SIZE];

  /**
   * Price in Dollars per Metric Ton for the current year of the model.
   */
  public double[] foodPrice = new double[EnumFood.SIZE];


  /**
   * Land Yield in Metric Tons / Square Kilometer for USA in first year of model.
   */
  public double[] foodYieldStart = new double[EnumFood.SIZE];


  /**
   * Land Yield in Metric Tons / Square Kilometer for USA in current year of model.
   */
  public double[] foodYield = new double[EnumFood.SIZE];


  public CropData()
  {
    final String PATH = "/sim/CropData.csv";
    CSVReader fileReader;
    try {
      fileReader = new CSVReader(PATH, 2);
    } catch (FileNotFoundException ex)
    {
      LOGGER.severe("**ERROR** Resource does not exist     " + PATH);
      LOGGER.severe("**ERROR** Check your class path or IDE resource settings");
      return;
    }

    for (int i=0; i< EnumFood.SIZE; i++)
    {
      String fields[] = fileReader.readRecord(10);
      if ((fields==null) || (!fields[0].equals(EnumFood.values()[i].name())))
      {
        LOGGER.severe("**ERROR** Reading " + PATH);
        return;
      }
      try
      {
        foodPriceStart[i] = Double.parseDouble(fields[1]);
        foodYieldStart[i] = Double.parseDouble(fields[2]);

        foodPrice[i] = foodPriceStart[i];
        foodYield[i] = foodYieldStart[i];
      }
      catch (Exception e)
      { LOGGER.severe(e.getMessage());
        e.printStackTrace();
      }
    }
  }
}
