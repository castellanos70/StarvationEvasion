package starvationevasion.client.GUI.DraftLayout.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
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
   * May god bless our children for he has given us hashtagification
   * @param dirtyString filthy, un-hashtagged string
   * @return a true blessing
   */
  public static String hashtagify(String dirtyString)
  {
    return "#" + dirtyString;
  }
}

