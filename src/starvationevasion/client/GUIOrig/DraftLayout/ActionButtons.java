package starvationevasion.client.GUIOrig.DraftLayout;

import starvationevasion.client.GUIOrig.DraftLayout.hand.ClientPolicyCard;
import starvationevasion.client.GUIOrig.DraftLayout.hand.Hand;
import starvationevasion.client.GUIOrig.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * ActionButtons are the GUIOrig element which are responsible for allowing the user to tell the server that they're done with drafting,
 * if the want to undo a move, or if they want to discard up to 3 cards
 */
public class ActionButtons extends HBox
{
  Text tempText;
  GUI gui;
  Button bigDiscard;

  /**
   * Default constructor. Takes a reference to the GUIOrig which owns it
   * @param gui
   */
  public ActionButtons(GUI gui)
  {
    this.gui = gui;
    HBox layout = new HBox();

    tempText = new Text("ACTION BUTTONS");
    tempText.setFill(Color.BLUE);
    ImageView image =new ImageView(gui.getImageGetter().getUndoButton());

    Button undo=new Button("",image);

    undo.setOnAction(event -> gui.getDraftLayout().getHand().undo());
    Button endTurn=new Button();
    endTurn.setText("End Turn");


    endTurn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
      endTurn();
      }
    });

    Button doneDiscarding=new Button("Done Discarding");
    doneDiscarding.setDisable(true);
    this.getStylesheets().add("/starvationevasion/client/GUIOrig/DraftLayout/style.css");
    this.getStyleClass().add("actionbuttons");
    VBox notUndoButtons=new VBox();

    bigDiscard=new Button("Discard up to 3");
    bigDiscard.setOnMouseClicked(event ->
    {
      bigDiscard.setDisable(true);
      doneDiscarding.setDisable(false);
      gui.getDraftLayout().getHand().setSelectingCard(true);
    });

    doneDiscarding.setOnMouseClicked(event ->
    {
      Hand hand =gui.getDraftLayout().getHand();
      if(hand.getNumberOfActionsUsed()<1)bigDiscard.setDisable(false);
      else bigDiscard.setDisable(true);
      doneDiscarding.setDisable(true);
      hand.setSelectingCard(false);
      hand.discardSelected();
    });
    notUndoButtons.getChildren().addAll(endTurn,bigDiscard,doneDiscarding);
    layout.getChildren().addAll(undo, notUndoButtons);

    this.getChildren().add(layout);
  }

  /**
   * Disables or enables the discardButton
   * @param bool
   */
  public void setDisableOnBigDiscardButton(boolean bool){bigDiscard.setDisable(bool);}

  /**
   * endTurn function tells the client to send the cards which the user has selected for drafting to the server
   */
  public void endTurn()
  {
    ArrayList<ClientPolicyCard> clientPolicyCards = gui.getDraftLayout().getHand().getDraftedCards();
    if (clientPolicyCards != null)
    {
      for (ClientPolicyCard card : clientPolicyCards)
      {
        //gui.client.draftCard(card.getPolicyCard());
      }
      ArrayList<ClientPolicyCard> discardedCards = gui.getDraftLayout().getHand().getDiscardCards();
      for (ClientPolicyCard card : discardedCards)
      {
       // gui.client.discard(card.getPolicy());
      }
    }
    gui.switchScenes();
  }
}
