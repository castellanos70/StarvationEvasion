package starvationevasion.common;


import javafx.scene.shape.Shape;

import java.awt.*;
import java.awt.geom.Area;

public class MapProjectionMollweide
{
  private static final double DEG_TO_RAD = Math.PI / 180.0;
  private static final double ROOT2 = Math.sqrt(2.0);
  private static final double CX = 2.0*ROOT2/Math.PI;
  private static final double TOLERANCE = 0.0001;


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
  }


  /**
   * @param regionID
   * @return
   */
  public Shape getPerimeterDrawable(EnumRegion regionID)
  {
return null;
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
