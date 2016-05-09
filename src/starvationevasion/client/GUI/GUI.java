package starvationevasion.client.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.DraftLayout;
import starvationevasion.client.GUI.DraftLayout.HandNode;
import starvationevasion.client.GUI.DraftLayout.map.GamePhaseMapController;
import starvationevasion.client.GUI.DraftLayout.map.MapController;
import starvationevasion.client.GUI.Graphs.GraphManager;
import starvationevasion.client.GUI.Popups.PopupManager;
import starvationevasion.client.GUI.images.ImageGetter;
//import starvationevasion.client.GUI.votingHud.VotingLayout;
import starvationevasion.client.GUI.VotingLayout.VotingLayout;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.client.Logic.LocalDataContainer;
import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;

/**
 * GUI.java is the class which holds the thread for running the main GUI of
 * starvation evasion The GUI is responsible for drawing the game state on the
 * screen for the user and for receiving user input
 *
 * The GUI displays to the user the the time remaining in the phase, the
 * visualization, data graphs information related to food products, the user's
 * other information relating to the draft.
 *
 * This is how the user interacts with the game
 */
public class GUI extends Application
{
  final int STARTING_HAND = 7;
  private Stage primaryStage;
  private LocalDataContainer localDataContainer;
  private double boxHeight;
  private double boxWidth;

  private int maxHeight = 700;
  private int maxWidth = 1300;

  // the layout for the drafting phase
  DraftLayout draftLayout;

  // the layout for the voting phase
  VotingLayout votingLayout;

  private Scene gameScene;

  private Node currentRoot;

  ArrayList<EnumFood> productList;

  // Helper classes
  ImageGetter imageGetter;
  PopupManager popupManager;
  GraphManager graphManager;

  // The user's assigned region
  EnumRegion assignedRegion;
  private ChatNode chatNode;

  private ArrayList<EnumPolicy> cardsInHand = new ArrayList<>();
  // context variables
  boolean selectingRegion = false;
  boolean selectingProduct = false;
  private boolean draftingPhase = true;
  private boolean needHand = true;

  /**
   * Default constructor for GUI Used for debugging the GUI, cannot connect to a
   * game
   */
  public GUI()
  {
    super();
  }

  /**
   * Constructor for the GUI which the client calls
   */
  private Client client;

  public GUI(Client client, LocalDataContainer localDataContainer)
  {
    super();
    this.client = client;
    this.localDataContainer = localDataContainer;
    assignedRegion = client.getRegion();
  }

  public Client getClient()
  {
    return client;
  }

//  /**
//   * Main function which launches the GUI thread
//   * 
//   * @param args
//   */
//  public static void main(String[] args)
//  {
//    launch(args);
//  }

  @Override
  public void start(Stage primaryStage)
  {
    // TODO: THIS WILL BE REMOVED WHEN PHASE HANDLING IS FULLY IMPLEMENTED
    MapController.setCurrentController(GamePhaseMapController.class);
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Starvation Evasion");

    // fills a list of all the product types
    initializeProductList();

    // primaryStage.setMaxHeight(maxHeight);
    // primaryStage.setMinHeight(maxHeight);
    primaryStage.setResizable(true);

    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    primaryStage.setX(bounds.getMinX());
    primaryStage.setY(bounds.getMinY());
    primaryStage.setWidth(bounds.getWidth());
    primaryStage.setHeight(bounds.getHeight());

    boxHeight = primaryStage.getWidth() / DraftLayout.ROWS;
    boxWidth = primaryStage.getWidth() / DraftLayout.COLS;

    // instantiate helper classes
    imageGetter = new ImageGetter();
    popupManager = new PopupManager(this);
    graphManager = new GraphManager(this);

    // instantiate the DraftLayout
    draftLayout = new DraftLayout(this);
    votingLayout = new VotingLayout(this);
   
    

    // make a scene for displaying the game
    gameScene = new Scene(draftLayout);
    currentRoot = draftLayout;
    primaryStage.setScene(gameScene);

    primaryStage.show();

    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
    {
      @Override
      public void handle(WindowEvent arg0)
      {
        client.shutdown();
        Platform.exit();
      }
    });

    initGame();
  }

  @Override
  public void stop()
  {
    client.shutdown();
  }

  /**
   * basic initializing of things that only need to be called once
   */
  public void initGame()
  {
    cardsInHand = client.getHand();
    assignedRegion = client.getRegion();
    if (cardsInHand != null)
    {
      getDraftLayout().getHand().setPolicies(cardsInHand.toArray(new EnumPolicy[cardsInHand.size()]));
    }

    getDraftLayout().getSummaryBar().setRegion(assignedRegion);
  }

  /**
   * Getter for hand arraylist
   * 
   * @return
   */
  public ArrayList<EnumPolicy> getCardsInHand()
  {
    return cardsInHand;
  }

  /**
   * Sets the cards in hand and sets flag to indicate that cards have been set
   * 
   * @param cards
   */
  public void setCardsInHand(ArrayList<EnumPolicy> cards)
  {
    needHand = false;
    cardsInHand = cards;
  }

  public void setAssignedRegion(EnumRegion region)
  {
    assignedRegion = region;
  }

  /**
   * Returns flag indicating if hand has been set
   * 
   * @return
   */
  public boolean needsHand()
  {
    return needHand;
  }

  /**
   * resets hand and buttons
   */
  public void resetDraftingPhase()
  {
    needHand = true;
    cardsInHand.clear();
    cardsInHand = null;
//    draftLayout.getHand().newTurn();
    draftLayout.getActionButtons().resetActionButtons();
  }

  public void updateState()
  {
    draftLayout.update();
  }

  /**
   * resets what cards are being voted on
   */
  public void resetVotingPhase()
  {
    votingLayout.resetVotingLayout();
  }

  /**
   * Simple getter in case any node needs to get the stage
   */
  public Stage getPrimaryStage()
  {
    return primaryStage;
  }

  /**
   * Simple getter to let nodes access the PopupManager
   * 
   * @return popup manager in the GUI
   */
  public PopupManager getPopupManager()
  {
    return popupManager;
  }

  /**
   * Simple getter to let nodes access the ImageGetter
   * 
   * @return
   */
  public ImageGetter getImageGetter()
  {
    return imageGetter;
  }

  /**
   * Simple getter to allow classes to view the list of products Written before
   * there was an list of products in the ENUM file
   *
   * @return
   */
  public ArrayList<EnumFood> getProductList()
  {
    return productList;
  }

  public ChatNode getChatNode()
  {
    return chatNode;
  }

  /**
   * Method which switches the which phase of the game is being displayed on the
   * GUI
   */
  public void switchScenes()
  {
    if (currentRoot == draftLayout)
    {
      primaryStage.getScene().setRoot(votingLayout);
      currentRoot = votingLayout;
      draftingPhase=false;
    }
    else
    {
      primaryStage.getScene().setRoot(draftLayout);
      currentRoot = draftLayout;
      draftingPhase=true;
    }
  }

  public boolean isDraftLayout()
  {
    if (currentRoot == draftLayout) return true;
    else return false;
  }

  /**
   * Getter that returns whether it is the drafting phase or not
   * 
   * @return boolean true is drafting phase, false if voting
   */
  public boolean isDraftingPhase()
  {
    return draftingPhase;
  }

  /**
   * sets the context that the user is selecting a region to draft a card to the
   * passed in param
   * 
   * @param toSet
   */
  public void setSelectingRegion(boolean toSet)
  {
    selectingRegion = toSet;
  }

  /**
   * sets the context that the user is selecting a product to draft a card to
   * the passed in param
   * 
   * @param toSet
   */
  public void setSelectingProduct(boolean toSet)
  {
    selectingProduct = toSet;
  }

  /**
   * Checks to see if the GUI's context that the user needs to select a product
   * to draft a card
   * 
   * @return
   */
  public boolean getSeletingProduct()
  {
    return selectingProduct;
  }

  /**
   * Simple getter for the screen height in pixels
   * 
   * @return screen height
   */
  public double getMaxHeight()
  {
    return maxHeight;
  }

  /**
   * Simple getter for the screen width in pixels
   * 
   * @return screen width
   */
  public double getMaxWidth()
  {
    return maxWidth;
  }

  /**
   * Gets the height of one of the grid spaces in the draft layout
   * 
   * @return height of a grid box in pixels
   */
  public double getBoxHeight()
  {
    return boxHeight;
  }

  /**
   * gets the width of one of the grid spaces in the draft layout
   * 
   * @return height of a gird box in pixels
   */
  public double getBoxWidth()
  {
    return boxWidth;
  }

  /**
   * Gets the DraftLayout to manipulate GUI elements of the draft phase
   * 
   * @return The DraftLayout
   */
  public DraftLayout getDraftLayout()
  {
    return draftLayout;
  }

  public VotingLayout getVotingLayout()
  {
    return votingLayout;
  }

  /**
   * Returns the GraphManger. Used by nodes which displays graphs
   * 
   * @return GraphManager
   */
  public GraphManager getGraphManager()
  {
    return graphManager;
  }

  /**
   * gets the FoodType at index of the food product list
   * 
   * @param index
   * @return EnumFood at the specified index
   */
  public EnumFood getFoodType(int index)
  {
    return productList.get(index);
  }

  /**
   * Updates the GUI's assigned region to the passed in region
   * 
   * @param region
   */
  public void updateAssignedRegion(EnumRegion region)
  {
    this.assignedRegion = region;
  }

  /**
   * Returns the assigned region that the user was assigned
   * 
   * @return region which the user has been assigned
   */
  public EnumRegion getAssignedRegion()
  {
    return assignedRegion;
  }

  private void initTimer()
  {
    java.util.Timer timer = new java.util.Timer();
    ChatNode chatNodeDraft = draftLayout.getChatNode();
    ChatNode chatNodeVote = votingLayout.getChatNode();
    ChatManager chatManager = client.getChatManager();
    HandNode hand = getDraftLayout().getHand();
    TimerTask timerTask = new TimerTask()
    {
      boolean flag = false;

      @Override
      public void run()
      {
        Platform.runLater(() ->
        {

          chatNodeDraft.setChatMessages(chatManager.getChat());
          chatNodeVote.setChatMessages(chatManager.getChat());
          if (needsHand() && client.getHand() != null && !client.getHand().isEmpty())
          {

            setCardsInHand(client.getHand());
            cardsInHand = client.getHand();
            hand.setPolicies(client.getHand().toArray(new EnumPolicy[cardsInHand.size()]));
            System.out.println("please work " + Arrays.toString(hand.getPolicies()) + Platform.isFxApplicationThread());
          }
          if (getDraftLayout().getHand().getPolicies() != null)
          {
            chatNodeDraft.setHand(getDraftLayout().getHand().getPolicies());
            chatNodeVote.setHand(getDraftLayout().getHand().getPolicies());
          }
          if (isDraftingPhase() && client.getState().equals(starvationevasion.server.model.State.VOTING))
          {
            resetDraftingPhase();
            switchScenes();
          }
          if (!isDraftingPhase() && (client.getState().equals(starvationevasion.server.model.State.DRAFTING)
              || client.getState().equals(starvationevasion.server.model.State.DRAWING)))
          {
            resetVotingPhase();
            switchScenes();
          }
           if (client.getVotingCards() != null &&
           !getVotingLayout().hasReceivedCards())
           getVotingLayout().updateCardSpaces(client.getVotingCards());

        });
      }
    };

    timer.schedule(timerTask, 100, 1000);
  }

  private void initializeProductList()
  {
    productList = new ArrayList<>();
    productList.add(EnumFood.CITRUS);
    productList.add(EnumFood.FRUIT);
    productList.add(EnumFood.NUT);
    productList.add(EnumFood.GRAIN);
    productList.add(EnumFood.OIL);
    productList.add(EnumFood.VEGGIES);
    productList.add(EnumFood.SPECIAL);
    productList.add(EnumFood.FEED);
    productList.add(EnumFood.FISH);
    productList.add(EnumFood.MEAT);
    productList.add(EnumFood.POULTRY);
    productList.add(EnumFood.DAIRY);
  }

}
