package starvationevasion.client.GUI.DraftLayout;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.sim.CardDeck;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Hand is the GUI element responsible for allowing the user to interact with the current cards in their hand
 * It will display the hards in the user's hand
 * On mouseover, the cards will become enlarged
 * On mouseclick, the player will attempt to play the card
 * Maybe on rightmouseclick, the player can set card variables
 *
 */
public class Hand extends GridPane
{
  Stage primaryStage;
  GUI gui;
  ArrayList<ClientPolicyCard> elements;
  Stack<ClientPolicyCard> usedCards;
  ArrayList<ClientPolicyCard> discardPile;
  ArrayList<ClientPolicyCard> cardsPlayed;
  DraftedCards draftedCards;
  DeckNode deckNode;
  public EnumPolicy[] hand;
  boolean playedSupportCard=false;
  boolean discardedSingleCard=false;
  boolean selectingCard=false;
  int numberOfActionsUsed=0;
  ArrayList<ClientPolicyCard> selectedCards=new ArrayList<>();
  
  public Hand(GUI gui, Stage stage)
  {
    this.gui = gui;
    primaryStage = gui.getPrimaryStage();

  }

  /**
   * Sets the hand with an Array of EnumPolicies
   * This will update the actual hand in the GUI
   * @param hand
   */
  public void setHand(EnumPolicy[] hand)
  {
    this.hand = hand;
    updateHand();
  }
  public EnumPolicy[] getHand()
  {
    return hand;
  }
  public void setSelectingCard(boolean bool){selectingCard=bool;}
  public int getNumberOfActionsUsed(){return numberOfActionsUsed;}
  public ArrayList<ClientPolicyCard> getDraftedCards()
  {
    return cardsPlayed;
  }
  public ArrayList<ClientPolicyCard> getDiscardCards(){return discardPile;}

  private void updateHand()
  {
    getChildren().clear();
    elements = new ArrayList<>();
    usedCards=new Stack<>();
    discardPile=new ArrayList<>();
    cardsPlayed=new ArrayList<>();
    for (int i = 0; i <hand.length ; i++)
    {
      //Needs working connection
      // ClientPolicyCard clientPolicyCard=new ClientPolicyCard(GUI.client.getAssignedRegion(),hand[i],GUI);
      ClientPolicyCard clientPolicyCard=new ClientPolicyCard(gui.getAssignedRegion(),hand[i],gui);
      clientPolicyCard.setHandIndex(i);
      elements.add(clientPolicyCard);

      add(clientPolicyCard, i, 0);
    }
    setListeners();
  }
  public void newTurn()
  {
     playedSupportCard=false;
     discardedSingleCard=false;
     selectingCard=false;
     numberOfActionsUsed=0;
  }
  private void setListeners()
  {
    for(final ClientPolicyCard card : elements)
    {
      card.setOnMouseEntered(event -> {
        if(!card.getIsFlipped())
        {
          if(!card.getDrafted()&&!card.isDiscarded())
          {
            if(numberOfActionsUsed>=2)
            {
              card.setDraftButton(true);
            }
            if(card.isSupportCard()&&playedSupportCard) card.setDraftButton(true);
            if(discardedSingleCard) card.setDiscardButton(true);
            card.setDetailedCard();
            card.toFront();
          }else
          {
            card.setDetailedDraftedCard();
            card.toFront();
          }
        }
      });
      card.setOnMouseExited(event -> {
        if(!card.getDrafted()&&!card.isDiscarded()&&!card.getIsFlipped())
        {
          card.setIsFlipped(false);
          card.setBasicCard();
        }
        else if(!card.getIsFlipped()) card.setDraftedCard();
      });
      card.setOnMouseClicked(event -> {
        if(selectingCard&&selectedCards.size()<3||card.isSelected())
        {
          selectCard(card);
        }
      });
      //Checks for when player clicks drafted card
      card.getDraftButton().setOnMouseClicked(event -> {
        draftCard(card);
      });
      card.getDiscardButton().setOnMouseClicked(event -> {
        discardCard(card);
      });

    }
  }

  /**
   * Puts a card in the Draft zone
   * @param card the card you're drafting
   */
  public void draftCard(ClientPolicyCard card)
  {
    if (numberOfActionsUsed < 2)
    {
          numberOfActionsUsed++;
          draftedCards = gui.getDraftLayout().getDraftedCards();
          usedCards.add(card);
          cardsPlayed.add(card);
         // card.setDrafted(true);
          card.setIsFlipped(false);
          //draftedCards.addCard(card);
          //elements.remove(card);
          card.setDraftStyle();
          card.setBasicCard();
          gui.setSelectingProduct(false);
          gui.getDraftLayout().unselectSelectedProduct();
          card.getDraftButton().setDisable(true);
          card.getDiscardButton().setDisable(true);
          if (card.isSupportCard()) playedSupportCard = true;
          if (numberOfActionsUsed == 2) gui.getDraftLayout().getActionButtons().setDisableOnBigDiscardButton(true);
          //card.setSelectedFood(gui.getDraftLayout().getProductBar().getSelectedElement());
    }
  }

  /**
   * Removes card from hand and adds it to the local discard
   * @param card card to be discarded
   */
  public void discardCard(ClientPolicyCard card)
  {
    if (!discardedSingleCard)
    {
      deckNode = gui.getDraftLayout().getDeckNode();
      discardPile.add(card);
      usedCards.add(card);
     // card.setDiscarded(true);
    //  card.setIsFlipped(false);
      //deckNode.discardCard(card);
      //elements.remove(card);
      card.setDiscardStyle();
      discardedSingleCard = true;
      card.getDraftButton().setDisable(true);
      card.setBasicCard();
    }
  }


  private void selectCard(ClientPolicyCard clientPolicyCard)
  {
    clientPolicyCard.selectCard();
    if(clientPolicyCard.isSelected())
    {
      selectedCards.add(clientPolicyCard);
    } else
    {
      selectedCards.remove(clientPolicyCard);
    }
  }

  /**
   * removes selected cards from the hand
   */
  public void discardSelected()
  {
    deckNode = gui.getDraftLayout().getDeckNode();
    if(selectedCards.size()>0)
    {
      numberOfActionsUsed++;
    }
    for(ClientPolicyCard card:selectedCards)
    {
      discardPile.add(card);
      card.setDiscarded(true);
      card.setIsFlipped(false);
      deckNode.discardCard(card);
      elements.remove(card);
    }
    selectedCards.clear();
  }

  /**
   * Undo's last move. Used by ActionButton
   */
  public void undo()
  {
    if(usedCards!=null&&usedCards.size()!=0)
    {
      ClientPolicyCard undoCard=usedCards.pop();
      undoCard.setBasicCard();
      undoCard.setDrafted(false);
      undoCard.setDiscarded(false);
      undoCard.setIsFlipped(false);
      undoCard.setBasicStyle();
      elements.add(undoCard);
      //add(undoCard,undoCard.getHandIndex(),0);

      if(undoCard.isSupportCard())
      {
        playedSupportCard=false;
      }
      if(cardsPlayed.contains(undoCard))
      {
        numberOfActionsUsed--;
        gui.getDraftLayout().getActionButtons().setDisableOnBigDiscardButton(false);
        draftedCards.removeCard(undoCard);
        cardsPlayed.remove(undoCard);

        for(ClientPolicyCard card: elements)
        {
          card.setDraftButton(false);
        }
      }
      if(discardPile.contains(undoCard))
      { for(ClientPolicyCard card: elements)
      {
        card.setDiscardButton(false);
      }
        undoCard.setDiscarded(false);
        discardedSingleCard=false;
        discardPile.remove(undoCard);
        deckNode.undoDiscard(undoCard);
      }
    }
  }

  /**
   * Just for testing, deals out basic hand of random cards
   */
  public void dealRandomHand()
  {
    CardDeck deck =new CardDeck(EnumRegion.USA_CALIFORNIA);
    setHand(deck.drawCards());
  }

  private boolean isLegal(ClientPolicyCard card)
  {
    if(card.isSupportCard()&&playedSupportCard) return false;
    if(numberOfActionsUsed>=2) return false;
    return true;
  }


}
