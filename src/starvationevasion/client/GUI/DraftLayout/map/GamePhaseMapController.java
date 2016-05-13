package starvationevasion.client.GUI.DraftLayout.map;

import starvationevasion.client.Logic.geography.MapUtil;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * Defines the behavior of the US map[ when the game is in any phase other than ConfigPhase
 */
public class GamePhaseMapController extends MapController
{

  /**
   * Defines the behavior of the map when a region is entered by the user's mouse
   * @param event Mouse event that triggered the entering of the region
   */
  @Override
  public void handleMouseEntered(MouseEvent event) {
    Group region = (Group) event.getSource();
    enlargeRegionGroup(region);
  }

  /**
   * Defines the behavior of the map when a region is exited by the user's mouse
   * @param event Mouse even that was triggered upon exiting a region
   */
  @Override
  public void handleMouseExited(MouseEvent event)
  {
    Group region = (Group) event.getSource();
    region.setScaleX(1.0);
    region.setScaleY(1.0);
    region.setScaleZ(1.0);
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
    Effect selectEffect = new InnerShadow(SELECT_RADIUS, Color.BLACK);
    Effect unselectEffect = new InnerShadow(UNSELECT_RADIUS, Color.BLACK);
    if (region.getEffect() instanceof InnerShadow)
    {
      InnerShadow currentEffect = (InnerShadow) region.getEffect();
      if (currentEffect.getRadius() == SELECT_RADIUS)
      {
        Map.currentlySelectedRegion = Optional.empty();
        region.setEffect(unselectEffect);
      }
      else
      {
        Map.currentlySelectedRegion = Optional.of(MapUtil.regionIdToEnumRegion(region.getId()));
        region.setEffect(selectEffect);
      }
    }
    else
    {
      Map.currentlySelectedRegion = Optional.of(MapUtil.regionIdToEnumRegion(region.getId()));
      region.setEffect(selectEffect);
    }
    for (Node child : region.getParent().getChildrenUnmodifiable())
    {
      if (!child.getId().equals(region.getId())) child.setEffect(unselectEffect);
    }
  }
}
