package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.common.MapPoint;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;
import starvationevasion.vis.controller.EarthViewer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;


/**
 * Created by tessdaughton on 11/30/15.
 * Contains all groups relevant to Earth Rendering
 */
public class Earth
{

  private final  ResourceLoader RESOURCE_LOADER = EarthViewer.RESOURCE_LOADER;
  private static final double ROTATE_SECS = 30;

  private final double MINI_EARTH_RADIUS;
  private final double LARGE_EARTH_RADIUS;

  private RotateTransition largeRotate;
  private RotateTransition smallRotate;


  private Group earthGroup;
  private Group smallEarthGroup;
  private Group earthOverlay;
  private Group earthWeather;

  /**
   * Earth constructor
   * @param miniRadius  not yet being used, but I'm leaving it in case we want it later
   * @param largeRadius used to determine in lat and long calcuations inside of UserEventHandler
   */
  public Earth(int miniRadius, int largeRadius)
  {
    MINI_EARTH_RADIUS=miniRadius;
    LARGE_EARTH_RADIUS=largeRadius;

    ResourceLoader.LARGE_EARTH_RADIUS = largeRadius;
    ResourceLoader.MINI_EARTH_RADIUS = miniRadius;

    earthGroup = buildScene(largeRadius);
    smallEarthGroup = buildScene(miniRadius);
    earthOverlay = buildOverlay();
    earthWeather = buildClouds();

    startRotate();
  }

  /**
   * Creates the "Earth" by placing PhongMaterials on a Sphere
   * @return  the Group "earthGroup" which will contain all of this
   */
  public Group buildScene(int radius)
  {
    Sphere earth = new Sphere(radius);
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(RESOURCE_LOADER.DIFF_MAP);
    earthMaterial.setBumpMap(RESOURCE_LOADER.NORM_MAP);
    earth.setMaterial(earthMaterial);
    return new Group(earth);
  }

  /**
   * Creates a transparent sphere overlay for any events needing to be displayed on the globe
   * @return Group  the Group containing the transparent sphere, to be attached on the earthGroup to render
   * */
  private Group buildOverlay()
  {
    Sphere overlay = new Sphere(LARGE_EARTH_RADIUS);
    final PhongMaterial cloudMaterial = new PhongMaterial();
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.REGION_OVERLAY);
    overlay.setMaterial(cloudMaterial);
    return new Group(overlay);
  }

  /**
   * Creates a transparent sphere overlay for weather (clouds) to be displayed on the globe
   * @return Group  the Group containing the transparent sphere, to be attached on the earthGroup to render
   */
  private Group buildClouds()
  {
    Sphere cloud = new Sphere(LARGE_EARTH_RADIUS * 1.05);
    final PhongMaterial cloudMaterial = new PhongMaterial();
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.REGION_OVERLAY);
    cloud.setMaterial(cloudMaterial);
    return new Group(cloud);
  }



  /**
   * Used by controller to access universe group
   * @return returns universe group to be attached in client's layout
   */
  public Group getEarth()
  {
    return earthGroup;
  }

  public Group getSmallEarth()
  {
    return smallEarthGroup;
  }

  /**
   * Used by VisualizerLayout to access overlay group
   * @return returns universe group to be attached in client's layout
   */
  public Group getEarthOverlay()
  {
    return earthOverlay;
  }
  /**
   * Used by VisualizerLayout to access weather group
   * @return returns weather group to be attached in client's layout
   */
  public Group getEarthWeather()
  {
    return earthWeather;
  }

  /**
   * Runs a continuous animation of the Earth rotating around its y-axis
   * Used for Mini Earth Mode in client GUI
   */
  public void startRotate()
  {
    rotateAroundYAxis(earthGroup).play();
    rotateAroundYAxis(smallEarthGroup).play();
  }

  /**
   * Creates a RotateTransition which facilitates continuous rotation around y-axis
   * @param node
   * @return
   */
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
  /**
   * Used for Big Earth mode to stop rotation when user clicks and drags
   */
  protected void pauseRotation()
  {
    rotateAroundYAxis(earthGroup).pause();
  }

  /**
   * Vis Team Testing Purposes
   **/
  public static void main(String args[])
  {
    int scale = 50;
    BufferedImage i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);

    Collection<GeographicArea> modelGeography = new GeographyXMLparser().getGeography();
    for (GeographicArea a : modelGeography)
    {
      java.awt.Polygon poly = new java.awt.Polygon();
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

    try
    {
      ImageIO.write(i, "PNG", new File("/Users/laurencemirabal/Desktop/test.png"));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.println("Done!");
    g.dispose();
  }

}

