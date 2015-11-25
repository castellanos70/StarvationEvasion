package spring2015code.util;

import spring2015code.model.MapPoint;
import spring2015code.model.geography.GeographicArea;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 @author david
 created: 2015-01-26
 <p/>
 description:  MapConverter implementation for Equirectangular map
 projections with a constant scaling factor */
public class EquirectangularConverter extends MapConverter
{

  private static final MapPoint DEFAULT_REF = new MapPoint(0, 0);
  private static final double SCALING_FACTOR = 10;
  
  public static final double PROJECTION_HEIGHT = 180 * SCALING_FACTOR;
  public static final double PROJECTION_WIDTH = 360 * SCALING_FACTOR;
  public static final Dimension2D DIMENSION = new Dimension2D()
  {
    @Override
    public double getWidth()
    {
      return PROJECTION_WIDTH;
    }

    @Override
    public double getHeight()
    {
      return PROJECTION_HEIGHT;
    }

    @Override
    public void setSize(double width, double height)
    { 
      throw new UnsupportedOperationException("setSize not supported");
    }
  };


  /**
   Convert latitude to graphics Y given a point of reference

   @param lat
   @param refPoint

   @return the latitude, scaled and projected in Y
   */
  public double latToY(double lat, MapPoint refPoint)
  {
    return -lat * SCALING_FACTOR; /* silly, but keeps API consistent */
  }


  /**
   Convert latitude to graphics Y, assuming (0,0) is the point of reference
   in the spherical coord system

   @param lat

   @return the latitude, scaled and projected in Y
   */
  @Override
  public double latToY(double lat)
  {
    return latToY(lat, DEFAULT_REF);
  }


  /**
   Convert longitude to graphics X, given a reference point in spherical
   coords

   @param lon
   decimal longitude to convert
   @param refPoint
   mapPoint of reference

   @return longitude, scaled and projected in X
   */
  public double lonToX(double lon, MapPoint refPoint)
  {
    return lon * Math.cos(Math.toRadians(refPoint.getLat())) * SCALING_FACTOR;
  }
  
  /**
   Convert longitude to graphics X, assuming a reference point of (0,0) in
   spherical coords

   @param lon
   decimal longitude to convert

   @return longitude, scaled and projected in X
   */
  @Override
  public double lonToX(double lon)
  {
    return lonToX(lon, DEFAULT_REF);
  }


  /**
   Convert a MapPoint (lat, lon) to a graphics-space point, assuming the parallel
   of no distortion is the equator.  This is a Plate-Caree projection.

   @param mp
   MapPoint to convert
   @return a Point in graphics-space
   */
  @Override
  public Point mapPointToPoint(MapPoint mp)
  {
    int x = (int) (lonToX(mp.getLon()));
    int y = (int) (latToY(mp.getLat()));
    return new Point(x, y);
  }

  
  /**
   Convert a Point in graphics-space to a MapPoint assuming the parallel of no 
   distortion is  the equator.  This converts from a Plate-Caree projection back
   to lat and lon
   @return
   A MapPoint, reversing the projection defined by this class
    @param p
    Point to convert
   */
  @Override
  public MapPoint pointToMapPoint(Point2D p)
  {
    return new MapPoint(p.getX() / SCALING_FACTOR, - p.getY() / SCALING_FACTOR);
  }
  

  /**
   Converts a GeographicArea to a Polygon in graphics-space
   @param r region object to be transformed.
   @return a Polygon representing the passed GeographicArea, appropriately scaled and converted
   */
  @Override
  public Polygon regionToPolygon(GeographicArea r)
  {

    Polygon poly = new Polygon();
    for (MapPoint mPoint : r.getPerimeter())
    {
      int x = (int) lonToX(mPoint.getLon());
      int y = (int) latToY(mPoint.getLat());
      poly.addPoint(x, y);
    }

    return poly;
  }

  /**
   @return the factor by which this converter scales coordinates
   */
  @Override
  public double getScale()
  {
    return SCALING_FACTOR;
  }


  /**
   generates a projected grid of latitude and longitude lines converted to
   the scaled graphics space
   @return  A list of Line2Ds representing the Lon Lat grid in graphics-space
   */
  @Override
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

  @Override
  public Dimension2D getProjectionDimensions()
  {
    return DIMENSION;
  }

  @Override
  public double getWidth()
  {
    return PROJECTION_WIDTH;
  }

  @Override
  public double getHeight()
  {
    return PROJECTION_HEIGHT;
  }
}
