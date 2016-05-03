package starvationevasion.client.GUI.DraftLayout;


import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.ProductBar;
import starvationevasion.client.GUI.DraftLayout.Hand;
import starvationevasion.client.GUI.DraftLayout.map.Map;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.Popups.DiscardDisplay;
import starvationevasion.client.GUI.Popups.GraphDisplay;
import starvationevasion.client.GUI.Popups.ProductBarDataDisplay;
import starvationevasion.client.GUI.SummaryBar;

import java.util.ArrayList;

/**
 * The DraftLayout class is responsible for the layout the GUI displays during the draft phase of the game
 * It holds the GUI elements which let the player view the visualizer, graphs, data about the food products,
 * the player's hand, the played cards, action buttons, draft status, summary bar, and the player's deck and discard pile
 *
 */
public class DraftLayout extends GridPane
{
  private ArrayList<ColumnConstraints> colConstraintsList;
  private ArrayList<RowConstraints> rowConstraintsList;

  Stage primaryStage;

  //nodes and their relatively self-evident function in the DraftLayout scene
  ChatNode chatNode;
  VisNode visNode;
  GraphNode graphNode;
  ProductBar productBar;
  DeckNode deckNode;
  DraftedCards draftedCards;
  ActionButtons actionButtons;
  SummaryBar summaryBar;
  DraftStatus draftStatus;
  ProductBarDataDisplay pbDataDisplay;
  GraphDisplay graphDisplay;
  DiscardDisplay discardDisplay;

  Hand hand;
  DraftTimer draftTimer;
  Map map;
  WorldMap worldMap;
  //pointer to the main GUI
  GUI gui;


  final boolean TESTING_MODE=false;

  /**
   * Constructor for the DraftLayout scene
   * @param gui main GUI class which will allow nodes send requests to the client
   */
  public DraftLayout(GUI gui)
  {
    this.gui = gui;
    this.primaryStage = gui.getPrimaryStage();

    //set the preferred size to the screen width and height
    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());

    //draw a grid to put nodes in
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);

   testLayout();
  }
  private void testLayout()
  {
    setGridLinesVisible(true);
    //node to let the user see graphs and region statistics
    graphNode = new GraphNode(gui);
    this.add(graphNode, 0, 4, 1, 2);

    //node at the top of the screen to let the user know basic stats
    summaryBar = new SummaryBar(gui);
    this.add(summaryBar, 1, 0, 12, 1);

    //node which lets the user see if other players have played cards/finished draft phase
    draftStatus = new DraftStatus();
    this.add(draftStatus, 11, 1, 2, 4);

    //node which lets the user select and view the map of the US
    map = new Map();
    Node mapNode = map.getGameMapNode();    
    
    worldMap=new WorldMap(gui);
    this.add(worldMap, 1, 1, 10, 6);
  
    
    //node which holds the user's deck/discard pile information
    deckNode = new DeckNode(gui);
    this.add(deckNode, 0,6,1,3);

    //node which holds the ProductBar
    productBar = new ProductBar(gui);
    int productBarSize = productBar.getElements().size();
    for (int i = 0; i < productBarSize; ++i)
    {
      this.add(productBar.getElements().get(i), i+1, 9,1 ,1);
    }

    //node which allows the user to undo/discard cards with the click of a button
    actionButtons = new ActionButtons(gui);
    this.add(actionButtons, 11, 8, 2, 1);

    draftedCards = new DraftedCards();
    this.add(draftedCards, 11, 5, 2, 3);

    //node which allows the user to view the current cards in their hand
    hand = new Hand(gui, primaryStage);
    this.add(hand, 1, 7, 10, 2);

    draftTimer = new DraftTimer();
    this.add(draftTimer, 11, 0, 2, 1);

    pbDataDisplay = new ProductBarDataDisplay(gui);
    this.add(pbDataDisplay, 0, 5, 13, 4);

    graphDisplay = new GraphDisplay(gui);
    this.add(graphDisplay, 1, 1, 10, 6);

    discardDisplay = new DiscardDisplay(gui);
    this.add(discardDisplay,1, 5, 3, 4);

    chatNode=new ChatNode(gui);
    this.add(chatNode,0,0,1,4);


    //Places a background, not in use for testing
//    this.setBackground(new Background(new BackgroundImage(
//            gui.getImageGetter().getBackground(),
//            BackgroundRepeat.NO_REPEAT,
//            BackgroundRepeat.NO_REPEAT,
//            BackgroundPosition.CENTER,
//            BackgroundSize.DEFAULT)));

    gui.getPopupManager().setGraphDisplay(graphDisplay);
    gui.getPopupManager().setPbDataDisplay(pbDataDisplay);
    gui.getPopupManager().setDiscardDisplay(discardDisplay);
  }





  private void initDraftLayout()
  {
    //node to let the user access the visualizer

    //visNode = new VisNode(primaryStage,GUI,this);
    //this.add(visNode, 0,0,1,3);


    //node to let the user see graphs and region statistics
    graphNode = new GraphNode(gui);
    this.add(graphNode, 0, 4, 1, 2);
    chatNode=new ChatNode(gui);
    this.add(chatNode,0,0,1,3);

    //node at the top of the screen to let the user know basic stats
    summaryBar = new SummaryBar(gui);
    this.add(summaryBar, 1, 0, 12, 1);

    //node which lets the user see if other players have played cards/finished draft phase
    draftStatus = new DraftStatus();
    this.add(draftStatus, 11, 1, 2, 4);

    //node which lets the user select and view the map of the US
    map = new Map();
    Node mapNode = map.getGameMapNode();
    this.add(mapNode, 1, 1, 10, 6);

    //node which holds the user's deck/discard pile information
    deckNode = new DeckNode(gui);
    this.add(deckNode, 0,6,1,3);

    //node which holds the ProductBar
    productBar = new ProductBar(gui);
    int productBarSize = productBar.getElements().size();
    for (int i = 0; i < productBarSize; ++i)
    {
      this.add(productBar.getElements().get(i), i+1, 9,1 ,1);
    }

    //node which allows the user to undo/discard cards with the click of a button
    actionButtons = new ActionButtons(gui);
    this.add(actionButtons, 11, 8, 2, 1);

    draftedCards = new DraftedCards();
    this.add(draftedCards, 11, 5, 2, 3);

    //node which allows the user to view the current cards in their hand
//    hand = new Hand(gui, primaryStage);
//    this.add(hand, 1, 7, 10, 2);


    draftTimer = new DraftTimer();
    this.add(draftTimer, 11, 0, 2, 1);

    pbDataDisplay = new ProductBarDataDisplay(gui);
    this.add(pbDataDisplay, 0, 5, 13, 4);

    graphDisplay = new GraphDisplay(gui);
    this.add(graphDisplay, 1, 1, 10, 6);

    discardDisplay = new DiscardDisplay(gui);
    this.add(discardDisplay,1, 5, 3, 4);



    this.setBackground(new Background(new BackgroundImage(
            gui.getImageGetter().getBackground(),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT)));

    gui.getPopupManager().setGraphDisplay(graphDisplay);
    gui.getPopupManager().setPbDataDisplay(pbDataDisplay);
    gui.getPopupManager().setDiscardDisplay(discardDisplay);
  }
  private void initializeGridSizes()
  {
    colConstraintsList = new ArrayList<>();
    colConstraintsList.add(new ColumnConstraints());
    colConstraintsList.get(0).setPercentWidth(16);

    for (int i = 1; i < 13; ++i)
    {
      colConstraintsList.add(new ColumnConstraints());
      colConstraintsList.get(i).setPercentWidth(7);
    }

    rowConstraintsList = new ArrayList<>();
    for (int i = 0; i < 10; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(10);
    }
  }

  /**
   * Simple getter function called by the main GUI to let the GUI get the product bar
   * Used the user's selected product is updated by clicking on the product bar
   * Probably unnecessary and selection information will eventually be held in DraftLayout
   * instead of the main GUI
   * @return the productBar node
   */
  public ProductBar getProductBar()
  {
    return productBar;
  }


  /**
   * Function which unslects the selected product from the product bar
   */
  public void unselectSelectedProduct()
  {
    productBar.unselectSelected();
  }

  public SummaryBar getSummaryBar()
  {
    return summaryBar;
  }


  /**
   * Used by hand to let drafted cards know when a card is drafted
   * @return draftedCards node
   */
  public DraftedCards getDraftedCards(){return draftedCards;}

  public DeckNode getDeckNode()
  {
    return deckNode;
  }
  public ActionButtons getActionButtons(){return actionButtons;}

  /**
   * Gets the MapNode for manipulation
   * @return the MapNode
   */
  public Map getMap(){return map;}
  /**
   * Used by actionButtons to undo cards played
   * @return hand node
   */
  public Hand getHand(){return hand;}


  public ChatNode getChatNode(){return chatNode;}

}