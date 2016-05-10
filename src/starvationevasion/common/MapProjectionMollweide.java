package starvationevasion.common;


import javafx.scene.shape.Polygon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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
  public Polygon getPerimeterDrawable(EnumRegion regionID)
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

    ArrayList<Area> perimeterList = new ArrayList<>();

    //System.out.printf("edge=%.1f  %s%n",edgeLongitude, regionID);

    if (perimeter.intersects(edgeLongitude - 0.05,-90, 0.1,180))
    {
      //System.out.println(regionID + "     crosses edge");

      Shape shapeEast = new Rectangle2D.Double(centralMeridian, -90, 179.9, 180);
      Area areaEast = new Area(shapeEast);
      areaEast.intersect(perimeter);
      perimeterList.add(areaEast);

      if (centralMeridian > 0)
      {
        shapeEast = new Rectangle2D.Double(-180, -90, centralMeridian-0.1, 180);
        areaEast = new Area(shapeEast);
        areaEast.intersect(perimeter);
        perimeterList.add(areaEast);
      }


      Shape shapeWest = new Rectangle2D.Double(edgeLongitude+0.1, -90, 179.9, 180);
      Area areaWest = new Area(shapeWest);
      areaWest.intersect(perimeter);
      perimeterList.add(areaWest);
    }
    else
    {
      perimeterList.add(perimeter);
    }


    Polygon drawBoundary = new Polygon();

    Point pixel = new Point();
    double[] coords = new double[6];
    ArrayList<Double> vertexList = new ArrayList<>();

    double lastLatitude = 0;
    double lastLongitude = 0;
    for (Area segment : perimeterList)
    {
      PathIterator path = segment.getPathIterator(null);

      while(!path.isDone())
      {
        int type = path.currentSegment(coords);
        path.next();

        if (type == PathIterator.SEG_LINETO)
        {
          curveTo(vertexList, pixel, lastLatitude, lastLongitude, coords[1], coords[0]);
        }
        else if(type == PathIterator.SEG_MOVETO)
        {
          setPoint(pixel, coords[1], coords[0]);
          vertexList.clear();
          vertexList.add(new Double(pixel.x));
          vertexList.add(new Double(pixel.y));
        }
        else if(type == PathIterator.SEG_CLOSE)
        {
          curveTo(vertexList, pixel, lastLatitude, lastLongitude, coords[1], coords[0]);
          double[] vertexArray = new double[vertexList.size()];
          for (int i=0; i<vertexArray.length; i++)
          {
            vertexArray[i] = vertexList.get(i).doubleValue();
          }
          Polygon shape = new Polygon(vertexArray);
          drawBoundary.union(drawBoundary, shape);
        }
        else
        {
          System.out.println("************ ERROR ***********");
        }
        lastLatitude = coords[1];
        lastLongitude = coords[0];
      }
    }

    return drawBoundary;
  }


  private void curveTo(ArrayList<Double> vertexList, Point pixel,
                       double startLatitude, double startLongitude,
                       double endLatitude, double endLongitude)
  {
    if (Math.abs(endLatitude - startLatitude) > 1.5) //degrees
    {
      int numSteps = (int) Math.abs(endLatitude - startLatitude);
      double deltaLatitude  = (endLatitude  - startLatitude)/numSteps;
      double deltaLongitude = (endLongitude - startLongitude)/numSteps;

      for (int i=0; i<numSteps; i++)
      {
        startLatitude  = startLatitude + deltaLatitude;
        startLongitude = startLongitude + deltaLongitude;
        setPoint(pixel, startLatitude, startLongitude);
        vertexList.add(new Double(pixel.x));
        vertexList.add(new Double(pixel.y));
      }
    }
    setPoint(pixel, endLatitude, endLongitude);
    vertexList.add(new Double(pixel.x));
    vertexList.add(new Double(pixel.y));
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
