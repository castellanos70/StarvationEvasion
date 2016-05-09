package starvationevasion.client.GUI.Popups;

import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.Graphs.Graph;
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

    foodImg = new ImageView(gui.getImageGetter().getImageForFoodType64(gui.getFoodType(id)));
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
    //centerPane.getChildren().add(box1);
    

    this.setRight(centerPane);
    setAlignment(title, Pos.TOP_CENTER);
    this.setTop(title);
  }


  public BorderPane getGraph()
  {
	  return this;
  }
  
  public int getID()
  {
    return ID;
  }
}
