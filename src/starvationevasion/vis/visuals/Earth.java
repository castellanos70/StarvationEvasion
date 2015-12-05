package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.vis.model.Coordinate;


/**
 * Created by tessdaughton on 11/30/15.
 */
public class Earth
{

  private static final double ROTATE_SECS = 30;
  private final ResourceLoader RESOURCE_LOADER;
  private final double MINI_EARTH_RADIUS;
  private final double LARGE_EARTH_RADIUS;

  private final UserEventHandler userEventHandler;

  private static Group earth = new Group();

  public Earth (int miniRadius, int largeRadius, ResourceLoader resourceLoader)
  {
    RESOURCE_LOADER = resourceLoader;
    MINI_EARTH_RADIUS=miniRadius;
    LARGE_EARTH_RADIUS=largeRadius;
    earth = buildScene();
    userEventHandler = new UserEventHandler(earth);
    earth.addEventFilter(Event.ANY, event -> userEventHandler.handle(event));
    startRotate();
  }
  public Group buildScene()
  {
    Sphere earth = new Sphere(LARGE_EARTH_RADIUS);
    earth.setId("earth");


    /* Material */
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(RESOURCE_LOADER.DIFF_MAP);
    earthMaterial.setBumpMap(RESOURCE_LOADER.NORM_MAP);
    earthMaterial.setSpecularMap(RESOURCE_LOADER.SPEC_MAP);
    earth.setMaterial(earthMaterial);

    /* Lat Long handler */
    EventHandler<MouseEvent> handler = event -> {
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
    };
    earth.setOnMouseClicked(handler);
    return new Group(earth);
  }

  /**
   * Used by controller to access universe group
   *
   * @return returns universe group to be attached in client's layout
   */
  public Group getUniverse()
  {
    return earth;
  }



  /**
   * Runs a continuous animation of the Earth rotating around its y-axis
   * Used for Mini Earth Mode in client GUI
   */
  public void startRotate()
  {
    rotateAroundYAxis(earth).play();
  }



  private RotateTransition rotateAroundYAxis(Node node)
  {
    RotateTransition rotate = new RotateTransition(Duration.seconds(ROTATE_SECS), node);
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setFromAngle(360);
    rotate.setToAngle(0);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(RotateTransition.INDEFINITE);

    return rotate;

  }
}

