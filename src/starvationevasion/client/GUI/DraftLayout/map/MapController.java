package starvationevasion.client.GUI.DraftLayout.map;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Logic.geography.MapConstants;
import starvationevasion.client.Logic.geography.MapUtil;
import starvationevasion.common.EnumRegion;

import java.util.TreeMap;

//import starvationevasion.common.messages.LoginResponse;

/**
 * Controller for handling interactivity with the US Region Map
 */
public class MapController implements MapConstants
{

  protected Client client;

  private static MapController currentController;


  /**
   * Defines the behavior of the map when a region is entered by the user's mouse
   * @param event Mouse event that triggered the entering of the region
   */
  public void handleMouseEntered(MouseEvent event) {
    currentController.handleMouseEntered(event);
  }

  /**
   * Defines the behavior of the map when a region is exited by the user's mouse
   * @param event Mouse event that was triggered upon exiting a region
   */
  public void handleMouseExited(MouseEvent event)
  {
    currentController.handleMouseExited(event);
  }

  /**
   * Defines the behavior of the map when the mouse is moved by the user
   * @param event Mouse event that was triggered upon moving the mouse
   */
  public void handleMouseMoved(MouseEvent event)
  {
    currentController.handleMouseMoved(event);
  }

  /**
   * Defines the behavior of the map whena region is click on by the user's mouse
   * @param event Mouse event that was triggered upon clicking a region
   */
  public void handleMouseClicked(MouseEvent event)
  {
    currentController.setClient(client);
    currentController.handleMouseClicked(event);
  }

  /**
   * Sets the current controller for the map
   * @param controllerClass the controller to set as current
   * @param <T> the class type of controller
   */

  public static <T extends MapController> void setCurrentController(Class<T> controllerClass)
  {
    if (controllerClass.isAssignableFrom(ConfigPhaseMapController.class))
    {
      currentController = new ConfigPhaseMapController();
    }
    else if (controllerClass.isAssignableFrom(GamePhaseMapController.class))
    {
      currentController = new GamePhaseMapController();
    }
  }

  protected void enlargeRegionGroup(Group regionGroup)
  {
    double scaleFactor = 1.0;
    String id = regionGroup.getId();
    if (id.equals(CALIFORNIA)) scaleFactor = 1.3;
    else if (id.equals(HEARTLAND) || id.equals(SOUTHERN_PLAINS)) scaleFactor = 1.2;
    else if (id.equals(NORTHERN_CRESCENT) || id.equals(NORTHERN_PLAINS) || id.equals(SOUTHEAST)) scaleFactor = 1.1;
    else if (id.equals(MOUNTAIN)) scaleFactor = 1.075;
    regionGroup.setScaleX(scaleFactor);
    regionGroup.setScaleY(scaleFactor);
    regionGroup.setScaleZ(scaleFactor);
    regionGroup.toFront();
  }

  /**
   * Styles a specified region in the FXML US map with a specified color and an optional, nullable style effect.
   * Must look for region in map.
   * @param mapNode the parent-most Node in the FXML file for the US map
   * @param region the specified region to style
   * @param color the color to style the region with
   * @param effect the optional effect to add to the region
   */
  public void styleRegion(Node mapNode, EnumRegion region, Color color, Effect effect)
  {
    Group regionNode = ((Group) mapNode.lookup(Map.hashtagify(MapUtil.enumRegionToRegionId(region))));
    colorGroup(regionNode, color);
    if (effect != null)
    {
      regionNode.setEffect(effect);
    }
  }

  /**
   * Colors an available region. The color of an available region is dependent on the current
   * game type (chosen vs. assigned)
   * @param mapNode the parent-most Node in the FXML file for the US map
   * @param regions the specifies region to color as available
   * @param gameType the current game type (chosen vs. assigned
   */
//  public void colorAvailableRegions(Node mapNode, ArrayList<EnumRegion> regions, LoginResponse.ResponseType gameType)
//  {
//    for (EnumRegion region : regions)
//    {
//      if (gameType == LoginResponse.ResponseType.ASSIGNED_REGION)
//      {
//        styleRegion(mapNode, region, Color.GREY, new InnerShadow(UNSELECT_RADIUS, Color.BLACK));
//      } else if (gameType == LoginResponse.ResponseType.CHOOSE_REGION)
//      {
//        styleRegion(mapNode, region, MapUtil.getRegionColor(region), new InnerShadow(UNSELECT_RADIUS, Color.BLACK));
//      }
//    }
//  }

  /**
   * Colors a taken region red
   * @param mapNode the parent-most Node in the FXML file for the US map
   * @param regions the specifies treemap of taken regions to color as taken
   */
  public void colorTakenRegions(Node mapNode, TreeMap<EnumRegion, String> regions)
  {
    for (java.util.Map.Entry<EnumRegion, String> regionEntry : regions.entrySet())
    {
      Color styleColor;
      // if (regionEntry.getValue().equals(client.getUsername()))
      {
        styleColor = Color.GREEN;
        //RegionChooser.selectionText.setText("Selected:\n\n" + regionEntry.getKey().toString());
      }
      // else styleColor = Color.RED;
      styleRegion(mapNode, regionEntry.getKey(), styleColor, new InnerShadow(SELECT_RADIUS, Color.BLACK));
    }
  }

  /**
   * Colors a Group in the FXML US map. Recursively calls nested groups until all SVGPath components have been filled
   * @param group Parent group to color
   * @param color color to fill the Group's SVGPath components with
   */
  private void colorGroup(Group group, Color color)
  {
    group.getChildren().forEach(c ->
    {
      if (c.getClass().isAssignableFrom(SVGPath.class))
      {
        ((SVGPath) c).setFill(color);
      }
      else if (c.getClass().isAssignableFrom(Group.class))
      {
        colorGroup((Group) c, color);
      }
    });
  }

  /**
   * Initializes the reference tot he client that is associated with this controller
   * @param client Client to be referenced
   */
  public void setClient(Client client)
  {
    this.client = client;
  }
}
