package starvationevasion.vis.visuals;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Created by Tess Daughton 11/14/15.
 * This class will be used to load/parse any resources necessary for the Visualization rendering.
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

  protected static Image COLD_IMAGE;
  protected static Image HOT_IMAGE;
  protected static Image STAR_BACKGROUND;
  protected static Image SPEC_MAP;
  protected static Image NORM_MAP;
  protected static Image DIFF_MAP;
  public static String STYLE_SHEET;

  private static final double MAP_WIDTH = 8192 / 2d;
  private static final double MAP_HEIGHT = 4092 / 2d;

  public ResourceLoader()
  {
    try
    {
      DIFF_MAP = new Image(getClass().getClassLoader().getResourceAsStream(DIFFUSE_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      NORM_MAP = new Image(getClass().getClassLoader().getResourceAsStream(NORMAL_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      SPEC_MAP = new Image(getClass().getClassLoader().getResourceAsStream(SPECULAR_MAP), MAP_WIDTH, MAP_HEIGHT, true, true);
      STAR_BACKGROUND = new Image(getClass().getClassLoader().getResourceAsStream(STARS), MAP_WIDTH, MAP_HEIGHT, true, true);
      COLD_IMAGE = new Image(  getClass().getClassLoader().getResourceAsStream("visResources/snowflake.png"));
      HOT_IMAGE = new Image(getClass().getClassLoader().getResourceAsStream("visResources/hot.png"));
      STYLE_SHEET = this.getClass().getClassLoader().getResource("visResources/style.css").toExternalForm();

    }
    catch(NullPointerException e)
    {
      e.printStackTrace();
    }
  }
}
