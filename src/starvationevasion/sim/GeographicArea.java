package starvationevasion.sim;


import starvationevasion.sim.util.EquirectangularConverter;
import starvationevasion.sim.util.MapConverter;
import starvationevasion.common.MapPoint;

import java.awt.*;
import java.util.List;



/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class GeographicArea
{
  //todo someday make the mapconverter a singleton class.
  public final static MapConverter mapConverter = new EquirectangularConverter();
  private Territory agriculturalUnit;
  private List<MapPoint> perimeter;
  private String name;
  private String type;

  private Polygon mapSpacePoly;

  public AbstractAgriculturalUnit getAgriculturalUnit()
  {
    return agriculturalUnit;
  }

  public void setAgriculturalUnit(Territory agriculturalUnit)
  {
    this.agriculturalUnit = agriculturalUnit;
    agriculturalUnit.addRegion(this);
  }

  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (mapSpacePoly == null) mapSpacePoly = mapConverter.regionToPolygon(this);

    Point point = mapConverter.mapPointToPoint(mapPoint);
    return mapSpacePoly.contains(point);
  }

  public String getName()
  {
    return name;
  }


  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public List<MapPoint> getPerimeter()
  {
    return perimeter;
  }

  public void setPerimeter(List<MapPoint> perimeter)
  {
    this.perimeter = perimeter;
  }


  public String toString()
  {
    return "GeographicArea{" +
      "name='" + name + '\'' +
      '}';
  }
}
