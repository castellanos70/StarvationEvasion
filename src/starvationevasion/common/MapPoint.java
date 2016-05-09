package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;
import starvationevasion.sim.Simulator;

/**
 * Simple data class for specifying a location on the Earth's Surface.
 * The ISO 6709 Standard representation of geographic point location by coordinates is the international
 * standard for representation of latitude, longitude and altitude for geographic point locations.<br>
 * The ISO 6709 Standard follows the conventions:
 * <ol>
 * <li> Latitude comes before longitude (Latitude, Longitude).</li>
 * <li> Latitude ranges from -90 to 90. North latitude is positive.</li>
 * <li> Longitude ranges from -180 to 180. East longitude is positive.</li>
 * <li> Fraction of degrees is preferred in digital data exchange</li>
 * </ol>
 */
public class MapPoint implements Sendable
{
  /**
   * Specifies the north-south position of a point on the Earth's surface.
   * Latitude is an angle in degrees which ranges from 0 degrees at the Equator to +-90 degrees
   * (+North to-South) at the poles.
   * Lines of constant latitude, or parallels, run east-west as circles parallel to the equator.
   */
  public float latitude;

  /**
   * Specifies the east-west position of a point on the Earth's surface.
   * It is an angular measurement expressed in degrees.
   * The 0 degrees longitude line (Prime Meridian), passes through the Royal Observatory,
   * Greenwich, England.
   * The longitude of other places as the angle east or west from the Prime Meridian, ranging
   * from 0 degrees at the Prime Meridian to +180 degrees eastward and ?180 degrees westward.
   * Points with the same longitude lie in lines running from the North Pole to the South Pole.
   */
  public float longitude;

  public MapPoint(float latitude, float longitude)
  {
    if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180)
    {
      throw new IllegalArgumentException("MapPoint("+latitude+", " + longitude +
        "): Argument out of bounds error.");
    }
    this.latitude = latitude;
    this.longitude = longitude;
  }


  @Override
  public String toString()
  {
    return String.format("Location{%.3f, %.3f}", latitude, longitude);
  }

  @Override
  public JSONDocument toJSON()
  {
    JSONDocument json = JSONDocument.createObject();

    json.setNumber("latitude", latitude);
    json.setNumber("longitude", longitude);


    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(doc);
    latitude = (float) json.getNumber("latitude");
    longitude = (float) json.getNumber("longitude");
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
    { return true; }
    if (!(o instanceof MapPoint))
    { return false; }
    MapPoint map = (MapPoint) o;
    if (Double.compare(map.longitude, this.longitude) != 0)
    { return false; }
    if (Double.compare(map.latitude, this.latitude) != 0)
    { return false; }
    return true;
  }


  /**
   *
   * Test example that starts the simulator then asks of lat, long points are within a region
   */
  public static void main(String[] args)
  {
    Simulator sim = new Simulator();


    System.out.println("MatPoint.main(): Testing Map points:" );
    System.out.println("   Albuquerque(35,-106): " + sim.getRegion(35,-106));
    System.out.println("   Beijing(40,116): " + sim.getRegion(40,116));
    System.out.println("   Belfast, Northern Ireland(54.5970, -5.93): " + sim.getRegion(54.5970f, -5.93f));
    System.out.println("   Irish Sea(53.347309, -5.681383): " + sim.getRegion(53.347309f, -5.681383f));
    System.out.println("   English Channel(50.39, -1.7): " + sim.getRegion(50.39f, -1.7f));
    System.out.println("   Greenland(75.833995, -43.088791): " + sim.getRegion(75.833995f, -43.088791f));
    System.out.println("   Reykjavik, Iceland(64.107676, -21.812973): " + sim.getRegion(64.107676f, -21.812973f));
    System.out.println("   Antarctica(-84, 51): " + sim.getRegion(-84, 51));
  }

  @Override
  public Type getType ()
  {
    return null;
  }
}

