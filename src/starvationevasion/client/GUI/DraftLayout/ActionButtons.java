package starvationevasion.client.GUI.DraftLayout;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.ResizablePane;

import java.util.ArrayList;

/**
 * ActionButtons are the GUI element which are responsible for allowing the user to tell the server that they're done with drafting,
 * if the want to undo a move, or if they want to discard up to 3 cards
 */
public class ActionButtons extends HBox
{
  Text tempText;
  GUI gui;
  Button bigDiscard;
  Button endTurn;
  /**
   * Default constructor. Takes a reference to the GUI which owns it
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

    undo.setOnAction(event -> gui.getDraftLayout().getHand().reset());
    endTurn=new Button();
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
    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("actionbuttons");
    VBox notUndoButtons=new VBox();

    bigDiscard=new Button("Discard up to 3");
    bigDiscard.setOnMouseClicked(event ->
    {
      bigDiscard.setDisable(true);
      doneDiscarding.setDisable(false);
//      gui.getDraftLayout().getHand().setSelectingCard(true);
    });

//    doneDiscarding.setOnMouseClicked(event ->
//    {
//      HandNode hand =gui.getDraftLayout().getHand();
//      if(hand.getNumberOfActionsUsed()<1)bigDiscard.setDisable(false);
//      else bigDiscard.setDisable(true);
//      doneDiscarding.setDisable(true);
//      hand.setSelectingCard(false);
//      hand.discardSelected();
//    });
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
    ArrayList<ResizablePane> clientPolicyCards = gui.getDraftLayout().getHand().getDraftedCards();
   // ArrayList<CardView> clientPolicyCards = gui.getDraftLayout().getHand().getDraftedCards();
    if (clientPolicyCards != null)
    {
      for (ResizablePane card : clientPolicyCards)
      //for (CardView card : clientPolicyCards)
      {
        //gui.getClient().draftCard(card.getGameCard());

      }
      ArrayList<ResizablePane> discardedCards = gui.getDraftLayout().getHand().getDiscardedCards();
      //ArrayList<CardView> discardedCards = gui.getDraftLayout().getHand().getDiscardedCards();
      //for (CardView card : discardedCards)
      for (ResizablePane card : discardedCards)
      {
        //gui.getClient().discardCard(card.getGameCard());
      }
    }
    endTurn.setDisable(true);
    gui.getClient().done();
   // gui.switchScenes();
  }
  public void resetActionButtons()
  {
    endTurn.setDisable(false);
  }
}
