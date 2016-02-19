package starvationevasion.client.GUIOrig.VotingLayout;

import starvationevasion.client.GUIOrig.DraftLayout.hand.ClientPolicyCard;
import starvationevasion.client.GUIOrig.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

/**
 * Defines the StackPane where a card may be placed for voting purposes in the VotingLayout
 */
public class CardSpace extends StackPane
{
  GUI gui;
  Label label;

  /**
   * Constructor for the CardSpace class
   * @param gui instance reference of the GUIOrig
   * @param region region associated with card being voted on
   * @param cardNumber first or second
   */
  public CardSpace(GUI gui, EnumRegion region, int cardNumber)
  {
    this.gui = gui;
    this.getStylesheets().add("/starvationevasion/client/GUIOrig/VotingLayout/style.css");
    this.getStyleClass().add("cardspace");
    initializeLabel(region, cardNumber);
    this.setAlignment(Pos.TOP_LEFT);
    this.getChildren().add(label);
  }

  private void initializeLabel(EnumRegion region, int cardNumber)
  {
    switch (region)
    {
      case CALIFORNIA:
        label = new Label("CALIFORNIA: CARD " + cardNumber);
        break;
      case MOUNTAIN:
        label = new Label("MOUNTAIN: CARD " + cardNumber);
        break;
      case SOUTHERN_PLAINS:
        label = new Label("SOUTHERN PLAINS: CARD " + cardNumber);
        break;
      case NORTHERN_PLAINS:
        label =  new Label("NORTHERN PLAINS: CARD " + cardNumber);
        break;
      case HEARTLAND:
        label = new Label("HEARTLAND: CARD " + cardNumber);
        break;
      case SOUTHEAST:
        label = new Label("SOUTHEAST: CARD " + cardNumber);
        break;
      case NORTHERN_CRESCENT:
        label = new Label("NORTHERN CRESENT: CARD" + cardNumber);
        break;
      default:
        label = new Label("prepare for an exception");
        break;
    }
  }
  public void setCard(EnumRegion region,EnumPolicy card,GUI gui)
  {
    ClientPolicyCard clientPolicyCard=new ClientPolicyCard(region,card,gui);
    this.getChildren().add(clientPolicyCard);
  }


}
