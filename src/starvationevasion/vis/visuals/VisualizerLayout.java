package starvationevasion.vis.visuals;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
  private Earth earth;
  private Group earthGroup;
  private Group earthOverlay;
  private Group earthWeather;
  private PointLight pointLight = new PointLight();
  private boolean earthRotating = true;
  private boolean showOverlay = false;
  private boolean showClouds = false;

  private VBox earthInfo;
  private GridPane center;

  private Label country;
  private Label latLong;
  private Label avgTemp;
  private Label crops;

  private Button regionOverlay;
  private Button weather;
  private Button rotate;



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
    this.setPrefSize(500, 500);
    center.add(earthGroup,0,0);
    this.setCenter(center);
    pointLight.setColor(Color.WHITE);
    pointLight.setTranslateZ(-4000);
    pointLight.setTranslateX(4000);
    pointLight.setTranslateY(-1000);
    this.getChildren().add(pointLight);
  }

  private void initEarthInfo()
  {
    earthInfo = new VBox();
    earthInfo.setMaxWidth(300);
    earthInfo.setPrefWidth(300);
    earthInfo.setMaxHeight(200);
    earthInfo.setPrefHeight(200);
    earthInfo.setId("earthInfo");
    country = new Label("Country: ");
    latLong = new Label("Global Position: ");
    avgTemp = new Label("Average Temperature: ");
    crops = new Label("Crops Grown: ");
    rotate = new Button("Earth Rotation: Off");
    weather = new Button("Show Weather Events");
    regionOverlay = new Button("Show Region Map");
    country.setTextFill(Color.WHITE);
    latLong.setTextFill(Color.WHITE);
    avgTemp.setTextFill(Color.WHITE);
    crops.setTextFill(Color.WHITE);
    weather.setTextFill(Color.WHITE);
    rotate.setTextFill(Color.WHITE);
    regionOverlay.setTextFill(Color.WHITE);
    weather.setId("button");
    rotate.setId("button");
    regionOverlay.setId("button");
    earthInfo.getChildren().addAll(country, latLong, avgTemp, crops, rotate, weather, regionOverlay);
    this.setLeft(earthInfo);

  }

  /**
   * Gets necessary overlays from Earth class
   */
  private void initOverlays()
  {
    earthOverlay = earth.getEarthOverlay();
    earthWeather = earth.getEarthWeather();
  }

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
    regionOverlay.setOnAction(event -> handleOverlay(event));
    rotate.setOnAction(event->handleRotate(event));

  }

  protected void setRegionString(String regionName)
  {
    country.setText("Country: " + regionName);
  }

  protected void setLatLong(double lat, double lon)
  {
    latLong.setText("Global Position: " + String.format("%.3f, %.3f", lat, lon));
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

  public void handleWeather(ActionEvent event)
  {
    if (showClouds)
    {
      weather.setText("Show Weather Events");
      this.removeWeather();
      showClouds = false;
    } else
    {
      weather.setText("Hide Weather Events");
      this.showWeather();
      showClouds = true;
    }
  }
  public void handleOverlay(ActionEvent event)
  {
    if (showOverlay)
    {
      regionOverlay.setText("Show Region Map");
      this.removeOverlay();
      showOverlay = false;
    } else
    {
      regionOverlay.setText("Hide Region Map");
      this.showOverlay();
      showOverlay = true;
    }
  }
  public void handleRotate(ActionEvent event)
  {
    if (earthRotating)
    {
      rotate.setText("Earth Rotation: Off");
      earth.pauseRotation();
    }
    else
    {
      rotate.setText("Earth Rotation: On");
      earth.resumeRotation();
    }
  }
}
