package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;
import starvationevasion.sim.Territory;
import starvationevasion.util.Picture;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class GeographicArea implements Sendable
{
  private final String name;

  public enum BoundaryType
  {
    ISLAND, HOLE
  }

  ;

  /**
   * boundaryList is an ArrayList of an ArrayList of MapPoints.<br>.
   * Each ArrayList of MapPoints is the perimeter of <b><i>one continuous segment</i></b> of
   * the GeographicArea. <br><br>
   * <p>
   * For example, a GeographicArea consisting of six islands would be represented as a
   * boundaryList of six array lists where each array list is the perimeter of one of
   * one of the islands.
   */
  //private ArrayList<ArrayList> islandList = new ArrayList<>();

  private Area perimeter = new Area();

  public GeographicArea(String name)
  {
    this.name = name;
    //System.out.println("++++++++++++++++++++++++GeographicArea("+name+")");
  }


  /**
   * This method converts an ordered set of MapPoints to a closed java.awt.geom.Path2D.
   * The path is then added to adds that path to a java.awt.geom.Area. <br><br>
   * <p>
   * java.awt.geom.Path2D expects points to be 2D cartesian coordinates; however
   * this method adds untransformed latitude and longitude points. While this creates a distorted space
   * that cannot correctly measure distances, the methods we need contains() and perimeter
   * calculation, will work with the untransformed latitude and longitude coordinates.
   *
   * @param island
   */
  public void addToPerimeter(ArrayList<MapPoint> island, BoundaryType type)
  {

    //islandList.add(island);

    Path2D.Float shape = new Path2D.Float();

    MapPoint firstPoint = island.get(0);
    double firstX = firstPoint.longitude;
    double firstY = firstPoint.latitude;
    shape.moveTo(firstX, firstY);

    for (int i = 1; i < island.size(); i++)
    {
      MapPoint mapPoint = island.get(i);
      shape.lineTo(mapPoint.longitude, mapPoint.latitude);
    }
    shape.lineTo(firstX, firstY);

    Area area = new Area(shape);
    //assert(area.isSingular());

    //if (!area.isSingular())
    //{
    //  System.out.println("ERROR: GeographicArea.addToPerimeter() Not a simple polygon: "+ name +
    //    " ["+islandList.size()+"]");
    //
    //}

    if (type == BoundaryType.ISLAND) { perimeter.add(area); }
    else { perimeter.subtract(area); }
  }

  //public ArrayList<ArrayList>getIslandList() {return islandList;}


  public boolean contains(MapPoint mapPoint)
  {
    return contains(mapPoint.latitude, mapPoint.longitude);
  }

  public boolean contains(double latitude, double longitude)
  {
    return perimeter.contains(longitude, latitude);
  }


  public String getName()
  {
    return name;
  }


  public Type getType()
  {
    return Type.AREA;
  }

  public Area getPerimeter()
  {
    return perimeter;
  }

  public String toString()
  {
    return "GeographicArea{" +
      "name='" + name + '\'' +
      '}';
  }


  @Override
  public JSONDocument toJSON()
  {
    JSONDocument json = JSONDocument.createObject();
    json.setString("name", name);
//    JSONDocument jsonPerim = JSONDocument.createArray(perimeter.);
//    int i =0;
//    for (MapPoint mapPoint : perimeterMapPoints)
//    {
//      jsonPerim.set(i, mapPoint.toJSON());
//      i++;
//    }
//    json.set("perimeter", jsonPerim);

    return json;
  }

  @Override
  public void fromJSON(Object doc)
  {

  }
}
