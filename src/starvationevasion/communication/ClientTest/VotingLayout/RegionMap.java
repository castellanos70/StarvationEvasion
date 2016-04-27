package starvationevasion.communication.ClientTest.VotingLayout;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import starvationevasion.common.EnumRegion;
import starvationevasion.communication.ClientTest.GUI;

import java.io.IOException;

/**
 * Class which represents the regions of the GUI which shows the individual regions of the USA
 * used for voting phase.
 */
public class RegionMap extends Group
{
  GUI gui;
  EnumRegion region;
  Node regionNode;

  public RegionMap(GUI gui, EnumRegion region)
  {
    this.gui = gui;
    this.region = region;

    this.getStylesheets().add("/starvationevasion/client/GUI/VotingLayout/style.css");
    this.getStyleClass().add("regionmap");

    initializeRegionNode();
    this.getChildren().add(regionNode);
  }

  private void initializeRegionNode()
  {
    String fxmlRegionFileName;
    float scale;
    float xOffset;
    switch (region)
    {
      case USA_CALIFORNIA:
        fxmlRegionFileName = "California.fxml";
        scale = 0.6f;
        xOffset = 40;
        break;
      case USA_MOUNTAIN:
        fxmlRegionFileName = "Mountain.fxml";
        scale = 0.35f;
        xOffset = 20;
        break;
      case USA_SOUTHERN_PLAINS:
        fxmlRegionFileName = "Southern_Plains.fxml";
        scale = 0.525f;
        xOffset = 0;
        break;
      case USA_NORTHERN_PLAINS:
        fxmlRegionFileName = "Northern_Plains.fxml";
        scale = 0.5f;
        xOffset = 40;
        break;
      case USA_HEARTLAND:
        fxmlRegionFileName = "Heartland.fxml";
        scale = 0.8f;
        xOffset = 10;
        break;
      case USA_SOUTHEAST:
        fxmlRegionFileName = "Southeast.fxml";
        scale = 0.49f;
        xOffset = 20;
        break;
      case USA_NORTHERN_CRESCENT:
        fxmlRegionFileName = "Northern_Crescent.fxml";
        scale = 0.45f;
        xOffset = 0;
        break;
      default:
        fxmlRegionFileName = "prepare yourself for an exception";
        scale = 0.0f;
        xOffset = 0;
        break;

    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRegionFileName));
    try {
      this.regionNode = loader.load();
      regionNode.setScaleX(scale);
      regionNode.setScaleY(scale);
      regionNode.setScaleZ(scale);
      this.setTranslateX(xOffset);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

}
