package starvationevasion.client.GUIOrig.DraftLayout;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import starvationevasion.client.GUIOrig.GUI;

/**
 * Created by Dayloki on 3/29/2016.
 */
public class WorldMap extends ScrollPane
{
  GUI gui;
  public WorldMap(GUI gui)
  {
    this.gui=gui;
    Image imageMap=gui.getImageGetter().getWorldMap();
    ImageView worldMap=new ImageView(imageMap);
    VBox holder=new VBox();
    holder.getChildren().add(worldMap);
    worldMap.setFitWidth(gui.getBoxWidth() * 10);
    worldMap.setLayoutX(gui.getBoxWidth() * 10);
    //worldMap.setFitHeight(gui.getBoxHeight()*6);
    worldMap.setPreserveRatio(true);
    worldMap.setSmooth(true);
    worldMap.setCache(true);
    worldMap.fitHeightProperty().bind(gui.getPrimaryStage().heightProperty());
    worldMap.fitWidthProperty().bind(gui.getPrimaryStage().widthProperty());
    setContent(worldMap);
    setPannable(true);


  }
}
