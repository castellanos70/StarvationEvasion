package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class GeographicArea implements Sendable
{
  private static final double SCALING_FACTOR = 10;
  private static final MapPoint DEFAULT_REF = new MapPoint(0, 0);
  private ArrayList<MapPoint> perimeter;
  private final String name;

  private Polygon mapSpacePoly;

  public GeographicArea(String name)
  { this.name = name;
    //System.out.println("++++++++++++++++++++++++GeographicArea("+name+")");
  }

  public Polygon getPolygon()
  {
    if (mapSpacePoly == null) mapSpacePoly = regionToPolygon(this);
    return mapSpacePoly;
  }

  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (mapSpacePoly == null) mapSpacePoly = regionToPolygon(this);

    Point point = mapPointToPoint(mapPoint);
    return mapSpacePoly.contains(point);
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


  public String getName()
  {
    return name;
  }


  public Type getType()
  {
    return Type.AREA;
  }

  public ArrayList<MapPoint> getPerimeter()
  {
    return perimeter;
  }

  public void setPerimeter(ArrayList<MapPoint> perimeter)
  {
    this.perimeter = perimeter;
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
   * Convert longitude to graphics X, assuming a reference point of (0,0) in
   * spherical coords.
   *
   * @param lon decimal longitude to convert
   * @return longitude, scaled and projected in X
   */
  private double lonToX(double lon)
  {
    return lonToX(lon, DEFAULT_REF);
  }

  /**
   * Convert latitude to graphics Y, assuming (0,0) is the point of reference
   * in the spherical coord system
   *
   * @param lat
   * @return the latitude, scaled and projected in Y
   */
  private double latToY(double lat)
  {
    return latToY(lat, DEFAULT_REF);
  }


  /**
   * Convert latitude to graphics Y given a point of reference
   *
   * @param lat
   * @param refPoint
   * @return the latitude, scaled and projected in Y
   */
  private double latToY(double lat, MapPoint refPoint)
  {
    return -lat * SCALING_FACTOR; /* silly, but keeps API consistent */
  }


  /**
   * Convert longitude to graphics X, given a reference point in spherical
   * coords
   *
   * @param lon      decimal longitude to convert
   * @param refPoint mapPoint of reference
   * @return longitude, scaled and projected in X
   */
  private double lonToX(double lon, MapPoint refPoint)
  {
    return lon * Math.cos(Math.toRadians(refPoint.latitude)) * SCALING_FACTOR;
  }



  public String toString()
  {
    return "GeographicArea{" +
      "name='" + name + '\'' +
      '}';
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = JSONDocument.createObject();
    json.setString("name", name);
    JSONDocument jsonPerim = JSONDocument.createArray(perimeter.size());
    int i =0;
    for (MapPoint mapPoint : perimeter)
    {
      jsonPerim.set(i, mapPoint.toJSON());
      i++;
    }
    json.set("perimeter", jsonPerim);

    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }
}
