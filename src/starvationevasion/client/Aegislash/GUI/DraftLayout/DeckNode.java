package starvationevasion.client.Aegislash.GUI.DraftLayout;

import starvationevasion.client.Aegislash.GUI.GUI;
import starvationevasion.client.Aegislash.GUI.DraftLayout.hand.ClientPolicyCard;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


/**
 * DeckNode is the starvationevasion.client.Aegislash.GUI element responsible for allowing the user to interact with their deck and their discard pile.
 * The DiscardPile should show the image of the last card. While it is not currently implement, the future idea was for
 * a discard pile popup to show when the user clicks on the discard pile. The popup would then allow the user to scroll through
 * the discarded cards
 */
public class  DeckNode extends VBox
{
  GridPane gridPane=new GridPane();
  ClientPolicyCard deck;
  StackPane discardPile;

  /**
   * Constructor for the DeckNode
   * Takes a reference to the starvationevasion.client.Aegislash.GUI that owns it
   * @param gui
   */
  public DeckNode(GUI gui)
  {
    deck=new ClientPolicyCard(EnumRegion.CALIFORNIA, EnumPolicy.Clean_River_Incentive, gui);
    deck.backOfCard();
    discardPile=new StackPane();
    ClientPolicyCard firstCard=new ClientPolicyCard(EnumRegion.CALIFORNIA,EnumPolicy.Educate_the_Women_Campaign,gui);
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

    gridPane.add(deck,0,1);
    gridPane.add(discardPile,1,1);
    this.getStylesheets().add("/starvationevasion/client/Aegislash/GUI/DraftLayout/style.css");
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
