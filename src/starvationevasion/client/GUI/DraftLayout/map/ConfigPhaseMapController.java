package starvationevasion.client.GUI.DraftLayout.map;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

/**
 * Controls the behavior of the US Map when the game is in the configuration phase
 * (AKA Region selection)
 */
public class ConfigPhaseMapController extends MapController
{

  /**
   * Defines the behavior of the map when a region is entered by the user's mouse
   * @param event Mouse event that triggered the entering of the region
   */
  @Override
  public void handleMouseEntered(MouseEvent event) {

  }

  /**
   * Defines the behavior of the map when a region is exited by the user's mouse
   * @param event Mouse event that was triggered upon exiting a region
   */
  @Override
  public void handleMouseExited(MouseEvent event)
  {

  }

  /**
   * Defines the behavior of the map when the mouse is moved by the user
   * @param event Mouse event that was triggered upon moving the mouse
   */
  @Override
  public void handleMouseMoved(MouseEvent event)
  {

  }

  /**
   * Defines the behavior of the map whena region is click on by the user's mouse
   * @param event Mouse event that was triggered upon clicking a region
   */
  @Override
  public void handleMouseClicked(MouseEvent event)
  {
    Group region = (Group) event.getSource();
    region.setScaleX(1.0);
    region.setScaleY(1.0);
    region.setScaleZ(1.0);
    //  if (client.getAvailableRegions().contains(MapUtil.regionIdToEnumRegion(region.getId())))
    {
      //   client.sendRegionChoice(MapUtil.regionIdToEnumRegion(region.getId()));
    }
  }
}
