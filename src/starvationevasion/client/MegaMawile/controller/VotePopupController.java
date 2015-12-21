package starvationevasion.client.MegaMawile.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import starvationevasion.client.MegaMawile.view.VotePopup;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.Vote;
import starvationevasion.common.messages.VoteType;
import starvationevasion.server.ServerState;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls the vote popup window.
 */
public class VotePopupController implements Initializable, GameController
{
  private AbstractPlayerController playerController;
  private GameEngine gameEngine;
  private PolicyCard card;

  private boolean startOfPhase = true;

  private final Stage view;

  @FXML
  public Label curVoteCardName;
  @FXML
  public Text curVoteCardText;
  @FXML
  public Button voteYes, voteNo, voteAbstain;

  private HashMap<EnumRegion, PolicyCard> voteCards = new HashMap();

  public VotePopupController(AbstractPlayerController playerController, GameEngine gameEngine)
  {
    this.playerController = playerController;
    this.gameEngine = gameEngine;

    view = new VotePopup(this);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
  }

  public void show(String str)
  {
    switch (str)
    {
      case "voteCalif":
        card = voteCards.get(EnumRegion.CALIFORNIA);
        break;
      case "voteMountain":
        card = voteCards.get(EnumRegion.MOUNTAIN);
        break;
      case "voteNorthplain":
        card = voteCards.get(EnumRegion.NORTHERN_PLAINS);
        break;
      case "voteHeartland":
        card = voteCards.get(EnumRegion.HEARTLAND);
        break;
      case "voteDelta":
        card = voteCards.get(EnumRegion.SOUTHERN_PLAINS);
        break;
      case "voteCrescent":
        card = voteCards.get(EnumRegion.NORTHERN_CRESCENT);
        break;
      case "voteSoutheast":
        card = voteCards.get(EnumRegion.SOUTHEAST);
        break;
      default:
        card = null;
        break;
    }

    if (card != null)
    {
      curVoteCardName.setText(card.getPolicyName());

      String policyText = card.getGameText();
      if (policyText.contains("X"))
      {
        policyText.replace("X", Integer.toString(card.getX()));
      }
      if (policyText.contains("Y"))
      {
        policyText.replace("Y", Integer.toString(card.getY()));
      }
      if (policyText.contains("Z"))
      {
        policyText.replace("Z", Integer.toString(card.getZ()));
      }
      curVoteCardText.setText(card.getGameText());

    }
    view.show();
  }

  /**
   * Handles a mouse click on a vote button inside the policy card voting window.
   *
   * @param event the mouse event.
   */
  public void handleVote(MouseEvent event)
  {
    Button source = (Button) event.getSource();
    String str = source.getText();

    if (str.contains("Yes"))
    {
      gameEngine.getPlayer().setVote(card, VoteType.FOR);
      gameEngine.getNetworkHandler().getOurClient().send(new Vote(gameEngine.getPlayer().getRegion(), VoteType.FOR));
    }
    else if (str.contains("No"))
    {
      gameEngine.getPlayer().setVote(card, VoteType.AGAINST);
      gameEngine.getNetworkHandler().getOurClient().send(new Vote(gameEngine.getPlayer().getRegion(), VoteType.AGAINST));
    }
    else
    {
      gameEngine.getPlayer().setVote(card, VoteType.ABSTAIN);
      gameEngine.getNetworkHandler().getOurClient().send(new Vote(gameEngine.getPlayer().getRegion(), VoteType.ABSTAIN));
    }

    switch (card.getOwner())
    {
      case CALIFORNIA:
        gameEngine.voteCalif.setDisable(true);
        break;
      case MOUNTAIN:
        gameEngine.voteMountain.setDisable(true);
        break;
      case NORTHERN_PLAINS:
        gameEngine.voteNorthplain.setDisable(true);
        break;
      case HEARTLAND:
        gameEngine.voteHeartland.setDisable(true);
        break;
      case SOUTHERN_PLAINS:
        gameEngine.voteDelta.setDisable(true);
        break;
      case NORTHERN_CRESCENT:
        gameEngine.voteCrescent.setDisable(true);
        break;
      case SOUTHEAST:
        gameEngine.voteSoutheast.setDisable(true);
        break;
    }

    view.close();
  }


  /**
   * Called at the beginning of each voting phase to initialize the policy card buttons on the voting pane.
   */
  private void initVotingCards()
  {

    ArrayList<PolicyCard> cardsToVote = gameEngine.getPlayer().getBallot().getVotingHand();

    for (PolicyCard card : cardsToVote)
    {
      EnumRegion owner = card.getOwner();

      switch (owner)
      {
        case CALIFORNIA:
          voteCards.put(EnumRegion.CALIFORNIA, card);
          gameEngine.voteCalif.setDisable(false);
          break;
        case MOUNTAIN:
          voteCards.put(EnumRegion.MOUNTAIN, card);
          gameEngine.voteMountain.setDisable(false);
          break;
        case HEARTLAND:
          voteCards.put(EnumRegion.HEARTLAND, card);
          gameEngine.voteHeartland.setDisable(false);
          break;
        case NORTHERN_CRESCENT:
          voteCards.put(EnumRegion.NORTHERN_CRESCENT, card);
          gameEngine.voteCrescent.setDisable(false);
          break;
        case NORTHERN_PLAINS:
          voteCards.put(EnumRegion.NORTHERN_PLAINS, card);
          gameEngine.voteNorthplain.setDisable(false);
          break;
        case SOUTHERN_PLAINS:
          voteCards.put(EnumRegion.SOUTHERN_PLAINS, card);
          gameEngine.voteDelta.setDisable(false);
          break;
        case SOUTHEAST:
          voteCards.put(EnumRegion.SOUTHEAST, card);
          gameEngine.voteSoutheast.setDisable(false);
          break;
      }
    }
  }

  /**
   * Initializes the buttons on the main gui depending on whether or not the region has cards requiring support.
   */
  private void initVotingButtons()
  {

  }

  @Override
  public void update(float deltaTime)
  {
    if (gameEngine.getGameState().getServerState() == ServerState.VOTING && gameEngine.getPlayer().getBallot() != null && startOfPhase)
    {
      startOfPhase = false;
      initVotingCards();
    }
  }
}
