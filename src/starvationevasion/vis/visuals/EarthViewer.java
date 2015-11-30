package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.vis.model.Coordinate;
//import starvationevasion.simvis.visuals.smallevents.CropsTest;


/**
 * http://stackoverflow.com/questions/19621423/javafx-materials-bump-and-spec-maps
 * Original Author:jewelsea,http://stackoverflow.com/users/1155209/jewelsea
 * Modified by:Tess Daughton
 **/


public class EarthViewer
{

  private static final double ROTATE_SECS = 30;

  private static final double MAP_WIDTH = 8192 / 2d;
  private static final double MAP_HEIGHT = 4092 / 2d;

  private final double MINI_EARTH_RADIUS;
  private final double LARGE_EARTH_RADIUS;

  private double zoomPosition = 0;
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  double anchorX, anchorY;
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;

  private static final String DIFFUSE_MAP = "visResources/DIFFUSE_MAP.jpg";//"vis_resources/DIFFUSE_MAP.jpg";
  //"http://www.daidegasforum.com/images/22/world-map-satellite-day-nasa-earth.jpg";
  private static final String NORMAL_MAP = "visResources/NORMAL_MAP.jpg";//"vis_resources/NORMAL_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_normalmap_flat_8192x4096.jpg";
  private static final String SPECULAR_MAP = "visResources/SPEC_MAP.jpg";//"vis_resources/SPEC_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_specularmap_flat_8192x4096.jpg";

  private static Group largeEarth;
  private static Group miniEarth;


  public EarthViewer(double smallEarthRadius, double largeEarthRadius)
  {
    MINI_EARTH_RADIUS = smallEarthRadius;
    LARGE_EARTH_RADIUS = largeEarthRadius;
    largeEarth = buildScene(LARGE_EARTH_RADIUS);
    miniEarth = buildScene(MINI_EARTH_RADIUS);
  }

  public Group buildScene(double earthRadius)
  {
    Sphere earth = new Sphere(earthRadius);


    /* Material */
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap
        (new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setBumpMap
        (new Image(getClass().getClassLoader().getResourceAsStream(NORMAL_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setSpecularMap
        (new Image(getClass().getClassLoader().getResourceAsStream(SPECULAR_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));

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
   * Used by Client GUI to toggle between Earth Viewing Modes
   *
   * @return Large Earth Group to be attached in client's layout
   */
  public Group getLargeEarth()
  {
    return largeEarth;
  }


  /**
   * Used by Client GUI to toggle between Earth Viewing Modes
   *
   * @return Mini Earth Group to be attached in client's layout
   */
  public Group getMiniEarth()
  {
    return miniEarth;
  }

  /**
   * Runs a continuous animation of the Earth rotating around its y-axis
   * Used for Mini Earth Mode in client GUI
   */
  public void startRotate()
  {
    rotateAroundYAxis(miniEarth).play();
  }

  public void startEarth()
  {
    /* Init group */
    Rotate groupXRotate, groupYRotate;
    largeEarth.getTransforms().setAll(
        groupXRotate = new Rotate(0, Rotate.X_AXIS),
        groupYRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    groupXRotate.angleProperty().bind(angleX);
    groupYRotate.angleProperty().bind(angleY);

    largeEarth.setOnMouseDragged((MouseEvent event) -> {
      angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
      angleY.set(anchorAngleY + anchorX - event.getSceneX());
    });
    largeEarth.setOnMousePressed((MouseEvent event) -> {
      anchorX = event.getSceneX();
      anchorY = event.getSceneY();
      anchorAngleX = angleX.get();
      anchorAngleY = angleY.get();
    });
    largeEarth.setOnScroll(event ->
    {
      System.out.println("test)");
    });
    largeEarth.setOnKeyPressed(new EventHandler<KeyEvent>()
    {
      @Override
      public void handle(KeyEvent event)
      {
        switch (event.getCode())
        {
          case P:
            rotateAroundYAxis(largeEarth).play();
        }
      }
    });

    /**setTranslate can be used to zoom in and out on the world*/
    largeEarth.setOnScroll(me ->
        {

          if (me.getDeltaY() < 0 && zoomPosition > -840)
          {
            largeEarth.setTranslateZ(zoomPosition -= 10);
          }
          else if (me.getDeltaY() > 0 && zoomPosition < 500)
          {
            largeEarth.setTranslateZ(zoomPosition += 10);
          }
          //System.out.println(String.format("deltaX: %.3f deltaY: %.3f", me.getDeltaX(), me.getDeltaY()));
          //System.out.println(zoomPosition);
        });


    largeEarth.setOnMousePressed(me ->
        {
          // not sure what this listener was for as there was no code in the body
          //but I went ahead and replaced with a lambda to clean up a bit
        });


    largeEarth.setOnKeyPressed(event->
      {
        switch (event.getCode())
        {
          case P:
            rotateAroundYAxis(largeEarth).play();
        }
      });

  }

  public void startRotate(Group group)
  {
    rotateAroundYAxis(group).play();
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
