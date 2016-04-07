package starvationevasion.client.GUI.VotingLayout;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.GUI;
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

    this.getStylesheets().add("/starvationevasion/client/GUI/VotingLayout/style.css");
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
      case USA_CALIFORNIA:
        label = new Label("VOTE CARD:" + cardNumber);
        break;
      case USA_MOUNTAIN:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case USA_SOUTHERN_PLAINS:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case USA_NORTHERN_PLAINS:
        label =  new Label("VOTE CARD: " + cardNumber);
        break;
      case USA_HEARTLAND:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case USA_SOUTHEAST:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      case USA_NORTHERN_CRESCENT:
        label = new Label("VOTE CARD: " + cardNumber);
        break;
      default:
        label = new Label("prepare for exception");
        break;

    }
  }
}
