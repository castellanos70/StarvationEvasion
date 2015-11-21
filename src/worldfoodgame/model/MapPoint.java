package worldfoodgame.model;


import java.awt.geom.Point2D;

/**
 * Data class representing long and lat on a map.
 */
public class MapPoint
{
  private double lat;
  private double lon;

  /**
   Construct a new MapPoint with given longitude and latitude values
   
   @param lon   Longitude of coord
   @param lat   Latitude of coord
   */
  public MapPoint(double lon, double lat)
  {
    this.lat = lat;
    this.lon = lon;
  }

  /**
   Construct a new MapPoint given another MapPoint as a reference
   @param source  MapPoint to copy
   */
  public MapPoint(MapPoint source)
  {
    this.lat = source.lat;
    this.lon = source.lon;
  }

  public double getLat()
  {
    return lat;
  }

  public void setLat(double lat)
  {
    this.lat = lat;
  }

  public double getLon()
  {
    return lon;
  }

  public void setLon(double lon)
  {
    this.lon = lon;
  }


  /**
   Returns the *Euclidean* distance between this point and another MapPoint
   assuming a rectangular coordinate system.
   
   @param p MapPoint to calculated distance to
   @return  distance to MapPoint p
   */
  public double distance(MapPoint p)
  {
    return Point2D.distance(p.lon, p.lat, lon, lat);
  }

  /**
   Returns the *Euclidean* distance squared between this point and another MapPoint
   assuming a rectangular coordinate system.

   @param p MapPoint to calculated squared distance to
   @return  squared distance to MapPoint p
   */
  public double distanceSq(MapPoint p)
  {
    return Point2D.distanceSq(p.lon, p.lat, lon, lat);
  }

  @Override
  public String toString()
  {
    return "MapPoint{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
  }
}
