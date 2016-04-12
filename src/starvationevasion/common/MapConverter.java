package starvationevasion.common;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * class is used to convert between the spring2015code.model and the starvationevasion.client.Aegislash.GUI
 * for converting between lat lon to   x, y and back.
 */
public class MapConverter
{
  private static final MapPoint DEFAULT_REF = new MapPoint(0, 0);
  private static final double SCALING_FACTOR = 10;

  public static final double PROJECTION_HEIGHT = 180 * SCALING_FACTOR;
  public static final double PROJECTION_WIDTH = 360 * SCALING_FACTOR;


  /**
   * Convert latitude to graphics Y given a point of reference
   *
   * @param lat
   * @param refPoint
   * @return the latitude, scaled and projected in Y
   */
  public double latToY(double lat, MapPoint refPoint)
  {
    return -lat * SCALING_FACTOR; /* silly, but keeps API consistent */
  }


  /**
   * Convert latitude to graphics Y, assuming (0,0) is the point of reference
   * in the spherical coord system
   *
   * @param lat
   * @return the latitude, scaled and projected in Y
   */
  public double latToY(double lat)
  {
    return latToY(lat, DEFAULT_REF);
  }


  /**
   * Convert longitude to graphics X, given a reference point in spherical
   * coords
   *
   * @param lon      decimal longitude to convert
   * @param refPoint mapPoint of reference
   * @return longitude, scaled and projected in X
   */
  public double lonToX(double lon, MapPoint refPoint)
  {
    return lon * Math.cos(Math.toRadians(refPoint.latitude)) * SCALING_FACTOR;
  }

  /**
   * Convert longitude to graphics X, assuming a reference point of (0,0) in
   * spherical coords.
   *
   * @param lon decimal longitude to convert
   * @return longitude, scaled and projected in X
   */
  public double lonToX(double lon)
  {
    return lonToX(lon, DEFAULT_REF);
  }


  /**
   * Convert a MapPoint (lat, lon) to a graphics-space point, assuming the parallel
   * of no distortion is the equator.  This is a Plate-Caree projection.
   *
   * @param mp MapPoint to convert
   * @return a Point in graphics-space
   */
  public Point mapPointToPoint(MapPoint mp)
  {
    int x = (int) (lonToX(mp.longitude));
    int y = (int) (latToY(mp.latitude));
    return new Point(x, y);
  }


  /**
   * Convert a Point in graphics-space to a MapPoint assuming the parallel of no
   * distortion is  the equator.  This converts from a Plate-Caree projection back
   * to lat and lon
   *
   * @param p Point to convert
   * @return A MapPoint, reversing the projection defined by this class
   */
  public MapPoint pointToMapPoint(Point2D p)
  {
    // Y is latitude, X is longitude.
    //
    return new MapPoint(-p.getY() / SCALING_FACTOR, p.getX() / SCALING_FACTOR);
  }


  /**
   * Converts a GeographicArea to a Polygon in graphics-space
   *
   * @param r region object to be transformed.
   * @return a Polygon representing the passed GeographicArea, appropriately scaled and converted
   */
  public Polygon regionToPolygon(GeographicArea r)
  {

    Polygon poly = new Polygon();
    for (MapPoint mPoint : r.getPerimeter())
    {
      int x = (int) lonToX(mPoint.longitude);
      int y = (int) latToY(mPoint.latitude);
      poly.addPoint(x, y);
    }

    return poly;
  }

  /**
   * @return the factor by which this converter scales coordinates
   */
  public double getScale()
  {
    return SCALING_FACTOR;
  }


  /**
   * generates a projected grid of latitude and longitude lines converted to
   * the scaled graphics space
   *
   * @return A list of Line2Ds representing the Lon Lat grid in graphics-space
   */
  public List<Line2D> getLatLonGrid()
  {

    List<Line2D> lines = new ArrayList<>();
    int maxLat = 90;
    int maxLon = 180;
    int inc = 5;

    for (int lon = -maxLon; lon <= maxLon; lon += inc)
    {
      double x = lonToX(lon);
      double y = latToY(maxLat);
      Line2D l = new Line2D.Double(x, y, x, -y);
      lines.add(l);
    }
    for (int lat = -maxLat; lat <= maxLat; lat += inc)
    {
      double y = latToY(lat);
      double x = lonToX(maxLon);
      Line2D.Double l = new Line2D.Double(x, y, -x, y);
      lines.add(l);
    }
    return lines;
  }

  /**
   * Takes a coordinate point and converts it to an (x,y) point on a Mollweide projection.
   *
   * @param coordinate  - Point.Double(latitude, longitude)
   * @param imageWidth  - Width of the Mollweide map
   * @param imageHeight - Height of the Mollweide map
   * @return A Point.Double of the (x, y) position on the Mollweide map
   */
  public Point.Double mollweideProjection(Point.Double coordinate, double imageWidth, double imageHeight)
  {
    double scaleX = imageWidth / (2 * Math.sqrt(2));
    double scaleY = imageHeight / (2 * Math.sqrt(2));
    double latitude = coordinate.getX();
    double longitude = coordinate.getY();
    double radLon = (longitude * (Math.PI / 180.0));//convert to radians
    double radLat = (latitude * (Math.PI / 180.0));
    double x, y;
    x = (scaleX * (2 * Math.sqrt(2) / Math.PI) * radLon * Math.cos(radLat));
    y = -(scaleY * (Math.sqrt(2) * Math.sin(radLat)));

//    double posX = x + imageWidth / 2;
//    double posY = y + imageHeight / 2;
//    Point.Double newPoint = new Point.Double(posX, posY);
    Point.Double newPoint = new Point.Double(x, y);//could change to posX, posY if not already offset
    return newPoint;
  }

  public double getWidth()
  {
    return PROJECTION_WIDTH;
  }

  public double getHeight()
  {
    return PROJECTION_HEIGHT;
  }
}
