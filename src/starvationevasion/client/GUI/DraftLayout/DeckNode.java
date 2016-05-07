package starvationevasion.client.GUI.DraftLayout;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;


/**
 * DeckNode is the GUI element responsible for allowing the user to interact with their deck and their discard pile.
 * The DiscardPile should show the image of the last card. While it is not currently implement, the future idea was for
 * a discard pile popup to show when the user clicks on the discard pile. The popup would then allow the user to scroll through
 * the discarded cards
 */
public class  DeckNode extends VBox
{
  GridPane gridPane=new GridPane();
//  ClientPolicyCard deck;
  StackPane discardPile;

  /**
   * Constructor for the DeckNode
   * Takes a reference to the GUI that owns it
   * @param gui
   */
  public DeckNode(GUI gui)
  {
//    deck=new ClientPolicyCard(EnumRegion.USA_CALIFORNIA, EnumPolicy.Policy_CleanRiverIncentive, gui);
//    deck.backOfCard();
    discardPile=new StackPane();
    ClientPolicyCard firstCard=new ClientPolicyCard(EnumRegion.USA_CALIFORNIA,EnumPolicy.Policy_EducateTheWomenCampaign,gui);
    firstCard.discardPile();
    discardPile.getChildren().add(firstCard);

    discardPile.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        gui.getPopupManager().toggleDiscardDisplay();
      }
    });

//    gridPane.add(deck,0,1);
    gridPane.add(discardPile,0,0);
    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("decknode");

    this.getChildren().add(gridPane);
  }

  /**
   * Adds the image of the passed in card to the discard pile
   * @param clientPolicyCard
   */
  public void discardCard(ClientPolicyCard clientPolicyCard)
  {
    discardPile.getChildren().add(clientPolicyCard);
  }

  /**
   * Undo's the last discard the user has done by removing the last card added to the pile
   * @param clientPolicyCard
   */
  public void undoDiscard(ClientPolicyCard clientPolicyCard)
  {
    discardPile.getChildren().remove(clientPolicyCard);
  }
}
