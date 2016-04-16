package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.sim.io.CSVReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * LandTiles are used to hold climate data for the model.<br>
 * This model uses past and future values of climate data from third party measurements and models
 * sampled on a geodesic grid of regular hexagons with 0.25 degree resolution.<br><br>
 *
 * Areas of the globe not over one of the modeled territories have been pre filtered from the data file.<br><br>
 *
 * Each tile is a hexagon with center at latitude, longitude.<br><br>
 * Each game territory is contains the LandTiles that cover that territory. <br><br>
 *
 *
 * Data source: http://www.nccs.nasa.gov/<br>
 * Temperatures are near surface, air temperatures in degrees C.<br>
 * The raw data from is daily.<br><br>
 * Precipitation is the average volume of water in the form of rain, snow, hail,
 * or sleet that falls per unit of area and per unit of time at the site.
 * The raw data is measured in units of (kg / m2)/sec. We converted this to
 * (kg / m2) = millimeters by multiplying by 24*60*60.
 */

public class LandTile
{
  public enum RawDataYears
  { y2000, y2001, y2002, y2003, y2004, y2005, y2010, y2015, y2020, y2025, y2030, y2035, y2040, y2045, y2050;
    static int SIZE = values().length;
  }

  private static final String PATH_COORDINATES = "/sim/climate/ArableCoordinates.csv";
  private static final String PATH_CLIMATE_DIR = "/sim/climate/Climate_";

  private static int PATH_COORDINATES_FIELD_COUNT = 3; //Latitude,Longitude,Territory


  private enum FileHeaders
  {
    TempMonthLow,     //Temperature Monthly Low (deg C).
    TempMonthHigh,    //Temperature Monthly High (deg C).
    TempMeanDailyLow, //Temperature Mean Daily Low  (deg C).
    TempMeanDailyHigh,//Temperature Mean Daily High (deg C).
    Rain;             //Precipitation Mean Daily (kg/m2, which is the same as millimeters height).
    static int SIZE = values().length;
  }

  public enum Field
  {
    TEMP_MONTHLY_LOW,     //Temperature Monthly Low (deg C).
    TEMP_MONTHLY_HIGH,    //Temperature Monthly High (deg C).
    TEMP_MEAN_DAILY_LOW, //Temperature Mean Daily Low  (deg C).
    TEMP_MEAN_DAILY_HIGH,//Temperature Mean Daily High (deg C).
    RAIN;             //Precipitation Mean Daily (kg/m2, which is the same as millimeters height).
    static int SIZE = values().length;
  }

  private float latitude;
  private float longitude;

  /**
   * curCrop == null indicates there that no crop is currently planted in this LandTile.
   */
  private EnumFood curCrop = null;

  private float[][][] data = new float[RawDataYears.SIZE][Constant.Month.SIZE][Field.SIZE];

  private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


  /**
   Constructor used for initial creation of data set
   @param lat Latitude ranges from -90 to 90. North latitude is positive.
   @param lon Longitude ranges from -180 to 180. East longitude is positive.
   */
  public LandTile(float lat, float lon)
  {
    latitude  = lat;
    longitude = lon;
  }

  public float getField(Field field, int year, Constant.Month month)
  {
    int lowIdx = 0;
    int lowYear = Constant.FIRST_DATA_YEAR;
    for (RawDataYears yearEnum : RawDataYears.values())
    {
      int tempYear = Integer.valueOf(yearEnum.name().substring(1));
      if (tempYear <= year)
      {
        lowIdx = yearEnum.ordinal();
        lowYear = tempYear;
      }
      if (tempYear >= year) break;
    }

    float lowValue = data[lowIdx][month.ordinal()][field.ordinal()];
    if (lowYear == year) return lowValue;

    int highIdx = Math.min(lowIdx+1, RawDataYears.SIZE-1);
    int highYear = Integer.valueOf(RawDataYears.values()[highIdx].name().substring(1));
    float highValue = data[highIdx][month.ordinal()][field.ordinal()];
    if (highYear >= year) return highValue;

    return Util.linearInterpolate(lowYear, year, highYear, lowValue, highValue);
  }


  /**
   * @return Latitude ranges from -90 to 90. North latitude is positive.
   */
  public float getLatitude() {return latitude;}

  /**
   * @return Longitude ranges from -180 to 180. East longitude is positive.
   */
  public float getLongitude() {return longitude;}

  public void setCrop(EnumFood crop)
  {
    curCrop = crop;
  }
  public EnumFood getCrop() { return curCrop; }





  /**
   * Rates tile's suitability for a particular crop.
   * @param crop  crop for which we want rating (wheat, corn, rice, or soy)
   * @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   * @throws NullPointerException if called with argument EnumFood.OTHER_CROPS, will throw an
   * exception because OTHER_CROPS required climate varies by country;
   * rating cannot be calculated using crop alone.
   */
  public EnumCropZone rateTileForCrop(EnumFood crop) throws NullPointerException
  {
    /*
    CropZoneData data = CropZoneData.mapFood(crop);
    if (data == null)
    {
      //Logger.getGlobal().log(Level.SEVERE, "Couldn't find crop climate data for " + crop + ". Defaulting to POOR.");
      return EnumCropZone.POOR;
    }

    int cropDayT = data.dayTemp;
    int cropNightT = data.nightTemp;
    int cropMaxT = data.maxTemp;
    int cropMinT = data.minTemp;
    int cropMaxR = data.maxRain;
    int cropMinR = data.minRain;

    double tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    double rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum Rainfall range
    if (isBetween(avgDayTemp, cropNightT, cropDayT) &&
      isBetween(avgNightTemp, cropNightT, cropDayT) &&
      isBetween(rainfall, cropMinR, cropMaxR) &&
      maxAnnualTemp <= cropMaxT && minAnnualTemp >= cropMinT)
    {
      return EnumCropZone.IDEAL;
    }
    else if (isBetween(avgDayTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(avgNightTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(rainfall, cropMinR - 0.3 * rRange, cropMaxR + 0.3 * rRange) &&
      maxAnnualTemp <= cropMaxT && minAnnualTemp >= cropMinT)
    {
      return EnumCropZone.ACCEPTABLE;
    }
    else
    {
      return EnumCropZone.POOR;                                               // otherwise tile is POOR for crop
    }
    */
    return null;
  }

  /**
   * Rate tile's suitability for a particular country's  other crops.
   * @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   */
  public EnumCropZone rateTileForOtherCrops()
  {
    /*
    float cropDayT = otherCropsData.dayTemp;
    float cropNightT = otherCropsData.nightTemp;
    float cropMaxT = otherCropsData.maxTemp;
    float cropMinT = otherCropsData.minTemp;
    float cropMaxR = otherCropsData.maxRain;
    float cropMinR = otherCropsData.minRain;
    float tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    float rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum Rainfall range
    if (isBetween(avgDayTemp, cropNightT, cropDayT) &&
      isBetween(avgNightTemp, cropNightT, cropDayT) &&
      isBetween(rainfall, cropMinR, cropMaxR) &&
      minAnnualTemp >= cropMinT && maxAnnualTemp <= cropMaxT)
    {
      return EnumCropZone.IDEAL;
    }
    else if (isBetween(avgDayTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(avgNightTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(rainfall, cropMinR - 0.3 * rRange, cropMaxR + 0.3 * rRange) &&
      minAnnualTemp >= cropMinT && maxAnnualTemp <= cropMaxT)
    {
      return EnumCropZone.ACCEPTABLE;
    }
    else
    {
      return EnumCropZone.POOR;                                               // otherwise tile is POOR for crop
    }
    */
    return null;
  }

 /**
   * Get percent of country's yield for crop tile will yield, base on its zone rating and
   * current use
   * @param crop    crop in question
   * @param zone    tile's zone for that crop
   * @return        percent of country's yield tile can yield
   */
  public double getTileYieldPercent(EnumFood crop, EnumCropZone zone)
  {
    double zonePercent = 0;
    double usePercent;
    switch (zone)
    {
      case IDEAL:
        zonePercent = 1;
        break;
      case ACCEPTABLE:
        zonePercent = 0.6;
        break;
      case POOR:
        zonePercent = 0.25;
        break;
    }
    if (curCrop == crop) usePercent = 1;
    else if (curCrop == null) usePercent = 0.1;
    else usePercent = 0.5;
    return zonePercent * usePercent;
  }

  private boolean isBetween(Number numToTest, Number lowVal, Number highVal)
  {
    if (numToTest.doubleValue() >= lowVal.doubleValue() && numToTest.doubleValue() <= highVal.doubleValue())
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  @Override
  public String toString()
  {
    return "LandTile{" +
            ", current crop =" + curCrop +
            '}';


  }

  /**
   * Given the game's full list of regions, this static method reads the climate data files,
   * creates a LandTile for each location and adds each LandTile to the territory within the
   * region to which it belongs.
   * @param model the model.
   */
  public static void load(Model model)
  {
    //try
    //{
    Date dateStart = new Date();
    System.out.println("LandTile.load() Loading Climate Data: " +dateFormat.format(dateStart));
    int preGameYears = Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR;

    int year = 2000;
    int month = 7;
    int yearIdx = year - Constant.FIRST_DATA_YEAR;
    String monthStr = String.format("%02d", month+1);
    String path = PATH_CLIMATE_DIR + year + '_' +monthStr + ".csv";

    CSVReader fileReader = new CSVReader(PATH_COORDINATES, 0);

    String[] fieldList;
    fileReader.trashRecord();
    Territory territory = null;

    //  FileWriter writer = new FileWriter("joel.csv", true);

    //String out;

    while ((fieldList = fileReader.readRecord(PATH_COORDINATES_FIELD_COUNT)) != null)
    {
      //System.out.println("ProductionCSVLoader(): record="+fieldList[0]+", "+fieldList[2]+", len="+fieldList.length);

      float latitude = Float.parseFloat(fieldList[0]);
      float longitude = Float.parseFloat(fieldList[1]);
      LandTile tile = new LandTile(latitude, longitude);

      if ((territory == null) || (!territory.getName().equals(fieldList[2])))
      {
        territory = model.getTerritory(fieldList[2]);
      }
      territory.addLandTile(tile);
    }
    fileReader.close();
    //  writer.close();
    Date dateDone = new Date();
    double deltaSec = (dateDone.getTime() - dateStart.getTime())/1000.0;
    System.out.println("LandTile.load() Done: elapsed sec=" +deltaSec);

/*
      for (int i=2; i<FileHeaders.SIZE; i++)
      {
        int ii = i-2;
        float value= Float.parseFloat(fieldList[i]);

        tile.data[yearIdx][month][ii] = value;
      }
    }
    fileReader.close();
    Date dateDone = new Date();
    double deltaSec = (dateDone.getTime() - dateStart.getTime())/1000.0;
    System.out.println("LandTile.load() Done: elapsed sec=" +deltaSec);
*/
    //}
    //catch (Exception e)
    //{
    //  System.out.println(e.getMessage());
    //  e.printStackTrace();
    //  System.exit(0);
    //}
  }
}
