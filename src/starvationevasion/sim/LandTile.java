package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.CropZoneData.EnumCropZone;

import java.nio.ByteBuffer;

/**
 @author david
 created: 2015-03-21

 description: 
 LandTile class describes a single section of the equal area projection of the
 world map.  It holds y0Elevation, climate and climate projection data found
 at www.worldclim.org.  The class also describes a ByteBuffer format for a
 custom binary file.  This allows the data from the raw sets to be parsed, projected
 and averaged across equal-area sections of the globe once.  This data can then
 simply be loaded in at the program start. (see CropZoneDataIO class)
 */

public class LandTile
{
  
  // PAB 12/15 - The data is divided into 3 sets :
  // 1. The 'year 0' data points.  The Fall 2015 project has a longer timeline 
  //    than the Spring 2015 team, but the Spring teams were working with 2014 
  //    data points.  Rather than try to extrapolate back to 1981, 2014 is 
  //    considered 'year 0' for the purposes of land climate data.  
  //
  private float y0Elevation = 0;     /* in meters above sea level */
  private float y0MaxAnnualTemp = 0; /* in degrees Celsius. */
  private float y0MinAnnualTemp = 0; /* in degrees Celsius. */
  private float y0AvgDayTemp = 0;    /* in degrees Celsius. */
  private float y0AvgNightTemp = 0;  /* in degrees Celsius. */
  private float y0Rainfall = 0;      /* in cm */
  
  // 2. The 2050 data points necessary to interpolate the values between the
  //    years.  Both the Spring and Fall 2015 projects use 2050 as the end year.
  //
  private float proj2050MaxAnnualTemp = 0; /* in degrees Celsius. */
  private float proj2050MinAnnualTemp = 0; /* in degrees Celsius. */
  private float proj2050AvgDayTemp = 0;    /* in degrees Celsius. */
  private float proj2050AvgNightTemp = 0;  /* in degrees Celsius. */
  private float proj2050rainfall = 0;      /* in cm */

  // 3. The data for the current simulation year.
  //
  private float elevation = 0;     /* in meters above sea level */
  private float maxAnnualTemp = 0; /* in degrees Celsius. */
  private float minAnnualTemp = 0; /* in degrees Celsius. */
  private float avgDayTemp = 0;    /* in degrees Celsius. */
  private float avgNightTemp = 0;  /* in degrees Celsius. */
  private float rainfall = 0;      /* in cm */

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
    center = new MapPoint(lat, lon);
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

    y0Elevation = buf.getFloat(BYTE_DEF.ELEVATION.index());

    y0MaxAnnualTemp = buf.getFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index());
    y0MinAnnualTemp = buf.getFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index());
    y0AvgDayTemp = buf.getFloat(BYTE_DEF.AVG_DAY_TEMP.index());
    y0AvgNightTemp = buf.getFloat(BYTE_DEF.AVG_NIGHT_TEMP.index());
    y0Rainfall = buf.getFloat(BYTE_DEF.RAINFALL.index());

    proj2050MaxAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index());
    proj2050MinAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index());
    proj2050AvgDayTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index());
    proj2050AvgNightTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index());
    proj2050rainfall = buf.getFloat(BYTE_DEF.PROJ_RAINFALL.index());

    // The Fall 2015 project needs 1981 values, which is not supported by the data
    // collected by the Spring 2015 project team.  Rather than attempt to compute
    // 1981 - 1993 values, Joel has asked that we simply use 1994's values for
    // those years.
    //
    elevation = y0Elevation;
    maxAnnualTemp = y0MaxAnnualTemp;
    minAnnualTemp = y0MinAnnualTemp;
    avgDayTemp = proj2050AvgDayTemp;
    avgNightTemp = y0AvgNightTemp;
    rainfall = y0Rainfall;

    center = new MapPoint(lat, lon);
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
   * @param elev  tile's y0Elevation
   */
  public void setElev(float elev)
  {
    y0Elevation = elev;
  }

  /**
   * @return  tool tip text when cursor over tile
   */
  public String toolTipText()
  {
    return String.format("<html>(lon:%.2f, lat:%.2f)<br>" +
        "y0Rainfall:%.6fcm<br>" +
        "daily temp range: (%.2f C, %.2f C)<br>" +
        "yearly temp range: (%.2f C, %.2f C)<br>" +
        "crop: %s</html>",
      center.longitude, center.latitude, y0Rainfall,
            y0AvgNightTemp, y0AvgDayTemp, y0MinAnnualTemp, y0MaxAnnualTemp, currCrop);
  }

  public ByteBuffer toByteBuffer()
  {
    ByteBuffer buf = ByteBuffer.allocate(BYTE_DEF.SIZE_IN_BYTES);

    buf.putFloat(BYTE_DEF.LONGITUDE.index(), (float) center.longitude);
    buf.putFloat(BYTE_DEF.LATITUDE.index(), (float) center.latitude);
    buf.putFloat(BYTE_DEF.ELEVATION.index(), y0Elevation);

    buf.putFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index(), y0MaxAnnualTemp);
    buf.putFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index(), y0MinAnnualTemp);
    buf.putFloat(BYTE_DEF.AVG_DAY_TEMP.index(), y0AvgDayTemp);
    buf.putFloat(BYTE_DEF.AVG_NIGHT_TEMP.index(), y0AvgNightTemp);
    buf.putFloat(BYTE_DEF.RAINFALL.index(), y0Rainfall);

    buf.putFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index(), proj2050MaxAnnualTemp);
    buf.putFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index(), proj2050MinAnnualTemp);
    buf.putFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index(), proj2050AvgDayTemp);
    buf.putFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index(), proj2050AvgNightTemp);
    buf.putFloat(BYTE_DEF.PROJ_RAINFALL.index(), proj2050rainfall);

    return buf;
  }

  public double getLon()
  {
    return center.longitude;
  }

  public double getLat()
  {
    return center.latitude;
  }

  public MapPoint getCenter()
  {
    return center;
  }

  /**
   * Mutates tile's values when year changes
   * @param year  The simulation year
   */
  public void setClimate(int year)
  {
    if (year < 2014)
    {
      // The Fall 2015 project needs 1981-2013 values, which is not supported by the
      // Spring 2015 project data.  Joel has asked that we simply use 1994's values for
      // those years.
      //
      elevation = y0Elevation;
      maxAnnualTemp = y0MaxAnnualTemp;
      minAnnualTemp = y0MinAnnualTemp;
      avgDayTemp = proj2050AvgDayTemp;
      avgNightTemp = y0AvgNightTemp;
      rainfall = y0Rainfall;

      return;
    }
    
    int yearsRemaining = Constant.LAST_YEAR - year;

    maxAnnualTemp = interpolate(y0MaxAnnualTemp, proj2050MaxAnnualTemp, yearsRemaining, 1);
    minAnnualTemp = interpolate(y0MinAnnualTemp, proj2050MinAnnualTemp, yearsRemaining, 1);
    avgDayTemp = interpolate(y0AvgDayTemp, proj2050AvgDayTemp, yearsRemaining, 1);
    avgNightTemp = interpolate(y0AvgNightTemp, proj2050AvgNightTemp, yearsRemaining, 1);
    rainfall = interpolate(y0Rainfall, proj2050rainfall, yearsRemaining, 1);
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

  public float getProj2050MaxAnnualTemp()
  {
    return proj2050MaxAnnualTemp;
  }

  public void setProj2050MaxAnnualTemp(float proj2050MaxAnnualTemp)
  {
    this.proj2050MaxAnnualTemp = proj2050MaxAnnualTemp;
  }

  public float getProj2050MinAnnualTemp()
  {
    return proj2050MinAnnualTemp;
  }

  public void setProj2050MinAnnualTemp(float proj2050MinAnnualTemp)
  {
    this.proj2050MinAnnualTemp = proj2050MinAnnualTemp;
  }

  public float getProj2050AvgDayTemp()
  {
    return proj2050AvgDayTemp;
  }

  public void setProj2050AvgDayTemp(float proj2050AvgDayTemp)
  {
    this.proj2050AvgDayTemp = proj2050AvgDayTemp;
  }

  public float getProj2050AvgNightTemp()
  {
    return proj2050AvgNightTemp;
  }

  public void setProj2050AvgNightTemp(float proj2050AvgNightTemp)
  {
    this.proj2050AvgNightTemp = proj2050AvgNightTemp;
  }

  public float getProj2050Rainfall()
  {
    return proj2050rainfall;
  }

  public void setProj2050Rainfall(float proj2050rainfall)
  {
    this.proj2050rainfall = proj2050rainfall;
  }

  protected void setCurrCrop(EnumFood crop)
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
  }

  /**
   * Rate tile's suitability for a particular country's  other crops.
   * @param otherCropsData  a country's otherCropsData object
   * @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   */
  public EnumCropZone rateTileForOtherCrops(CropZoneData otherCropsData)
  {
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
    return "LandTile{" + center +
            ", current crop =" + currCrop +
            '}';

//    return "LandTile{" +
//      "center=" + center +
//      ", Rainfall=" + rainfall +
//      ", avgNightTemp=" + avgNightTemp +
//      ", avgDayTemp=" + avgDayTemp +
//      ", minAnnualTemp=" + minAnnualTemp +
//      ", maxAnnualTemp=" + maxAnnualTemp +
//      ", Elevation=" + Elevation +
//    ", current crop =" + currCrop +
//      '}';
  }

  public String toDetailedString()
  {
    return "LandTile{" +
      "center=" + center +
      ", Rainfall=" + rainfall +
      ", avgNightTemp=" + avgNightTemp +
      ", avgDayTemp=" + avgDayTemp +
      ", minAnnualTemp=" + minAnnualTemp +
      ", maxAnnualTemp=" + maxAnnualTemp +
      ", Elevation=" + elevation +
    ", current crop =" + currCrop +
      '}';
  }
}
