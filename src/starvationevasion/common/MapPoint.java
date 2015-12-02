package starvationevasion.common;

import java.io.Serializable;

/**
 * Simple data class for specifying a location on the Earth's Surface.
 */
public class MapPoint implements Serializable
{
  /**
   * Specifies the north-south position of a point on the Earth's surface.
   * Latitude is an angle in degrees which ranges from 0 degrees at the Equator to +-90 degrees
   * (+North to-South) at the poles.
   * Lines of constant latitude, or parallels, run east-west as circles parallel to the equator.
   */
  public double latitude;

  /**
   * Specifies the east-west position of a point on the Earth's surface.
   * It is an angular measurement expressed in degrees.
   * The 0 degrees longitude line (Prime Meridian), passes through the Royal Observatory,
   * Greenwich, England.
   * The longitude of other places as the angle east or west from the Prime Meridian, ranging
   * from 0 degrees at the Prime Meridian to +180 degrees eastward and ?180 degrees westward.
   * Points with the same longitude lie in lines running from the North Pole to the South Pole.
   */
  public double longitude;

  public MapPoint(double latitude, double longitude)
  {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString()
  {
    return String.format("Location{%.2f, %.2f}", latitude, longitude);
  }
}
