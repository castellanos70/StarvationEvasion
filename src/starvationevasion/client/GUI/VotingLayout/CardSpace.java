package starvationevasion.client.GUI.VotingLayout;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import starvationevasion.client.GUI.DraftLayout.CardView;
import starvationevasion.client.GUI.DraftLayout.ClientPolicyCard;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.PolicyCard;

/**
 * Defines the StackPane where a card may be placed for voting purposes in the VotingLayout
 */
public class CardSpace extends VBox
{
  private final FXMLLoader loader;
  GUI gui;
  Label label;
  private CardView clientPolicyCard;
  /**
   * Constructor for the CardSpace class
   * @param gui instance reference of the GUI
   * @param region region associated with card being voted on
   * @param cardNumber first or second
   */
  public CardSpace(GUI gui, EnumRegion region, int cardNumber)
  {

    this.gui = gui;
    this.getStylesheets().add("/starvationevasion/client/GUI/VotingLayout/style.css");
    this.getStyleClass().add("cardspace");
    initializeLabel(region, cardNumber);
    //this.setAlignment(Pos.TOP_LEFT);
    //this.getChildren().add(label);
    this.loader = new FXMLLoader(getClass().getResource("/starvationevasion/client/GUI/VotingLayout/VotePopup.fxml"));
    this.setOnMouseEntered(event -> {
      toFront();
    });
  }
  private void initializeLabel(EnumRegion region, int cardNumber)
  {
    switch (region)
    {
      case USA_CALIFORNIA:
        label = new Label("USA_CALIFORNIA: CARD " + cardNumber);
        break;
      case USA_MOUNTAIN:
        label = new Label("USA_MOUNTAIN: CARD " + cardNumber);
        break;
      case USA_SOUTHERN_PLAINS:
        label = new Label("SOUTHERN PLAINS: CARD " + cardNumber);
        break;
      case USA_NORTHERN_PLAINS:
        label =  new Label("NORTHERN PLAINS: CARD " + cardNumber);
        break;
      case USA_HEARTLAND:
        label = new Label("USA_HEARTLAND: CARD " + cardNumber);
        break;
      case USA_SOUTHEAST:
        label = new Label("USA_SOUTHEAST: CARD " + cardNumber);
        break;
      case USA_NORTHERN_CRESCENT:
        label = new Label("NORTHERN CRESENT: CARD" + cardNumber);
        break;
      default:
        label = new Label("prepare for an exception");
        break;
    }
  }
  public void setCard(PolicyCard policyCard, GUI gui)
  {
    clientPolicyCard=new CardView(policyCard);
    this.getChildren().add(clientPolicyCard);
  }
  public void removeCard()
  {
    this.getChildren().clear();
    clientPolicyCard=null;
  }
public CardView getCard(){return clientPolicyCard;}

}
