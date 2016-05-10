package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumCropZone;
import starvationevasion.common.EnumFood;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.io.CSVReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


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
  private static final String COORDINATE_FILENAME = "GeodesicArableCoordinates_778km2";
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



  private int currentProduction;

  private double productionMultiplier = 1;

  private int currentCost;

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

  /**
   * @return total production of tile for current year in USD
   */
  public int getCurrentProduction()
  {
    return (int) (currentProduction * productionMultiplier);
  }

  public void setCurrentProduction(int currentProduction)
  {
    this.currentProduction = currentProduction;
  }

  public int getCurrentCost()
  {
    return currentCost;
  }

  public void setCurrentCost(int currentCost)
  {
    this.currentCost = currentCost;
  }

  public void setCrop(EnumFood crop)
  {
    curCrop = crop;
  }
  
  public double getProductionMultiplier()
  {
    return productionMultiplier;
  }
  
  public void setProductionMultiplier(double productionMultiplier)
  {
    this.productionMultiplier = productionMultiplier;
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
   * Rates a given tile's suitability for a particular crop.
   * 
   * @param crop
   *          crop for which we want rating (citrus, fruit, nut, grain, oil,
   *          veggies, special, or feed)
   * @return EnumCropZone (IDEAL, GOOD, ACCEPTABLE, or POOR)
   * @throws NullPointerException
   *           if called with argument EnumFood.OTHER_CROPS, will throw an
   *           exception because OTHER_CROPS required climate varies by country;
   *           rating cannot be calculated using crop alone.
   */
  public EnumCropZone rateTileForCrop(EnumFood crop, Region region, int dataYear, CropData cropData, double rainMultiplier)
      throws NullPointerException
  {
    Constant.Month currentMonth;

    // isAcceptable is set to true during the loop if the crop is ever found to
    // be acceptable. We do not immediately return once finding that a tile is
    // acceptable for a crop as we may find that a tile is also ideal at a
    // later time.
    boolean isAcceptable = false;
    boolean isGood = false;

    // The current running Acceptable or better grow days. The loop starts on
    // January, and if the month is deemed ideal and/or acceptable, add the
    // current months total days to its respective value. If February is
    // neither ideal or acceptable, set them both back to 0. If these values
    // ever reach the crops required grow days, we know that the tile is not
    // poor.
    int consecutiveAcceptableGrowDays = 0;
    int consecutiveGoodGrowDays = 0;
    int consecutiveIdealGrowDays = 0;
    
    int consecutiveAcceptableWater = 0;
    int consecutiveGoodWater = 0;
    int consecutiveIdealWater = 0;

    // This value corresponds to the consecutive number of acceptable or ideal
    // grow days starting from January up to the first non acceptable or ideal 
    // month.
    //
    // The acceptable or ideal buffer is set to false once the first month
    // is found that does not meet the acceptable or ideal conditions. The
    // number of consecutive grow days is then saved to its respective
    // consecutiveBufferValue.
    //
    // We do this to check if the combination of the beginning and the end
    // of a years consecutive grow days reach an acceptable or ideal value.

    boolean consecutiveAcceptableBuffer = true;
    boolean consecutiveGoodBuffer = true;
    boolean consecutiveIdealBuffer = true;
    int consecutiveAcceptableBufferValue = 0;
    int consecutiveGoodBufferValue = 0;
    int consecutiveIdealBufferValue = 0;
    
    int consecutiveAcceptableWaterBuff = 0;
    int consecutiveGoodWaterBuff = 0;
    int consecutiveIdealWaterBuff = 0;

    // these values per month
    float tileMonthlyLowT;
    float tileMeanDailyHighT;
    float tileMeanDailyLowT;
    float tileRain;
    
    // The crop values are the information needed from the crop to rate a tile
    int cropIdealHigh = cropData.getData(CropData.Field.TEMPERATURE_IDEAL_HIGH, crop);
    int cropIdealLow = cropData.getData(CropData.Field.TEMPERATURE_IDEAL_LOW, crop);
    int cropTempMin = cropData.getData(CropData.Field.TEMPERATURE_MIN, crop);
    int cropGrowdays = cropData.getData(CropData.Field.GROW_DAYS, crop);
    
    // 1000 kg/m3 is the mass density of water. Multiply the required water
    // necessary for a crop given in units m3/ton by the mass density of water
    // to get the required water necessary for a crop in units kg/ton.
    float cropWaterRequired = cropData.getData(CropData.Field.WATER, crop) * 1000; //in m3/ton
    
    if (region.getCropArea(dataYear, crop) == 0)
    {
      return EnumCropZone.POOR;
    }
    
    // Crop density in the given region. Mass of crop per square kilometers
    long cropMassPerArea = region.getCropProduction(dataYear, crop) / region.getCropArea(dataYear,
        crop);
    
    // Multiply these two values to get the amount of water required in units
    // kg/km2. Divide by 1 000 000 to convert from kg/km2 to kg/m2. Now we will
    // directly compare this value, the necessary total amount of water
    // necessary for a crop, to the amount of water produced in a landtile's
    // climate
    cropWaterRequired = (cropWaterRequired * cropMassPerArea) / 1000000; // kg/m2
    
    for (int i = 0; i < Constant.Month.SIZE; i++)
    { // Iterate through each month checking if suitable conditions exist for
      // the necessary growdays
      currentMonth = Constant.Month.values()[i];
      tileMonthlyLowT = getField(Field.TEMP_MONTHLY_LOW, dataYear, currentMonth);
      tileMeanDailyHighT = getField(Field.TEMP_MEAN_DAILY_HIGH, dataYear, currentMonth);
      tileMeanDailyLowT = getField(Field.TEMP_MEAN_DAILY_LOW, dataYear, currentMonth);
      tileRain = getField(Field.RAIN, dataYear, currentMonth); //kg/m2

      if (tileMonthlyLowT < cropTempMin)
      { // if the crops will freeze this month, tile is poor for this month
        
        //update buffers if necessary
        if (consecutiveAcceptableBuffer)
        {
          consecutiveAcceptableBuffer = false;
          consecutiveAcceptableBufferValue = consecutiveAcceptableGrowDays;
          consecutiveAcceptableWaterBuff = consecutiveAcceptableWater;

          if (consecutiveGoodBuffer)
          {
            consecutiveGoodBuffer = false;
            consecutiveGoodBufferValue = consecutiveGoodGrowDays;
            consecutiveGoodWaterBuff = consecutiveGoodWater;

            if (consecutiveIdealBuffer)
            {
              consecutiveIdealBuffer = false;
              consecutiveIdealBufferValue = consecutiveIdealGrowDays;
              consecutiveIdealWaterBuff = consecutiveIdealWater;              
            }
          }
        }
        
        //reset all values
        
        consecutiveIdealGrowDays = 0;
        consecutiveGoodGrowDays = 0;
        consecutiveAcceptableGrowDays = 0;
        
        consecutiveIdealWater = 0;
        consecutiveGoodWater = 0;
        consecutiveAcceptableWater = 0;
      }
      else //the crop will at least be acceptable/not freeze.
      {
        if (isBetween(tileMeanDailyLowT, cropIdealLow, cropIdealHigh) && isBetween(
            tileMeanDailyHighT, cropIdealLow, cropIdealHigh) && !isGood)
        { // if the tile is at least good for this crop this month
          consecutiveGoodGrowDays += currentMonth.days();
          consecutiveAcceptableGrowDays += currentMonth.days();
          
          consecutiveGoodWater += tileRain;
          consecutiveAcceptableWater += tileRain;

          if (isBetween(tileMonthlyLowT, cropIdealLow, cropIdealHigh))
          { // if the tile is ideal for this crop this month
            consecutiveIdealGrowDays += currentMonth.days();
            consecutiveIdealWater += tileRain;
          }
          else
          { // not ideal
            if (consecutiveIdealBuffer)
            {
              consecutiveIdealBuffer = false;
              consecutiveIdealBufferValue = consecutiveIdealGrowDays;
              consecutiveIdealWaterBuff = consecutiveIdealWater;
            }

            consecutiveIdealGrowDays = 0;
            consecutiveIdealWater = 0;
          }
        }
        else if (!isAcceptable && !isGood) //not good, only acceptable
        {
          if (consecutiveGoodBuffer)
          {
            consecutiveGoodBuffer = false;
            consecutiveGoodBufferValue = consecutiveGoodGrowDays;
            consecutiveGoodWaterBuff = consecutiveGoodWater;
          }

          consecutiveGoodGrowDays = 0;
          consecutiveGoodWater = 0;
          
          consecutiveAcceptableGrowDays += currentMonth.days();
          consecutiveAcceptableWater += tileRain;
        }
      }
      
      // check if we can determine anything with new consecutiveGrowDay values
      if (consecutiveIdealGrowDays >= cropGrowdays && consecutiveIdealWater >= cropWaterRequired)
      { //if Ideal just return immediately.
        return EnumCropZone.IDEAL;
      }
      else if (!isGood && consecutiveGoodGrowDays >= cropGrowdays
          && consecutiveGoodWater >= cropWaterRequired)
      { //if isGood is true this elseif never executes
        isGood = true;
      }
      else if (!isGood && !isAcceptable && consecutiveAcceptableGrowDays >= cropGrowdays
          && consecutiveAcceptableWater >= cropWaterRequired)
      {//if isGood is true we don't care if it's acceptable.
        isAcceptable = true;
      }
    }
    
    // At this point, the consecutiveGrowDay values are what the values are
    // through December. If it wasn't acceptable/good/ideal in December, this
    // value is 0. We will add this value to its respective buffer. If January
    // wasn't acceptable/good/ideal, the respective buffer is also 0.

    // Check if the beginning + the end of a year result in an ideal tile
    // for the given crop
    if (consecutiveIdealGrowDays + consecutiveIdealBufferValue >= cropGrowdays
        && consecutiveIdealWater + consecutiveIdealWaterBuff >= cropWaterRequired)
    {
      return EnumCropZone.IDEAL;
    }
    // Else check if we ever had a period that was deemed Good or if the
    // beginning + end of a year results in a Good tile for the crop
    else if (isGood || (consecutiveGoodGrowDays + consecutiveGoodBufferValue >= cropGrowdays
        && consecutiveGoodWater + consecutiveGoodWaterBuff >= cropWaterRequired))
    {
      return EnumCropZone.GOOD;
    }
    else if (isAcceptable || (consecutiveAcceptableGrowDays
        + consecutiveAcceptableBufferValue >= cropGrowdays && consecutiveAcceptableWater
            + consecutiveAcceptableWaterBuff >= cropWaterRequired))
    {
      return EnumCropZone.ACCEPTABLE;
    }
    else
    { // If it's not Ideal/Good/Acceptable, it's poor.
      return EnumCropZone.POOR;
    }
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
          while ((fieldList = fileReader.readRecord(Field.SIZE)) != null)
          {
            LandTile tile = tileList.get(recordIdx);

            //System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);
            //Read each field of record.
            for (int i=0; i<Field.SIZE; i++)
            {
              float value= Float.parseFloat(fieldList[i]);

              //There are locations for which rain data does not exist. For now, we set
              // these values to 0.
              if (i == Field.RAIN.ordinal() && value < 0f) value = 0f;


              //System.out.printf("     tile[%d].data[%d][%d][%d]=%f\n", recordIdx.yearEnum.ordinal(),month,i,value);
              tile.data[yearEnum.ordinal()][month][i] = value;

              //if (yearIdx == 0 && month == System.out.println("rain="+fieldList[DataHeaders.Rain.ordinal()]+" at " + tile.latitude + ", " + tile.latitude);

            }
            recordIdx++;
          }
          fileReader.close();
          assert (recordIdx == Model.TOTAL_LAND_TILES);
          month++;
        }
        zipFile.close();
      }

      catch (Exception e)
      {
        System.out.println("          Record# "+recordIdx+" "+ e.getMessage());
        e.printStackTrace();
        System.exit(0);
      }
    }
  }
 }
