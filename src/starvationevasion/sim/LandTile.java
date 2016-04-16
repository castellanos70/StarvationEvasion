package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.sim.io.CSVReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


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

  /**
   * Each record of PATH_COORDINATES must be in a one-to-one,
   * ordered matching with each record in each month of each annual file of PATH_CLIMATE_PREFIX.
   * TODO: Currently Chris Wu has too many records in the PATH_CLIMATE_PREFIX files and the
   * climate data does not match at all with the coordinates in PATH_COORDINATES.
   */

  private static final String PATH_COORDINATES = "/sim/climate/ArableCoordinates.csv";
  private static final String PATH_CLIMATE_PREFIX = "/sim/climate/Climate_Historical_";

  private static int PATH_COORDINATES_FIELD_COUNT = 3; //Latitude,Longitude,Territory


  private enum DataHeaders
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
    System.out.println("getField("+field+" , " + year + ", " + month + ") rain=" +
    data[0][month.ordinal()][field.ordinal()]);

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
    Date dateStart = new Date();
    System.out.println("LandTile.load() Loading Climate Data: " +dateFormat.format(dateStart));



    //Read the latitude longitude coordinates of each record in the PATH_CLIMATE_PREFIX files.
    CSVReader fileReader = new CSVReader(PATH_COORDINATES, 1);
    String[] fieldList;
    Territory territory = null;

    ArrayList<LandTile> tileList = new ArrayList<>();

    while ((fieldList = fileReader.readRecord(PATH_COORDINATES_FIELD_COUNT)) != null)
    {
      float latitude = Float.parseFloat(fieldList[0]);
      float longitude = Float.parseFloat(fieldList[1]);
      LandTile tile = new LandTile(latitude, longitude);
      tileList.add(tile);


      if ((territory == null) || (!territory.getName().equals(fieldList[2])))
      {
        territory = model.getTerritory(fieldList[2]);
        //assert(territory.containsMapPoint(new MapPoint(latitude,longitude)));
        //if (!territory.containsMapPoint(new MapPoint(latitude,longitude)))
        //{
        //  System.out.println("*********** ERROR " + territory.getName() + " does not contain " + new MapPoint(latitude,longitude));
        //}

      }
      territory.addLandTile(tile);
    }
    fileReader.close();


    //Climate data is stored in .zip files by year where each year contains subfiles for each
    // month. The locations of each record in the PATH_CLIMATE_PREFIX files is given by
    // its record number matched with record numbers of PATH_COORDINATES.
    int preGameYears = Constant.FIRST_GAME_YEAR - Constant.FIRST_DATA_YEAR;

    int year = 2000;
    //int yearIdx = year - Constant.FIRST_DATA_YEAR;
    String path = PATH_CLIMATE_PREFIX + year + ".zip";

    try
    {
      ZipFile file = new ZipFile(model.getClass().getResource(path).getFile());
      System.out.println("================Iterating over zip file : " + path);
      Enumeration<? extends ZipEntry> entries = file.entries();

      //Open sub-file for each month of year.
      int month = 0;
      while (entries.hasMoreElements())
      { ZipEntry entry = entries.nextElement();
        System.out.printf("File: %s Size %d Modified on %TD %n", entry.getName(), entry.getSize(), new Date(entry.getTime()));
        //extractEntry(entry, file.getInputStream(entry));

        fileReader = new CSVReader(file.getInputStream(entry), 2);

        int recordIdx = 0;
        //Read each record of file.
        while ((fieldList = fileReader.readRecord(DataHeaders.SIZE)) != null)
        {
          LandTile tile = tileList.get(recordIdx);

          //System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);
          //Read each field of record.
          for (int i=0; i<DataHeaders.SIZE; i++)
          {
            float value= Float.parseFloat(fieldList[i]);

            //TODO: Now we only have data from 2000, so for now, just copy the values for
            //every year we should be reading.
            for (int yearIdx=0; yearIdx<RawDataYears.SIZE; yearIdx++)
            { tile.data[yearIdx][month][i] = value;

              //if (yearIdx == 0 && month == System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);

            }
          }
          recordIdx++;
        }
        fileReader.close();
        month++;
      }
      file.close();

    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(0);
    }





    Date dateDone = new Date();
    double deltaSec = (dateDone.getTime() - dateStart.getTime())/1000.0;
    System.out.println("LandTile.load() Done: elapsed sec=" +deltaSec);

  }


  /**
   * Utility method to read data from InputStream
   */
  /*
  private static void extractEntry(final ZipEntry entry, InputStream is) throws IOException
  { String exractedFile = OUTPUT_DIR + entry.getName();
    FileOutputStream fos = null; try { fos = new FileOutputStream(exractedFile);
    final byte[] buf = new byte[BUFFER_SIZE];
    int read = 0; int length;
    while ((length = is.read(buf, 0, buf.length)) >= 0)
    { fos.write(buf, 0, length);
    }
  }
  catch (IOException ioex) { fos.close(); }
  }
*/
 }
