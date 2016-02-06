package starvationevasion.client.controller.guiController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Created by MohammadR on 2/5/2016.
 */
public class CardViewController extends GuiController
{
  @FXML
  public VBox cardViewBar;

  public void addCardImage(Image image)
  {
    ImageView cardImage = new ImageView();
    cardImage.setFitWidth(50);
    cardImage.setFitHeight(70);
    cardImage.setRotate(-30);
    cardImage.setImage(image);
    cardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new SideBarEventHandler(cardViewBar.getChildren().size()));
    cardViewBar.getChildren().add(cardImage);
  }

  public void removeCardImage(int index)
  {
    if (index < cardViewBar.getChildren().size()) cardViewBar.getChildren().remove(index);
  }

  public CardViewController(FXMLLoader loader)
  {
    super(loader);
    this.loader.setController(this);
  }

  class SideBarEventHandler implements EventHandler<MouseEvent>
  {
    int index;

    SideBarEventHandler(int index)
    {
      this.index = index;
    }

    public int getIndex()
    {
      return index;
    }

    @Override
    public void handle(MouseEvent event)
    {
      System.out.println(getIndex());
    }
  }
}
