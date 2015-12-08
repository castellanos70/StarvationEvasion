package starvationevasion.vis.visuals;

import javafx.animation.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.common.*;
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
  private Group earthWeather = new Group();
  private Group earthHeatMap = new Group();


  private HashMap<EnumRegion, int[]> foodProduced = new HashMap<>();
  private ArrayList<SpecialEventData> specialEventDatas =  new ArrayList<>();
  private ArrayList<LandTile> landTiles = new ArrayList<>();
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
    earthHeatMap = buildHeatMapOverlay();
    startRotate();
  }

  public void rebuildHeatMapOverlay()
  {
    earthHeatMap = buildHeatMapOverlay();
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
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.DIFF_MAP);
    cloudMaterial.setDiffuseMap(RESOURCE_LOADER.REGION_OVERLAY);
    overlay.setMaterial(cloudMaterial);
    overlay.setMaterial(cloudMaterial);

    return new Group(overlay);
  }

  private Group buildHeatMapOverlay()
  {
    Sphere heatMap = new Sphere(LARGE_EARTH_RADIUS);
    final PhongMaterial heatMapMaterial = new PhongMaterial();

    int scale = 10;
    BufferedImage i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);

    i = new BufferedImage(360 * scale, 180 * scale, BufferedImage.TYPE_INT_ARGB);
    g = i.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, 360 * scale, 180 * scale);
    g.setColor(Color.BLACK);
    g.setComposite(AlphaComposite.Src);
    int alpha = 135;
    Color red = new Color(255,0,0,alpha);
    Color blue = new Color(0,0,255,alpha);
    Color white = new Color(255,255,255,alpha);
    Color yellow = new Color(255,255,0,alpha);
    Color orange = new Color(255,100,10,alpha);


    ArrayList<Color> colors = new ArrayList<>();
    colors.add(white);
    colors.add(blue);
    colors.add(yellow);
    colors.add(orange);
    colors.add(red);

//    for (Map.Entry<MapPoint, Float> e : data.entrySet()) {

    for (LandTile l : landTiles){
//      if(SIM_PARSER.getRegion(l.center) == null) continue;
      if (l.center.latitude > 90 || l.center.latitude < -90) continue;
      if (l.center.longitude > 180 || l.center.longitude < -180) continue;

      int x = (int) ((l.center.longitude + 180) * scale);
      int y = (int) ((l.center.latitude - 90) * -1 * scale);
      float t = l.maxAnnualTemp;

      g.setColor(colors.get(0));
      if (t < 32) g.setColor(colors.get(0));
      else if (t < 55) g.setColor(colors.get(1));
      else if (t < 75) g.setColor(colors.get(2));
      else if (t < 90) g.setColor(colors.get(3));
      else g.setColor(colors.get(4));
      g.fillRect(x, y, 10, 10);

    }
    g.dispose();

    //make map image into an fx imag
    WritableImage image;
    image = SwingFXUtils.toFXImage(i, null);

    //put dat image on the sphere
    heatMapMaterial.setDiffuseMap(image);
    heatMap.setMaterial(heatMapMaterial);
    return new Group(heatMap);
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
   * Used by VisualizerLayout to access heat map group
   *
   * @return returns heat map group to be attached in client's layout
   */
  public Group getEarthHeatMap() {
    return earthHeatMap;
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

  /**
   * Initiate rotations
   **/
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
   * @param lat latitude of the given point given by GUI
   * @param lon longitude of the given point given by GUI
   * @return float value of temperature
   */
  public float getTemperature(double lat, double lon) {
    Collections.sort(landTiles, new Comparator<LandTile>()
    {
      public int compare(LandTile t1, LandTile t2)
      {
        MapPoint p1 = t1.center;
        MapPoint p2 = t2.center;
        double d1 = Point.distance(p1.latitude, p1.longitude, lat, lon);
        double d2 = Point.distance(p2.latitude, p2.longitude, lat, lon);
        return Double.compare(d1, d2);
      }
    });
    for(LandTile t : landTiles)
    {
      if ((int)t.center.latitude == (int) lat && (int)t.center.longitude == (int) lon)
      {
        return t.maxAnnualTemp;
      }
    }
    return Float.MAX_VALUE;
  }

  public void setSpecialEventDatas(ArrayList<SpecialEventData> data) {
    specialEventDatas.clear();
    specialEventDatas.addAll(data);
  }

  /**
   * @param lat - latitude
   * @param lon - longitude
   * @return food produced data
   **/
  public int[] getFoodProducedData(double lat, double lon)
  {
    EnumRegion r = SIM_PARSER.getRegion(lat, lon);
    boolean containsRegion = foodProduced.containsKey(r);
    if (containsRegion) return foodProduced.get(r);
    return null;
  }

  /**
   * @param d - list of land tiles parsed for food, temp, region information
   * */
  public void setLandTiles(ArrayList<LandTile> d)
  {
    landTiles.clear();
    landTiles.addAll(d);

    foodProduced.clear();
    for (LandTile t : d)
    {
      EnumRegion r = SIM_PARSER.getRegion(t.center);
      if (r == null) continue;
      if (!foodProduced.containsKey(r)) foodProduced.put(r, new int [EnumFood.SIZE]);
      foodProduced.get(r)[t.currCrop.ordinal()]++;
    }
  }

  /**
   * @param lat - latitude
   * @param lon - longitude
   * @return the region selected
   *
   **/
  public String getRegionString(double lat, double lon)
  {
    return SIM_PARSER.parse(lat, lon);
  }

  /**
   * Vis Team Testing Purposes- testing map overlays. Will create an overlay of all region on the earth
   *
   **/
  public static void main(String args[]) {
    int scale = 10;
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
      ImageIO.write(i, "PNG", new File("/Users/laurencemirabal/Desktop/image.png"));
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
    Color red = new Color(255,0,0,75);
    Color blue = new Color(0,0,255,75);
    Color white = new Color(255,255,255,75);
    Color yellow = new Color(255,255,0,75);
    Color orange = new Color(255,100,10,75);


    ArrayList<Color> colors = new ArrayList<>();
    colors.add(white);
    colors.add(blue);
    colors.add(yellow);
    colors.add(orange);
    colors.add(red);

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
      //g.drawOval(x, y, 1, 1);
      g.fillRect(x, y, 10, 10);

    }
    g.dispose();
    try {
      WritableImage image = SwingFXUtils.toFXImage(i, null);

     // WritableImage writableImage = new W

      ImageIO.write(i, "PNG", new File("~/heatImage.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
