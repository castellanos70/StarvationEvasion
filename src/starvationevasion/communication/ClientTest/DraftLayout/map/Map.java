package starvationevasion.communication.ClientTest.DraftLayout.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.DraftLayout.map.MapController;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Logic.geography.MapConstants;
import starvationevasion.client.Logic.geography.MapUtil;
import starvationevasion.common.EnumRegion;

import java.io.IOException;
import java.util.Optional;

//import starvationevasion.common.messages.LoginResponse;

/**
 * Class containing the USA map Node
 */
public class Map implements MapConstants
{

  private final Node mapNode;

  private final FXMLLoader loader;

  public static Optional<EnumRegion> currentlySelectedRegion;

  /**
   * Constructor for the Map class
   */
  public Map()
  {
    this.loader = new FXMLLoader(getClass().getResource("US_Map.fxml"));
    currentlySelectedRegion = Optional.empty();
    try {
      this.mapNode = loader.load();
      mapNode.setScaleX(0.75);
      mapNode.setScaleY(0.75);
      mapNode.setScaleZ(0.75);
      mapNode.setTranslateX(-20);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  public FXMLLoader getLoader()
  {
    return loader;
  }

  /**
   * Creates an instance of Map and instantiates the USA Node
   */
  public Node getGameMapNode()
  {
    return this.mapNode;
  }

  /**
   * Returns an instance of the map node styled for the configuration phase
   * @param client
   * @return
   */
  public Node getConfigMapNode(Client client)
  {
    this.mapNode.setTranslateY(-40);
    loader.<MapController>getController().setClient(client);
   // updateStyling(client.getAvailableRegions(), client.getTakenRegions(), client.getGameConfigurationType());
    return mapNode;
  }

  /**
   * Updates the styling for the map with a new set of region data
   * @param availableRegions list of currently available regions
   * @param takenRegions list of taken regions
   *  gameConfigurationType type of game configuration (chosen vs. assigned)
   */
//  public void updateStyling(
//      ArrayList<EnumRegion> availableRegions,
//      TreeMap<EnumRegion, String> takenRegions,
//    //  LoginResponse.ResponseType gameConfigurationType)
//  {
//    // color available and taken regions
//    //loader.<MapController>getController().colorAvailableRegions(mapNode, availableRegions, gameConfigurationType);
//    loader.<MapController>getController().colorTakenRegions(mapNode, takenRegions);
//  }

  /**
   * Unselects the currently selected region, if it exists
   */
  public void unselectRegion()
  {
    Map.currentlySelectedRegion = Optional.empty();
    for (EnumRegion usRegion : EnumRegion.US_REGIONS)
    {
      loader.<MapController>getController().styleRegion(
        mapNode, usRegion, MapUtil.getRegionColor(usRegion), new InnerShadow(UNSELECT_RADIUS, Color.BLACK));
    }
  }

  /**
   * May god bless our children for he has given us hashtagification
   * @param dirtyString filthy, un-hashtagged string
   * @return a true blessing
   */
  public static String hashtagify(String dirtyString)
  {
    return "#" + dirtyString;
  }
}

