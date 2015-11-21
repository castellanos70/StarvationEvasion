package worldfoodgame.model;

import starvationevasion.common.EnumFood;
import starvationevasion.model.CropClimateData;
import worldfoodgame.common.CropZoneData.EnumCropZone;

import java.nio.ByteBuffer;

/**
 @author david
 created: 2015-03-21

 description: 
 LandTile class describes a single section of the equal area projection of the
 world map.  It holds elevation, climate and climate projection data found
 at www.worldclim.org.  The class also describes a ByteBuffer format for a
 custom binary file.  This allows the data from the raw sets to be parsed, projected
 and averaged across equal-area sections of the globe once.  This data can then
 simply be loaded in at the program start. (see CropZoneDataIO class)
 */

public class LandTile
{
  
  private float elevation = 0;     /* in meters above sea level */
  private float maxAnnualTemp = 0; /* in degrees Celsius. */
  private float minAnnualTemp = 0; /* in degrees Celsius. */
  private float avgDayTemp = 0;    /* in degrees Celsius. */
  private float avgNightTemp = 0;  /* in degrees Celsius. */
  private float rainfall = 0;      /* in cm */
  private float proj_maxAnnualTemp = 0; /* in degrees Celsius. */
  private float proj_minAnnualTemp = 0; /* in degrees Celsius. */
  private float proj_avgDayTemp = 0;    /* in degrees Celsius. */
  private float proj_avgNightTemp = 0;  /* in degrees Celsius. */
  private float proj_rainfall = 0;      /* in cm */
  private MapPoint center;
  private EnumFood currCrop;

  /**
   Constructor used for initial creation of data set
   @param lon
   longitude of this LandTile
   @param lat
   latitude of this LandTile
   */
  public LandTile(double lon, double lat)
  {
    center = new MapPoint(lon, lat);
  }

  /**
   * Constructor using custom binary file
   * @param buf   custom binary file
   */
  public LandTile(ByteBuffer buf)
  {
    int len = buf.array().length;
    if (len != BYTE_DEF.SIZE_IN_BYTES)
    {
      throw new IllegalArgumentException(
        String.format("ByteBuffer of incorrect size%n" +
          "Expected: %d%n" +
          "Received: %d%n", BYTE_DEF.SIZE_IN_BYTES, len));
    }

    float lon = buf.getFloat(BYTE_DEF.LONGITUDE.index());
    float lat = buf.getFloat(BYTE_DEF.LATITUDE.index());
    elevation = buf.getFloat(BYTE_DEF.ELEVATION.index());

    maxAnnualTemp = buf.getFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index());
    minAnnualTemp = buf.getFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index());
    avgDayTemp = buf.getFloat(BYTE_DEF.AVG_DAY_TEMP.index());
    avgNightTemp = buf.getFloat(BYTE_DEF.AVG_NIGHT_TEMP.index());
    rainfall = buf.getFloat(BYTE_DEF.RAINFALL.index());

    proj_maxAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index());
    proj_minAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index());
    proj_avgDayTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index());
    proj_avgNightTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index());
    proj_rainfall = buf.getFloat(BYTE_DEF.PROJ_RAINFALL.index());

    center = new MapPoint(lon, lat);
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

  
  /**
   * @param elev  tile's elevation
   */
  public void setElev(float elev)
  {
    elevation = elev;
  }

  /**
   * @return  tool tip text when cursor over tile
   */
  public String toolTipText()
  {
    return String.format("<html>(lon:%.2f, lat:%.2f)<br>" +
        "rainfall:%.6fcm<br>" +
        "daily temp range: (%.2f C, %.2f C)<br>" +
        "yearly temp range: (%.2f C, %.2f C)<br>" +
        "crop: %s</html>",
      center.getLon(), center.getLat(), rainfall,
      avgNightTemp, avgDayTemp, minAnnualTemp, maxAnnualTemp, currCrop);
  }

  public ByteBuffer toByteBuffer()
  {
    ByteBuffer buf = ByteBuffer.allocate(BYTE_DEF.SIZE_IN_BYTES);

    buf.putFloat(BYTE_DEF.LONGITUDE.index(), (float) center.getLon());
    buf.putFloat(BYTE_DEF.LATITUDE.index(), (float) center.getLat());
    buf.putFloat(BYTE_DEF.ELEVATION.index(), elevation);

    buf.putFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index(), maxAnnualTemp);
    buf.putFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index(), minAnnualTemp);
    buf.putFloat(BYTE_DEF.AVG_DAY_TEMP.index(), avgDayTemp);
    buf.putFloat(BYTE_DEF.AVG_NIGHT_TEMP.index(), avgNightTemp);
    buf.putFloat(BYTE_DEF.RAINFALL.index(), rainfall);

    buf.putFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index(), proj_maxAnnualTemp);
    buf.putFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index(), proj_minAnnualTemp);
    buf.putFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index(), proj_avgDayTemp);
    buf.putFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index(), proj_avgNightTemp);
    buf.putFloat(BYTE_DEF.PROJ_RAINFALL.index(), proj_rainfall);

    return buf;
  }

  public double getLon()
  {
    return center.getLon();
  }

  public double getLat()
  {
    return center.getLat();
  }

  public MapPoint getCenter()
  {
    return center;
  }

  /**
   * Mutates tile's values when year changes
   * @param yearsRemaining  years remaining in game
   */
  public void stepTile(int yearsRemaining)
  {
    maxAnnualTemp = interpolate(maxAnnualTemp, proj_maxAnnualTemp, yearsRemaining,1);
    minAnnualTemp = interpolate(minAnnualTemp, proj_minAnnualTemp, yearsRemaining,1);
    avgDayTemp = interpolate(avgDayTemp, proj_avgDayTemp, yearsRemaining,1);
    avgNightTemp = interpolate(avgNightTemp, proj_avgNightTemp, yearsRemaining,1);
    rainfall = interpolate(rainfall, proj_rainfall, yearsRemaining,1);
  }

  public float getElevation()
  {
    return elevation;
  }

  public float getMaxAnnualTemp()
  {
    return maxAnnualTemp;
  }

  public void setMaxAnnualTemp(float maxAnnualTemp)
  {
    this.maxAnnualTemp = maxAnnualTemp;
  }

  public float getMinAnnualTemp()
  {
    return minAnnualTemp;
  }

  public void setMinAnnualTemp(float minAnnualTemp)
  {
    this.minAnnualTemp = minAnnualTemp;
  }

  public float getAvgDayTemp()
  {
    return avgDayTemp;
  }

  public void setAvgDayTemp(float avgDayTemp)
  {
    this.avgDayTemp = avgDayTemp;
  }

  public float getAvgNightTemp()
  {
    return avgNightTemp;
  }

  public void setAvgNightTemp(float avgNightTemp)
  {
    this.avgNightTemp = avgNightTemp;
  }

  public float getRainfall()
  {
    return rainfall;
  }

  public void setRainfall(float rainfall)
  {
    this.rainfall = Math.max(0, rainfall);
  }

  public float getProj_maxAnnualTemp()
  {
    return proj_maxAnnualTemp;
  }

  public void setProj_maxAnnualTemp(float proj_maxAnnualTemp)
  {
    this.proj_maxAnnualTemp = proj_maxAnnualTemp;
  }

  public float getProj_minAnnualTemp()
  {
    return proj_minAnnualTemp;
  }

  public void setProj_minAnnualTemp(float proj_minAnnualTemp)
  {
    this.proj_minAnnualTemp = proj_minAnnualTemp;
  }

  public float getProj_avgDayTemp()
  {
    return proj_avgDayTemp;
  }

  public void setProj_avgDayTemp(float proj_avgDayTemp)
  {
    this.proj_avgDayTemp = proj_avgDayTemp;
  }

  public float getProj_avgNightTemp()
  {
    return proj_avgNightTemp;
  }

  public void setProj_avgNightTemp(float proj_avgNightTemp)
  {
    this.proj_avgNightTemp = proj_avgNightTemp;
  }

  public float getProj_rainfall()
  {
    return proj_rainfall;
  }

  public void setProj_rainfall(float proj_rainfall)
  {
    this.proj_rainfall = proj_rainfall;
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
    CropClimateData data = CropClimateData.mapFood(crop);

    int cropDayT = data.dayTemp;
    int cropNightT = data.nightTemp;
    int cropMaxT = data.maxTemp;
    int cropMinT = data.minTemp;
    int cropMaxR = data.maxRain;
    int cropMinR = data.minRain;
    
    double tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    double rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum rainfall range
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
  }

  /**
   * Rate tile's suitability for a particular country's  other crops.
   * @param otherCropsData  a country's otherCropsData object
   * @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   */
  public EnumCropZone rateTileForOtherCrops(CropClimateData otherCropsData)
  {
    float cropDayT = otherCropsData.dayTemp;
    float cropNightT = otherCropsData.nightTemp;
    float cropMaxT = otherCropsData.maxTemp;
    float cropMinT = otherCropsData.minTemp;
    float cropMaxR = otherCropsData.maxRain;
    float cropMinR = otherCropsData.minRain;
    float tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    float rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum rainfall range
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

  /**
   * Retuns the crop currently planted on this tile.
   * @return planted crop
   */
  public EnumFood getCurrentCrop()
  {
    return currCrop;
  }

  /**
   BYTE_DEF enum generalizes the binary format the LandTiles can be stored in
   */
  public enum BYTE_DEF
  {
    LONGITUDE, LATITUDE, ELEVATION,
    MAX_ANNUAL_TEMP, MIN_ANNUAL_TEMP,
    AVG_DAY_TEMP, AVG_NIGHT_TEMP,
    RAINFALL,
    PROJ_MAX_ANNUAL_TEMP, PROJ_MIN_ANNUAL_TEMP,
    PROJ_AVG_DAY_TEMP, PROJ_AVG_NIGHT_TEMP,
    PROJ_RAINFALL;

    public static final int SIZE = values().length;

    public static final int SIZE_IN_BYTES = SIZE * Float.SIZE / Byte.SIZE;
    int index()
    {
      return ordinal() * Float.SIZE / Byte.SIZE;
    }

  }
  
  @Override
  public String toString()
  {
    return "LandTile{" +
      "rainfall=" + rainfall +
      ", avgNightTemp=" + avgNightTemp +
      ", avgDayTemp=" + avgDayTemp +
      ", minAnnualTemp=" + minAnnualTemp +
      ", maxAnnualTemp=" + maxAnnualTemp +
      ", elevation=" + elevation +
      ", center=" + center +
      '}';
  }


}
