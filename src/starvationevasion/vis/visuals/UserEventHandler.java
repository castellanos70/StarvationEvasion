package starvationevasion.vis.visuals;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.event.Event;
import javafx.scene.input.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import starvationevasion.vis.controller.SimParser;


/**
 * Created by Tess Daughton on 11/15/15.
 */
public class UserEventHandler  implements EventHandler
{
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  private double anchorX, anchorY;
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;
  private Group earthGroup;
  private Earth earth;
  private Scale earthScale;
  private double LARGE_EARTH_RADIUS;
  private VisualizerLayout visLayout;

  /**
   * UserEventHandler Constructor
   * @param earth An earth object to get the large Earth group from, as well as to access pause/start rotations
   * @param visLayout The layout that this handler is registered to, the handler
   *                  updates information inside this layout on relevant user interaction
   */
  public UserEventHandler(Earth earth, VisualizerLayout visLayout)
  {
    this.earth = earth;
    this.visLayout = visLayout;
    this.earthGroup = earth.getEarth();
//    earthScale = new Scale();
//    earth.getTransforms().add(earthScale);
    Rotate groupXRotate, groupYRotate;
    earthGroup.getTransforms().setAll(
        groupXRotate = new Rotate(0, Rotate.X_AXIS),
        groupYRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    groupXRotate.angleProperty().bind(angleX);
    groupYRotate.angleProperty().bind(angleY);

  }

  /**
   * Used for the Lat/Long handling
   * @param radius    radius of the large Earth specified by client
   */
  protected void setLargeEarthRadius(double radius)
  {
    this.LARGE_EARTH_RADIUS = radius;
  }

  /**
   * Based on anchors that have been set when intially click, start rotating. Inside method there is a variable called
   * scale. Adjusting this double value will cause the rotation to be slower or faster. 1 = normal speed, less than 1
   * means slower, and greater than 1 means faster.
   *
   * @param event Event should contain x and y of scene.
   */
  protected void earthScroll(MouseEvent event)
  {
    double scale = .1; //Adjust this to slow down rotations,
    angleX.set(anchorAngleX - ((anchorY - event.getSceneY()) * scale));
    angleY.set(anchorAngleY + ((anchorX - event.getSceneX()) * scale));
  }

  /**
   * Before starting to rotate, set some anchor points to rotate against
   *
   * @param event Event should contain x and y
   */
  protected void earthStartScroll(MouseEvent event)
  {
    anchorX = event.getSceneX();
    anchorY = event.getSceneY();
    anchorAngleX = angleX.get();
    anchorAngleY = angleY.get();
    angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
    angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
  }

  /**
   * Set the earth scale depending on the zoom direction
   * @param event   User scrolling in event
   */
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

  /**
   * Set the earth scale depending on the zoom direction
   * @param event   User zooming in event (two fingers on touch pad)
   */
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
    Point2D point = pickResult.getIntersectedTexCoord(); //in percentages
    double lat = (point.getY() - 0.5) * -180;
    double lon = (point.getX() - 0.5) * 360;
    visLayout.setLatLong(lat, lon);
  }

  public void displayEarthInformation(MouseEvent event)
  {
    PickResult pickResult = event.getPickResult();

      /* Pick point on texture to derive lat long from java x y axis */
    Point2D point = pickResult.getIntersectedTexCoord(); //in percentages
    double lat = (point.getY() - 0.5) * -180;
    double lon = (point.getX() - 0.5) * 360;

    visLayout.setRegionString(earth.getRegionString(lat, lon));
    visLayout.setTemperature(earth.getTemperature(lat, lon));
    visLayout.setFoodProduced(earth.getFoodProducedData(lat, lon));
  }


  @Override
  /**
   * Sends any event that has been filtered in to the proper method to "handle" it
   * @param event   User interaction event (clicking, scolling, zooming, dragging)
   */
  public void handle(Event event)
  {
    if (event.getEventType().equals(MouseDragEvent.MOUSE_DRAGGED))
    {
      earth.pauseRotation();
      earthScroll((MouseEvent) event);
      event.consume();
    }
    else if (event instanceof ScrollEvent)
    {
      earth.pauseRotation();
      earthZoom((ScrollEvent) event);
      event.consume();
    }
    else if (event instanceof ZoomEvent)
    {
      earthZoom((ZoomEvent) event);
    }
    else if (event instanceof MouseEvent)
    {
      if ((event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
          || event.getEventType().equals(MouseEvent.MOUSE_MOVED)))
      {
        displayEarthInformation((MouseEvent) event);
        latLongHandler((MouseEvent) event);
        event.consume();
      }
      earthStartScroll((MouseEvent) event);

    }
  }
}



