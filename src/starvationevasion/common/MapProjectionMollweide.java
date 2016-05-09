package starvationevasion.common;


//import javafx.scene.shape.Shape;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class MapProjectionMollweide
{
  private static final double DEG_TO_RAD = Math.PI / 180.0;
  private static final double ROOT2 = Math.sqrt(2.0);
  private static final double CX = 2.0*ROOT2/Math.PI;
  private static final double TOLERANCE = 0.0001;

  private Area[] regionPerimetersSpherical;

  private int pixelWidth;
  private int pixelHeight;
  private double centralMeridian = 0;

  private double scaleX;
  private double scaleY;


  public MapProjectionMollweide(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth  = pixelWidth;
    this.pixelHeight = pixelHeight;
    scaleX = (pixelWidth / 2.0) / (2*ROOT2);
    scaleY = (pixelHeight / 2.0) / ROOT2;
  }



  public void setCentralMeridian(double degrees)
  {
    if (Math.abs(degrees) > 180.0)
    {
      throw new IllegalArgumentException(
        "MapProjectionMollweide.setCentralMeridian("+degrees +
        "): Argument out of bounds error.");
    }
    centralMeridian = degrees*DEG_TO_RAD;
  }

  public void setRegionPerimetersSpherical(Area[] regionPerimeters)
  {
    regionPerimetersSpherical = regionPerimeters;
  }


  /**
   * @param regionID
   * @return
   */
  //public Shape getPerimeterDrawable(EnumRegion regionID)
  public Area getPerimeterDrawable(EnumRegion regionID)
  {
    //default region (when regionID == null) is Antarctica's ice shelf
    //   stored at the end of the regionPerimetersSpherical[] array.
    Area perimeter = regionPerimetersSpherical[regionPerimetersSpherical.length -1];
    if (regionID != null)
    {
      perimeter = regionPerimetersSpherical[regionID.ordinal()];
    }

    double edgeLongitude1 = centralMeridian - Math.PI;
    double edgeLongitude2 = centralMeridian + Math.PI;

    if (edgeLongitude1 < -Math.PI) edgeLongitude1 = 2.0*Math.PI + edgeLongitude1;
    if (edgeLongitude2 >  Math.PI) edgeLongitude2 =-2.0*Math.PI + edgeLongitude2;

    Area[] perimeterList = null;

    if (perimeter.intersects(edgeLongitude2-0.01, -Math.PI/2, 0.25, Math.PI))
    {
      perimeterList = new Area[2];
      perimeterList[0] = new Area(perimeter);
      perimeterList[1] = new Area(perimeter);
      Area eastArea = null;
      double x2 = edgeLongitude1+Math.PI;
      if (x2>Math.PI)
      {
        x2 = Math.PI;
        double x3 = -Math.PI;

        Shape east1 = new Rectangle2D.Double(edgeLongitude1, -Math.PI/2, Math.PI-edgeLongitude1, Math.PI);
        Shape east2 = new Rectangle2D.Double(-Math.PI, -Math.PI/2, centralMeridian+Math.PI, Math.PI);
        eastArea = new Area(east1);
        eastArea.add(new Area(east2));
      }
      else if (
        perimeterList[0].intersect(eastArea);
    }
    else
    {
      perimeterList = new Area[1];
      perimeterList[0] = perimeter;
    }

    Area drawBoundary = new Area();

    Point pixel = new Point();
    double[] coords = new double[6];
    Path2D.Float shape = null;

    PathIterator path = perimeter.getPathIterator(null);
    while(!path.isDone())
    {
      int type = path.currentSegment(coords);
      path.next();
      setPoint(pixel, coords[1], coords[0]);
      if (type == PathIterator.SEG_LINETO)
      {
        shape.lineTo(pixel.x, pixel.y);
      }
      else if(type == PathIterator.SEG_MOVETO)
      {
        shape = new Path2D.Float();
        shape.moveTo(pixel.x, pixel.y);
      }
      else if(type == PathIterator.SEG_CLOSE)
      {
        shape.lineTo(pixel.x, pixel.y);
        Area area = new Area(shape);
        drawBoundary.add(area);
      }
      else
      {
        System.out.println("************ ERROR ***********");
      }
    }

    return drawBoundary;
  }

  /**
   *  * <li> Latitude</li>
   * <li> </li>
    *@param pixel This is output. By making it an argument, rather than a returned value, the
   *              same memoryu
   * @param latitude  ranges from -90 to 90. North latitude is positive.
   * @param longitude ranges from -180 to 180. East longitude is positive.
   */
  public void setPoint(Point pixel, double latitude, double longitude)
  {
    double lon = (longitude * DEG_TO_RAD);//convert to radians
    double lat = (latitude * DEG_TO_RAD);

    //To avoid division by zero at the poles, set theta0 equal +- pi/2.
    double theta0 = Math.PI;
    if (lat < 0.0) theta0 = -Math.PI;
    double theta = lat;

    int count = 0;
    //A closed form expression for theta has not yet been found.
    //For mid latitudes, this converges in about 10 iterations. It takes longer near the poles.
    while (Math.abs(theta0 - theta) > TOLERANCE)
    for (int i=0; i<10; i++)
    {
      count++;
      theta0 = theta;
      double a = 2.0*theta + Math.sin(2.0*theta) - Math.PI*Math.sin(lat);
      double b = 2.0 + 2.0*Math.cos(2.0*theta);
      theta = theta - a/b;
    }
    //System.out.println("count="+count);

    double lon2 = lon - centralMeridian;
    if (lon2 > Math.PI) lon2 = -Math.PI + (lon2 - Math.PI);
    else if (lon2 < -Math.PI) lon2 = Math.PI + (lon2 + Math.PI);

    double x = CX*lon2*Math.cos(theta);
    double y = ROOT2*Math.sin(theta);
    pixel.x = pixelWidth/2  +(int)(scaleX * x);
    pixel.y = pixelHeight/2 -(int)(scaleY * y);
  }
}
