package starvationevasion.vis.controller;


import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;
import starvationevasion.common.SpecialEventData;
import starvationevasion.vis.visuals.Earth;
import starvationevasion.vis.visuals.ResourceLoader;
import starvationevasion.vis.visuals.SpecialEffect;
import starvationevasion.vis.visuals.VisualizerLayout;

import java.util.ArrayList;
import java.util.HashMap;
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
    specialEffect.buildClouds();
    specialEffect.buildPinPoint(0,20);
    specialEffect.buildEffect("hurricane", 180.0, 0.0);
    specialEffect.buildEffect("forestFire", 0.0, 20.0);
    specialEffect.buildEffect("flood", 0.0, -20.0);
    specialEffect.buildEffect("drought", 20.0, 0.0);
    specialEffect.buildEffect("blight", 20.0, -20.0);


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
   * @param specialEventData contains each of the six special event data. Each specialeventdata can contain multiple locations.
   */
  public void updateEvents(ArrayList<SpecialEventData> specialEventData)
  {

  }

  public void updateTemperature(HashMap<MapPoint, Float> data)
  {
    if (data != null) earth.setTemperatures(data);

    /* testing purposes */
//    int count = 0;
//    for (Map.Entry<MapPoint, Float> entry : data.entrySet())
//    {
//      int x = (int)entry.getKey().latitude;
//      int y = (int)entry.getKey().longitude;
//      if (entry.getValue() - earth.getTemperature(x, y) > .01)
//      {
//        System.out.println(entry.getValue() + ":" +earth.getTemperature(x, y));
//      }
//      count++;
//      if (count % 100 == 0) System.out.println(count + "/" + data.entrySet().size());
//    }

  }

  public void updateFoodProduced(HashMap<EnumRegion, int[]> data)
  {
    if (data != null) earth.setFoodProducedData(data);
  }

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

