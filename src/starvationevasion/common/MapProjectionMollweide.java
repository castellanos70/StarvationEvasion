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
  private double centralMeridian = 0; //degrees

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
   // centralMeridian = degrees*DEG_TO_RAD;
    centralMeridian = degrees;
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

    double edgeLongitude = centralMeridian + 180;
    if(edgeLongitude  >  180) edgeLongitude = edgeLongitude - 360;

    Area[] perimeterList = null;

    System.out.printf("edge=%.1f  %s%n",edgeLongitude, regionID);

    if (perimeter.intersects(edgeLongitude - 0.05,-90, 0.1, 90))
    {
      System.out.println(regionID + "     crosses edge");
      perimeterList = new Area[2];
      perimeterList[0] = new Area(perimeter);
      perimeterList[1] = new Area(perimeter);
      Area eastArea = null;
      if (edgeLongitude>0)
      {
        Shape east1 = new Rectangle2D.Double(edgeLongitude, -90, 180, 90);
        Shape east2 = new Rectangle2D.Double(-180, -90, 180, 90);
        eastArea = new Area(east1);
        eastArea.add(new Area(east2));
      }
      else
      {
        Shape east1 = new Rectangle2D.Double(edgeLongitude, -90, 180+edgeLongitude, 90);
        Shape east2 = new Rectangle2D.Double(0, -90, 180, 90);
        eastArea = new Area(east1);
        eastArea.add(new Area(east2));
      }

      perimeterList[0].intersect(eastArea);
    }

/*
      Area westArea = null;
      if (centralMeridian+Math.PI>Math.PI)
      {
        Shape west1 = new Rectangle2D.Double(centralMeridian, -Math.PI/2, Math.PI-centralMeridian, Math.PI);
        Shape west2 = new Rectangle2D.Double(-Math.PI, -Math.PI/2, centralMeridian+Math.PI, Math.PI);
        westArea = new Area(west1);
        westArea.add(new Area(west2));
      }
      else if (edgeLongitude1 < 0 && edgeLongitude1 > -Math.PI)
      {
        Shape west1 = new Rectangle2D.Double(edgeLongitude1, -Math.PI/2, Math.PI+edgeLongitude1, Math.PI);
        Shape west2 = new Rectangle2D.Double(0, -Math.PI/2, centralMeridian, Math.PI);
        westArea = new Area(west1);
        westArea.add(new Area(west2));
      }
      else
      {
        Shape west1 = new Rectangle2D.Double(centralMeridian, -Math.PI/2, Math.PI, Math.PI);
        eastArea = new Area(west1);
      }
      perimeterList[1].intersect(westArea);
    }
*/

    else
    {
      perimeterList = new Area[1];
      perimeterList[0] = perimeter;
    }


    Area drawBoundary = new Area();

    Point pixel = new Point();
    double[] coords = new double[6];
    Path2D.Float shape = null;

    for (int i=0; i<perimeterList.length; i++)
    {
      PathIterator path = perimeterList[i].getPathIterator(null);
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

    double lon2 = lon - centralMeridian * DEG_TO_RAD;
    if (lon2 > Math.PI) lon2 = -Math.PI + (lon2 - Math.PI);
    else if (lon2 < -Math.PI) lon2 = Math.PI + (lon2 + Math.PI);

    double x = CX*lon2*Math.cos(theta);
    double y = ROOT2*Math.sin(theta);
    pixel.x = pixelWidth/2  +(int)(scaleX * x);
    pixel.y = pixelHeight/2 -(int)(scaleY * y);
  }
}
