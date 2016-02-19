package starvationevasion.client.GUIOrig.Popups;

import starvationevasion.client.GUIOrig.GUI;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import starvationevasion.common.EnumRegion;

/**
 * GUIOrig element which is supposed to display the discard cards in the discard pile
 * It currently does not work at all
 */
public class DiscardDisplay extends BorderPane
{
  GUI gui;
  EnumRegion selectedRegion;
  ImageView leftArrow;
  ImageView rightArrow;

  StackPane left;
  StackPane right;
  Node cardDisplay;

  /**
   * Constructor for the DiscardDisplay
   * Takes GUIOrig as an argument
   * @param gui GUIOrig which owns it
   */
  public DiscardDisplay(GUI gui)
  {
    this.gui = gui;
    this.getStylesheets().add("/starvationevasion/client/GUIOrig/DraftLayout/style.css");
    this.getStyleClass().add("graphdisplay");

    initializeLeft();
    initializeRight();

    this.setLeft(left);
    this.setRight(right);
    this.setVisible(false);
  }


  /**
   * Hides this popup
   */
  public void close()
  {
    this.setVisible(false);
  }

  /**
   * Opens this popup
   */
  public void open()
  {
    this.setVisible(true);
  }

  private void initializeLeft()
  {
    leftArrow = new ImageView(gui.getImageGetter().getDiscardLeftArrowSmall());
    left = new StackPane();
    left.getChildren().add(leftArrow);

    left.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {

      }
    });
  }

  private void initializeRight()
  {
    rightArrow = new ImageView(gui.getImageGetter().getDiscardRightArrowSmall());
    right = new StackPane();
    right.getChildren().add(rightArrow);
    right.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {

      }
    });
  }
}
