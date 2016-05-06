package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.Util;
import starvationevasion.sim.io.CSVReader;

import java.util.logging.Logger;

public class CropData
{
  private final static Logger LOGGER = Logger.getLogger(CropData.class.getName());
  private final String PATH_CROPDATA = "/sim/CropData.csv";

  private enum EnumHeader
  {
    food,      // EnumCrop
    price2000, // int: Price (2000 US import price, Dollars per Metric Ton)
    price2009, // int: Price (2009 US import price, Dollars per Metric Ton)
    water,     // int: Water (Cubic meters per Metric Ton)
    energy,    // not implemented yet.
    fertilizerN,    // not implemented yet.
    fertilizerP2O5, // not implemented yet.
    fertilizerK2O,  // not implemented yet.
    growDays,       //Growing Period (days/year)
    temperatureMin,        //Temperature Min (Deg C). Crops die if temperature drops below this within its growing period.
    temperatureIdealLow,  // Temperature Ideal Low (Deg C). Crops have max productivity if all days are within this range.
    temperatureIdealHigh; // Temperature Ideal High (Deg C). Crops have max productivity if all days are within this range.
    private static final int SIZE = values().length;
  }


  public enum Field
  {
    WATER,     // int: Water (Cubic meters per Metric Ton)
    ENERGY,    // not implemented yet.
    FERTILIZER_N,    // not implemented yet.
    FERTILIZER_P2O5, // not implemented yet.
    FERTILIZER_K2O,  // not implemented yet.
    GROW_DAYS,       //Growing Period (days/year)
    TEMPERATURE_MIN,        //Temperature Min (Deg C). Crops die if temperature drops below this within its growing period.
    TEMPERATURE_IDEAL_LOW,  // Temperature Ideal Low (Deg C). Crops have max productivity if all days are within this range.
    TEMPERATURE_IDEAL_HIGH; // Temperature Ideal High (Deg C). Crops have max productivity if all days are within this range.
    public static final int SIZE = values().length;
  }

  private int[][] foodPrice = new int[Model.YEARS_OF_DATA][EnumFood.SIZE];
  private int[][] data = new int[Field.SIZE][EnumFood.SIZE];


  public CropData()
  {
    CSVReader fileReader = new CSVReader(PATH_CROPDATA, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);

    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      if (!header.name().equals(fieldList[i]))
      {
        LOGGER.severe("**ERROR** Reading " + PATH_CROPDATA +
          ": Expected header[" + i + "]=" + header + ", Found: " + fieldList[i]);
        return;
      }
    }
    fileReader.trashRecord();

    // read until end of file is found
    while ((fieldList = fileReader.readRecord(EnumHeader.SIZE)) != null)
    {
      //System.out.println("CropData(): record="+fieldList[0]);

      EnumFood food = EnumFood.valueOf(fieldList[0]);
      int foodIdx = food.ordinal();

      for (EnumHeader header : EnumHeader.values())
      {
        int i = header.ordinal();
        if (i == 0)  continue;
        if (fieldList[i].equals("")) continue;

        int value = Integer.parseInt(fieldList[i]);
        switch (header)
        {
          case price2000:
            foodPrice[0][foodIdx] = value;  break;
          case price2009:
            foodPrice[9][foodIdx] = value;  break;
          case water:
            data[Field.WATER.ordinal()][foodIdx] = value; break;
          case energy:
            data[Field.ENERGY.ordinal()][foodIdx] = value; break;
          case fertilizerN:
            data[Field.FERTILIZER_N.ordinal()][foodIdx] = value; break;
          case fertilizerP2O5:
            data[Field.FERTILIZER_P2O5.ordinal()][foodIdx] = value; break;
          case fertilizerK2O:
            data[Field.FERTILIZER_K2O.ordinal()][foodIdx] = value; break;
          case growDays:
            data[Field.GROW_DAYS.ordinal()][foodIdx] = value; break;
          case temperatureMin:
            data[Field.TEMPERATURE_MIN.ordinal()][foodIdx] = value; break;
          case temperatureIdealLow:
            data[Field.TEMPERATURE_IDEAL_LOW.ordinal()][foodIdx] = value; break;
          case temperatureIdealHigh:
            data[Field.TEMPERATURE_IDEAL_HIGH.ordinal()][foodIdx] = value; break;
        }
      }


      for (int yearIdx=1; yearIdx<9; yearIdx++)
      {
        foodPrice[yearIdx][foodIdx] =
          (int) Util.linearInterpolate(0, yearIdx, 9, foodPrice[0][foodIdx], foodPrice[9][foodIdx]);
      }
    }
    fileReader.close();
  }

  public int getPrice(int year, EnumFood food)
  { return foodPrice[year-Constant.FIRST_DATA_YEAR][food.ordinal()];
  }

  public int getData(Field field, EnumFood food)
  { return data[field.ordinal()][food.ordinal()];
  }
}
