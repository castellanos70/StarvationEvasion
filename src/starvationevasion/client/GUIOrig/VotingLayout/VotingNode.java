package starvationevasion.client.GUIOrig.VotingLayout;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.common.EnumRegion;

public class VotingNode extends BorderPane
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

    this.getStylesheets().add("/starvationevasion/client/GUIOrig/VotingLayout/style.css");
    this.getStyleClass().add("votingnode");

    Button voteFor=new Button("Yes");
    Button voteAgainst=new Button("No");
    HBox buttons=new HBox();
    buttons.getChildren().add(voteFor);
    buttons.getChildren().add(voteAgainst);
    initializeLabel(region, cardNumber);
    label.setTextFill(Color.WHITE);
    //this.setTop(label);
    this.setBottom(buttons);
    this.setAlignment(buttons, Pos.CENTER);
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
