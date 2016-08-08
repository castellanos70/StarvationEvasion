package starvationevasion.client.GUI.VotingLayout;

import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.CardView;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.TickerReel;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.SummaryBar;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.util.ArrayList;
import java.util.Stack;

public class VotingLayout extends GridPane
{
  private ArrayList<ColumnConstraints> colConstraintsList;
  private ArrayList<RowConstraints> rowConstraintsList;

  private Stage primaryStage;
  private ImageView backGround;
  private GUI gui;

  FinishButton finishButton;
  SummaryBar summaryBar;
  VotingTimer timer;
  TickerReel tickerReel;

  // places on the GUI where the cards will go
  CardSpace[][] cardSpaces=new CardSpace[7][3];

  // places on the GUI where the voting buttons will go
  //XY coordinate grid of voting cards corresponding to cards
  VotingNode[][] votingNodes = new VotingNode[7][3];

  // places on the GUI where the maps of the regions will go
  ArrayList<RegionMap> regionMaps = new ArrayList<>();

  private GridPane centerPane;
  private ChatNode chatNode;

  private CardViewNode cardViewNode;

  private boolean receivedCards = false;

  public VotingLayout(GUI gui)
  {
    this.gui = gui;
   // this.setGridLinesVisible(true);
    // gui.getChatNode();
    primaryStage = this.gui.getPrimaryStage();

    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());
    centerPane = new GridPane();
   // centerPane.setGridLinesVisible(false);
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);
    this.setGridLinesVisible(true);
    ImageView backGround= new ImageView(gui.getImageGetter().getWorldMap());
    this.add(backGround, 0, 15);
    finishButton = new FinishButton(this.gui);
    // this.setLeft(finishButton);
    this.add(finishButton, 0,24, 1, 1);
    summaryBar = new SummaryBar(this.gui);
    // this.setTop(summaryBar);
    this.add(summaryBar, 0, 1, 1, 1);
    timer = new VotingTimer(this.gui);
    // this.setRight(timer);
    this.add(timer, 6, 1, 1, 1);
    initializeVotingNodes();
    initializeCardSpaces();
    initializeRegionMaps();
    // setCenter(centerPane);
    // setBottom(chatNode);
    chatNode = new ChatNode(gui);
    this.add(chatNode, 0, 14, 1, 10);

    VBox real = new VBox();
    tickerReel = new TickerReel(real);
    real.getChildren().add(tickerReel);
    this.add(real, 0, 0, 13, 1);
    cardViewNode=new CardViewNode();
    this.add(cardViewNode,0,1);
//    GraphNode graphNode = new GraphNode(gui);
//    this.add(graphNode, 0, 10, 1, 5);
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

  public void updateCardSpaces(ArrayList<PolicyCard> cards)
  {
    resetVotingLayout();
    if (cards != null)
    {
      receivedCards = true;
      for (PolicyCard card : cards)
      {
        int index = getIndexOfRegion(card.getOwner());
        if (card.getCardType().getVotesRequired() == 0)
        {
          if (cardSpaces[index][0].getCard()== null) cardSpaces[index][0].setCard(card, gui);
          else if (cardSpaces[index][1].getCard() == null) cardSpaces[index][1].setCard(card, gui);
          else if (cardSpaces[index][2].getCard() == null) cardSpaces[index][2].setCard(card, gui);
        }
        else
        {
          final int availableRow;
          if(cardSpaces[index][0].getCard()==null) {
            cardSpaces[index][0].setCard(card, gui);
            availableRow=0;
          }else if(cardSpaces[index][1].getCard()==null) {
            cardSpaces[index][1].setCard(card, gui);
            availableRow=1;
          }else if(cardSpaces[index][2].getCard()==null) {
            availableRow=2;
            cardSpaces[index][2].setCard(card, gui);
          }else availableRow=0;
          Button[] buttons = votingNodes[index][availableRow].addVotingButtons();
          buttons[0].setOnAction(event ->
          {
            gui.getClient().voteUp(cardSpaces[index][availableRow].getCard().getGameCard());
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });
          buttons[1].setOnAction(event ->
          {
            gui.getClient().voteDown(cardSpaces[index][availableRow].getCard().getGameCard());
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });

        }
      }
    }
  }

  public void resetVotingLayout()
  {
    receivedCards = false;
    for (int i = 0; i <7 ; i++) {
      for(int j=0;j<3;j++){
        cardSpaces[i][j].removeCard();
      }
    }
  }

  public TickerReel getTickerReel(){return tickerReel;}

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
      CardSpace thirdNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 3);

      firstNewCardSpace.setOnMouseEntered(event ->
      {
        if(firstNewCardSpace.getCard()!=null){
          cardViewNode.addCard(firstNewCardSpace.getCard().getGameCard());
        }
      });secondNewCardSpace.setOnMouseEntered(event ->
      {
        if(secondNewCardSpace.getCard()!=null){
          cardViewNode.addCard(secondNewCardSpace.getCard().getGameCard());
        }
      });thirdNewCardSpace.setOnMouseEntered(event ->
      {
        if(thirdNewCardSpace.getCard()!=null){
          cardViewNode.addCard(thirdNewCardSpace.getCard().getGameCard());
        }
      });
      // firstNewCardSpace.setCard(EnumRegion.US_REGIONS[i],
      // EnumPolicy.Clean_River_Incentive,gui);
      cardSpaces[i][0]=(firstNewCardSpace);
      cardSpaces[i][1]=(secondNewCardSpace);
      cardSpaces[i][2]=(thirdNewCardSpace);
      this.add(firstNewCardSpace, i + 2, 7, 1, 5);
      this.add(secondNewCardSpace, i + 2, 13, 1, 5);
      this.add(thirdNewCardSpace, i + 2, 19, 1, 5);
    }
  }

  private void initializeVotingNodes()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      VotingNode firstNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 1);
      VotingNode secondNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 2);
      VotingNode thirdNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 3);
      votingNodes[i][0]=(firstNewVotingNode);
      votingNodes[i][1]=(secondNewVotingNode);
      votingNodes[i][2]=(thirdNewVotingNode);

      this.add(firstNewVotingNode, i + 2, 12, 1, 1);
      this.add(secondNewVotingNode, i + 2, 18, 1, 1);
      this.add(thirdNewVotingNode, i + 2, 24, 1, 1);
    }
  }

  /**
   * Initializes the nodes which will hold the images of the regions of the USA
   */
  private void initializeRegionMaps()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      ImageView newRegionMap=new ImageView(EnumRegion.US_REGIONS[i].getIconLarge());
     // newRegionMap.setFitWidth((getColumnConstraints().get(i).getPercentWidth()*primaryStage.getWidth())/100);
     // newRegionMap.setFitHeight((getRowConstraints().get(i).getPercentHeight()*primaryStage.getHeight())/100);
      newRegionMap.setFitWidth(128);
      newRegionMap.setFitHeight(128);
      String s = EnumRegion.US_REGIONS[i].name();
      Label label;
      label = new Label(s);
      label.setWrapText(true);
      label.setTextFill(Color.BLACK);
      label.setVisible(false);
      label.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
      label.getStyleClass().add("pbarelement");
      newRegionMap.setOnMouseEntered(event -> {
        label.setVisible(true);
      });
      newRegionMap.setOnMouseExited(event -> {
        label.setVisible(false);
      });
      label.setMouseTransparent(true);
      StackPane stackPane=new StackPane(newRegionMap,label);
      this.add(stackPane, i + 2, 1, 1, 8);
      GridPane.setHalignment(newRegionMap, HPos.CENTER);
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
