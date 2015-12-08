package starvationevasion.common;

/**
 * Created by Anand on 12/7/2015.
 */
public class LandTile {

  public float elevation = 0;     /* in meters above sea level */
  public float maxAnnualTemp = 0; /* in degrees Celsius. */
  public float minAnnualTemp = 0; /* in degrees Celsius. */
  public float avgDayTemp = 0;    /* in degrees Celsius. */
  public float avgNightTemp = 0;  /* in degrees Celsius. */
  public float rainfall = 0;      /* in cm */

  public MapPoint center;
  public EnumFood currCrop;

  public LandTile(double lon, double lat)
  {
    center = new MapPoint(lat, lon);
  }
}
