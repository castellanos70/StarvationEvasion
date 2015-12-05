package starvationevasion.vis.visuals;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Scale;
import starvationevasion.vis.controller.EarthViewer;

/**
 * Created by Tess Daughton on 11/23/15.
 * This is my CustomLayout class but it is not necessary for embedding the Earth to work.
 * One thing that you will need is some sort of Layout Manager, an instance of EarthViewer, and
 * a method to switch between showing the Earth in full mode or mini mode
 */
public class VisualizerLayout extends StackPane
{
  private  ResourceLoader RESOURCE_LOADER = EarthViewer.RESOURCE_LOADER;
  private GridPane center = new GridPane();
  private final Earth EARTH = EarthViewer.earth;
  private  Group earthView = new Group();
  private final Scale SET_SIZE = new Scale();


  public VisualizerLayout()
  {
    earthView = EARTH.getUniverse();
    earthView.setScaleZ(1.0);
    earthView.setScaleY(1.0);
    earthView.setScaleX(1.0);
    earthView.setDisable(false);
    earthView.requestFocus();
    earthView.getStylesheets().add(RESOURCE_LOADER.STYLE_SHEET);
    this.setPrefSize(500,500);
    this.getChildren().add(earthView);

  }
}
