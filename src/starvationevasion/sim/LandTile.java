package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.sim.io.CSVReader;


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

  private static final String PATH_CLIMATE_DIR = "/data/sim/climate_";

  private enum FileHeaders
  {
    Latitude,          //Latitude ranges from -90 to 90. North latitude is positive.
    Longitude,        //Longitude ranges from -180 to 180. East longitude is positive.
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
   * @param regionList The full list of game regions where each region in the list has already had
   * all of its member territories assigned.
   */
  public static void load(Region[] regionList)
  {
    /*
    int preGameYears = Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR;
    long[][] usa_exports     = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_imports     = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_production  = new long[preGameYears][EnumFood.SIZE];
    long[][] usa_area        = new long[preGameYears][EnumFood.SIZE];

    int year = 2000;
    int month = 0;
    String monthStr = String.format("%02d", month);
    String path = PATH_CLIMATE_DIR + year + '_' +monthStr + ".csv");

    CSVReader fileReader = new CSVReader(path, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(FileHeaders.SIZE);

    for (FileHeaders header : FileHeaders.values())
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
            //TODO: for now, data is 1981, but this should be changed
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
    */
  }



}
