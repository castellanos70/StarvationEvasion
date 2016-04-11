package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.awt.*;
import java.util.List;



/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class GeographicArea implements Sendable
{
  public final static MapConverter mapConverter = new MapConverter();
  private List<MapPoint> perimeter;
  private final String name;

  private Polygon mapSpacePoly;

  public GeographicArea(String name)
  { this.name = name;
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


  public Type getType()
  {
    return Type.AREA;
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

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = JSONDocument.createObject();
    json.setString("name", name);
    JSONDocument jsonPerim = JSONDocument.createArray(perimeter.size());
    int i =0;
    for (MapPoint mapPoint : perimeter)
    {
      jsonPerim.set(i, mapPoint.toJSON());
      i++;
    }
    json.set("perimeter", jsonPerim);

    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }
}
