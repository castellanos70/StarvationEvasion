package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.sim.CropZoneData.EnumCropZone;

public class LandTile
{
  public enum Month
  { JAN, FEB, MAR, APR, MAY, JUN, JLY, AUG, SEP, OCT, NOV, DEC;
    static int SIZE = values().length;
  }

  private float latitude;
  private float longitude;
  private float[][] temperatureMonthlyLow = new float[Model.YEARS_OF_DATA][Month.SIZE];   //degrees Celsius.
  private float[][] temperatureMonthlyHigh = new float[Model.YEARS_OF_DATA][Month.SIZE];  //degrees Celsius.
  private float[][] temperatureMeanDailyLow = new float[Model.YEARS_OF_DATA][Month.SIZE]; //degrees Celsius.
  private float[][] temperatureMeanDailyHigh = new float[Model.YEARS_OF_DATA][Month.SIZE];//degrees Celsius.
  private float[][] rainMeanDaily = new float[Model.YEARS_OF_DATA][Month.SIZE];           //cm
  private float[][] rainMaxDaily = new float[Model.YEARS_OF_DATA][Month.SIZE];            //cm

  private EnumFood currCrop = null;

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


  /**
   Find an interpolated value given extremes of the range across which to
   interpolate, the number of steps to divide that range into and the step of
   the range to find.

   @param start   beginning (min) of the range
   @param end     end (max) of the range
   @param slices  slices to divide range into
   @param n       slice desired from interpolation
   @return        interpolated value
   */
  public static float interpolate(float start, float end, float slices, float n)
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


  public void setCurrCrop(EnumFood crop)
  {
    currCrop = crop;
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
    if (currCrop == crop) usePercent = 1;
    else if (currCrop == null) usePercent = 0.1;
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
            ", current crop =" + currCrop +
            '}';


  }
}
