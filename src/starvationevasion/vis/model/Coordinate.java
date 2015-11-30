package starvationevasion.vis.model;

/**
 * Created by Tess Daughton on 11/15/15.
 * It might be good to extend this class. i.e. coordinates for the borders of countries versus coordinates of event sources
 */
public class Coordinate
{
  final double LATITUDE;
  final double LONGITUDE;

  public Coordinate(double lon, double lat)
  {
    LATITUDE = lat;
    LONGITUDE = lon;
  }

  public String toString()
  {
    String lat = Math.abs(LATITUDE) + ((LATITUDE >= 0) ? "W" : "E");
    String lon = Math.abs(LONGITUDE) + ((LONGITUDE >= 0) ? "N" : "S");
    return lon + ", " + lat;
  }
}
