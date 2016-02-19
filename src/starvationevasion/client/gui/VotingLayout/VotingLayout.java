package starvationevasion.client.GUI.VotingLayout;

import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.SummaryBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
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

  // places on the GUI where the cards will go
  ArrayList<CardSpace> cardSpaces = new ArrayList<>();

  // places on the GUI where the voting buttons will go
  ArrayList<VotingNode> votingNodes = new ArrayList<>();

  //places on the GUI where the maps of the regions will go
  ArrayList<RegionMap> regionMaps = new ArrayList<>();

  public VotingLayout(GUI gui)
  {
    this.gui = gui;
    primaryStage = this.gui.getPrimaryStage();

    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());

    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);
    this.setGridLinesVisible(true);

    finishButton = new FinishButton(this.gui);
    this.add(finishButton, 7, 19, 1, 1);

    summaryBar = new SummaryBar(this.gui);
    this.add(summaryBar, 0,0,7,2);

    timer = new VotingTimer(this.gui);
    this.add(timer, 7,0,1,2);

    initializeCardSpaces();
    initializeVotingNodes();
    initializeRegionMaps();
  }

  public void setCards(PolicyCard[] cards)
  {
   // for (int i = 0; i < ; i++)
    {

    }

  }
  {

  }

  private void initializeCardSpaces()
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      CardSpace firstNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 1);
      CardSpace secondNewCardSpace = new CardSpace(gui, EnumRegion.US_REGIONS[i], 2);
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
      this.add(newRegionMap, i, 2, 1, 8);
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
    for (int i = 0; i < 20; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(10);
    }
  }
}