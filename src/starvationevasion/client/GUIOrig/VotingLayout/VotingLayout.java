package starvationevasion.client.GUIOrig.VotingLayout;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import starvationevasion.client.GUIOrig.DraftLayout.ChatNode;
import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.SummaryBar;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

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

  // places on the GUIOrig where the cards will go
  ArrayList<CardSpace> cardSpaces = new ArrayList<>();

  // places on the GUIOrig where the voting buttons will go
  ArrayList<VotingNode> votingNodes = new ArrayList<>();

  //places on the GUIOrig where the maps of the regions will go
  ArrayList<RegionMap> regionMaps = new ArrayList<>();

  private GridPane centerPane;
  private ChatNode chatNode;
  public VotingLayout(GUI gui)
  {
    this.gui = gui;
    chatNode=new ChatNode(gui);//gui.getChatNode();
    primaryStage = this.gui.getPrimaryStage();


    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());
    centerPane=new GridPane();
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);
    this.setGridLinesVisible(true);

    finishButton = new FinishButton(this.gui);
   // this.setLeft(finishButton);
    this.add(finishButton,7,20,1,1);
    summaryBar = new SummaryBar(this.gui);
    //this.setTop(summaryBar);
    this.add(summaryBar, 0, 1, 1, 1);
    timer = new VotingTimer(this.gui);
   // this.setRight(timer);
    this.add(timer, 6, 0, 1, 1);
    initializeCardSpaces();
    initializeVotingNodes();
    initializeRegionMaps();
   // setCenter(centerPane);
    //setBottom(chatNode);
    this.add(chatNode, 0, 20, 7, 6);

    this.setBackground(new Background(new BackgroundImage(
            gui.getImageGetter().getBackground(),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT)));
  }

  public void setCards(PolicyCard[] cards)
  {
   // for (int i = 0; i < ; i++)
    {

    }

  }
  public ChatNode getChatNode(){return chatNode;}
  {

  }

  private void initializeCardSpaces()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      CardSpace firstNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 1);
      CardSpace secondNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 2);
      firstNewCardSpace.setCard(EnumRegion.US_REGIONS[i], EnumPolicy.Clean_River_Incentive,gui);
      cardSpaces.add(firstNewCardSpace);
      cardSpaces.add(secondNewCardSpace);
      this.add(firstNewCardSpace, i, 10, 1, 4);
      this.add(secondNewCardSpace, i, 15, 1, 4);
    }
  }

  private void initializeVotingNodes()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      VotingNode firstNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 1);
      VotingNode secondNewVotingNode = new VotingNode(gui, EnumRegion.US_REGIONS[i], 2);
      votingNodes.add(firstNewVotingNode);
      votingNodes.add(secondNewVotingNode);
      this.add(firstNewVotingNode, i, 14, 1, 1);
      this.add(secondNewVotingNode, i, 19, 1, 1);
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
      this.add(newRegionMap, i, 1, 1, 8);
    }
  }

  private void initializeGridSizes()
  {
    colConstraintsList = new ArrayList<>();

    for (int i = 0; i < 7; ++i)
    {
      colConstraintsList.add(new ColumnConstraints());
      colConstraintsList.get(i).setPercentWidth(13);
    }
    colConstraintsList.add(new ColumnConstraints());
    colConstraintsList.get(7).setPercentWidth(9);

    rowConstraintsList = new ArrayList<>();
    for (int i = 0; i < 25; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(10);
    }
  }
}