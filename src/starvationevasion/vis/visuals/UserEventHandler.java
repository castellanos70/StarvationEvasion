package starvationevasion.vis.visuals;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.event.Event;
import javafx.scene.input.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import starvationevasion.vis.controller.SimParser;
import starvationevasion.vis.model.Coordinate;


/**
 * Created by Tess Daughton on 11/15/15.
 */
public class UserEventHandler  implements EventHandler
{
  private final SimParser SIM_PARSER = new SimParser();
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  private double anchorX, anchorY;
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;
  private Group earthGroup;
  private Earth earth;
  private Scale earthScale;
  private double LARGE_EARTH_RADIUS;


  public UserEventHandler(Earth earth)
  {
    this.earth = earth;
    this.earthGroup = earth.getEarth();
//    earthScale = new Scale();
//    earthGroup.getTransforms().add(earthScale);
  }

  protected void setLargeEarthRadius(double radius)
  {
    this.LARGE_EARTH_RADIUS = radius;
  }

  protected void earthScroll(MouseDragEvent event)
  {
    Rotate groupXRotate, groupYRotate;
    earthGroup.getTransforms().setAll(
        groupXRotate = new Rotate(0, Rotate.X_AXIS),
        groupYRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    groupXRotate.angleProperty().bind(angleX);
    groupYRotate.angleProperty().bind(angleY);
    anchorX = event.getSceneX();
    anchorY = event.getSceneY();
    anchorAngleX = angleX.get();
    anchorAngleY = angleY.get();
    angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
    angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
  }

  protected void earthZoom(ScrollEvent event)
  {
    /**setTranslate can be used to zoom in and out on the world*/
    double scrollFactor = event.getDeltaY();
    if (scrollFactor > 0)
    {
      if (earthGroup.getScaleZ() < 0.5) return;
      earthGroup.setScaleZ(earthGroup.getScaleZ() * .99);
      earthGroup.setScaleX(earthGroup.getScaleX() * .99);
      earthGroup.setScaleY(earthGroup.getScaleY() * .99);
    } else if (scrollFactor < 0)
    {
      if (earthGroup.getScaleZ() > 2) return;
      earthGroup.setScaleZ(earthGroup.getScaleZ() * 1.01);
      earthGroup.setScaleX(earthGroup.getScaleX() * 1.01);
      earthGroup.setScaleY(earthGroup.getScaleY() * 1.01);
    }
    System.out.println(String.format("deltaX: %.3f", scrollFactor));
  }

  protected void earthZoom(ZoomEvent event)
  {
    double zoomFactor = event.getX();
    if (zoomFactor > 0)
    {
      if (earthGroup.getScaleZ() > 2) return;
      earthGroup.setScaleZ(earthGroup.getScaleZ() * .99);
      earthGroup.setScaleX(earthGroup.getScaleX() * .99);
      earthGroup.setScaleY(earthGroup.getScaleY() * .99);
    } else if (zoomFactor < 0)
    {
      if (earthGroup.getScaleZ() < 0.5) return;
      earthGroup.setScaleZ(earthGroup.getScaleZ() * 1.01);
      earthGroup.setScaleX(earthGroup.getScaleX() * 1.01);
      earthGroup.setScaleY(earthGroup.getScaleY() * 1.01);
    }
    System.out.println(String.format("deltaX: %.3f", zoomFactor));
  }

  protected void latLongHandler(MouseEvent event)
  {

    PickResult pickResult = event.getPickResult();
    Point3D point = pickResult.getIntersectedPoint();

    double x = point.getX();
    double y = point.getY();
    double z = point.getZ();
    double lat = Math.toDegrees(Math.acos(y / LARGE_EARTH_RADIUS) - Math.PI / 2); //theta
    double lon = Math.toDegrees(Math.atan(x / z)); //phi
    if (z > 0) lon += (180 * Math.signum(-lon));
    Coordinate c = new Coordinate(lon, lat);
//      System.out.println(lon + " " + lat + " " + point);
  }

  public void displayEarthInformation(MouseEvent event)
  {

    PickResult pickResult = event.getPickResult();

      /* Pick point on texture to derive lat long from java x y axis */
    Point2D point = pickResult.getIntersectedTexCoord(); //in percentages
    double lat = (point.getY() - 0.5) * -180;
    double lon = (point.getX() - 0.5) * 360;
    String regionName = SIM_PARSER.parse(lat, lon);
  }


  @Override
  public void handle(Event event)
  {
    if (event instanceof MouseDragEvent)
    {
      System.out.println("Drag");
      earthScroll((MouseDragEvent) event);
      event.consume();
    } else if (event instanceof ScrollEvent)
    {
      earth.pauseRotation();
      earthZoom((ScrollEvent) event);
      event.consume();
    } else if (event instanceof ZoomEvent)
    {
      earthZoom((ZoomEvent) event);
    }
    event.consume();
  }
//    else if(event instanceof MouseEvent)
//    {
//      displayEarthInformation((MouseEvent) event);
//      latLongHandler((MouseEvent) event);
//    }
}


