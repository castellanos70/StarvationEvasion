package starvationevasion.vis.controller;


import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import starvationevasion.vis.visuals.Earth;
import starvationevasion.vis.visuals.ResourceLoader;
import starvationevasion.vis.visuals.SpecialEffect;
import starvationevasion.vis.visuals.VisualizerLayout;

import java.util.Queue;

/**
 * http://stackoverflow.com/questions/19621423/javafx-materials-bump-and-spec-maps
 * Original Author:jewelsea,http://stackoverflow.com/users/1155209/jewelsea
 * Modified by:Tess Daughton
 **/

public class EarthViewer
{
  public static Queue<Event> SIM_EVENTS;
  private  VisualizerLayout visLayout;
  public static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();
  public Earth earth;
  private Group userView;
  private SpecialEffect specialEffect;
  private int LARGE_EARTH_RADIUS;

  public EarthViewer(int smallEarthRadius, int largeEarthRadius)
  {
    earth = new Earth(smallEarthRadius, largeEarthRadius);
    specialEffect = new SpecialEffect(earth);
//    specialEffect.buildClouds();
    specialEffect.buildPinPoint(-45,0);


    this.LARGE_EARTH_RADIUS=largeEarthRadius;
  }

  /**
   * Used when Toggling into full Earth Mode
   * Returns a layout manager for the Visualizer GUI
   * use this line inside ClientGUI: Scene earthScene = new Scene(earthViewer.updateFull(),700,700);
   * See handle() method inside ClientGUI for more info
   * @return  Layout manager for Visualizer to use as Root for new Large Earth Scene
   */
  public VisualizerLayout updateFull()
  {
    return new VisualizerLayout(this,LARGE_EARTH_RADIUS);
  }

  /**
   * Returns small rotating non interactive globe for display in corner of ClientGUI
   * @return
   */
  public Group updateMini()
  {
    userView = earth.getSmallEarth();
    return userView;
  }

  /**
   * Called yearly/whenever Client decides to update Global Events (e.g., beginning of turn, beginning of year).
   * Pass in an array with global event data for Visualizer to parse and then display
   * @param eventData
   */
  public void updateEvents(String[] eventData)
  {}

  /**
   * To be called by ClientGUI to add Vis style sheet to the Scene client creates
   * @param scene Client created Scene using VisualizerLayout (returned by updateFull) as root
   */
  public void addVisStyleSheet(Scene scene)
  {
    scene.setCamera(new PerspectiveCamera());
    scene.getStylesheets().add(RESOURCE_LOADER.STYLE_SHEET);
  }

  /**
   * Utilized by VisualizerLayout to access Earth
   * @return  earth object containing earth Graphics stuff to embed in VisualizerLayout
   */
  public Earth getEarth()
  {
    return earth;
  }

}

