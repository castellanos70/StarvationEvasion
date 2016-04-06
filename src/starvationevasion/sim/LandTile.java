package starvationevasion.sim;

import starvationevasion.common.EnumFood;
import starvationevasion.sim.CropZoneData.EnumCropZone;


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
  public enum Month
  { JAN, FEB, MAR, APR, MAY, JUN, JLY, AUG, SEP, OCT, NOV, DEC;
    static int SIZE = values().length;
  }

  public enum RawDataYears
  { y2000, y2001, y2002, y2003, y2004, y2005, y2010, y2015, y2020, y2025, y2030, y2035, y2040, y2045, y2050;
    static int SIZE = values().length;
  }





  private float latitude;
  private float longitude;

  private float[][] temperatureMonthlyLow = new float[RawDataYears.SIZE][Month.SIZE];   //degrees Celsius.
  private float[][] temperatureMonthlyHigh = new float[RawDataYears.SIZE][Month.SIZE];  //degrees Celsius.
  private float[][] temperatureMeanDailyLow = new float[RawDataYears.SIZE][Month.SIZE]; //degrees Celsius.
  private float[][] temperatureMeanDailyHigh = new float[RawDataYears.SIZE][Month.SIZE];//degrees Celsius.
  private float[][] rainMeanDaily = new float[RawDataYears.SIZE][Month.SIZE];           //cm

  private EnumFood curCrop = null;

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



  public float getinterpolate(float start, float end, float slices, float n)
  {
    if (slices < 0) return end;
    float stepSize = (end - start) / slices;
    return n * stepSize + start;
  }

  public double getLon()
  {
    return longitude;
  }

  public double getLat()
  {
    return latitude;
  }


  public void setCurCrop(EnumFood crop)
  {
    curCrop = crop;
  }

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
   * @param otherCropsData  a country's otherCropsData object
   * @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   */
  public EnumCropZone rateTileForOtherCrops(CropZoneData otherCropsData)
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
}
