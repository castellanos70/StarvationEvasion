package starvationevasion.client.GUI.DraftLayout;



import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.map.Map;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.SummaryBar;
import starvationevasion.client.GUI.Popups.DiscardDisplay;
import starvationevasion.client.GUI.Popups.GraphDisplay;
import starvationevasion.client.GUI.Popups.ProductBarDataDisplay;

import java.util.ArrayList;

/**
 * The DraftLayout class is responsible for the layout the GUI displays during
 * the draft phase of the game It holds the GUI elements which let the player
 * view the visualizer, graphs, data about the food products, the player's hand,
 * the played cards, action buttons, draft status, summary bar, and the player's
 * deck and discard pile
 *
 */
public class DraftLayout extends GridPane
{
  public static final int COLS = 33;
  public static final int ROWS = 18;
  private ArrayList<ColumnConstraints> colConstraintsList;
  private ArrayList<RowConstraints> rowConstraintsList;

  Stage primaryStage;

  // nodes and their relatively self-evident function in the DraftLayout scene
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
  GlobalPricesVisNode globalPriceNode;
  PopVisNode popVisNode;
  HDIVisNode HDINode;
  TickerReel reel;

  HandNode hand;
  DraftTimer draftTimer;
  Map map;
  WorldMap worldMap;
  // pointer to the main GUI
  GUI gui;

  final boolean TESTING_MODE = false;

  /**
   * Constructor for the DraftLayout scene
   * 
   * @param gui
   *          main GUI class which will allow nodes send requests to the client
   */
  public DraftLayout(GUI gui)
  {
    this.gui = gui;
    this.primaryStage = gui.getPrimaryStage();

    // set the preferred size to the screen width and height
    this.setMaxSize(gui.getMaxWidth(), gui.getMaxHeight());
    this.setMinSize(gui.getMaxWidth(), gui.getMaxHeight());

    // draw a grid to put nodes in
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);
    
    this.setOnMouseMoved(new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event)
      {
        if (hand == null) return;
        
        Point2D p = new Point2D(event.getSceneX() - hand.getLayoutX(), event.getSceneY() - hand.getLayoutY());
        hand.calculateMouseOver(p.getX(), p.getY());
      }
    });
    
    testLayout();
  }

  private void testLayout()
  {
    setGridLinesVisible(true);
    worldMap = new WorldMap(gui);
    this.add(worldMap, 0, 0, COLS, ROWS);

    VBox tickerReel = new VBox();
    reel = new TickerReel(tickerReel);
    tickerReel.getChildren().add(reel);
    this.add(tickerReel, 0, 0, COLS, 1);

    // node to let the user see graphs and region statistics
    graphNode = new GraphNode(gui);
    this.add(graphNode, 0, 1, 3, 1);

    globalPriceNode = new GlobalPricesVisNode(gui);
    this.add(globalPriceNode, 0, 2,3,1);
    
    popVisNode = new PopVisNode(gui);
    this.add(popVisNode,0,3,3,1);
    
    HDINode = new HDIVisNode(gui);
    this.add(HDINode, 0, 4,3,1);
    
    // node at the top of the screen to let the user know basic stats
    summaryBar = new SummaryBar(gui);
    this.add(summaryBar, 8, 2, 17, 3);

    // node which lets the user see if other players have played cards/finished
    // draft phase
    draftStatus = new DraftStatus();
    // this.add(draftStatus, 11, 1, 2, 4);

    // node which lets the user select and view the map of the US
    map = new Map();
    Node mapNode = map.getGameMapNode();
    

    // node which holds the ProductBar
    productBar = new ProductBar(gui);
    int productBarSize = productBar.getElements().size();
    for (int i = 0; i < productBarSize; ++i)
    {
      this.add(productBar.getElements().get(i), i + COLS / 2 - productBarSize / 2, 1, 1, 1);
    }

    // node which holds the user's deck/discard pile information
    deckNode = new DeckNode(gui);
    // this.add(deckNode, 0,12,6,3);

    // node which allows the user to undo/discard cards with the click of a
    // button
    actionButtons = new ActionButtons(gui);
    this.add(actionButtons, 0, 15, 6, 3);

    draftedCards = new DraftedCards();
   // this.add(draftedCards, 0, 8, 6, 6);

    //node which allows the user to view the current cards in their hand
    hand = new HandNode(gui);
    this.add(hand, 7, 13, 19, 5);

    draftTimer = new DraftTimer();
    this.add(draftTimer, 27, 1, 5, 2);

    pbDataDisplay = new ProductBarDataDisplay(gui);
    this.add(pbDataDisplay, 7, 4, 13, 4);

    graphDisplay = new GraphDisplay(gui);
    this.add(graphDisplay, 8, 6, 15, 6);

    discardDisplay = new DiscardDisplay(gui);
    this.add(discardDisplay, 1, 5, 3, 4);

    chatNode = new ChatNode(gui);
    chatNode.setStyle("-fx-background-color:rgb(84, 84, 84, 0.5)");
    this.add(chatNode, 27, 7, 6, 11);

    // Places a background, not in use for testing
    // this.setBackground(new Background(new BackgroundImage(
    // gui.getImageGetter().getBackground(),
    // BackgroundRepeat.NO_REPEAT,
    // BackgroundRepeat.NO_REPEAT,
    // BackgroundPosition.CENTER,
    // BackgroundSize.DEFAULT)));

    gui.getPopupManager().setGraphDisplay(graphDisplay);
    gui.getPopupManager().setPbDataDisplay(pbDataDisplay);
    gui.getPopupManager().setDiscardDisplay(discardDisplay);
  }

  public void update()
  {
    worldMap.getBoardersManager().update();
    ;
  }

  /**
   * Sets the Row and Col constraints for the gridpane to equal partitions of
   * the total length divided by the number of rows and columns
   */
  private void initializeGridSizes()
  {
    colConstraintsList = new ArrayList<>();
    rowConstraintsList = new ArrayList<>();

    for (int i = 0; i < COLS; ++i)
    {
      colConstraintsList.add(new ColumnConstraints());
      colConstraintsList.get(i).setPercentWidth(100d / COLS);
    }

    for (int i = 0; i < ROWS; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(100d / ROWS);
    }
  }

  
  
  /**
   * 
   * @return Reference to the world map.
   */
  public WorldMap getWorldMap()
  {
    return worldMap;
  }

  /**
   * Simple getter function called by the main GUI to let the GUI get the
   * product bar Used the user's selected product is updated by clicking on the
   * product bar Probably unnecessary and selection information will eventually
   * be held in DraftLayout instead of the main GUI
   * 
   * @return the productBar node
   */
  public ProductBar getProductBar()
  {
    return productBar;
  }

  /**
   * 
   * @return Instance of Ticker Real 
   */
  public TickerReel getTickerReel()
  {
    return reel;
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
   * 
   * @return draftedCards node
   */
  public DraftedCards getDraftedCards()
  {
    return draftedCards;
  }

  public DeckNode getDeckNode()
  {
    return deckNode;
  }

  public ActionButtons getActionButtons()
  {
    return actionButtons;
  }

  /**
   * Gets the MapNode for manipulation
   * 
   * @return the MapNode
   */
  public Map getMap()
  {
    return map;
  }

  /**
   * Used by actionButtons to undo cards played
   * 
   * @return hand node
   */
  public HandNode getHand()
  {
    return hand;
  }

  public ChatNode getChatNode()
  {
    return chatNode;
  }

  public void setChatNode(ChatNode chatNode2)
  {
    chatNode = chatNode2;

  }
  /**
   * 
   * @return Instance of Graph Display
   */
  public GraphDisplay getGraphDisplay()
  {
    return graphDisplay;
  }

}
