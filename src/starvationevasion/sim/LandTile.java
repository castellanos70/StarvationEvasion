package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumCropZone;
import starvationevasion.common.EnumFood;
import starvationevasion.common.Util;
import starvationevasion.sim.io.CSVReader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


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
  { y2000, y2005, y2010, y2015, y2020, y2025, y2030, y2035, y2040, y2045, y2050;
    static int SIZE = values().length;
  }

  public static short[] PACKED_CROP_RATINGS;
  public static int[] PACKED_TILE_COORDINATES;
  
  /**
   * Each record of PATH_COORDINATES must be in a one-to-one,
   * ordered matching with each record in each month of each annual file of PATH_CLIMATE_PREFIX.
   */
  private static final String PATH_COORDINATES = "/sim/climate/ArableCoordinates.csv";
  private static final String PATH_CLIMATE = "/sim/climate/Climate_";
  private static final String PREFIX_HISTORICAL = "Historical";
  private static final String PREFIX_RCP45 = "RCP45";
  private static final String PREFIX_RCP85 = "RCP85";

  private static int PATH_COORDINATES_FIELD_COUNT = 2; //Latitude,Longitude

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
  
  /**
   * cropRatings[EnumFood.CITRUS.ordinal()] returns this LandTiles rating for
   * citrus.
   */
  private EnumCropZone[] cropRatings = new EnumCropZone[EnumFood.SIZE];

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
    //System.out.println("getField("+field+" , " + year + ", " + month + ") rain=" +
    //data[0][month.ordinal()][field.ordinal()]);

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
  
  public EnumCropZone[] getCropRatings() { return cropRatings; }
  
  public void updateRating(EnumCropZone[] ratings)
  {
    for(int i = 0; i < cropRatings.length; i++)
    {
      cropRatings[i] = ratings[i];
    }
  }
  
  public void print(int i)
  {
    System.out.println(cropRatings[i]);
  }

  private static void packData(LandTile tile, int index)
  {
    //short to store ratings for each crop : 2 bits for each of the 8 crops, 16 bits total.
    short packedRating = 0;
    //pack 2 bits at a time
    for(int i = 0; i < tile.cropRatings.length;i++)
    {
      packedRating |= (tile.cropRatings[i].ordinal() << (2*i));
    }
    PACKED_CROP_RATINGS[index] = packedRating;

    //lat and lon rounded to 2 decimal places
    int packedLatLon = 0;
    int roundedLat = Math.round(tile.getLatitude()  * 100);
    int roundedLon = Math.round(tile.getLongitude() * 100);
    //pack lat onto first 16 bits, lon onto next 16 bits
    packedLatLon |= (roundedLat << 0);
    packedLatLon |= (roundedLon << 16);
    PACKED_TILE_COORDINATES[index] = packedLatLon;

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
   * @return the total amount of LandTiles
   */
  public static void load(Model model)
  {
    Date dateStart = new Date();

    String representativeConcentrationPathway = PREFIX_RCP45;
    if (Util.rand.nextBoolean()) representativeConcentrationPathway = PREFIX_RCP85;
    System.out.println("LandTile.load() Loading Climate Data ["+representativeConcentrationPathway+
         "]: " +dateFormat.format(dateStart));

    //Read the latitude longitude coordinates of each record in the PATH_CLIMATE_PREFIX files.
    CSVReader coorFileReader = new CSVReader(PATH_COORDINATES, 1);
    String[] fieldList;
    Territory territory = null;

    ArrayList<LandTile> tileList = new ArrayList<>();

    while ((fieldList = coorFileReader.readRecord(PATH_COORDINATES_FIELD_COUNT)) != null)
    {
      float latitude = Float.parseFloat(fieldList[0]);
      float longitude = Float.parseFloat(fieldList[1]);
      LandTile tile = new LandTile(latitude, longitude);
      tileList.add(tile);

      if ((territory == null) || (!territory.contains(latitude, longitude)))
      {
        territory = model.getTerritory(latitude, longitude);
      }

      if (territory != null) territory.addLandTile(tile);


    }
    coorFileReader.close();


    //Climate data is stored in .zip files by year where each year contains subfiles for each
    // month. The locations of each record in the PATH_CLIMATE_PREFIX files is given by
    // its record number matched with record numbers of PATH_COORDINATES.

    for (RawDataYears yearEnum : RawDataYears.values())
    {
      //For now, just read in climate data through 2015.
      if (yearEnum.ordinal() > RawDataYears.y2015.ordinal()) break;

      String yearStr =  yearEnum.name().substring(1);
      int year =  Integer.parseInt(yearStr);
      String prefix = PREFIX_HISTORICAL;
      if (year > 2005) prefix = representativeConcentrationPathway;

      String path = PATH_CLIMATE + prefix + "_"+yearStr + ".zip";

      try
      {
        System.out.printf("     Archive: %s\n", path);
        ZipFile zipFile = new ZipFile(model.getClass().getResource(path).toURI().getPath());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        //Open sub-file for each month of year.
        int month = 0;
        while (entries.hasMoreElements())
        { ZipEntry entry = entries.nextElement();
          //System.out.printf("     File: %s\n", entry.getName());
          //extractEntry(entry, file.getInputStream(entry));

          CSVReader subfileReader = new CSVReader(zipFile.getInputStream(entry), 2);

          int recordIdx = 0;
          //Read each record of file.
          while ((fieldList = subfileReader.readRecord(DataHeaders.SIZE)) != null)
          {
            LandTile tile = tileList.get(recordIdx);

            //System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);
            //Read each field of record.
            for (int i=0; i<DataHeaders.SIZE; i++)
            {
              float value= Float.parseFloat(fieldList[i]);

              //System.out.printf("     tile[%d].data[%d][%d][%d]=%f\n", recordIdx.yearEnum.ordinal(),month,i,value);
              tile.data[yearEnum.ordinal()][month][i] = value;

              //if (yearIdx == 0 && month == System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);

            }
            recordIdx++;
          }
          subfileReader.close();
          month++;
        }
        zipFile.close();
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.exit(0);
      }
    }


    Date dateDone = new Date();
    double deltaSec = (dateDone.getTime() - dateStart.getTime())/1000.0;
    System.out.println("LandTile.load() Done: elapsed sec=" +deltaSec);
  }
}