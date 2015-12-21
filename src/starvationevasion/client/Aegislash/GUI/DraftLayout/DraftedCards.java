package starvationevasion.client.Aegislash.GUI.DraftLayout;

import starvationevasion.client.Aegislash.GUI.DraftLayout.hand.ClientPolicyCard;
import javafx.scene.layout.GridPane;

/**
 * DraftedCards is the starvationevasion.client.Aegislash.GUI element responsible for displaying the cards the user has currently selected for drafting
 * When a user selects draft card on a card, it will appear in this node on the starvationevasion.client.Aegislash.GUI
 */
public class DraftedCards extends GridPane
{

  private int numberOfCards;

  /**
   * Default constructor for the DraftedCards region
   */
  public DraftedCards()
  {
    numberOfCards=0;

    this.getStylesheets().add("/starvationevasion/client/Aegislash/GUI/DraftLayout/style.css");
    this.getStyleClass().add("draftedcards");
  }

  /**
   * Add a card to the DrafedCard region of the starvationevasion.client.Aegislash.GUI
   * @param clientPolicyCard card to add to the drafted card region
   */
  public void addCard(ClientPolicyCard clientPolicyCard)
  {
    numberOfCards++;
    setRowSpan(clientPolicyCard,numberOfCards);
    add(clientPolicyCard, 0, numberOfCards);
  }

  /**
   * Removes card from the drafed card region
   * @param clientPolicyCard card to remove
   */
  public void removeCard(ClientPolicyCard clientPolicyCard)
  {
    getChildren().remove(clientPolicyCard);
    numberOfCards--;
  }
}
