package starvationevasion.client.GUI.DraftLayout;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * 
 * 
 * @Author Christian Seely
 * 
 *         This class constructs all of the SVGPath objects that compose of each
 *         region and place them on the correct location on the world map. Note
 *         the Region colors, Region glow colors, region fill colors, and region
 *         size are all customizable. Also each reason as listeners associated
 *         with it which can tell when a mouse enters and leaves a region (some
 *         small islands that are part of a region do not have listeners but if
 *         wanted they can be added). Also if you click on the region it lights
 *         up and prints out that it should trigger a pane showing the
 *         statistics (similar as to demo'ed in class )of the region to display
 *         (TODO).
 *
 *         For question/access to the SVG vector paths/editing software to
 *         manipulate them you can contact me at (cseely@unm.edu).
 *
 *         In case anyone is wondering why components of the tiles or lat/long
 *         number were not used in the creation of the region boarders here is
 *         why: 1. Using the Lat/Long values would required calculating which
 *         lat/long value belongs to each region all on the clients side (this
 *         way does not require that). 2. The Lat/Long points would be many more
 *         data points and slower to render (this has much less data points) 3.
 *         It's not just displaying the regions on the map that's important it's
 *         having the ability to add listeners to them. So in some way those
 *         lat/long points would have to be used to create some component that
 *         can have listeners (The current way has a wide variety of listeners
 *         available to use) 4. The Lat/Long point were being drawn using AWT
 *         which does not have the 'Styling' capabilities of JavaFX. (The
 *         current way can use the 'Styling' capabilities of JavaFX)
 * 
 *         But Note: The lat/long numbers will be needed for when crop heat maps
 *         are implemented but to create those the regional boarders don't
 *         matter so again no extra calculations are needed on the clients side.
 *
 */
public class BuildInteractiveRegionBoarders
{
  private final int DEPTH = 70;
  private final Color TRANSPARENT = Color.TRANSPARENT;
  private ArrayList<SVGPath> africaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> southAmericaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> middleAmericaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> californiaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> PNWandMNTPaths = new ArrayList<>();;
  private ArrayList<SVGPath> northernPlanesPaths = new ArrayList<>();;
  private ArrayList<SVGPath> heartLandPaths = new ArrayList<>();;
  private ArrayList<SVGPath> southernPlanesDeltaStatesPaths = new ArrayList<>();;
  private ArrayList<SVGPath> southEastPaths = new ArrayList<>();;
  private ArrayList<SVGPath> northernCrescentPaths = new ArrayList<>();;;
  private ArrayList<SVGPath> middleEastPaths = new ArrayList<>();;
  private ArrayList<SVGPath> southAsiaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> centralAsiaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> arcticAmericaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> russiaCaucausPaths = new ArrayList<>();;
  private ArrayList<SVGPath> europePaths = new ArrayList<>();;
  private ArrayList<SVGPath> eastAsiaPaths = new ArrayList<>();;
  private ArrayList<SVGPath> oceaniaPaths = new ArrayList<>();;

  private SVGPathsHolder SVG = new SVGPathsHolder();
  StackPane sp;

  BuildInteractiveRegionBoarders(StackPane sp)
  {
    this.sp = sp;
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Africa's Region Path
   */
  public void buildAfricaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow africaBoarderGlow = new DropShadow();
    africaBoarderGlow.setOffsetY(0f);
    africaBoarderGlow.setOffsetX(0f);
    africaBoarderGlow.setColor(GlowColor);
    africaBoarderGlow.setWidth(DEPTH);
    africaBoarderGlow.setHeight(DEPTH);

    SVGPath africaMainPath = new SVGPath();
    africaMainPath.setFill(TRANSPARENT);
    africaMainPath.setStroke(TRANSPARENT);
    africaMainPath.setStrokeWidth(5.0);
    africaMainPath.setScaleX(.8);
    africaMainPath.setScaleY(.8);
    africaMainPath.setTranslateY(27);
    africaMainPath.setTranslateX(96);
    africaMainPath.setContent(SVG.AFRICA_PATHS[0]);
    SVGPath africaSubPath = new SVGPath();
    africaSubPath.setFill(TRANSPARENT);
    africaSubPath.setStroke(TRANSPARENT);
    africaSubPath.setStrokeWidth(5.0);
    africaSubPath.setScaleX(.8);
    africaSubPath.setScaleY(.8);
    africaSubPath.setTranslateY(130);
    africaSubPath.setTranslateX(255);
    africaSubPath.setContent(SVG.AFRICA_PATHS[1]);

    africaPaths.add(africaMainPath);
    africaPaths.add(africaSubPath);

    africaMainPath.setEffect(africaBoarderGlow);
    africaSubPath.setEffect(africaBoarderGlow);

    africaMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(1, RegionColor, 1); // Change Stroke Color
    });

    africaMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(1, TRANSPARENT, 1); // Change Stroke Color
    });

    africaSubPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(1, RegionColor, 1); // Change Stroke Color
    });

    africaSubPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(1, TRANSPARENT, 1); // Change Stroke Color
    });

    africaMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Africa Regional Stats");
      // TODO: Have Africa's Regional Statics be displayed.
      styleRegionPaths(1, fillColor, 2); // Change Fill Color

    });
    africaSubPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Africa Regional Stats");
      // TODO: Have Africa's Regional Statics be displayed.
      styleRegionPaths(1, fillColor, 2); // Change Fill Color
    });
    africaMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(1, TRANSPARENT, 2); // Change Fill Color

    });
    africaSubPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(1, TRANSPARENT, 2); // Change Fill Color
    });
    sp.getChildren().addAll(africaMainPath, africaSubPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build South America's Region Path.
   */
  public void buildSouthAmericaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    SVGPath southAmericaPath = new SVGPath();
    southAmericaPath.setFill(TRANSPARENT);
    southAmericaPath.setStroke(TRANSPARENT);
    southAmericaPath.setStrokeWidth(5.0);
    southAmericaPath.setScaleX(.8);
    southAmericaPath.setScaleY(.8);
    southAmericaPath.setTranslateY(138);
    southAmericaPath.setTranslateX(-330);
    southAmericaPath.setContent(SVG.SOUTH_AMERICA_PATHS[0]);

    DropShadow southAmericaBoarderGlow = new DropShadow();
    southAmericaBoarderGlow.setOffsetY(0f);
    southAmericaBoarderGlow.setOffsetX(0f);
    southAmericaBoarderGlow.setColor(GlowColor);
    southAmericaBoarderGlow.setWidth(DEPTH);
    southAmericaBoarderGlow.setHeight(DEPTH);
    southAmericaPath.setEffect(southAmericaBoarderGlow);

    southAmericaPaths.add(southAmericaPath);

    southAmericaPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(2, RegionColor, 1); // Change Stroke Color
    });

    southAmericaPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(2, TRANSPARENT, 1); // Change Stroke Color
    });

    southAmericaPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger South American Regional Stats");
      // TODO: Have South America's Regional Statics be displayed.
      styleRegionPaths(2, fillColor, 2); // Change Fill Color
    });

    southAmericaPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(2, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(southAmericaPath);

  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Middle America's Region Path
   */
  public void buildMiddleAmericaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow middleAmericaBoarderGlow = new DropShadow();
    middleAmericaBoarderGlow.setOffsetY(0f);
    middleAmericaBoarderGlow.setOffsetX(0f);
    middleAmericaBoarderGlow.setColor(GlowColor);
    middleAmericaBoarderGlow.setWidth(DEPTH);
    middleAmericaBoarderGlow.setHeight(DEPTH);

    SVGPath middleAmericaMainPath = new SVGPath();
    middleAmericaMainPath.setFill(TRANSPARENT);
    middleAmericaMainPath.setStroke(TRANSPARENT);
    middleAmericaMainPath.setStrokeWidth(5.0);
    middleAmericaMainPath.setScaleX(.8);
    middleAmericaMainPath.setScaleY(.8);
    middleAmericaMainPath.setTranslateY(-138);
    middleAmericaMainPath.setTranslateX(-520);
    middleAmericaMainPath.setContent(SVG.MIDDLE_AMERICA_PATHS[0]);

    SVGPath middleAmericaLargeSubPath1 = new SVGPath();
    middleAmericaLargeSubPath1.setFill(TRANSPARENT);
    middleAmericaLargeSubPath1.setStroke(TRANSPARENT);
    middleAmericaLargeSubPath1.setStrokeWidth(5.0);
    middleAmericaLargeSubPath1.setScaleX(.8);
    middleAmericaLargeSubPath1.setScaleY(.8);
    middleAmericaLargeSubPath1.setTranslateY(-151);
    middleAmericaLargeSubPath1.setTranslateX(-435);
    middleAmericaLargeSubPath1.setContent(SVG.MIDDLE_AMERICA_PATHS[1]);

    SVGPath middleAmericaLargeSubPath2 = new SVGPath();
    middleAmericaLargeSubPath2.setFill(TRANSPARENT);
    middleAmericaLargeSubPath2.setStroke(TRANSPARENT);
    middleAmericaLargeSubPath2.setStrokeWidth(5.0);
    middleAmericaLargeSubPath2.setScaleX(.8);
    middleAmericaLargeSubPath2.setScaleY(.8);
    middleAmericaLargeSubPath2.setTranslateY(-132);
    middleAmericaLargeSubPath2.setTranslateX(-392);
    middleAmericaLargeSubPath2.setContent(SVG.MIDDLE_AMERICA_PATHS[2]);

    SVGPath middleAmericaSmallSubPath1 = new SVGPath();
    middleAmericaSmallSubPath1.setFill(TRANSPARENT);
    middleAmericaSmallSubPath1.setStroke(TRANSPARENT);
    middleAmericaSmallSubPath1.setStrokeWidth(5.0);
    middleAmericaSmallSubPath1.setScaleX(.8);
    middleAmericaSmallSubPath1.setScaleY(.8);
    middleAmericaSmallSubPath1.setTranslateY(-128);
    middleAmericaSmallSubPath1.setTranslateX(-425);
    middleAmericaSmallSubPath1.setContent(SVG.MIDDLE_AMERICA_PATHS[3]);

    SVGPath middleAmericaSmallSubPath2 = new SVGPath();
    middleAmericaSmallSubPath2.setFill(TRANSPARENT);
    middleAmericaSmallSubPath2.setStroke(TRANSPARENT);
    middleAmericaSmallSubPath2.setStrokeWidth(5.0);
    middleAmericaSmallSubPath2.setScaleX(.8);
    middleAmericaSmallSubPath2.setScaleY(.8);
    middleAmericaSmallSubPath2.setTranslateY(-128);
    middleAmericaSmallSubPath2.setTranslateX(-360);
    middleAmericaSmallSubPath2.setContent(SVG.MIDDLE_AMERICA_PATHS[4]);

    SVGPath middleAmericaSmallSubPath3 = new SVGPath();
    middleAmericaSmallSubPath3.setFill(TRANSPARENT);
    middleAmericaSmallSubPath3.setStroke(TRANSPARENT);
    middleAmericaSmallSubPath3.setStrokeWidth(5.0);
    middleAmericaSmallSubPath3.setScaleX(.8);
    middleAmericaSmallSubPath3.setScaleY(.8);
    middleAmericaSmallSubPath3.setTranslateY(-171);
    middleAmericaSmallSubPath3.setTranslateX(-420);
    middleAmericaSmallSubPath3.setContent(SVG.MIDDLE_AMERICA_PATHS[5]);

    SVGPath middleAmericaSmallSubPath4 = new SVGPath();
    middleAmericaSmallSubPath4.setFill(TRANSPARENT);
    middleAmericaSmallSubPath4.setStroke(TRANSPARENT);
    middleAmericaSmallSubPath4.setStrokeWidth(5.0);
    middleAmericaSmallSubPath4.setScaleX(.8);
    middleAmericaSmallSubPath4.setScaleY(.8);
    middleAmericaSmallSubPath4.setTranslateY(-186);
    middleAmericaSmallSubPath4.setTranslateX(-410);
    middleAmericaSmallSubPath4.setContent(SVG.MIDDLE_AMERICA_PATHS[6]);

    middleAmericaPaths.add(middleAmericaMainPath);
    middleAmericaPaths.add(middleAmericaLargeSubPath1);
    middleAmericaPaths.add(middleAmericaLargeSubPath2);
    middleAmericaPaths.add(middleAmericaSmallSubPath1);
    middleAmericaPaths.add(middleAmericaSmallSubPath2);
    middleAmericaPaths.add(middleAmericaSmallSubPath3);
    middleAmericaPaths.add(middleAmericaSmallSubPath4);

    middleAmericaMainPath.setEffect(middleAmericaBoarderGlow);
    middleAmericaLargeSubPath1.setEffect(middleAmericaBoarderGlow);
    middleAmericaLargeSubPath2.setEffect(middleAmericaBoarderGlow);
    middleAmericaSmallSubPath1.setEffect(middleAmericaBoarderGlow);
    middleAmericaSmallSubPath2.setEffect(middleAmericaBoarderGlow);
    middleAmericaSmallSubPath3.setEffect(middleAmericaBoarderGlow);
    middleAmericaSmallSubPath4.setEffect(middleAmericaBoarderGlow);

    middleAmericaMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(3, RegionColor, 1); // Change Stroke Color
    });

    middleAmericaMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(3, TRANSPARENT, 1); // Change Stroke Color
    });
    middleAmericaLargeSubPath1.setOnMouseMoved(evt ->
    {
      styleRegionPaths(3, RegionColor, 1); // Change Stroke Color
    });

    middleAmericaLargeSubPath2.setOnMouseMoved(evt ->
    {
      styleRegionPaths(3, RegionColor, 1); // Change Stroke Color
    });

    middleAmericaLargeSubPath1.setOnMouseExited(evt ->
    {
      styleRegionPaths(3, TRANSPARENT, 1); // Change Stroke Color
    });

    middleAmericaLargeSubPath2.setOnMouseExited(evt ->
    {
      styleRegionPaths(3, TRANSPARENT, 1); // Change Stroke Color
    });

    middleAmericaMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Middle American Regional Stats");
      // TODO: Have Middle America's Regional Statics be displayed.
      styleRegionPaths(3, fillColor, 2); // Change Fill Color
    });
    middleAmericaLargeSubPath1.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Middle American Regional Stats");
      styleRegionPaths(3, fillColor, 2); // Change Fill Color
      // TODO: Have Middle America's Regional Statics be displayed.
    });
    middleAmericaLargeSubPath2.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Middle American Regional Stats");
      styleRegionPaths(3, fillColor, 2); // Change Fill Color
      // TODO: Have Middle America's Regional Statics be displayed.
    });
    middleAmericaMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(3, TRANSPARENT, 2); // Change Fill Color

    });
    middleAmericaLargeSubPath1.setOnMouseReleased((event) ->
    {
      styleRegionPaths(3, TRANSPARENT, 2); // Change Fill Color
    });
    middleAmericaLargeSubPath2.setOnMouseReleased((event) ->
    {
      styleRegionPaths(3, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().addAll(middleAmericaMainPath, middleAmericaLargeSubPath1,
        middleAmericaLargeSubPath2, middleAmericaSmallSubPath1,
        middleAmericaSmallSubPath2, middleAmericaSmallSubPath3,
        middleAmericaSmallSubPath4);

  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   * 
   *          Build California's Region Path
   */
  public void buildCaliforniaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath californiaPath = new SVGPath();
    californiaPath.setFill(TRANSPARENT);
    californiaPath.setStroke(TRANSPARENT);
    californiaPath.setStrokeWidth(5.0);
    californiaPath.setScaleX(.8);
    californiaPath.setScaleY(.8);
    californiaPath.setTranslateY(-255);
    californiaPath.setTranslateX(-592);
    californiaPath.setContent(SVG.CALIFORNIA_PATHS[0]);

    DropShadow californiaBoarderGlow = new DropShadow();
    californiaBoarderGlow.setOffsetY(0f);
    californiaBoarderGlow.setOffsetX(0f);
    californiaBoarderGlow.setColor(GlowColor);
    californiaBoarderGlow.setWidth(DEPTH);
    californiaBoarderGlow.setHeight(DEPTH);
    californiaPath.setEffect(californiaBoarderGlow);

    californiaPaths.add(californiaPath);

    californiaPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(4, RegionColor, 1); // Change Stroke Color
    });

    californiaPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(4, TRANSPARENT, 1); // Change Stroke Color
    });

    californiaPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger California Regional Stats");
      // TODO: Have California's Statics be displayed.
      styleRegionPaths(4, fillColor, 2); // Change Fill Color
    });

    californiaPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(4, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(californiaPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build PNW and MNT Region Path.
   */
  public void buildPNWAndMNTPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath PNWAndMNTPath = new SVGPath();
    PNWAndMNTPath.setFill(TRANSPARENT);
    PNWAndMNTPath.setStroke(TRANSPARENT);
    PNWAndMNTPath.setStrokeWidth(5.0);
    PNWAndMNTPath.setScaleX(.8);
    PNWAndMNTPath.setScaleY(.8);
    PNWAndMNTPath.setTranslateY(-270);
    PNWAndMNTPath.setTranslateX(-525);
    PNWAndMNTPath.setContent(SVG.PNW_AND_MNT_PATHS[0]);

    DropShadow PNWAndMNTPathBoarderGlow = new DropShadow();
    PNWAndMNTPathBoarderGlow.setOffsetY(0f);
    PNWAndMNTPathBoarderGlow.setOffsetX(0f);
    PNWAndMNTPathBoarderGlow.setColor(GlowColor);
    PNWAndMNTPathBoarderGlow.setWidth(DEPTH);
    PNWAndMNTPathBoarderGlow.setHeight(DEPTH);
    PNWAndMNTPath.setEffect(PNWAndMNTPathBoarderGlow);

    PNWandMNTPaths.add(PNWAndMNTPath);

    PNWAndMNTPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(5, RegionColor, 1); // Change Stroke Color
    });

    PNWAndMNTPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(5, TRANSPARENT, 1); // Change Stroke Color
    });

    PNWAndMNTPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger PWN & MNT Regional Stats");
      // TODO: Have PWN & MNT Regional Statics be displayed.
      styleRegionPaths(5, fillColor, 2); // Change Fill Color
    });

    PNWAndMNTPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(5, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(PNWAndMNTPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Northern Planes Region Path.
   */
  public void buildNorthernPlanesPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath northernPlanesPath = new SVGPath();
    northernPlanesPath.setFill(TRANSPARENT);
    northernPlanesPath.setStroke(TRANSPARENT);
    northernPlanesPath.setStrokeWidth(5.0);
    northernPlanesPath.setScaleX(.8);
    northernPlanesPath.setScaleY(.8);
    northernPlanesPath.setTranslateY(-288);
    northernPlanesPath.setTranslateX(-450);
    northernPlanesPath.setContent(SVG.NORTHERN_PLAINS_PATHS[0]);

    DropShadow northernPlanesBoarderGlow = new DropShadow();
    northernPlanesBoarderGlow.setOffsetY(0f);
    northernPlanesBoarderGlow.setOffsetX(0f);
    northernPlanesBoarderGlow.setColor(GlowColor);
    northernPlanesBoarderGlow.setWidth(DEPTH);
    northernPlanesBoarderGlow.setHeight(DEPTH);
    northernPlanesPath.setEffect(northernPlanesBoarderGlow);

    northernPlanesPaths.add(northernPlanesPath);

    northernPlanesPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(6, RegionColor, 1); // Change Stroke Color
    });

    northernPlanesPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(6, TRANSPARENT, 1); // Change Stroke Color
    });

    northernPlanesPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Northern Planes Regional Stats");
      // TODO: Have Northern Planes Regional Statics be displayed.
      styleRegionPaths(6, fillColor, 2); // Change Fill Color
    });
    northernPlanesPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(6, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(northernPlanesPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   * 
   *          Build HeartLands Region Path.
   */
  public void buildHeartLandsPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath heartLandsPath = new SVGPath();
    heartLandsPath.setFill(TRANSPARENT);
    heartLandsPath.setStroke(TRANSPARENT);
    heartLandsPath.setStrokeWidth(5.0);
    heartLandsPath.setScaleX(.8);
    heartLandsPath.setScaleY(.8);
    heartLandsPath.setTranslateY(-270);
    heartLandsPath.setTranslateX(-442);
    heartLandsPath.setContent(SVG.HEARTLAND_PATHS[0]);

    DropShadow heartLandsBoarderGlow = new DropShadow();
    heartLandsBoarderGlow.setOffsetY(0f);
    heartLandsBoarderGlow.setOffsetX(0f);
    heartLandsBoarderGlow.setColor(GlowColor);
    heartLandsBoarderGlow.setWidth(DEPTH);
    heartLandsBoarderGlow.setHeight(DEPTH);
    heartLandsPath.setEffect(heartLandsBoarderGlow);

    heartLandPaths.add(heartLandsPath);

    heartLandsPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(7, RegionColor, 1); // Change Stroke Color
    });

    heartLandsPath.setOnMouseExited(evt ->
    {
      heartLandsPath.setStroke(TRANSPARENT);
      styleRegionPaths(7, TRANSPARENT, 1); // Change Stroke Color
    });

    heartLandsPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger HeartLand Regional Stats");
      // TODO: Have HeartLand's Regional Statics be displayed.
      styleRegionPaths(7, fillColor, 2); // Change Fill Color
    });
    heartLandsPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(7, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(heartLandsPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Southern Planes and Delta States Region Path.
   */
  public void buildSouthernPlanesDeltaStatesPath(Color RegionColor,
      Color GlowColor, Color fillColor)
  {
    SVGPath southernPlanesDeltaStatesPath = new SVGPath();
    southernPlanesDeltaStatesPath.setFill(TRANSPARENT);
    southernPlanesDeltaStatesPath.setStroke(TRANSPARENT);
    southernPlanesDeltaStatesPath.setStrokeWidth(5.0);
    southernPlanesDeltaStatesPath.setScaleX(.8);
    southernPlanesDeltaStatesPath.setScaleY(.8);
    southernPlanesDeltaStatesPath.setTranslateY(-215);
    southernPlanesDeltaStatesPath.setTranslateX(-500);
    southernPlanesDeltaStatesPath
        .setContent(SVG.SOUTHERN_PLAINS_AND_DELTA_PATHS[0]);

    DropShadow southernPlanesDeltaStatesBoarderGlow = new DropShadow();
    southernPlanesDeltaStatesBoarderGlow.setOffsetY(0f);
    southernPlanesDeltaStatesBoarderGlow.setOffsetX(0f);
    southernPlanesDeltaStatesBoarderGlow.setColor(GlowColor);
    southernPlanesDeltaStatesBoarderGlow.setWidth(DEPTH);
    southernPlanesDeltaStatesBoarderGlow.setHeight(DEPTH);
    southernPlanesDeltaStatesPath
        .setEffect(southernPlanesDeltaStatesBoarderGlow);

    southernPlanesDeltaStatesPaths.add(southernPlanesDeltaStatesPath);

    southernPlanesDeltaStatesPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(8, RegionColor, 1); // Change Stroke Color
    });

    southernPlanesDeltaStatesPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(8, TRANSPARENT, 1); // Change Stroke Color
    });

    southernPlanesDeltaStatesPath.setOnMousePressed((event) ->
    {
      System.out
          .println("Trigger Southern Planes and Delta States Regional Stats");
      // TODO: Have Southern Planes and Delta States Regional Statics be
      // displayed.
      styleRegionPaths(8, fillColor, 2); // Change Fill Color
    });

    southernPlanesDeltaStatesPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(8, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(southernPlanesDeltaStatesPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Southeast Region Path.
   */
  public void buildSoutheastPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath southeastPath = new SVGPath();
    southeastPath.setFill(TRANSPARENT);
    southeastPath.setStroke(TRANSPARENT);
    southeastPath.setStrokeWidth(5.0);
    southeastPath.setScaleX(.8);
    southeastPath.setScaleY(.8);
    southeastPath.setTranslateY(-220);
    southeastPath.setTranslateX(-422);
    southeastPath.setContent(SVG.SOUTHEAST_PATHS[0]);

    DropShadow southeastBoarderGlow = new DropShadow();
    southeastBoarderGlow.setOffsetY(0f);
    southeastBoarderGlow.setOffsetX(0f);
    southeastBoarderGlow.setColor(GlowColor);
    southeastBoarderGlow.setWidth(DEPTH);
    southeastBoarderGlow.setHeight(DEPTH);
    southeastPath.setEffect(southeastBoarderGlow);

    southEastPaths.add(southeastPath);

    southeastPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(9, RegionColor, 1); // Change Stroke Color
    });

    southeastPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(9, TRANSPARENT, 1); // Change Stroke Color
    });

    southeastPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Southeast Regional Stats");
      styleRegionPaths(9, fillColor, 2); // Change Fill Color
    });
    southeastPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(9, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(southeastPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Northern Crescent Region Path.
   */
  public void buildNorthernCrescentPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    SVGPath northernCrescentPath = new SVGPath();
    northernCrescentPath.setFill(TRANSPARENT);
    northernCrescentPath.setStroke(TRANSPARENT);
    northernCrescentPath.setStrokeWidth(5.0);
    northernCrescentPath.setScaleX(.8);
    northernCrescentPath.setScaleY(.8);
    northernCrescentPath.setTranslateY(-288);
    northernCrescentPath.setTranslateX(-369);
    northernCrescentPath.setContent(SVG.NORTHERN_CRECENT_PATHS[0]);

    DropShadow northernCrescentBoarderGlow = new DropShadow();
    northernCrescentBoarderGlow.setOffsetY(0f);
    northernCrescentBoarderGlow.setOffsetX(0f);
    northernCrescentBoarderGlow.setColor(GlowColor);
    northernCrescentBoarderGlow.setWidth(DEPTH);
    northernCrescentBoarderGlow.setHeight(DEPTH);
    northernCrescentPath.setEffect(northernCrescentBoarderGlow);

    northernCrescentPaths.add(northernCrescentPath);

    northernCrescentPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(10, RegionColor, 1); // Change Stroke Color
    });

    northernCrescentPath.setOnMouseExited(evt ->
    {
      ;
      styleRegionPaths(10, TRANSPARENT, 1); // Change Stroke Color
    });

    northernCrescentPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Northern Crecents Regional Stats");
      styleRegionPaths(10, fillColor, 2); // Change Fill Color
      // TODO: Have Northern Crecents Regional Statics be displayed.
    });
    northernCrescentPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(10, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().add(northernCrescentPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Middle East Region Path.
   */
  public void buildMiddleEastPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow middleEastBoarderGlow = new DropShadow();
    middleEastBoarderGlow.setOffsetY(0f);
    middleEastBoarderGlow.setOffsetX(0f);
    middleEastBoarderGlow.setColor(GlowColor);
    middleEastBoarderGlow.setWidth(DEPTH);
    middleEastBoarderGlow.setHeight(DEPTH);

    SVGPath middleEastMainPath = new SVGPath();
    middleEastMainPath.setFill(TRANSPARENT);
    middleEastMainPath.setStroke(TRANSPARENT);
    middleEastMainPath.setStrokeWidth(5.0);
    middleEastMainPath.setScaleX(.8);
    middleEastMainPath.setScaleY(.8);
    middleEastMainPath.setTranslateY(-175);
    middleEastMainPath.setTranslateX(132);
    middleEastMainPath.setContent(SVG.MIDDLE_EAST_PATHS[0]);

    SVGPath middleEastSubPath = new SVGPath();
    middleEastSubPath.setFill(TRANSPARENT);
    middleEastSubPath.setStroke(TRANSPARENT);
    middleEastSubPath.setStrokeWidth(2.5);
    middleEastSubPath.setScaleX(.8);
    middleEastSubPath.setScaleY(.8);
    middleEastSubPath.setTranslateY(-240);
    middleEastSubPath.setTranslateX(165);
    middleEastSubPath.setContent(SVG.MIDDLE_EAST_PATHS[1]);

    middleEastMainPath.setEffect(middleEastBoarderGlow);
    middleEastSubPath.setEffect(middleEastBoarderGlow);

    middleEastPaths.add(middleEastMainPath);
    middleEastPaths.add(middleEastSubPath);

    middleEastMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(11, RegionColor, 1); // Change Stroke Color
    });

    middleEastMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(11, TRANSPARENT, 1); // Change Stroke Color
    });

    middleEastMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Middle East Regional Stats");
      styleRegionPaths(11, fillColor, 2); // Change Fill Color
      // TODO: Have the Middle East's Regional Statics be displayed.
    });
    middleEastMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(11, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().addAll(middleEastMainPath, middleEastSubPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Central Asia Region Path.
   */
  public void buildCentralAsiaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    SVGPath centralAsiaPath = new SVGPath();
    centralAsiaPath.setFill(TRANSPARENT);
    centralAsiaPath.setStroke(TRANSPARENT);
    centralAsiaPath.setStrokeWidth(5.0);
    centralAsiaPath.setScaleX(.8);
    centralAsiaPath.setScaleY(.8);
    centralAsiaPath.setTranslateY(-284);
    centralAsiaPath.setTranslateX(294);
    centralAsiaPath.setContent(SVG.CENTRAL_ASIA_PATHS[0]);

    DropShadow centralAsiaBoarderGlow = new DropShadow();
    centralAsiaBoarderGlow.setOffsetY(0f);
    centralAsiaBoarderGlow.setOffsetX(0f);
    centralAsiaBoarderGlow.setColor(GlowColor);
    centralAsiaBoarderGlow.setWidth(DEPTH);
    centralAsiaBoarderGlow.setHeight(DEPTH);
    centralAsiaPath.setEffect(centralAsiaBoarderGlow);

    centralAsiaPaths.add(centralAsiaPath);

    centralAsiaPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(12, RegionColor, 1); // Change Stroke Color
    });

    centralAsiaPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(12, TRANSPARENT, 1); // Change Stroke Color
    });

    centralAsiaPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Central Asia's Regional Stats");
      styleRegionPaths(12, fillColor, 2); // Change Fill Color
      // TODO: Have Central Asia's Regional Statics be displayed.
    });

    centralAsiaPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(12, TRANSPARENT, 2); // Change Fill Color
    });
    sp.getChildren().add(centralAsiaPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build South Asia Region Path.
   */
  public void buildSouthAsiaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow southAsiaBoarderGlow = new DropShadow();
    southAsiaBoarderGlow.setOffsetY(0f);
    southAsiaBoarderGlow.setOffsetX(0f);
    southAsiaBoarderGlow.setColor(GlowColor);
    southAsiaBoarderGlow.setWidth(DEPTH);
    southAsiaBoarderGlow.setHeight(DEPTH);

    SVGPath southAsiaMainPath = new SVGPath();
    southAsiaMainPath.setFill(TRANSPARENT);
    southAsiaMainPath.setStroke(TRANSPARENT);
    southAsiaMainPath.setStrokeWidth(5.0);
    southAsiaMainPath.setScaleX(.8);
    southAsiaMainPath.setScaleY(.8);
    southAsiaMainPath.setTranslateY(-150);
    southAsiaMainPath.setTranslateX(451);
    southAsiaMainPath.setContent(SVG.SOUTH_ASIA_PATHS[0]);

    SVGPath southAsiaSubPath = new SVGPath();
    southAsiaSubPath.setFill(TRANSPARENT);
    southAsiaSubPath.setStroke(TRANSPARENT);
    southAsiaSubPath.setStrokeWidth(4.0);
    southAsiaSubPath.setScaleX(.8);
    southAsiaSubPath.setScaleY(.8);
    southAsiaSubPath.setTranslateY(-55);
    southAsiaSubPath.setTranslateX(455);
    southAsiaSubPath.setContent(SVG.SOUTH_ASIA_PATHS[1]);

    southAsiaMainPath.setEffect(southAsiaBoarderGlow);
    southAsiaSubPath.setEffect(southAsiaBoarderGlow);

    southAsiaPaths.add(southAsiaMainPath);
    southAsiaPaths.add(southAsiaSubPath);

    southAsiaMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(13, RegionColor, 1); // Change Stroke Color
    });

    southAsiaMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(13, TRANSPARENT, 1); // Change Stroke Color
    });

    southAsiaMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger South Asia's Regional Stats");
      styleRegionPaths(13, fillColor, 2); // Change Fill Color
      // TODO: Have South Asia's Regional Statics be displayed.
    });

    southAsiaMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(13, TRANSPARENT, 2); // Change Fill Color
    });
    sp.getChildren().addAll(southAsiaMainPath, southAsiaSubPath);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Arctic America Region Path.
   */
  public void buildArcticAmericaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow arcticAmericaBoarderGlow = new DropShadow();
    arcticAmericaBoarderGlow.setOffsetY(0f);
    arcticAmericaBoarderGlow.setOffsetX(0f);
    arcticAmericaBoarderGlow.setColor(GlowColor);
    arcticAmericaBoarderGlow.setWidth(DEPTH);
    arcticAmericaBoarderGlow.setHeight(DEPTH);

    SVGPath arcticAmericaMainPath1 = new SVGPath();
    arcticAmericaMainPath1.setFill(TRANSPARENT);
    arcticAmericaMainPath1.setStroke(TRANSPARENT);
    arcticAmericaMainPath1.setStrokeWidth(4.0);
    arcticAmericaMainPath1.setScaleX(.8);
    arcticAmericaMainPath1.setScaleY(.8);
    arcticAmericaMainPath1.setTranslateY(-372);
    arcticAmericaMainPath1.setTranslateX(-446);
    arcticAmericaMainPath1.setContent(SVG.ARTIC_AMERICA_PATHS[0]);

    SVGPath arcticAmericaMainPath2 = new SVGPath();
    arcticAmericaMainPath2.setFill(TRANSPARENT);
    arcticAmericaMainPath2.setStroke(TRANSPARENT);
    arcticAmericaMainPath2.setStrokeWidth(4.0);
    arcticAmericaMainPath2.setScaleX(.8);
    arcticAmericaMainPath2.setScaleY(.8);
    arcticAmericaMainPath2.setTranslateY(-442);
    arcticAmericaMainPath2.setTranslateX(-101);
    arcticAmericaMainPath2.setContent(SVG.ARTIC_AMERICA_PATHS[1]);

    SVGPath arcticAmericaSubPath1 = new SVGPath();
    arcticAmericaSubPath1.setFill(TRANSPARENT);
    arcticAmericaSubPath1.setStroke(TRANSPARENT);
    arcticAmericaSubPath1.setStrokeWidth(3.0);
    arcticAmericaSubPath1.setScaleX(.8);
    arcticAmericaSubPath1.setScaleY(.8);
    arcticAmericaSubPath1.setTranslateY(-447);
    arcticAmericaSubPath1.setTranslateX(-305);
    arcticAmericaSubPath1.setContent(SVG.ARTIC_AMERICA_PATHS[2]);

    SVGPath arcticAmericaSubPath2 = new SVGPath();
    arcticAmericaSubPath2.setFill(TRANSPARENT);
    arcticAmericaSubPath2.setStroke(TRANSPARENT);
    arcticAmericaSubPath2.setStrokeWidth(2.0);
    arcticAmericaSubPath2.setScaleX(.8);
    arcticAmericaSubPath2.setScaleY(.8);
    arcticAmericaSubPath2.setTranslateY(-427);
    arcticAmericaSubPath2.setTranslateX(-226);
    arcticAmericaSubPath2.setContent(SVG.ARTIC_AMERICA_PATHS[3]);

    SVGPath arcticAmericaSubPath3 = new SVGPath();
    arcticAmericaSubPath3.setFill(TRANSPARENT);
    arcticAmericaSubPath3.setStroke(TRANSPARENT);
    arcticAmericaSubPath3.setStrokeWidth(2.0);
    arcticAmericaSubPath3.setScaleX(.8);
    arcticAmericaSubPath3.setScaleY(.8);
    arcticAmericaSubPath3.setTranslateY(-479);
    arcticAmericaSubPath3.setTranslateX(-161);
    arcticAmericaSubPath3.setContent(SVG.ARTIC_AMERICA_PATHS[4]);

    SVGPath arcticAmericaSubPath4 = new SVGPath();
    arcticAmericaSubPath4.setFill(TRANSPARENT);
    arcticAmericaSubPath4.setStroke(TRANSPARENT);
    arcticAmericaSubPath4.setStrokeWidth(2.0);
    arcticAmericaSubPath4.setScaleX(.8);
    arcticAmericaSubPath4.setScaleY(.8);
    arcticAmericaSubPath4.setTranslateY(-464);
    arcticAmericaSubPath4.setTranslateX(-237);
    arcticAmericaSubPath4.setContent(SVG.ARTIC_AMERICA_PATHS[5]);

    arcticAmericaMainPath1.setEffect(arcticAmericaBoarderGlow);
    arcticAmericaMainPath2.setEffect(arcticAmericaBoarderGlow);
    arcticAmericaSubPath1.setEffect(arcticAmericaBoarderGlow);
    arcticAmericaSubPath2.setEffect(arcticAmericaBoarderGlow);
    arcticAmericaSubPath3.setEffect(arcticAmericaBoarderGlow);
    arcticAmericaSubPath4.setEffect(arcticAmericaBoarderGlow);

    arcticAmericaPaths.add(arcticAmericaMainPath1);
    arcticAmericaPaths.add(arcticAmericaMainPath2);
    arcticAmericaPaths.add(arcticAmericaSubPath1);
    arcticAmericaPaths.add(arcticAmericaSubPath2);
    arcticAmericaPaths.add(arcticAmericaSubPath3);
    arcticAmericaPaths.add(arcticAmericaSubPath4);

    arcticAmericaMainPath1.setOnMouseMoved(evt ->
    {
      styleRegionPaths(14, RegionColor, 1); // Change Stroke Color
    });
    arcticAmericaMainPath1.setOnMouseExited(evt ->
    {
      styleRegionPaths(14, TRANSPARENT, 1); // Change Stroke Color
    });
    arcticAmericaMainPath2.setOnMouseMoved(evt ->
    {
      ;
      styleRegionPaths(14, RegionColor, 1); // Change Stroke Color
    });

    arcticAmericaMainPath2.setOnMouseExited(evt ->
    {
      styleRegionPaths(14, TRANSPARENT, 1); // Change Stroke Color
    });

    arcticAmericaMainPath1.setOnMousePressed((event) ->
    {
      System.out.println("Trigger South Asia's Regional Stats");
      styleRegionPaths(14, fillColor, 2); // Change Fill Color
      // TODO: Have South Asia's Regional Statics be displayed.
    });
    arcticAmericaMainPath2.setOnMousePressed((event) ->
    {
      System.out.println("Trigger South Asia's Regional Stats");
      styleRegionPaths(14, fillColor, 2); // Change Fill Color
      // TODO: Have South Asia's Regional Statics be displayed.
    });

    arcticAmericaMainPath1.setOnMouseReleased((event) ->
    {
      styleRegionPaths(14, TRANSPARENT, 2); // Change Fill Color
    });
    arcticAmericaMainPath2.setOnMouseReleased((event) ->
    {
      styleRegionPaths(14, TRANSPARENT, 2); // Change Fill Color
    });
    sp.getChildren().addAll(arcticAmericaMainPath1, arcticAmericaMainPath2,
        arcticAmericaSubPath1, arcticAmericaSubPath2, arcticAmericaSubPath3,
        arcticAmericaSubPath4);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Russia Caucaus Region Path.
   */
  public void buildRussiaCaucausPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow russiaCaucausMainBoarderGlow = new DropShadow();
    russiaCaucausMainBoarderGlow.setOffsetY(0f);
    russiaCaucausMainBoarderGlow.setOffsetX(0f);
    russiaCaucausMainBoarderGlow.setColor(GlowColor);
    russiaCaucausMainBoarderGlow.setWidth(DEPTH);
    russiaCaucausMainBoarderGlow.setHeight(DEPTH);

    SVGPath russiaCaucausMainPath = new SVGPath();
    russiaCaucausMainPath.setFill(TRANSPARENT);
    russiaCaucausMainPath.setStroke(TRANSPARENT);
    russiaCaucausMainPath.setStrokeWidth(5.0);
    russiaCaucausMainPath.setScaleX(.8);
    russiaCaucausMainPath.setScaleY(.8);
    russiaCaucausMainPath.setTranslateY(-368);
    russiaCaucausMainPath.setTranslateX(377);
    russiaCaucausMainPath.setContent(SVG.RUSSIA_CAUCASUS_PATHS[0]);

    SVGPath russiaCaucausSubPath1 = new SVGPath();
    russiaCaucausSubPath1.setFill(TRANSPARENT);
    russiaCaucausSubPath1.setStroke(TRANSPARENT);
    russiaCaucausSubPath1.setStrokeWidth(5.0);
    russiaCaucausSubPath1.setScaleX(.8);
    russiaCaucausSubPath1.setScaleY(.8);
    russiaCaucausSubPath1.setTranslateY(-459);
    russiaCaucausSubPath1.setTranslateX(149);
    russiaCaucausSubPath1.setContent(SVG.RUSSIA_CAUCASUS_PATHS[1]);

    SVGPath russiaCaucausSubPath2 = new SVGPath();
    russiaCaucausSubPath2.setFill(TRANSPARENT);
    russiaCaucausSubPath2.setStroke(TRANSPARENT);
    russiaCaucausSubPath2.setStrokeWidth(3.0);
    russiaCaucausSubPath2.setScaleX(.8);
    russiaCaucausSubPath2.setScaleY(.8);
    russiaCaucausSubPath2.setTranslateY(-483);
    russiaCaucausSubPath2.setTranslateX(190);
    russiaCaucausSubPath2.setContent(SVG.RUSSIA_CAUCASUS_PATHS[2]);

    SVGPath russiaCaucausSubPath3 = new SVGPath();
    russiaCaucausSubPath3.setFill(TRANSPARENT);
    russiaCaucausSubPath3.setStroke(TRANSPARENT);
    russiaCaucausSubPath3.setStrokeWidth(3.0);
    russiaCaucausSubPath3.setScaleX(.8);
    russiaCaucausSubPath3.setScaleY(.8);
    russiaCaucausSubPath3.setTranslateY(-488);
    russiaCaucausSubPath3.setTranslateX(100);
    russiaCaucausSubPath3.setContent(SVG.RUSSIA_CAUCASUS_PATHS[3]);

    SVGPath russiaCaucausSubPath4 = new SVGPath();
    russiaCaucausSubPath4.setFill(TRANSPARENT);
    russiaCaucausSubPath4.setStroke(TRANSPARENT);
    russiaCaucausSubPath4.setStrokeWidth(3.0);
    russiaCaucausSubPath4.setScaleX(.8);
    russiaCaucausSubPath4.setScaleY(.8);
    russiaCaucausSubPath4.setTranslateY(-422);
    russiaCaucausSubPath4.setTranslateX(-560);
    russiaCaucausSubPath4.setContent(SVG.RUSSIA_CAUCASUS_PATHS[4]);

    russiaCaucausMainPath.setEffect(russiaCaucausMainBoarderGlow);
    russiaCaucausSubPath1.setEffect(russiaCaucausMainBoarderGlow);
    russiaCaucausSubPath2.setEffect(russiaCaucausMainBoarderGlow);
    russiaCaucausSubPath3.setEffect(russiaCaucausMainBoarderGlow);
    russiaCaucausSubPath4.setEffect(russiaCaucausMainBoarderGlow);

    russiaCaucausPaths.add(russiaCaucausMainPath);
    russiaCaucausPaths.add(russiaCaucausSubPath1);
    russiaCaucausPaths.add(russiaCaucausSubPath2);
    russiaCaucausPaths.add(russiaCaucausSubPath3);
    russiaCaucausPaths.add(russiaCaucausSubPath4);

    russiaCaucausMainPath.setOnMouseMoved(evt ->
    {
      ;
      styleRegionPaths(15, RegionColor, 1); // Change Stroke Color
    });

    russiaCaucausMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(15, TRANSPARENT, 1); // Change Stroke Color
    });

    russiaCaucausMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Russia and Caucaus Regional Stats");
      styleRegionPaths(15, fillColor, 2); // Change Fill Color
      // TODO: Have Russia and Caucaus Regional Statics be displayed.
    });

    russiaCaucausMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(15, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().addAll(russiaCaucausMainPath, russiaCaucausSubPath1,
        russiaCaucausSubPath2, russiaCaucausSubPath3, russiaCaucausSubPath4);

  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Europe Region Path.
   */
  public void buildEuropePath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow europeMainBoarderGlow = new DropShadow();
    europeMainBoarderGlow.setOffsetY(0f);
    europeMainBoarderGlow.setOffsetX(0f);
    europeMainBoarderGlow.setColor(GlowColor);
    europeMainBoarderGlow.setWidth(DEPTH);
    europeMainBoarderGlow.setHeight(DEPTH);

    SVGPath europeMainPath = new SVGPath();
    europeMainPath.setFill(TRANSPARENT);
    europeMainPath.setStroke(TRANSPARENT);
    europeMainPath.setStrokeWidth(5.0);
    europeMainPath.setScaleX(.8);
    europeMainPath.setScaleY(.8);
    europeMainPath.setTranslateY(-347);
    europeMainPath.setTranslateX(63);
    europeMainPath.setContent(SVG.EUROPE_PATHS[0]);

    SVGPath europeSubPath1 = new SVGPath();
    europeSubPath1.setFill(TRANSPARENT);
    europeSubPath1.setStroke(TRANSPARENT);
    europeSubPath1.setStrokeWidth(5.0);
    europeSubPath1.setScaleX(.8);
    europeSubPath1.setScaleY(.8);
    europeSubPath1.setTranslateY(-358);
    europeSubPath1.setTranslateX(-7);
    europeSubPath1.setContent(SVG.EUROPE_PATHS[1]);

    SVGPath europeSubPath2 = new SVGPath();
    europeSubPath2.setFill(TRANSPARENT);
    europeSubPath2.setStroke(TRANSPARENT);
    europeSubPath2.setStrokeWidth(5.0);
    europeSubPath2.setScaleX(.8);
    europeSubPath2.setScaleY(.8);
    europeSubPath2.setTranslateY(-354);
    europeSubPath2.setTranslateX(-34);
    europeSubPath2.setContent(SVG.EUROPE_PATHS[2]);

    SVGPath europeSubPath3 = new SVGPath();
    europeSubPath3.setFill(TRANSPARENT);
    europeSubPath3.setStroke(TRANSPARENT);
    europeSubPath3.setStrokeWidth(5.0);
    europeSubPath3.setScaleX(.8);
    europeSubPath3.setScaleY(.8);
    europeSubPath3.setTranslateY(-415);
    europeSubPath3.setTranslateX(-63);
    europeSubPath3.setContent(SVG.EUROPE_PATHS[3]);

    SVGPath europeSubPath4 = new SVGPath();
    europeSubPath4.setFill(TRANSPARENT);
    europeSubPath4.setStroke(TRANSPARENT);
    europeSubPath4.setStrokeWidth(3.0);
    europeSubPath4.setScaleX(.8);
    europeSubPath4.setScaleY(.8);
    europeSubPath4.setTranslateY(-477);
    europeSubPath4.setTranslateX(35);
    europeSubPath4.setContent(SVG.EUROPE_PATHS[4]);

    SVGPath europeSubPath5 = new SVGPath();
    europeSubPath5.setFill(TRANSPARENT);
    europeSubPath5.setStroke(TRANSPARENT);
    europeSubPath5.setStrokeWidth(3.0);
    europeSubPath5.setScaleX(.8);
    europeSubPath5.setScaleY(.8);
    europeSubPath5.setTranslateY(-240);
    europeSubPath5.setTranslateX(120);
    europeSubPath5.setContent(SVG.EUROPE_PATHS[5]);

    SVGPath europeSubPath6 = new SVGPath();
    europeSubPath6.setFill(TRANSPARENT);
    europeSubPath6.setStroke(TRANSPARENT);
    europeSubPath6.setStrokeWidth(3.0);
    europeSubPath6.setScaleX(.8);
    europeSubPath6.setScaleY(.8);
    europeSubPath6.setTranslateY(-278);
    europeSubPath6.setTranslateX(42);
    europeSubPath6.setContent(SVG.EUROPE_PATHS[6]);

    europeMainPath.setEffect(europeMainBoarderGlow);
    europeSubPath1.setEffect(europeMainBoarderGlow);
    europeSubPath2.setEffect(europeMainBoarderGlow);
    europeSubPath3.setEffect(europeMainBoarderGlow);
    europeSubPath4.setEffect(europeMainBoarderGlow);
    europeSubPath5.setEffect(europeMainBoarderGlow);
    europeSubPath6.setEffect(europeMainBoarderGlow);

    europePaths.add(europeMainPath);
    europePaths.add(europeSubPath1);
    europePaths.add(europeSubPath2);
    europePaths.add(europeSubPath3);
    europePaths.add(europeSubPath4);
    europePaths.add(europeSubPath5);
    europePaths.add(europeSubPath6);

    europeMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(16, RegionColor, 1); // Change Stroke Color
    });

    europeMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(16, TRANSPARENT, 1); // Change Stroke Color
    });
    europeSubPath1.setOnMouseMoved(evt ->
    {

      styleRegionPaths(16, RegionColor, 1); // Change Stroke Color
    });

    europeSubPath1.setOnMouseExited(evt ->
    {
      styleRegionPaths(16, TRANSPARENT, 1); // Change Stroke Color
    });

    europeSubPath2.setOnMouseMoved(evt ->
    {
      styleRegionPaths(16, RegionColor, 1); // Change Stroke Color
    });

    europeSubPath2.setOnMouseExited(evt ->
    {
      styleRegionPaths(16, TRANSPARENT, 1); // Change Stroke Color
    });

    europeSubPath3.setOnMouseMoved(evt ->
    {
      styleRegionPaths(16, RegionColor, 1); // Change Stroke Color
    });

    europeSubPath3.setOnMouseExited(evt ->
    {
      styleRegionPaths(16, TRANSPARENT, 1); // Change Stroke Color
    });

    europeMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Europes Regional Stats");
      styleRegionPaths(16, fillColor, 2); // Change Fill Color
      // TODO: Have Eurpoe's Regional Statics be displayed.
    });
    europeMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(16, TRANSPARENT, 2); // Change Fill Color
      // TODO: Have Eurpoe's Regional Statics be displayed.
    });

    europeSubPath1.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Europes Regional Stats");
      styleRegionPaths(16, fillColor, 2); // Change Fill Color
      // TODO: Have Eurpoe's Regional Statics be displayed.
    });
    europeSubPath2.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Europes Regional Stats");
      styleRegionPaths(16, fillColor, 2); // Change Fill Color
      // TODO: Have Eurpoe's Regional Statics be displayed.
    });
    europeSubPath3.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Europes Regional Stats");
      styleRegionPaths(16, fillColor, 2); // Change Fill Color
      // TODO: Have Eurpoe's Regional Statics be displayed.
    });

    europeSubPath1.setOnMouseReleased((event) ->
    {
      styleRegionPaths(16, TRANSPARENT, 2); // Change Fill Color
    });
    europeSubPath2.setOnMouseReleased((event) ->
    {
      styleRegionPaths(16, TRANSPARENT, 2); // Change Fill Color
    });

    europeSubPath3.setOnMouseReleased((event) ->
    {
      styleRegionPaths(16, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().addAll(europeMainPath, europeSubPath1, europeSubPath2,
        europeSubPath3, europeSubPath4, europeSubPath5, europeSubPath6);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build East Asia Region Path.
   */
  public void buildEastAsiaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {

    DropShadow southAmericaBoarderGlow = new DropShadow();
    southAmericaBoarderGlow.setOffsetY(0f);
    southAmericaBoarderGlow.setOffsetX(0f);
    southAmericaBoarderGlow.setColor(GlowColor);
    southAmericaBoarderGlow.setWidth(DEPTH);
    southAmericaBoarderGlow.setHeight(DEPTH);

    SVGPath eastAsiaMainPath = new SVGPath();
    eastAsiaMainPath.setFill(TRANSPARENT);
    eastAsiaMainPath.setStroke(TRANSPARENT);
    eastAsiaMainPath.setStrokeWidth(5.0);
    eastAsiaMainPath.setScaleX(.8);
    eastAsiaMainPath.setScaleY(.8);
    eastAsiaMainPath.setTranslateY(-206);
    eastAsiaMainPath.setTranslateX(499);
    eastAsiaMainPath.setContent(SVG.EAST_ASIA_PATHS[0]);

    SVGPath eastAsiaSubPath1 = new SVGPath();
    eastAsiaSubPath1.setFill(TRANSPARENT);
    eastAsiaSubPath1.setStroke(TRANSPARENT);
    eastAsiaSubPath1.setStrokeWidth(3.0);
    eastAsiaSubPath1.setScaleX(.8);
    eastAsiaSubPath1.setScaleY(.8);
    eastAsiaSubPath1.setTranslateY(-247);
    eastAsiaSubPath1.setTranslateX(681);
    eastAsiaSubPath1.setContent(SVG.EAST_ASIA_PATHS[2]);

    SVGPath eastAsiaSubPath2 = new SVGPath();
    eastAsiaSubPath2.setFill(TRANSPARENT);
    eastAsiaSubPath2.setStroke(TRANSPARENT);
    eastAsiaSubPath2.setStrokeWidth(3.0);
    eastAsiaSubPath2.setScaleX(.8);
    eastAsiaSubPath2.setScaleY(.8);
    eastAsiaSubPath2.setTranslateY(-296);
    eastAsiaSubPath2.setTranslateX(665);
    eastAsiaSubPath2.setContent(SVG.EAST_ASIA_PATHS[3]);

    SVGPath eastAsiaSubPath3 = new SVGPath();
    eastAsiaSubPath3.setFill(TRANSPARENT);
    eastAsiaSubPath3.setStroke(TRANSPARENT);
    eastAsiaSubPath3.setStrokeWidth(3.0);
    eastAsiaSubPath3.setScaleX(.8);
    eastAsiaSubPath3.setScaleY(.8);
    eastAsiaSubPath3.setTranslateY(-163);
    eastAsiaSubPath3.setTranslateX(651);
    eastAsiaSubPath3.setContent(SVG.EAST_ASIA_PATHS[1]);

    eastAsiaPaths.add(eastAsiaMainPath);
    eastAsiaPaths.add(eastAsiaSubPath1);
    eastAsiaPaths.add(eastAsiaSubPath2);
    eastAsiaPaths.add(eastAsiaSubPath3);

    eastAsiaMainPath.setEffect(southAmericaBoarderGlow);
    eastAsiaSubPath1.setEffect(southAmericaBoarderGlow);
    eastAsiaSubPath2.setEffect(southAmericaBoarderGlow);
    eastAsiaSubPath3.setEffect(southAmericaBoarderGlow);

    eastAsiaMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(17, RegionColor, 1); // Change Stroke Color
    });

    eastAsiaMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(17, TRANSPARENT, 1); // Change Stroke Color
    });

    eastAsiaSubPath1.setOnMouseMoved(evt ->
    {
      styleRegionPaths(17, RegionColor, 1); // Change Stroke Color
    });

    eastAsiaSubPath1.setOnMouseExited(evt ->
    {
      styleRegionPaths(17, TRANSPARENT, 1); // Change Stroke Color
    });

    eastAsiaSubPath2.setOnMouseMoved(evt ->
    {
      styleRegionPaths(17, RegionColor, 1); // Change Stroke Color
    });

    eastAsiaSubPath2.setOnMouseExited(evt ->
    {
      styleRegionPaths(17, TRANSPARENT, 1); // Change Stroke Color
    });

    eastAsiaMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger East Asia Regional Stats");
      // TODO: Have East Asia's Regional Statics be displayed.
      styleRegionPaths(17, fillColor, 2); // Change Fill Color
    });

    eastAsiaMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(17, TRANSPARENT, 2); // Change Fill Color
    });
    eastAsiaSubPath1.setOnMousePressed((event) ->
    {
      System.out.println("Trigger East Asia Regional Stats");
      // TODO: Have East Asia's Regional Statics be displayed.
      styleRegionPaths(17, fillColor, 2); // Change Fill Color
    });

    eastAsiaSubPath1.setOnMouseReleased((event) ->
    {
      styleRegionPaths(17, TRANSPARENT, 2); // Change Fill Color
    });

    eastAsiaSubPath2.setOnMousePressed((event) ->
    {
      System.out.println("Trigger East Asia Regional Stats");
      // TODO: Have East Asia's Regional Statics be displayed.
      styleRegionPaths(17, fillColor, 2); // Change Fill Color
    });

    eastAsiaSubPath2.setOnMouseReleased((event) ->
    {
      styleRegionPaths(17, TRANSPARENT, 2); // Change Fill Color
    });
    sp.getChildren().addAll(eastAsiaMainPath, eastAsiaSubPath1,
        eastAsiaSubPath2, eastAsiaSubPath3);
  }

  /**
   * 
   * @param RegionColor
   * @param GlowColor
   * @param fillColor
   *          Build Oceania Region path.
   */
  public void buildOceaniaPath(Color RegionColor, Color GlowColor,
      Color fillColor)
  {
    DropShadow oceaniaBoarderGlow = new DropShadow();
    oceaniaBoarderGlow.setOffsetY(0f);
    oceaniaBoarderGlow.setOffsetX(0f);
    oceaniaBoarderGlow.setColor(GlowColor);
    oceaniaBoarderGlow.setWidth(DEPTH);
    oceaniaBoarderGlow.setHeight(DEPTH);

    SVGPath oceaniaMainPath = new SVGPath();
    oceaniaMainPath.setFill(TRANSPARENT);
    oceaniaMainPath.setStroke(TRANSPARENT);
    oceaniaMainPath.setStrokeWidth(5.0);
    oceaniaMainPath.setScaleX(.8);
    oceaniaMainPath.setScaleY(.8);
    oceaniaMainPath.setTranslateY(168);
    oceaniaMainPath.setTranslateX(700);
    oceaniaMainPath.setContent(SVG.OCEANIA_PATHS[0]);

    SVGPath oceaniaSubPath1 = new SVGPath();
    oceaniaSubPath1.setFill(TRANSPARENT);
    oceaniaSubPath1.setStroke(TRANSPARENT);
    oceaniaSubPath1.setStrokeWidth(5.0);
    oceaniaSubPath1.setScaleX(.8);
    oceaniaSubPath1.setScaleY(.8);
    oceaniaSubPath1.setTranslateY(-2);
    oceaniaSubPath1.setTranslateX(570);
    oceaniaSubPath1.setContent(SVG.OCEANIA_PATHS[1]);

    SVGPath oceaniaSubPath2 = new SVGPath();
    oceaniaSubPath2.setFill(TRANSPARENT);
    oceaniaSubPath2.setStroke(TRANSPARENT);
    oceaniaSubPath2.setStrokeWidth(3.0);
    oceaniaSubPath2.setScaleX(.8);
    oceaniaSubPath2.setScaleY(.8);
    oceaniaSubPath2.setTranslateY(-50);
    oceaniaSubPath2.setTranslateX(666);
    oceaniaSubPath2.setContent(SVG.OCEANIA_PATHS[2]);

    SVGPath oceaniaSubPath3 = new SVGPath();
    oceaniaSubPath3.setFill(TRANSPARENT);
    oceaniaSubPath3.setStroke(TRANSPARENT);
    oceaniaSubPath3.setStrokeWidth(4.0);
    oceaniaSubPath3.setScaleX(.8);
    oceaniaSubPath3.setScaleY(.8);
    oceaniaSubPath3.setTranslateY(52);
    oceaniaSubPath3.setTranslateX(633);
    oceaniaSubPath3.setContent(SVG.OCEANIA_PATHS[3]);

    SVGPath oceaniaSubPath4 = new SVGPath();
    oceaniaSubPath4.setFill(TRANSPARENT);
    oceaniaSubPath4.setStroke(TRANSPARENT);
    oceaniaSubPath4.setStrokeWidth(5.0);
    oceaniaSubPath4.setScaleX(.8);
    oceaniaSubPath4.setScaleY(.8);
    oceaniaSubPath4.setTranslateY(38);
    oceaniaSubPath4.setTranslateX(795);
    oceaniaSubPath4.setContent(SVG.OCEANIA_PATHS[4]);

    SVGPath oceaniaSubPath5 = new SVGPath();
    oceaniaSubPath5.setFill(TRANSPARENT);
    oceaniaSubPath5.setStroke(TRANSPARENT);
    oceaniaSubPath5.setStrokeWidth(5.0);
    oceaniaSubPath5.setScaleX(.8);
    oceaniaSubPath5.setScaleY(.8);
    oceaniaSubPath5.setTranslateY(278);
    oceaniaSubPath5.setTranslateX(812);
    oceaniaSubPath5.setContent(SVG.OCEANIA_PATHS[5]);

    SVGPath oceaniaSubPath6 = new SVGPath();
    oceaniaSubPath6.setFill(TRANSPARENT);
    oceaniaSubPath6.setStroke(TRANSPARENT);
    oceaniaSubPath6.setStrokeWidth(3.0);
    oceaniaSubPath6.setScaleX(.8);
    oceaniaSubPath6.setScaleY(.8);
    oceaniaSubPath6.setTranslateY(22);
    oceaniaSubPath6.setTranslateX(695);
    oceaniaSubPath6.setContent(SVG.OCEANIA_PATHS[6]);

    SVGPath oceaniaSubPath7 = new SVGPath();
    oceaniaSubPath7.setFill(TRANSPARENT);
    oceaniaSubPath7.setStroke(TRANSPARENT);
    oceaniaSubPath7.setStrokeWidth(5.0);
    oceaniaSubPath7.setScaleX(.8);
    oceaniaSubPath7.setScaleY(.8);
    oceaniaSubPath7.setTranslateY(285);
    oceaniaSubPath7.setTranslateX(695);
    oceaniaSubPath7.setContent(SVG.OCEANIA_PATHS[7]);

    SVGPath oceaniaSubPath8 = new SVGPath();
    oceaniaSubPath8.setFill(TRANSPARENT);
    oceaniaSubPath8.setStroke(TRANSPARENT);
    oceaniaSubPath8.setStrokeWidth(3.0);
    oceaniaSubPath8.setScaleX(.8);
    oceaniaSubPath8.setScaleY(.8);
    oceaniaSubPath8.setTranslateY(-75);
    oceaniaSubPath8.setTranslateX(669);
    oceaniaSubPath8.setContent(SVG.OCEANIA_PATHS[8]);

    SVGPath oceaniaSubPath9 = new SVGPath();
    oceaniaSubPath9.setFill(TRANSPARENT);
    oceaniaSubPath9.setStroke(TRANSPARENT);
    oceaniaSubPath9.setStrokeWidth(3.0);
    oceaniaSubPath9.setScaleX(.8);
    oceaniaSubPath9.setScaleY(.8);
    oceaniaSubPath9.setTranslateY(-7);
    oceaniaSubPath9.setTranslateX(696);
    oceaniaSubPath9.setContent(SVG.OCEANIA_PATHS[9]);

    SVGPath oceaniaSubPath10 = new SVGPath();
    oceaniaSubPath10.setFill(TRANSPARENT);
    oceaniaSubPath10.setStroke(TRANSPARENT);
    oceaniaSubPath10.setStrokeWidth(3.0);
    oceaniaSubPath10.setScaleX(.8);
    oceaniaSubPath10.setScaleY(.8);
    oceaniaSubPath10.setTranslateY(-7);
    oceaniaSubPath10.setTranslateX(728);
    oceaniaSubPath10.setContent(SVG.OCEANIA_PATHS[10]);

    SVGPath oceaniaSubPath11 = new SVGPath();
    oceaniaSubPath11.setFill(TRANSPARENT);
    oceaniaSubPath11.setStroke(TRANSPARENT);
    oceaniaSubPath11.setStrokeWidth(3.0);
    oceaniaSubPath11.setScaleX(.8);
    oceaniaSubPath11.setScaleY(.8);
    oceaniaSubPath11.setTranslateY(25);
    oceaniaSubPath11.setTranslateX(730);
    oceaniaSubPath11.setContent(SVG.OCEANIA_PATHS[11]);

    SVGPath oceaniaSubPath12 = new SVGPath();
    oceaniaSubPath12.setFill(TRANSPARENT);
    oceaniaSubPath12.setStroke(TRANSPARENT);
    oceaniaSubPath12.setStrokeWidth(3.0);
    oceaniaSubPath12.setScaleX(.8);
    oceaniaSubPath12.setScaleY(.8);
    oceaniaSubPath12.setTranslateY(67);
    oceaniaSubPath12.setTranslateX(705);
    oceaniaSubPath12.setContent(SVG.OCEANIA_PATHS[12]);

    SVGPath oceaniaSubPath13 = new SVGPath();
    oceaniaSubPath13.setFill(TRANSPARENT);
    oceaniaSubPath13.setStroke(TRANSPARENT);
    oceaniaSubPath13.setStrokeWidth(3.0);
    oceaniaSubPath13.setScaleX(.8);
    oceaniaSubPath13.setScaleY(.8);
    oceaniaSubPath13.setTranslateY(30);
    oceaniaSubPath13.setTranslateX(855);
    oceaniaSubPath13.setContent(SVG.OCEANIA_PATHS[13]);

    SVGPath oceaniaSubPath14 = new SVGPath();
    oceaniaSubPath14.setFill(TRANSPARENT);
    oceaniaSubPath14.setStroke(TRANSPARENT);
    oceaniaSubPath14.setStrokeWidth(3.0);
    oceaniaSubPath14.setScaleX(.8);
    oceaniaSubPath14.setScaleY(.8);
    oceaniaSubPath14.setTranslateY(56);
    oceaniaSubPath14.setTranslateX(894);
    oceaniaSubPath14.setContent(SVG.OCEANIA_PATHS[14]);

    SVGPath oceaniaSubPath15 = new SVGPath();
    oceaniaSubPath15.setFill(TRANSPARENT);
    oceaniaSubPath15.setStroke(TRANSPARENT);
    oceaniaSubPath15.setStrokeWidth(3.0);
    oceaniaSubPath15.setScaleX(.8);
    oceaniaSubPath15.setScaleY(.8);
    oceaniaSubPath15.setTranslateY(124);
    oceaniaSubPath15.setTranslateX(928);
    oceaniaSubPath15.setContent(SVG.OCEANIA_PATHS[15]);

    SVGPath oceaniaSubPath16 = new SVGPath();
    oceaniaSubPath16.setFill(TRANSPARENT);
    oceaniaSubPath16.setStroke(TRANSPARENT);
    oceaniaSubPath16.setStrokeWidth(3.0);
    oceaniaSubPath16.setScaleX(.8);
    oceaniaSubPath16.setScaleY(.8);
    oceaniaSubPath16.setTranslateY(146);
    oceaniaSubPath16.setTranslateX(900);
    oceaniaSubPath16.setContent(SVG.OCEANIA_PATHS[16]);

    SVGPath oceaniaSubPath17 = new SVGPath();
    oceaniaSubPath17.setFill(TRANSPARENT);
    oceaniaSubPath17.setStroke(TRANSPARENT);
    oceaniaSubPath17.setStrokeWidth(3.0);
    oceaniaSubPath17.setScaleX(.8);
    oceaniaSubPath17.setScaleY(.8);
    oceaniaSubPath17.setTranslateY(61);
    oceaniaSubPath17.setTranslateX(690);
    oceaniaSubPath17.setContent(SVG.OCEANIA_PATHS[17]);

    oceaniaMainPath.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath1.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath2.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath3.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath4.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath5.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath6.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath7.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath8.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath9.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath10.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath11.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath12.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath13.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath14.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath15.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath16.setEffect(oceaniaBoarderGlow);
    oceaniaSubPath17.setEffect(oceaniaBoarderGlow);

    oceaniaPaths.add(oceaniaMainPath);
    oceaniaPaths.add(oceaniaSubPath1);
    oceaniaPaths.add(oceaniaSubPath2);
    oceaniaPaths.add(oceaniaSubPath3);
    oceaniaPaths.add(oceaniaSubPath4);
    oceaniaPaths.add(oceaniaSubPath5);
    oceaniaPaths.add(oceaniaSubPath6);
    oceaniaPaths.add(oceaniaSubPath7);
    oceaniaPaths.add(oceaniaSubPath8);
    oceaniaPaths.add(oceaniaSubPath9);
    oceaniaPaths.add(oceaniaSubPath10);
    oceaniaPaths.add(oceaniaSubPath11);
    oceaniaPaths.add(oceaniaSubPath12);
    oceaniaPaths.add(oceaniaSubPath13);
    oceaniaPaths.add(oceaniaSubPath14);
    oceaniaPaths.add(oceaniaSubPath15);
    oceaniaPaths.add(oceaniaSubPath16);
    oceaniaPaths.add(oceaniaSubPath17);

    oceaniaMainPath.setOnMouseMoved(evt ->
    {
      styleRegionPaths(18, RegionColor, 1); // Change Stroke Color
    });

    oceaniaMainPath.setOnMouseExited(evt ->
    {
      styleRegionPaths(18, TRANSPARENT, 1); // Change Stroke Color
    });
    oceaniaSubPath1.setOnMouseMoved(evt ->
    {
      styleRegionPaths(18, RegionColor, 1); // Change Stroke Color
    });

    oceaniaSubPath1.setOnMouseExited(evt ->
    {
      styleRegionPaths(18, TRANSPARENT, 1); // Change Stroke Color
    });
    oceaniaSubPath2.setOnMouseMoved(evt ->
    {
      styleRegionPaths(18, RegionColor, 1); // Change Stroke Color
    });

    oceaniaSubPath2.setOnMouseExited(evt ->
    {
      styleRegionPaths(18, TRANSPARENT, 1); // Change Stroke Color
    });
    oceaniaSubPath4.setOnMouseMoved(evt ->
    {
      styleRegionPaths(18, RegionColor, 1); // Change Stroke Color
    });

    oceaniaSubPath4.setOnMouseExited(evt ->
    {
      styleRegionPaths(18, TRANSPARENT, 1); // Change Stroke Color
    });

    oceaniaSubPath1.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Oceania Regional Stats");
      // TODO: Have Oceania's Regional Statics be displayed.
      styleRegionPaths(18, fillColor, 2); // Change Fill Color
    });

    oceaniaSubPath1.setOnMouseReleased((event) ->
    {
      styleRegionPaths(18, TRANSPARENT, 2); // Change Fill Color
    });

    oceaniaSubPath2.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Oceania Regional Stats");
      // TODO: Have Oceania's Regional Statics be displayed.
      styleRegionPaths(18, fillColor, 2); // Change Fill Color
    });

    oceaniaSubPath2.setOnMouseReleased((event) ->
    {
      styleRegionPaths(18, TRANSPARENT, 2); // Change Fill Color
    });

    oceaniaSubPath4.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Oceania Regional Stats");
      // TODO: Have Oceania's Regional Statics be displayed.
      styleRegionPaths(18, fillColor, 2); // Change Fill Color
    });

    oceaniaSubPath4.setOnMouseReleased((event) ->
    {
      styleRegionPaths(18, TRANSPARENT, 2); // Change Fill Color
    });

    oceaniaMainPath.setOnMousePressed((event) ->
    {
      System.out.println("Trigger Oceania Regional Stats");
      // TODO: Have Oceania's Regional Statics be displayed.
      styleRegionPaths(18, fillColor, 2); // Change Fill Color
    });

    oceaniaMainPath.setOnMouseReleased((event) ->
    {
      styleRegionPaths(18, TRANSPARENT, 2); // Change Fill Color
    });

    sp.getChildren().addAll(oceaniaMainPath, oceaniaSubPath1, oceaniaSubPath2,
        oceaniaSubPath3, oceaniaSubPath4, oceaniaSubPath5, oceaniaSubPath6,
        oceaniaSubPath7, oceaniaSubPath8, oceaniaSubPath9, oceaniaSubPath10,
        oceaniaSubPath11, oceaniaSubPath12, oceaniaSubPath13, oceaniaSubPath14,
        oceaniaSubPath15, oceaniaSubPath16, oceaniaSubPath17);
  }

  /**
   * 
   * @param Region
   * @param color
   * @param mode
   *          Method to change styles of a region e.g the boarder color or fill
   *          color, this method allows to get rid of (some..) repetitive code.
   *          Let Mode = 1 represent setting region paths stroke Let Mode = 2
   *          represent setting region paths fill
   * 
   */
  private void styleRegionPaths(int Region, Color color, int mode)
  {
    switch (Region)
    {
    case 1: // Africa
      if (mode == 1)
      {
        for (SVGPath path : africaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : africaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 2: // South America
      if (mode == 1)
      {
        for (SVGPath path : southAmericaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : southAmericaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 3: // Middle America
      if (mode == 1)
      {
        for (SVGPath path : middleAmericaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : middleAmericaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 4: // California
      if (mode == 1)
      {
        for (SVGPath path : californiaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : californiaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 5: // PWN and MNT
      if (mode == 1)
      {
        for (SVGPath path : PNWandMNTPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : PNWandMNTPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 6: // Northern Planes
      if (mode == 1)
      {
        for (SVGPath path : northernPlanesPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : northernPlanesPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 7: // Heart Land
      if (mode == 1)
      {
        for (SVGPath path : heartLandPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : heartLandPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 8: // Southern Planes and Delta States
      if (mode == 1)
      {
        for (SVGPath path : southernPlanesDeltaStatesPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : southernPlanesDeltaStatesPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 9: // South East
      if (mode == 1)
      {
        for (SVGPath path : southEastPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : southEastPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 10: // Northern Crescent
      if (mode == 1)
      {
        for (SVGPath path : northernCrescentPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : northernCrescentPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 11: // Middle East
      if (mode == 1)
      {
        for (SVGPath path : middleEastPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : middleEastPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 12: // Central Asia
      if (mode == 1)
      {
        for (SVGPath path : centralAsiaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : centralAsiaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 13: // South Asia
      if (mode == 1)
      {
        for (SVGPath path : southAsiaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : southAsiaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 14: // Arctic America
      if (mode == 1)
      {
        for (SVGPath path : arcticAmericaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : arcticAmericaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 15: // Russia and Caucaus
      if (mode == 1)
      {
        for (SVGPath path : russiaCaucausPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : russiaCaucausPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 16: // Europe
      if (mode == 1)
      {
        for (SVGPath path : europePaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : europePaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 17: // East Asia
      if (mode == 1)
      {
        for (SVGPath path : eastAsiaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : eastAsiaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    case 18: // Oceania
      if (mode == 1)
      {
        for (SVGPath path : oceaniaPaths)
        {
          path.setStroke(color);
        }
      }
      if (mode == 2)
      {
        for (SVGPath path : oceaniaPaths)
        {
          path.setFill(color);
        }
      }
      break;
    default:
      break;
    }
  }

}
