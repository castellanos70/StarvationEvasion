package starvationevasion.vis.visuals;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import starvationevasion.common.EnumFood;
import starvationevasion.vis.controller.EarthViewer;

import javax.swing.*;

/**
 * Created by Tess Daughton on 11/23/15.
 * Layout Manager for LargeEarthMode GUI
 */
public class VisualizerLayout extends BorderPane
{
  private ResourceLoader RESOURCE_LOADER = EarthViewer.RESOURCE_LOADER;
  private UserEventHandler userEventHandler;
  private SpecialEffect specialEffect;
  private int currentEffect = 0;
  private Earth earth;
  private Group earthGroup;
  private Group earthOverlay;
  private Group earthWeather = new Group();
//  private Group heatMapWeather = new Group();
  private PointLight pointLight = new PointLight();
  private boolean earthRotating = true;
  private boolean showOverlay = false;
  private boolean showClouds = false;
  private boolean showHeatMap = false;

  private VBox earthInfo;
  private VBox left = new VBox();
  private VBox cropNames = new VBox();
  private VBox cropNums = new VBox();
  private GridPane earthInfoGrid;
  private GridPane center;

  private Label country;
  private Label latLong;
  private Label avgTemp;
  private Label crop_Names;
  private Label crop_Nums;

  private Button regionOverlay;
  private Button weather;
  private Button heatMap;
  private Button rotate;
  private Button nextEffect;

  /**
   * VisualizerLayout Constructor
   * Adds the custom event handler to this layout, called UserEventHandler
   * Also adds a style sheet to the layout, located in assets/style.css
   * Creates a 3D view of Earth and initializes it to the center of the BorderPane
   */
  public VisualizerLayout(EarthViewer earthViewer, int largeRadius)
  {
    earth = earthViewer.getEarth();
    earthGroup = earth.getEarth();
    this.initEarth();
    this.initOverlays();
    this.initEarthInfo();
    this.initEventHandling(largeRadius);
    specialEffect = new SpecialEffect(earthWeather);
    specialEffect.buildClouds();
//    specialEffect.buildEffect("hurricane", 180.0, 0.0);
//    specialEffect.buildEffect("forestFire", 0.0, 20.0);
//    specialEffect.buildEffect("flood", 0.0, -20.0);
//    specialEffect.buildEffect("drought", 20.0, 0.0);
//    specialEffect.buildEffect("blight", 20.0, -20.0);
  }

  /**
   * Gets the Earth group from the Earth class and then scales it (might not be necessary, I will test without)
   * Requests focus so that event handling will be enabled inside of UserEventHandler
   */
  private void initEarth()
  {
    center=new GridPane();
    earthGroup = earth.getEarth();
    earthGroup.setScaleX(1.0);
    earthGroup.setScaleY(1.0);
    earthGroup.setScaleZ(1.0);
    earthGroup.setDisable(false);
    earthGroup.requestFocus();
    this.setPrefSize(800, 600);
    center.add(earthGroup, 0, 0);
    center.setPadding(new Insets(20, 20, 0, 0));
    center.setMaxWidth(700);

    this.setCenter(center);
    pointLight.setColor(Color.WHITE);
    pointLight.setTranslateZ(-4000);
    pointLight.setTranslateX(4000);
    pointLight.setTranslateY(-1000);
    this.getChildren().add(pointLight);
  }

  /**
   *  Initialize gui info for food, position, temp
   */
  private void initEarthInfo()
  {
    earthInfoGrid = new GridPane();
    earthInfo = new VBox();
    earthInfo.setMaxWidth(300);
    earthInfo.setPrefWidth(300);
    earthInfo.setMaxHeight(600);
    earthInfo.setPrefHeight(300);
    earthInfo.setSpacing(20);
    earthInfoGrid.setMaxWidth(300);
    earthInfoGrid.setPrefWidth(300);
    left.setId("earthInfo");

    country = new Label("Country: ");
    latLong = new Label("Global Position: ");
    avgTemp = new Label("Average Temperature: ");
    crop_Names = new Label("Crops Grown: ");
    crop_Nums = new Label("\n\n");
    crop_Nums.setPadding(new Insets(15,0,0,0));
    rotate = new Button("Earth Rotation: Off");
    weather = new Button("Show Weather Events");
    regionOverlay = new Button("Show Region Map");
    heatMap = new Button("Show Heat Map");
    nextEffect = new Button("Next Effect: Hurricane");
    crop_Names.setTextAlignment(TextAlignment.LEFT);
    crop_Nums.setTextAlignment(TextAlignment.LEFT);
    country.setTextFill(Color.WHITE);
    latLong.setTextFill(Color.WHITE);
    avgTemp.setTextFill(Color.WHITE);
    crop_Names.setTextFill(Color.WHITE);
    crop_Nums.setTextFill(Color.WHITE);

    weather.setTextFill(Color.WHITE);
    rotate.setTextFill(Color.WHITE);
    heatMap.setTextFill(Color.WHITE);
    regionOverlay.setTextFill(Color.WHITE);
    nextEffect.setTextFill(Color.WHITE);
    weather.setPrefWidth(200);
    rotate.setPrefWidth(200);
    heatMap.setPrefWidth(200);
    regionOverlay.setPrefWidth(200);
    nextEffect.setPrefWidth(200);
    weather.setId("button");
    rotate.setId("button");
    heatMap.setId("button");
    regionOverlay.setId("button");
    nextEffect.setId("button");
    cropNames.getChildren().add(crop_Names);
    cropNums.getChildren().add(crop_Nums);
    earthInfoGrid.getColumnConstraints().add(new ColumnConstraints(125));
    earthInfoGrid.add(crop_Names, 0, 0);
    earthInfoGrid.add(crop_Nums, 1, 0);
    earthInfo.getChildren().addAll(country, latLong, avgTemp, rotate, weather, regionOverlay, heatMap, nextEffect);
    left.getChildren().addAll(earthInfo,earthInfoGrid);
    this.setLeft(left);

  }

  /**
   * Gets necessary overlays from Earth class
   */
  private void initOverlays()
  {
    earthOverlay = earth.getEarthOverlay();
    earthWeather = earth.getEarthWeather();
//    heatMapWeather = earth.getEarthHeatMap();
  }

  /**
   * @param largeRadius - sets the earth size
   * initializes all events on the gui.
   */
  private void initEventHandling(int largeRadius)
  {
    userEventHandler = new UserEventHandler(earth, this);
    userEventHandler.setLargeEarthRadius(largeRadius);
    earthGroup.addEventFilter(MouseDragEvent.DRAG_DETECTED, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(MouseEvent.MOUSE_MOVED, event -> userEventHandler.handle(event));

    earthGroup.addEventFilter(ScrollEvent.ANY, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(ZoomEvent.ANY, event -> userEventHandler.handle(event));
    weather.setOnAction(event -> handleWeather(event));
    heatMap.setOnAction(event -> handleHeatMap(event));
    regionOverlay.setOnAction(event -> handleOverlay(event));
    rotate.setOnAction(event -> handleRotate(event));
    nextEffect.setOnAction(event -> handleNextEffect(event));

  }

  /**
   * @param regionName - sets regionName for gui to display
   */
  protected void setRegionString(String regionName)
  {
    country.setText("Country: " + regionName);
  }

  /**
   * @param lat - latitude
   * @param lon - longitude
   * sets params for the gui display
   */
  protected void setLatLong(double lat, double lon)
  {
    latLong.setText("Global Position: " + String.format("%.3f, %.3f", lat, lon));
  }

  /**
   * @param temperature - sets temperature for gui to display
   */
  protected void setTemperature(float temperature)
  {
    if (temperature == Float.MAX_VALUE)
    {
      avgTemp.setText("Average temperature: No value found");
    }
    else
    {
      avgTemp.setText("Average temperature: " + String.format("%.3f", temperature));
    }
  }

  /**
   * @param data - int array of food, sent to the gui
   */
  protected void setFoodProduced(int[] data)
  {
    String names = "Food:\n";
    String nums = "";
    if (data != null && data.length == EnumFood.SIZE)
    {
      for (int i = 0; i < data.length; i++)
      {
        names += (data[i] > 0) ? EnumFood.values()[i].name() + "\n" : "";
        nums +=  (data[i] > 0) ? data[i] + "\n" : "";
      }
    }
    else
    {
      names = "Food: N/A";
    }
    crop_Names.setText(names);

    crop_Nums.setText(nums);
  }
  /**
   * Called when user specifies they want to see earthOverlay inside of UserEventHandler
   * Will attach transparent earthOverlay to earthGroup
   */
  protected void showOverlay()
  {
    earthGroup.getChildren().add(earthOverlay);
  }

  /**
   *Called when user specifies they want to see weather earthOverlay inside of UserEventHandler
   * Will attach transparent earthOverlay to earthGroup
   */
  protected void showWeather()
  {
    earthGroup.getChildren().add(earthWeather);
  }

  /**
   *Called when user specifies they want to see heat map earthOverlay inside of UserEventHandler
   * Will attach transparent earthOverlay to earthGroup
   */
  protected void showHeatMap()
  {
    earthGroup.getChildren().add(earth.getEarthHeatMap());
  }

  /**
   * Remove transparent overlay
   */
  protected void removeOverlay()
  {
    earthGroup.getChildren().remove(earthOverlay);
  }

  /**
   * Remove transparent weather overlay
   */
  protected void removeWeather()
  {
    earthGroup.getChildren().remove(earthWeather);
  }

  /**
   * Remove transparent heat map overlay
   */
  protected void removeHeatMap()
  {
    earthGroup.getChildren().remove(earth.getEarthHeatMap());
  }

  /**
   * @param event - heat weather event
   * takes a button press and acts accordingly
   * */
  public void handleWeather(ActionEvent event)
  {
    if (showClouds)
    {
      weather.setText("Show Weather Events");
      this.removeWeather();
      showClouds = false;
    } else
    {
      if(showOverlay) {
        regionOverlay.setText("Show Region Map");
        removeOverlay();
        showOverlay=false;

      }
      weather.setText("Hide Weather Events");
      this.showWeather();
      showClouds = true;
    }
  }

  /**
   * @param event - handle overlay
   * takes a button press and acts accordingly
   * */
  public void handleOverlay(ActionEvent event)
  {
    if (showOverlay)
    {
      regionOverlay.setText("Show Region Map");
      this.removeOverlay();
      showOverlay = false;
    } else
    {
      if(showClouds) {
        weather.setText("Show Weather Events");
        removeWeather();
        showClouds=false;
      }
      regionOverlay.setText("Hide Region Map");
      this.showOverlay();
      showOverlay = true;
    }
  }

  /**
   * @param event - handle rotate
   * takes a button press and acts accordingly
   * */
  public void handleRotate(ActionEvent event)
  {
    if (earthRotating)
    {
      rotate.setText("Earth Rotation: Off");
      earth.pauseRotation();
      earthRotating=false;
    }
    else
    {
      rotate.setText("Earth Rotation: On");
      earth.resumeRotation();
      earthRotating=true;
    }
  }

  /**
   * @param event - heat map event
   * takes a button press and acts accordingly
   * */
  public void handleHeatMap(ActionEvent event)
  {
    if (showHeatMap)
    {
      heatMap.setText("Show Heat Map");
      this.removeHeatMap();
      showHeatMap = false;
    } else
    {
      heatMap.setText("Hide Heat Map");
      this.showHeatMap();
      showHeatMap = true;
    }
  }

  public void handleNextEffect(ActionEvent event)
  {
    // change through effects
    currentEffect++;
    if(currentEffect>5) currentEffect = 0;

    String effect = "";
    if(currentEffect == 0) effect = "Hurricane";
    else if(currentEffect == 1) effect = "Forest Fire";
    else if(currentEffect == 2) effect = "Flood";
    else if(currentEffect == 3) effect = "Drought";
    else if(currentEffect == 4) effect = "Blight";
    else if(currentEffect == 5) effect = "Clear";
    nextEffect.setText("Next Effect: " + effect);

    specialEffect.removeSpecialEffects();
    if(currentEffect == 1) specialEffect.buildEffect("hurricane",0,0);
    else if(currentEffect == 2) specialEffect.buildEffect("forestFire",0,0);
    else if(currentEffect == 3) specialEffect.buildEffect("flood",0,0);
    else if(currentEffect == 4) specialEffect.buildEffect("drought",0,0);
    else if(currentEffect == 5) specialEffect.buildEffect("blight",0,0);

  }

}
