package starvationevasion.vis.controller;

import java.util.ArrayList;

/**
 * Created by Tess Daughton on 11/15/15.
 * Check out the link below to see a visual representation of what this class is supposed to be representing.
 * http://www.colorado.edu/geography/gcraft/notes/mapproj/gif/unproj.gif
 */
public class GlobalCoordinatePlane
{
  public static final double X_MIN = -180; //farthest degree west
  public static final double X_MAX = 180; //farthest degree east
  public static final double Y_MIN = -90; //farthest degree south
  public static final double Y_MAX = 90; //farthest degree north
  public static ArrayList<Event> currentEvents;


  public void addEvent(Event e){}
}
