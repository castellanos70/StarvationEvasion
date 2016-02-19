package starvationevasion.client.GUIOrig.Popups;

import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.Graphs.Graph;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import starvationevasion.common.EnumFood;

public class ProductBarData extends BorderPane
{
  int ID;
  ImageView foodImg;
  EnumFood foodType;
  Graph foodGraph;

  Label cropList;
  Label growingConditions;

  VBox box1;
  VBox box2;

  GUI gui;

  HBox centerPane;

  public ProductBarData(int id, GUI gui)
  {
    this.ID = id;
    this.gui = gui;

    foodImg = new ImageView(gui.getImageGetter().getImageForFoodType256(gui.getFoodType(id)));
    foodType = gui.getProductList().get(id);

    foodGraph = gui.getGraphManager().getProductBarGraph(ID);

    Label title = new Label();
    title.setText(foodType.toString());
    title.setTextFill(Color.WHITE);

    cropList = new Label(foodType.toLongString());
    cropList.setWrapText(true);
    cropList.setTextFill(Color.WHITE);

    box1 = new VBox();
    box1.setPrefSize(256,256);
    box1.getChildren().add(cropList);

    centerPane = new HBox();
    centerPane.getChildren().add(foodImg);
    centerPane.getChildren().add(foodGraph.getLineChart());
    centerPane.getChildren().add(box1);


    this.setCenter(centerPane);
    setAlignment(title, Pos.TOP_CENTER);
    this.setTop(title);
  }


  public int getID()
  {
    return ID;
  }
}
