package starvationevasion.vis.visuals;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.event.Event;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;



/**
 * Created by Tess Daughton on 11/15/15.
 */
public class UserEventHandler  implements EventHandler
{
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  double anchorX, anchorY;
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;
  private Group earth;

  public UserEventHandler(Group earth)
  {
    this.earth=earth;
  }
  protected void earthScroll(Event event)
  {
  /* Init group */
    if (event instanceof MouseDragEvent)
    {
      MouseDragEvent mouseEvent = (MouseDragEvent) event;

      Rotate groupXRotate, groupYRotate;
      earth.getTransforms().addAll(
          groupXRotate = new Rotate(0, Rotate.X_AXIS),
          groupYRotate = new Rotate(0, Rotate.Y_AXIS)
      );
      groupXRotate.angleProperty().bind(angleX);
      groupYRotate.angleProperty().bind(angleY);

      angleX.set(anchorAngleX - (anchorY - mouseEvent.getSceneY()));
      angleY.set(anchorAngleY + (anchorX - mouseEvent.getSceneX()));
    }
  }

  protected void earthZoom(Event event)
  {
    /**setTranslate can be used to zoom in and out on the world*/
    Scale earthScale = new Scale();
    earth.getTransforms().add(earthScale);
    System.out.println(earth.getScaleZ());

    if (event instanceof ScrollEvent)
    {
      ScrollEvent scrollEvent = (ScrollEvent) event;
      double scrollFactor = scrollEvent.getDeltaY();
      if (scrollFactor > 0)
      {
        if(earth.getScaleZ()<0.5) return;
        earth.setScaleZ(earth.getScaleZ() * .99);
        earth.setScaleX(earth.getScaleX() * .99);
        earth.setScaleY(earth.getScaleY() * .99);
      }
      else if (scrollFactor < 0)
      {
        if(earth.getScaleZ()>2) return;
        earth.setScaleZ(earth.getScaleZ() * 1.01);
        earth.setScaleX(earth.getScaleX() * 1.01);
        earth.setScaleY(earth.getScaleY() * 1.01);
      }
      System.out.println(String.format("deltaX: %.3f", scrollFactor));
    }
    else if (event instanceof ZoomEvent)
    {
      ZoomEvent zoomEvent = (ZoomEvent) event;
      double zoomFactor = zoomEvent.getX();
      if (zoomFactor > 0)
      {
        if(earth.getScaleZ()>2) return;
        earth.setScaleZ(earth.getScaleZ() * .99);
        earth.setScaleX(earth.getScaleX() * .99);
        earth.setScaleY(earth.getScaleY() * .99);
      }
      else if (zoomFactor < 0)
      {
        if(earth.getScaleZ()<0.5) return;
        earth.setScaleZ(earth.getScaleZ() * 1.01);
        earth.setScaleX(earth.getScaleX() * 1.01);
        earth.setScaleY(earth.getScaleY() * 1.01);
      }
      System.out.println(String.format("deltaX: %.3f", zoomFactor));
    }
  }

  @Override
  public void handle(Event event)
  {
    if (event instanceof MouseDragEvent || event instanceof MouseEvent)
    {
      earthScroll(event);
    }
    else if (event instanceof ZoomEvent | event instanceof ScrollEvent)
    {
      earthZoom(event);
    }
  }
}

