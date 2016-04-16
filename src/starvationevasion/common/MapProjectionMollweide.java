package starvationevasion.common;


import java.awt.*;

public class MapProjectionMollweide
{
  private static final double DEG_TO_RAD = Math.PI / 180.0;
  private static final double ROOT2 = Math.sqrt(2.0);
  private static final double CX = 2.0*ROOT2/Math.PI;
  private static final double TOLERANCE = 0.0001;
  private double R;


  private int pixelWidth;
  private int pixelHeight;

  private double scaleX;
  private double scaleY;

  public MapProjectionMollweide(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth  = pixelWidth;
    this.pixelHeight = pixelHeight;
    scaleX = (pixelWidth / 2.0) / (2*ROOT2);
    scaleY = (pixelHeight / 2.0) / ROOT2;
  }

  public void setPoint(Point pixel, MapPoint mapPoint)
  {
    double lon = (mapPoint.longitude * DEG_TO_RAD);//convert to radians
    double lat = (mapPoint.latitude * DEG_TO_RAD);

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

    double x = CX*lon*Math.cos(theta);
    double y = ROOT2*Math.sin(theta);
    pixel.x = pixelWidth/2  +(int)(scaleX * x);
    pixel.y = pixelHeight/2 -(int)(scaleY * y);
  }
}
