package starvationevasion.client.GUI.VotingLayout;

import starvationevasion.client.GUI.GUI;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import starvationevasion.common.EnumRegion;

public class VotingNode extends StackPane
{
  GUI gui;

  EnumRegion region;

  int cardNumber;

  Label label;

  public VotingNode(GUI gui, EnumRegion region, int cardNumber)
  {
    this.gui = gui;
    this.region = region;
    this.cardNumber = cardNumber;

    this.getStylesheets().add("/starvationevasion/client/GUI/VotingLayout/style.css");
    this.getStyleClass().add("votingnode");

    initializeLabel(region, cardNumber);
    label.setTextFill(Color.WHITE);
    this.getChildren().add(label);
  }

  private void initializeLabel(EnumRegion region, int cardNumber)
  {
    switch (region)
    {
      case CALIFORNIA:
        label = new Label("VOTE CARD:" + cardNumber);
        break;
      case MOUNTAIN:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case SOUTHERN_PLAINS:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case NORTHERN_PLAINS:
        label =  new Label("VOTE CARD: " + cardNumber);
        break;
      case HEARTLAND:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case SOUTHEAST:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case NORTHERN_CRESCENT:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      default:
        label = new Label("prepare for exception");
        break;

    }
  }
}
