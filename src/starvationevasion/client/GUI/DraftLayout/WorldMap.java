package starvationevasion.client.GUI.DraftLayout;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.MapProjectionMollweide;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Territory;

//import java.awt.*;

/**
 * Created by Dayloki on 3/29/2016.
 */
public class WorldMap extends ScrollPane
{
	private static final String PATH_COORDINATES = "/sim/climate/ArableCoordinates.csv";
	private static final String PATH_CLIMATE = "/sim/climate/Climate_";
	private static final String PREFIX_HISTORICAL = "Historical";
	private static final String PREFIX_RCP45 = "RCP45";
	private static final String PREFIX_RCP85 = "RCP85";
  private GUI gui;
  private double zoomLevel=1;
  private static StackPane zoomGroup = new StackPane();
  private BuildInteractiveRegionBoarders buildBoarders;
  private Canvas canvas;
  Image imageMap;
  public WorldMap(GUI gui)
  {
   //System.out.println("SDFSD");
   // setHbarPolicy(ScrollBarPolicy.NEVER);
   // setVbarPolicy(ScrollBarPolicy.NEVER);
    this.gui=gui;
    imageMap=gui.getImageGetter().getWorldMap();
    ImageView worldMap=new ImageView(imageMap);

    VBox holder=new VBox();
    holder.getChildren().add(worldMap);
    //    worldMap.setFitWidth(gui.getBoxWidth() * 10);
    //    worldMap.setLayoutX(gui.getBoxWidth() * 10);
    //worldMap.setFitHeight(gui.getBoxHeight()*6);
    worldMap.setPreserveRatio(true);
    worldMap.setSmooth(true);
    worldMap.setCache(true);
    // worldMap.fitHeightProperty().bind(gui.getPrimaryStage().heightProperty());
    //  worldMap.fitWidthProperty().bind(gui.getPrimaryStage().widthProperty());
    
    //zoomGroup.add
    setHbarPolicy(ScrollBarPolicy.NEVER);
    setVbarPolicy(ScrollBarPolicy.NEVER);
    setPannable(true);
    zoomGroup = new StackPane();
    zoomGroup.getChildren().add(worldMap);
    setContent(zoomGroup);
    canvas=new Canvas(imageMap.getWidth(),imageMap.getHeight());
    MapProjectionMollweide map = new MapProjectionMollweide((int)imageMap.getWidth(), (int)imageMap.getHeight());
    
//    zoomGroup.setWidth(imageMap.getWidth()*4);
//    zoomGroup.setHeight(imageMap.getHeight()*4);
    zoomGroup.getChildren().add(canvas);
    
    //Create an object to build the regions interactive boarders. 
    buildBoarders = new BuildInteractiveRegionBoarders(zoomGroup,gui);   
    //Build each regions boarders. 
    //NOTE Parameters are as follow, (Region Color, Glow Color, Fill Color)
    buildBoarders.buildAfricaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildSouthAmericaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildMiddleAmericaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildCaliforniaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildHeartLandsPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildMiddleAmericaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildNorthernCrescentPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildNorthernPlanesPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildPNWAndMNTPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildSoutheastPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildSouthernPlanesDeltaStatesPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildMiddleEastPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildCentralAsiaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildSouthAsiaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildArcticAmericaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildRussiaCaucausPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildEuropePath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildEastAsiaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    buildBoarders.buildOceaniaPath(Color.CYAN, Color.CYAN, Color.rgb(0, 255, 255,0.5));
    
//    CSVReader coorFileReader = new CSVReader(PATH_COORDINATES, 1);
    String[] fieldList;
    Territory territory = null;

    ArrayList<LandTile> tileList = new ArrayList<>();
    Point p = new Point();
    Random r = new Random();
    double d;
//    while ((fieldList = coorFileReader.readRecord(2)) != null)
//    {
//      float latitude = Float.parseFloat(fieldList[0]);
//      float longitude = Float.parseFloat(fieldList[1]);
//       d = Math.random();
//    //  if (d < 0.5)
//     // {
//      map.setPoint(p, latitude, longitude);
//	  gfx.fillOval(p.x, p.y, 3, 3);
//    //  }
//
//    }
//    coorFileReader.close();
    //worldMap.setImage(timageMap);
    
    
//    
//  
//    for(double i = -90.0; i <=90.0 ; i+=.2)
//    {
//    	for(double j = -180.0; j <= 180.0; j+=.2)
//    	{
//    		map.setPoint(p, i, j);
//    		System.out.println(p.x + " " + p.y);
//    	    gfx.fillOval(p.x, p.y, 1, 1);
//    	}
//    }

    //drawOnMap();
    //drawOutline();
    
    addEventFilter(ScrollEvent.SCROLL, event -> {

      //      DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
//      worldMap.setFitHeight(zoomProperty.get());
//      worldMap.setFitWidth(zoomProperty.get());

      Group contentGroup = new Group();

      contentGroup.getChildren().add(zoomGroup);

      setContent(contentGroup);
      if(event.getDeltaY()>0&&zoomLevel<1.5)zoomLevel += .1;
      else if(event.getDeltaY()<0 &&zoomLevel>1) zoomLevel -= .1;
      Scale scaleTransform = new Scale(zoomLevel, zoomLevel, 0,0);
      zoomGroup.setScaleX(zoomLevel);
      zoomGroup.setScaleY(zoomLevel);
      // Scale scaleTransform = new Scale(2, 2, -100, -10000000);
      // scaleTransform.transform(event.getX(),event.getY());
       scaleTransform.setPivotX(-event.getX());
       scaleTransform.setPivotY(-event.getY());

      double oldHvalue=getHvalue();
      double oldVvalue=getVvalue();
      setHvalue(event.getX() /getWidth() + oldHvalue * zoomLevel);
      setVvalue(event.getY()/ getHeight()+oldVvalue*zoomLevel);

      //setHvalue(.75);
      //setVvalue(.75);

      //zoomGroup.getTransforms().removeAll();
      //zoomGroup.getTransforms().add(scaleTransform);
    });
  }
  
  
  public BuildInteractiveRegionBoarders getBoardersManager()
  {
    return buildBoarders;
  }
  
  
  /**
   * 
   * @return Reference to Graphics context. 
   */
  public GraphicsContext getGraphicsContext()
  {
    return canvas.getGraphicsContext2D();
  }
  
  
  /*
   * 
   * Get instance of the bane containing
   * the world map
   */
  public StackPane getWorldPane()
  {
	  return zoomGroup;
  }
  
//  

  

}