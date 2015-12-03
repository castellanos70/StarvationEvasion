package starvationevasion.vis.controller;

import starvationevasion.common.MapPoint;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Anand on 12/1/2015.
 */
public class test {
  public static void main(String args[]) {
    int scale = 50;
    BufferedImage i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);

    Collection<GeographicArea> modelGeography = new GeographyXMLparser().getGeography();
    for (GeographicArea a : modelGeography) {
      Polygon poly = new Polygon();
      for (MapPoint p : a.getPerimeter())
      {
         int latitude = (int) ((p.latitude - 90) * -1 * scale);
         int longitude = (int) ((p.longitude + 180) * scale);
        poly.addPoint(longitude, latitude);
      }
        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.RED);
        g.drawPolygon(poly);
    }

    try {
      ImageIO.write(i, "PNG", new File("/Users/laurencemirabal/Desktop/test.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Done!");
    g.dispose();

  }
}
