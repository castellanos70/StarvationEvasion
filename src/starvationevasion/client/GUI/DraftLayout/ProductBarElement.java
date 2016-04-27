package starvationevasion.client.GUI.DraftLayout;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumFood;

/**
 * ProductBarElement is the GUI element responsible for representing one of the products on the product bar
 * If a player clicks a ProductBarElement, the user should, depending on the context, either get data about
 * the product or select the product for use as a variable on a card
 */
public class ProductBarElement extends StackPane
{
  final EnumFood type;
  final int ID;
  GUI gui;
  ProductBar pb;
  ImageView foodImg;
  boolean selected = false;
  DropShadow ds;
  Label label;

  public ProductBarElement(GUI gui, final EnumFood food, final int ID, double width, double height, ProductBar pb)
  {
    this.gui = gui;
    this.ID = ID;
    this.type = food;
    this.foodImg = new ImageView(gui.getImageGetter().getImageForFoodType64(type));
    this.setMaxSize(width, height);
    this.setPrefSize(width, height);

    initializeLabel();

    this.pb = pb;
    this.ds = new DropShadow(20, Color.AQUA);

    this.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        if (gui.getSeletingProduct())
        {
          pb.pressElement(ID);
        }

        else
        {
          gui.getPopupManager().toggleFoodPopup(ID);
          pb.pressElement(ID);
        }
      }
    });

    this.setOnMouseEntered(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        label.setVisible(true);
      }
    });

    this.setOnMouseExited(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        label.setVisible(false);
      }
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().add(foodImg);
    this.getChildren().add(label);
  }

  /**
   * Press the element (add dropshadow)
   */
  public void press()
  {
    if (selected)
    {
      selected = false;
      foodImg.setEffect(null);
    }
    else
    {
      selected = true;
      foodImg.setEffect(ds);
    }
  }

  private void initializeLabel()
  {
    String s = type.toString();

    label = new Label(s);
    label.setWrapText(true);
    label.setTextFill(Color.BLACK);
    label.setVisible(false);
    label.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    label.getStyleClass().add("pbarelement");
  }

}
