package starvationevasion.util;

import starvationevasion.geography.MapPoint;
import starvationevasion.geography.GeographicArea;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 *
 * class is used to convert between the spring2015code.model and the GUI
 * for converting between lat lon =>  x, y and back.
 */
public abstract class MapConverter
{

  /**
   * Transforms the given GeographicArea object into a polygon suitable for drawing.
   * @param r region object to be transformed.
   * @return polygon representing the region in the appropriate map projection.
   */
  public abstract Polygon regionToPolygon(GeographicArea r);


  /**
   * Factor that the map points are scaled by.
   * @return double representing the conversion scaled between
   * spring2015code.gui space and map.modle space.
   */
  public abstract double getScale();


  /**
   * Converts a latitude point to a y value.
   * @param lat latitude measurement to be converted.
   * @return double y, in spring2015code.gui/Cartesian space.
   */
  public abstract double latToY(double lat);

  /**
   * Converts a longitude point to a y value.
   * @param lon longitude measurement to be converted.
   * @return double x, in spring2015code.gui/Cartesian space.
   */
  public abstract double lonToX(double lon);

  /**
   * Converts a point on a map given in latitude and longitude, and transforms
   * it into an x,y point on a Cartesian system, suitable for drawing.
   * @param mp point defined in map space.
   * @return converted point.
   */
  public abstract Point mapPointToPoint(MapPoint mp);

  /**
   * in the inverse function of mapPointToPoint. Given a point in Cartesian
   * space, returns a point in the map space.
   * @param p point defined in spring2015code.gui space
   * @return converted point.
   */
  public abstract MapPoint pointToMapPoint(Point2D p);

  /**
   * Returns a conventional grid in latitude and longitude as defined by the
   * converter.
   *
   * @return list of line2d objects representing a grid.
   */
  public abstract java.util.List<Line2D> getLatLonGrid();

  /**
   Returns the dimensions of the projection of this converter.  This really only
   makes sense for rectangular converters; Use accordingly.
   @return  projection dimensions
   */
  public abstract Dimension2D getProjectionDimensions();


  /**
   Returns the width of the projection of this converter.  The implementation of
   this may not be what is expected if the projection is non-rectangular.
   @return  width of the projection
   */
  public abstract double getWidth();


  /**
   Returns the height of the projection of this converter.  The implementation of
   this may not be what is expected if the projection is non-rectangular.
   @return  height of the projection
   */
  public abstract double getHeight();
}
