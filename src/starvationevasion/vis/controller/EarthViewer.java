package starvationevasion.vis.controller;


import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import starvationevasion.vis.visuals.Earth;
import starvationevasion.vis.visuals.ResourceLoader;
import starvationevasion.vis.visuals.SpecialEffect;
import starvationevasion.vis.visuals.VisualizerLayout;

import java.util.Queue;
//import starvationevasion.visuals.smallevents.CropsTest;


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
//    specialEffect = new SpecialEffect(earth);
//    specialEffect.buildClouds();
//    specialEffect.buildPinPoint(-20,40);
    this.LARGE_EARTH_RADIUS=largeEarthRadius;
  }

  public VisualizerLayout updateFull()
  {
    return new VisualizerLayout(this,LARGE_EARTH_RADIUS);
  }
  public Group updateMini()
  {
    userView = earth.getSmallEarth();
    return userView;
  }

  public void updateEvents(String[] eventData)
  {}

  public void addVisStyleSheet(Scene scene)
  {
    scene.setCamera(new PerspectiveCamera());
    scene.getStylesheets().add(RESOURCE_LOADER.STYLE_SHEET);

  }
  public Earth getEarth()
  {
    return earth;
  }

}

