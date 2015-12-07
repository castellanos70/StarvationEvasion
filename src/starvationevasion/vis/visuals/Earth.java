package starvationevasion.vis.visuals;

import javafx.animation.*;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;
import starvationevasion.common.SpecialEventData;
import starvationevasion.common.Util;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;
import starvationevasion.vis.controller.EarthViewer;
import starvationevasion.vis.controller.SimParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by tessdaughton on 11/30/15.
 * Contains all groups relevant to Earth Rendering
 */
public class Earth {

  private final SimParser SIM_PARSER = new SimParser();
  private final ResourceLoader RESOURCE_LOADER = EarthViewer.RESOURCE_LOADER;
  private static double ROTATE_SECS = 30;

  private final double MINI_EARTH_RADIUS;
  private final double LARGE_EARTH_RADIUS;

  private RotateTransition largeRotate;
  private RotateTransition smallRotate;

  private Group earthGroup;
  private Group smallEarthGroup;
  private Group earthOverlay;
  private Group earthWeather;

  private HashMap<EnumRegion, int[]> foodProduced = new HashMap<>();
  private HashMap<MapPoint, Float> temperatures = new HashMap<>();
  private ArrayList<SpecialEventData> specialEventDatas =  new ArrayList<>();

  /**
   * Earth constructor
   *
   * @param miniRadius  not yet being used, but I'm leaving it in case we want it later
   * @param largeRadius used to determine in lat and long calcuations inside of UserEventHandler
   */
  public Earth(int miniRadius, int largeRadius) {
    MINI_EARTH_RADIUS = miniRadius;
    LARGE_EARTH_RADIUS = largeRadius;

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
   *
   * @return the Group "earthGroup" which will contain all of this
   */
  public Group buildScene(int radius) {
    Sphere earth = new Sphere(radius);
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(RESOURCE_LOADER.DIFF_MAP);
    earthMaterial.setBumpMap(RESOURCE_LOADER.NORM_MAP);
//    earthMaterial.setSpecularMap((RESOURCE_LOADER.SPEC_MAP));

    earth.setMaterial(earthMaterial);
    return new Group(earth);
  }

  /**
   * Creates a transparent sphere overlay for any events needing to be displayed on the globe
   *
   * @return Group  the Group containing the transparent sphere, to be attached on the earthGroup to render
   */
  private Group buildOverlay() {
    Sphere overlay = new Sphere(LARGE_EARTH_RADIUS);
    final PhongMaterial cloudMaterial = new PhongMaterial();
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.REGION_OVERLAY);
    overlay.setMaterial(cloudMaterial);
    return new Group(overlay);
  }

  /**
   * Creates a transparent sphere overlay for weather (clouds) to be displayed on the globe
   *
   * @return Group  the Group containing the transparent sphere, to be attached on the earthGroup to render
   */
  private Group buildClouds() {
    Sphere cloud = new Sphere(LARGE_EARTH_RADIUS * 1.05);
    final PhongMaterial cloudMaterial = new PhongMaterial();
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.CLOUDS);
    cloud.setMaterial(cloudMaterial);
    return new Group(cloud);
  }

  /**
   * Used by controller to access universe group
   *
   * @return returns universe group to be attached in client's layout
   */
  public Group getEarth() {
    return earthGroup;
  }

  public Group getSmallEarth() {
    return smallEarthGroup;
  }

  /**
   * Used by VisualizerLayout to access overlay group
   *
   * @return returns universe group to be attached in client's layout
   */
  public Group getEarthOverlay() {
    return earthOverlay;
  }

  /**
   * Used by VisualizerLayout to access weather group
   *
   * @return returns weather group to be attached in client's layout
   */
  public Group getEarthWeather() {
    return earthWeather;
  }

  /**
   * Runs a continuous animation of the Earth rotating around its y-axis
   * Used for Mini Earth Mode in client GUI
   */
  public void startRotate() {
    initRotaters();
    rotateAroundYAxis(earthGroup, largeRotate).play();
    rotateAroundYAxis(smallEarthGroup, smallRotate).play();
  }

  /**
   * Creates a RotateTransition which facilitates continuous rotation around y-axis
   *
   * @param node
   * @return
   */
  private RotateTransition rotateAroundYAxis(Node node, RotateTransition rotate) {
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setFromAngle(360);
    rotate.setToAngle(0);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(RotateTransition.INDEFINITE);

    return rotate;
  }

  private void initRotaters() {
    largeRotate = new RotateTransition(Duration.seconds(ROTATE_SECS), earthGroup);
    smallRotate = new RotateTransition(Duration.seconds(ROTATE_SECS), smallEarthGroup);
    largeRotate.setCycleCount(1);
    smallRotate.setCycleCount(RotateTransition.INDEFINITE);
  }

  /**
   * Used for Big Earth mode to stop rotation when user clicks and drags
   */
  protected void pauseRotation() {
    largeRotate.pause();
  }

  /**
   * Used for Big Earth mode to restart rotation when user clicks and drags
   */
  protected void resumeRotation() {
    largeRotate.play();
  }

  public void setTemperatures(HashMap<MapPoint, Float> data) {

    temperatures.clear();
    temperatures.putAll(data);
  }

  /**
   * Gets the temperature at the closet point.
   *
   * @param point point containing lat and long
   * @return float value of temperature
   */
  public float getTemperature(MapPoint point) {
    return getTemperature(point.latitude, point.longitude);
  }

  /**
   * TODO: confirm with SIM team how precise their lat/long temperatures are being stored at
   * Uses a brute force method to find the closest temperature point at the given lat long.
   * gis.stackexchange.com/questions/8650/  -  tells us the the units place will give us 111 km x 111 km
   * precision so we will first check units place, then check first decimal place for closest point
   *
   * @param lat latitude of the given point
   * @param lon longitude of the given point
   * @return float value of temperature
   */
  public float getTemperature(double lat, double lon) {
    if (temperatures == null || temperatures.size() == 0) return Float.MAX_VALUE;
    ArrayList<MapPoint> closestPoints = new ArrayList<>();
    double precision = Math.pow(10, 0);

    for (Map.Entry<MapPoint, Float> entry : temperatures.entrySet())
    {
      double eLat = Math.abs(entry.getKey().latitude - lat);
      double eLon = Math.abs(entry.getKey().longitude - lon);
      if (eLat < precision && eLon < precision) closestPoints.add(entry.getKey());
    }
    Collections.sort(closestPoints, new Comparator<MapPoint>()
    {
      public int compare(MapPoint p1, MapPoint p2)
      {
        double d1 = Point.distance(p1.latitude, p1.longitude, lat, lon);
        double d2 = Point.distance(p2.latitude, p2.longitude, lat, lon);
        return Double.compare(d1, d2);
      }
    });
    return (closestPoints.size() > 0) ? temperatures.get(closestPoints.get(0)) : Float.MAX_VALUE;
  }

  public void setSpecialEventDatas(ArrayList<SpecialEventData> data) {
    specialEventDatas.clear();
    specialEventDatas.addAll(data);
  }

  public void setFoodProducedData(HashMap<EnumRegion, int[]> data)
  {
    foodProduced.clear();
    foodProduced.putAll(data);
  }

  public int[] getFoodProducedData(double lat, double lon)
  {
    EnumRegion r = SIM_PARSER.getRegion(lat, lon);
    boolean containsRegion = foodProduced.containsKey(r);
    if (containsRegion) return foodProduced.get(r);
    return null;
  }

  public String getRegionString(double lat, double lon)
  {
    return SIM_PARSER.parse(lat, lon);
  }

  /**
   * Vis Team Testing Purposes
   **/
  public static void main(String args[]) {
    int scale = 50;
    BufferedImage i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);

    Collection<GeographicArea> modelGeography = new GeographyXMLparser().getGeography();
    for (GeographicArea a : modelGeography) {
      java.awt.Polygon poly = new java.awt.Polygon();
      for (MapPoint p : a.getPerimeter()) {
        int latitude = (int) ((p.latitude - 90) * -1 * scale);
        int longitude = (int) ((p.longitude + 180) * scale);
        poly.addPoint(longitude, latitude);
      }
      g.setComposite(AlphaComposite.Src);
      g.setColor(Color.RED);
      g.drawPolygon(poly);
    }

    try {
      ImageIO.write(i, "PNG", new File("C:\\Users\\Anand\\Desktop\\test.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Done!");
    g.dispose();


    HashMap<MapPoint, Float> data = new HashMap<>();
    for (double lat = -90; lat < 90; lat += 1) {
      for (double lon = -180; lon < 180; lon += 1) {
        data.put(new MapPoint(lat, lon), Util.rand.nextFloat() * 100);
      }
    }
    System.out.println("done building test");


    i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);
    g.setComposite(AlphaComposite.Src);

    ArrayList<Color> colors = new ArrayList<>();
    colors.add(Color.white);
    colors.add(Color.BLUE);
    colors.add(Color.yellow);
    colors.add(Color.orange);
    colors.add(Color.red);

    for (Map.Entry<MapPoint, Float> e : data.entrySet()) {
      if (e.getKey().latitude > 90 || e.getKey().latitude < -90) continue;
      if (e.getKey().longitude > 180 || e.getKey().longitude < -180) continue;

      int x = (int) ((e.getKey().longitude + 180) * scale);
      int y = (int) ((e.getKey().latitude - 90) * -1 * scale);
      float t = e.getValue();

      g.setColor(colors.get(0));
      if (t < 32) g.setColor(colors.get(0));
      else if (t < 55) g.setColor(colors.get(1));
      else if (t < 75) g.setColor(colors.get(2));
      else if (t < 90) g.setColor(colors.get(3));
      else g.setColor(colors.get(4));
      g.drawOval(x, y, 1, 1);
    }
    g.dispose();
    try {
      ImageIO.write(i, "PNG", new File("C:\\Users\\Anand\\Desktop\\test.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}



