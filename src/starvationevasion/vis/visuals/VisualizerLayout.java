package starvationevasion.vis.visuals;

import javafx.event.Event;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import starvationevasion.vis.controller.EarthViewer;

/**
 * Created by Tess Daughton on 11/23/15.
 * Layout Manager for LargeEarthMode GUI
 */
public class VisualizerLayout extends BorderPane
{
  private  ResourceLoader RESOURCE_LOADER = EarthViewer.RESOURCE_LOADER;
  private  UserEventHandler userEventHandler;

  private Earth earth;
  private Group earthGroup;
  private Group earthOverlay;
  private Group earthWeather;
  private String regionTitle;
  private boolean showOverlay;
  private boolean showClouds;
  private PerspectiveCamera camera = new PerspectiveCamera();
  private PointLight pointLight = new PointLight();

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
    this.initEventHandling(largeRadius);
    this.initEarth();
    this.initKeyBindings();
    this.initOverlays();
  }

  /**
   * Gets the Earth group from the Earth class and then scales it (might not be necessary, I will test without)
   * Requests focus so that event handling will be enabled inside of UserEventHandler
   */
  private void initEarth()
  {
    earthGroup = earth.getEarth();
    earthGroup.setScaleX(1.0);
    earthGroup.setScaleY(1.0);
    earthGroup.setScaleZ(1.0);
    earthGroup.setDisable(false);
    earthGroup.requestFocus();
    this.setPrefSize(500, 500);
    this.setCenter(earthGroup);
    pointLight.setColor(Color.WHITE);
    pointLight.setTranslateZ(-4000);
    pointLight.setTranslateX(4000);
    pointLight.setTranslateY(-1000);
    this.getChildren().add(pointLight);
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
    userEventHandler = new UserEventHandler(earthGroup);
    userEventHandler.setLargeEarthRadius(largeRadius);
    earthGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(ScrollEvent.ANY, event -> userEventHandler.handle(event));
    earthGroup.addEventFilter(ZoomEvent.ANY, event -> userEventHandler.handle(event));



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

  private void initKeyBindings()
  {
    this.setOnKeyPressed(event ->
    {
      switch (event.getCode())
      {
        case P:
          //currently I cannot find a stop for this animation
          earth.startRotate();
          break;

        case R:
          if (showOverlay)
          {
            this.removeOverlay();
            showOverlay = false;
          }
        else
          { this.showOverlay();
            showOverlay = true;
          }
          break;

        case C:
          if (showClouds)
          {
            this.removeWeather();
            showClouds = false;
          } else
          {
            this.showWeather();
            showClouds = true;


          }
        break;


    }
  });
}

  /**
   * Called inside UserEventHandler when user clicks on a region
   * @param s   Title of the region to be displayed
   */
  public void setRegionTitle(String s)
  {
    regionTitle = s;
  }

  /**
   * @return
   */
  public String getRegionTitle()
  {
    return regionTitle;
  }
}
