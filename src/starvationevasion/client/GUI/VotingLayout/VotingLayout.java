package starvationevasion.client.GUI.VotingLayout;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.GraphNode;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.SummaryBar;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.GameCard;

import java.util.ArrayList;

public class VotingLayout extends GridPane
{
  private ArrayList<ColumnConstraints> colConstraintsList;
  private ArrayList<RowConstraints> rowConstraintsList;

  private Stage primaryStage;

  private GUI gui;

  FinishButton finishButton;
  SummaryBar summaryBar;
  VotingTimer timer;

  // places on the GUI where the cards will go
  ArrayList<CardSpace> cardSpacesFirstRow = new ArrayList<>();
  ArrayList<CardSpace> cardSpacesSecondRow = new ArrayList<>();

  // places on the GUI where the voting buttons will go
  ArrayList<VotingNode> votingNodes = new ArrayList<>();

  // places on the GUI where the maps of the regions will go
  ArrayList<RegionMap> regionMaps = new ArrayList<>();

  private GridPane centerPane;
  private ChatNode chatNode;

  private boolean receivedCards = false;

  public VotingLayout(GUI gui)
  {
    this.gui = gui;
    // gui.getChatNode();
    primaryStage = this.gui.getPrimaryStage();

    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());
    centerPane = new GridPane();
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);
    this.setGridLinesVisible(true);

    finishButton = new FinishButton(this.gui);
    // this.setLeft(finishButton);
    this.add(finishButton, 8, 20, 1, 1);
    summaryBar = new SummaryBar(this.gui);
    // this.setTop(summaryBar);
    this.add(summaryBar, 0, 1, 1, 1);
    timer = new VotingTimer(this.gui);
    // this.setRight(timer);
    this.add(timer, 6, 0, 1, 1);
    initializeCardSpaces();
    initializeVotingNodes();
    initializeRegionMaps();
    // setCenter(centerPane);
    // setBottom(chatNode);
    chatNode = new ChatNode(gui);
    this.add(chatNode, 0, 0, 1, 10);
    GraphNode graphNode = new GraphNode(gui);
    this.add(graphNode, 0, 10, 1, 5);
    // this.setBackground(new Background(new BackgroundImage(
    // gui.getImageGetter().getBackground(),
    // BackgroundRepeat.NO_REPEAT,
    // BackgroundRepeat.NO_REPEAT,
    // BackgroundPosition.CENTER,
    // BackgroundSize.DEFAULT)));
  }

  public boolean hasReceivedCards()
  {
    return receivedCards;
  }

  public ChatNode getChatNode()
  {
    return chatNode;
  }

  {}

  public void updateCardSpaces(ArrayList<GameCard> cards)
  {
    if (cards != null)
    {
      receivedCards = true;
      for (GameCard card : cards)
      {
        int index = getIndexOfRegion(card.getOwner());
        if (card.votesRequired() == 0)
        {
          if (cardSpacesSecondRow.get(index).getCard() == null)
          {
            cardSpacesSecondRow.get(index).setCard(card.getOwner(), card.getCardType(), gui);
          }
          else cardSpacesFirstRow.get(index).setCard(card.getOwner(), card.getCardType(), gui);
        }
        else
        {
          Button[] buttons = votingNodes.get(index).addVotingButtons();
          buttons[0].setOnAction(event ->
          {
            gui.getClient().voteUp(cardSpacesFirstRow.get(index).getCard().getPolicyCard());
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });
          buttons[1].setOnAction(event ->
          {
            gui.getClient().voteDown(cardSpacesFirstRow.get(index).getCard().getPolicyCard());
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });
          cardSpacesFirstRow.get(index).setCard(card.getOwner(), card.getCardType(), gui);
        }
      }
    }
  }

  public void resetVotingLayout()
  {
    receivedCards = false;
    for (CardSpace cardSpace : cardSpacesFirstRow)
    {
      cardSpace.removeCard();
    }
    for (CardSpace cardSpace : cardSpacesSecondRow)
    {
      cardSpace.removeCard();
    }
  }

  private int getIndexOfRegion(EnumRegion region)
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      if (region.equals(EnumRegion.US_REGIONS[i])) return i;
    }
    return -1;
  }

  private void initializeCardSpaces()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      CardSpace firstNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 1);
      CardSpace secondNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 2);
      // firstNewCardSpace.setCard(EnumRegion.US_REGIONS[i],
      // EnumPolicy.Clean_River_Incentive,gui);
      cardSpacesFirstRow.add(firstNewCardSpace);
      cardSpacesSecondRow.add(secondNewCardSpace);
      this.add(firstNewCardSpace, i + 1, 10, 1, 4);
      this.add(secondNewCardSpace, i + 1, 15, 1, 4);
    }
  }

  private void initializeVotingNodes()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      VotingNode firstNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 1);
      VotingNode secondNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 2);
      votingNodes.add(firstNewVotingNode);
      // votingNodes.add(secondNewVotingNode);

      this.add(firstNewVotingNode, i + 1, 14, 1, 1);
      this.add(secondNewVotingNode, i + 1, 19, 1, 1);
    }
  }

  /**
   * Initializes the nodes which will hold the images of the regions of the USA
   */
  private void initializeRegionMaps()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      RegionMap newRegionMap = new RegionMap(this.gui, EnumRegion.US_REGIONS[i]);
      regionMaps.add(newRegionMap);
      this.add(newRegionMap, i + 1, 1, 1, 8);
    }
  }

  private void initializeGridSizes()
  {
    colConstraintsList = new ArrayList<>();
    colConstraintsList.add(new ColumnConstraints());
    colConstraintsList.get(0).setPercentWidth(20);
    for (int i = 1; i < 8; ++i)
    {
      colConstraintsList.add(new ColumnConstraints());
      colConstraintsList.get(i).setPercentWidth(13);
    }
    colConstraintsList.add(new ColumnConstraints());
    colConstraintsList.get(8).setPercentWidth(9);

    rowConstraintsList = new ArrayList<>();
    for (int i = 0; i < 25; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(10);
    }
  }
}
