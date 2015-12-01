package starvationevasion.io;

import org.xml.sax.SAXException;
import starvationevasion.sim.GeographicArea;
import starvationevasion.sim.util.EquirectangularConverter;
import starvationevasion.sim.util.MapConverter;
import starvationevasion.common.MapPoint;

import java.awt.geom.Area;

/**
 * Created by winston on 1/25/15.
 * Phase_01 Utility class to validate regions at load time.
 * CS 351 spring 2015
 *
 */
public class GeographyValidator
{
  private static final  MapConverter CONVERTER = new EquirectangularConverter();

  public boolean validate(GeographicArea region) throws SAXException
  {
    for (MapPoint mp : region.getPerimeter())
    {
      if (! isValidMapPoint(mp) ) throw new SAXException("Invalid Map Point.");
    }

    // check to make sure all region polygons are simple.
    Area area = new Area(CONVERTER.regionToPolygon(region));
    boolean isSingular = area.isSingular();

    if (!isSingular)
    {
      throw new SAXException("Invalid GeographicArea shape");
    }

    return true;
  }


  private boolean isValidMapPoint(MapPoint mapPoint)
  {

    return Math.abs(mapPoint.latitude) <= 90.00 &&
           Math.abs(mapPoint.longitude) <= 180.00;

  }
}
