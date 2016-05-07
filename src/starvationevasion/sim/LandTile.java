package starvationevasion.sim;

import starvationevasion.common.*;
import starvationevasion.sim.io.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * LandTiles are used to hold climate data for the model.<br>
 * This model uses past and future values of climate data from third party measurements and models
 * sampled on a geodesic grid of uniformly sized, regular hexagons where each hexagon is approximately
 * 778 km2 (about 27x27 km)<br><br>
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

  /**
   * Each record of PATH_COORDINATES must be in a one-to-one,
   * ordered matching with each record in each month of each annual file of PATH_CLIMATE_PREFIX.
   */
  private static final String PATH_COORDINATES = "/data/sim/climate/";
  private static final String COORDINATE_FILENAME = "ArableCoordinates";
  private static final String PATH_CLIMATE = "/data/sim/climate/Climate_";
  private static final String PREFIX_HISTORICAL = "Historical";
  private static final String PREFIX_RCP45 = "RCP45";
  private static final String PREFIX_RCP85 = "RCP85";

  private static final int PATH_COORDINATES_FIELD_COUNT = 2; //Latitude,Longitude


  public enum Field
  {
    TEMP_MONTHLY_LOW,     //Temperature Monthly Low (deg C).
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
   * This loadLocations method without arguments is intended to be called once by the client to load the latitude longitude coordinates
   * of each of the server's internal LandTiles. There are approximately 250,000 of these locations.<br><br>
   *
   * At the start of each turn, the client should get ??????? object containing a bit packed byte array
   * of EnumCropZone values. Each object contains the EnumCropZone value for each of the 12 food products
   * for each of tile location. <br><br>
   *
   * The ????? class comes with accessor functions providing convenient access to the bit packed data.
   */
  public static ArrayList<MapPoint> loadLocations()
  {
    return loadLocations(null, null);
  }


  /**
   * This loadLocations method does one of two very different things:
   * 1) The client calls this indirectly by calling loadLocations(). Then loadLocations() calls
   *    loadLocations(null). In this case, the method reads the LandTile locations and returns them as
   *    an array of MapPoints.
   *
   * 2) The simulator calls this on the server side with an instance of Model. In this case,
   *    the method reads all the LandTile locations, creates a LandTile for each location,
   *    queries model for the territory containing each LandTile and adds each land tile to
   *    the territory containing it.
   *
   * @param model null when called by the client. When called by the simulator, model is an instance
   *              of Model.
   * @param tileList null when called by the client. When called by the simulator, tileList
   *                 is a list of all LandTiles.
   * @return If called by the client, this returns an ArrayList<MapPoint> of all tile locations.
   * If called by the server, returns null.
   */
  public static ArrayList<MapPoint> loadLocations(Model model, ArrayList<LandTile> tileList)
  {
    //String zipPath = PATH_COORDINATES + COORDINATE_FILENAME + ".zip";
    String osPath = System.getProperty("user.dir").replace('\\', '/');
    String zipPath = osPath + PATH_COORDINATES + COORDINATE_FILENAME + ".zip";
    //String zipPath = PATH_COORDINATES + "geodesic.zip";
    ArrayList<MapPoint> mapList = null;
    try
    {


      //ZipFile zipFile = new ZipFile(Util.rand.getClass().getResource(zipPath).toURI().getPath());
      ZipFile zipFile = new ZipFile(new File(zipPath));
      //ZipInputStream zStream = new ZipInputStream(Util.rand.getClass().getResourceAsStream(zipPath));
      ZipEntry entry = zipFile.getEntry(COORDINATE_FILENAME+".csv");
      //while ((entry = zStream.getNextEntry()) != null && !entry.getName().equals(COORDINATE_FILENAME + ".csv"))
      //  ;
      //ZipEntry entry = zipFile.getEntry("geodesic.csv");

      CSVReader fileReader = new CSVReader(zipFile.getInputStream(entry), 1);
      //CSVReader fileReader = new CSVReader(zStream, 1);

      String[] fieldList;
      Territory territory = null;

      if (model == null) mapList = new ArrayList<>(Model.TOTAL_LAND_TILES);

      while ((fieldList = fileReader.readRecord(PATH_COORDINATES_FIELD_COUNT)) != null)
      {
        float latitude = Float.parseFloat(fieldList[0]);
        float longitude = Float.parseFloat(fieldList[1]);
        if (model == null)
        {
          mapList.add(new MapPoint(latitude, longitude));
        }
        else
        {
          if ((territory == null) || (!territory.contains(latitude, longitude)))
          {
            territory = model.getTerritory(latitude, longitude);
          }
          LandTile tile = new LandTile(latitude, longitude);
          tileList.add(tile);

          if (territory != null) territory.addLandTile(tile);
        }
      }
      fileReader.close();

      /*
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File("GeodesicArableFilteredCoordinates.csv")));
      CSVReader.writeRecord(writer, "Latitude,Longitude\n");
      for (EnumRegion regionID : EnumRegion.values())
      {
        Region region = model.getRegion(regionID);
        ArrayList<Territory> myTerritoryList = region.getTerritoryList();
        for (Territory t : myTerritoryList)
        {
          ArrayList<LandTile> tileList2 = t.getLandTiles();

          for (LandTile tile : tileList2)
          {
            String str = String.format("%1.3f,%1.3f", tile.getLatitude(), tile.getLongitude());
            CSVReader.writeRecord(writer, str);
          }
        }
      }
      writer.close();
      */
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage()+"\n     Cannot read file: "+zipPath);
      e.printStackTrace();
      System.exit(0);
    }

    return mapList; //null if called by the simulator (model != null).
  }

  /**
   * Given the game's full list of regions, this static method reads the climate data files,
   * and sets the values in associated LandTile.
   * @param tileList ArrayList of all LandTiles. Pointers to each LandTile can be accessed
   *                 through the model, but not in a single list. The model has a list of
   *                 territories and each territory has a list of LandTiles that it contains.
   *                 Using the ArrayList is much faster than getting the LandTiles form the territories
   *                 because the index of each LandTile is equal to the record number of each record of
   *                 climate data in each file (and there are over 250,000 LandTiles with climate data
   *                 for each of 12 months for multiple years).
   */
  public static void loadClimate(ArrayList<LandTile> tileList)
  {
    String representativeConcentrationPathway = PREFIX_RCP45;
    if (Util.rand.nextBoolean()) representativeConcentrationPathway = PREFIX_RCP85;
    System.out.println("LandTile.loadClimate() ["+representativeConcentrationPathway +"]");

    for (RawDataYears yearEnum : RawDataYears.values())
    {
      //For now, just read in climate data through 2015.
      if (yearEnum.ordinal() > RawDataYears.y2015.ordinal()) break;

      String yearStr =  yearEnum.name().substring(1);
      int year =  Integer.parseInt(yearStr);
      String prefix = PREFIX_HISTORICAL;
      if (year > 2005) prefix = representativeConcentrationPathway;

      String osPath = System.getProperty("user.dir");
      String path = osPath + PATH_CLIMATE + prefix + "_"+yearStr + ".zip";

      int recordIdx=0;
      try
      {
        System.out.printf("     Archive: %s\n", path);
        //ZipFile zipFile = new ZipFile(Util.rand.getClass().getResource(path).toURI().getPath());
        ZipFile zipFile = new ZipFile(new File(path));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        //ZipInputStream zStream = new ZipInputStream(Util.rand.getClass().getResourceAsStream(path));

        //Open sub-file for each month of year.
        int month = 0;

        while (entries.hasMoreElements())
        //while (zStream.getNextEntry() != null)
        {
          ZipEntry entry = entries.nextElement();
          //System.out.printf("       File: %s\n", entry.getName());

          CSVReader fileReader = new CSVReader(zipFile.getInputStream(entry), 1);
          //CSVReader fileReader = new CSVReader(zStream, 1);

          String[] fieldList;
          recordIdx = 0;
          //Read each record of file.
          while ((fieldList = fileReader.readRecord(Field.SIZE+1)) != null)
          {
            LandTile tile = tileList.get(recordIdx);

            //System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);
            //Read each field of record.
            for (int i=0; i<Field.SIZE; i++)
            {
              int k = i;
              if (i >= 1) k = i + 1; //This is temperary to skip the now unused column of max monthly temp
              float value= Float.parseFloat(fieldList[k]);

              //System.out.printf("     tile[%d].data[%d][%d][%d]=%f\n", recordIdx.yearEnum.ordinal(),month,i,value);
              tile.data[yearEnum.ordinal()][month][i] = value;

              //if (yearIdx == 0 && month == System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);

            }
            recordIdx++;
          }
          //fileReader.close();
          month++;
        }
        //zStream.close();
        //zipFile.close();
      }
      catch (Exception e)
      {
        System.out.println("Record# "+recordIdx+" "+ e.getMessage());
        e.printStackTrace();
        System.exit(0);
      }
    }
  }
 }
