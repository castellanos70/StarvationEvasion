package starvationevasion.vis.visuals;


import javafx.scene.image.Image;

/**
 * Created by Tess Daughton 11/14/15.
 * This class will be used to load & contain resources necessary for the Visualization rendering.
 *
 * images
 * DIFF_FORESTFIRE - http://nobacks.com/photos/nature/page/39/
 * DIFF_CLOUD - http://nobacks.com/photos/nature/page/39/
 * DIFF_BLIGHT - http://www.clipartbest.com/cliparts/bcy/4Gj/bcy4GjgcL.jpeg
 */


public class ResourceLoader
{

  private static final String DIFFUSE_MAP = "visResources/DIFFUSE_MAP.jpg";//"vis_resources/DIFFUSE_MAP.jpg";
  //"http://www.daidegasforum.com/images/22/world-map-satellite-day-nasa-earth.jpg";
  private static final String NORMAL_MAP = "visResources/NORMAL_MAP.jpg";//"vis_resources/NORMAL_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_normalmap_flat_8192x4096.jpg";
  private static final String SPECULAR_MAP = "visResources/SPEC_MAP.jpg";//"vis_resources/SPEC_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_specularmap_flat_8192x4096.jpg";
  private static final String STARS = "visResources/night_sky.jpg";//"vis_resources/SPEC_MAP.jpg";
  private static final String REGION_OVERELAY_FILE = "visResources/WorldMapRegionsFull8x6.png";//"vis_resources/SPEC_MAP.jpg";
  private static final String CLOUD_FILE = "visResources/Cloud-32.png";//"vis_resources/SPEC_MAP.jpg";


  private static final String DIFFUSE_CLOUD = "visResources/Cloud-32.png";
  private static final String DIFFUSE_PINPOINT = "visResources/PinPoint.png";
  private static final String DIFFUSE_HURRICANE = "visResources/hurricane.png";
  private static final String DIFFUSE_HURRICANESHADOW = "visResources/hurricaneShadow.png";
  private static final String DIFFUSE_TEST = "visResources/animation1.gif";
  private static final String DIFFUSE_FORESTFIRE = "visResources/forestFire.png";
  private static final String DIFFUSE_FLOOD = "visResources/flood.png";
  private static final String DIFFUSE_DROUGHT = "visResources/drought.png";
  private static final String DIFFUSE_BLIGHT = "visResources/blight.png";








  protected static Image COLD_IMAGE;
  protected static Image HOT_IMAGE;
  protected static Image STAR_BACKGROUND;
  protected static Image SPEC_MAP;
  protected static Image NORM_MAP;
  protected static Image DIFF_MAP;
  protected static Image DIFF_CLOUD;
  protected static Image DIFF_HURRICANE;
  protected static Image DIFF_HURRICANESHADOW;
  protected static Image DIFF_PINPOINT;
  protected static Image DIFF_TEST;
  protected static Image DIFF_FORESTFIRE;
  protected static Image DIFF_FLOOD;
  protected static Image DIFF_DROUGHT;
  protected static Image DIFF_BLIGHT;



  protected static Image REGION_OVERLAY;
  protected static Image CLOUDS;
  public static String STYLE_SHEET;

  protected static int LARGE_EARTH_RADIUS;
  protected static int MINI_EARTH_RADIUS;

  private static final double MAP_WIDTH = 8192 / 2d;
  private static final double MAP_HEIGHT = 4092 / 2d;

  public ResourceLoader()
  {
    try
    {
      DIFF_MAP = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_CLOUD = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_CLOUD), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_HURRICANE = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_HURRICANE), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_HURRICANESHADOW = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_HURRICANESHADOW), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_TEST = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_TEST), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_FORESTFIRE = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_FORESTFIRE), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_FLOOD = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_FLOOD), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_DROUGHT = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_DROUGHT), MAP_WIDTH, MAP_HEIGHT, true, true);
      DIFF_BLIGHT = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_BLIGHT), MAP_WIDTH, MAP_HEIGHT, true, true);

      DIFF_PINPOINT = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_PINPOINT), MAP_WIDTH, MAP_HEIGHT, true, true);
      NORM_MAP = new Image(getClass().getClassLoader().getResourceAsStream(NORMAL_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      SPEC_MAP = new Image(getClass().getClassLoader().getResourceAsStream(SPECULAR_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      STAR_BACKGROUND = new Image(getClass().getClassLoader().getResourceAsStream(STARS), MAP_WIDTH, MAP_HEIGHT, true, true);
      COLD_IMAGE = new Image(  getClass().getClassLoader().getResourceAsStream("visResources/snowflake.png"));
      HOT_IMAGE = new Image(getClass().getClassLoader().getResourceAsStream("visResources/hot.png"));
      REGION_OVERLAY = new Image(getClass().getClassLoader().getResourceAsStream(REGION_OVERELAY_FILE), MAP_WIDTH, MAP_HEIGHT, true, true);
      CLOUDS = new Image(getClass().getClassLoader().getResourceAsStream(CLOUD_FILE), MAP_WIDTH, MAP_HEIGHT, true, true);
      STYLE_SHEET = this.getClass().getClassLoader().getResource("visResources/style.css").toExternalForm();


    }
    catch(NullPointerException e)
    {
      e.printStackTrace();
    }
  }
}
