package starvationevasion.client.GUIOrig;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.DraftLayout.ChatNode;
import starvationevasion.client.GUIOrig.DraftLayout.DraftLayout;
import starvationevasion.client.GUIOrig.DraftLayout.map.GamePhaseMapController;
import starvationevasion.client.GUIOrig.DraftLayout.map.MapController;
import starvationevasion.client.GUIOrig.Graphs.GraphManager;
import starvationevasion.client.GUIOrig.Popups.PopupManager;
import starvationevasion.client.GUIOrig.VotingLayout.VotingLayout;
import starvationevasion.client.GUIOrig.images.ImageGetter;
import starvationevasion.client.Logic.LocalDataContainer;
import starvationevasion.client.Logic.MainGameLoop;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;


/**
 * GUIOrig.java is the class which holds the thread for running the main GUIOrig of starvation evasion
 * The GUIOrig is responsible for drawing the game state on the screen for the user and for receiving user input
 *
 * The GUIOrig displays to the user the the time remaining in the phase, the visualization, data graphs
 * information related to food products, the user's hand, the cards the user has drafted, the user's deck and discard pile,
 * and other information relating to the draft.
 *
 * This is how the user interacts with the game
 */
public class GUI extends Application
{
  final int STARTING_HAND=7;
  private Stage primaryStage;
  private LocalDataContainer localDataContainer;
  private double boxHeight;
  private double boxWidth;

  private int maxHeight=800;
  private int maxWidth=1400;

  //the layout for the drafting phase
  DraftLayout draftLayout;

  //the layout for the voting phase
  VotingLayout votingLayout;

  private Scene gameScene;

  private Node currentRoot;

  ArrayList<EnumFood> productList;

  //Helper classes
  ImageGetter imageGetter;
  PopupManager popupManager;
  GraphManager graphManager;

  //The user's assigned region
  EnumRegion assignedRegion;
  private ChatNode chatNode;

  private ArrayList<EnumPolicy> cardsInHand=new ArrayList<>();
  //context variables
  boolean selectingRegion = false;
  boolean selectingProduct = false;
  private boolean draftingPhase=true;
  /**
   * Default constructor for GUIOrig
   * Used for debugging the GUIOrig, cannot connect to a game
   */
  public GUI()
  {
      super();
    //client2=new Client("Nathan", 2020);
    this.localDataContainer = localDataContainer;
    //assignedRegion = client2.getRegion();
  }

  /**
   * Constructor for the GUIOrig which the client calls
   */
  private Client client2;
  private MainGameLoop mainGameLoop;
  public GUI(Client client, LocalDataContainer localDataContainer)
  {
    super();
    client2=client;
   // this.client = client;
    this.localDataContainer = localDataContainer;
    assignedRegion = client.getRegion();
  }
  public Client getClient(){return client2;}
  /**
   * Main function which launches the GUIOrig thread
   * @param args
   */
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    // TODO: THIS WILL BE REMOVED WHEN PHASE HANDLING IS FULLY IMPLEMENTED
    MapController.setCurrentController(GamePhaseMapController.class);
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Starvation Evasion");

    //fills a list of all the product types
    initializeProductList();

    boxHeight = maxHeight*.1;
    boxWidth = maxWidth*.07;

//    primaryStage.setMaxHeight(maxHeight);
//    primaryStage.setMinHeight(maxHeight);
    primaryStage.setResizable(true);

    //instantiate helper classes
    imageGetter = new ImageGetter();
    popupManager = new PopupManager(this);
    graphManager = new GraphManager(this);

    //instantiate the DraftLayout
    draftLayout = new DraftLayout(this);
    votingLayout = new VotingLayout(this);

    //make a scene for displaying the game
    gameScene = new Scene(draftLayout);
    currentRoot = draftLayout;
    primaryStage.setScene(gameScene);


    mainGameLoop=new MainGameLoop(this);
    //if(!client.isAI)
    {
      primaryStage.show();
    }
    primaryStage.setOnCloseRequest(event2 ->
    {
      primaryStage.close();
      mainGameLoop.stop();
    });
    initGame();
  }
  @Override
  public void stop()
  {
    client2.closeAll();
  }
  /**
   * Simple getter in case any node needs to get the stage
   */
  public void initGame()
  {
    //client2.getHand();
    cardsInHand=client2.getHand();
    if(cardsInHand!=null)
    {
      getDraftLayout().getHand().setHand(cardsInHand.toArray(new EnumPolicy[cardsInHand.size()]));
    }
    assignedRegion=client2.getRegion();
    getDraftLayout().getSummaryBar().setRegion(assignedRegion);
  }
  public ArrayList<EnumPolicy> getCardsInHand() {return cardsInHand;}
  public void setCardsInHand(ArrayList<EnumPolicy> cards)
  {
    cardsInHand=cards;
    cardsInHand=client2.getHand();
    if(cardsInHand!=null)
    {
      getDraftLayout().getHand().setHand(cardsInHand.toArray(new EnumPolicy[cardsInHand.size()]));
    }
  }
  public Stage getPrimaryStage()
  {
    return primaryStage;
  }

  /**
   * Simple getter to let nodes access the PopupManager
   * @return popup manager in the GUIOrig
   */
  public PopupManager getPopupManager()
  {
    return popupManager;
  }

  /**
   * Simple getter to let nodes access the ImageGetter
   * @return
   */
  public ImageGetter getImageGetter()
  {
    return imageGetter;
  }

  /**
   * Simple getter to allow classes to view the list of products
   * Written before there was an list of products in the ENUM file
   *
   * @return
   */
  public ArrayList<EnumFood> getProductList()
  {
    return productList;
  }
  public ChatNode getChatNode(){return chatNode;}
  /**
   * Method which switches the which phase of the game is being displayed on the GUIOrig
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

  /**
   * Getter that returns whether it is the drafting phase or not
   * @return boolean true is drafting phase, false if voting
   */
  public boolean isDraftingPhase(){return draftingPhase;}
  /**
   * sets the context that the user is selecting a region to draft a card to the passed in param
   * @param toSet
   */
  public void setSelectingRegion(boolean toSet)
  {
    selectingRegion = toSet;
  }

  /**
   * sets the context that the user is selecting a product to draft a card to the passed in param
   * @param toSet
   */
  public void setSelectingProduct(boolean toSet)
  {
    selectingProduct = toSet;
  }

  /**
   * Checks to see if the GUIOrig's context that the user needs to select a product to draft a card
   * @return
   */
  public boolean getSeletingProduct()
  {
    return selectingProduct;
  }

  /**
   * Simple getter for the screen height in pixels
   * @return screen height
   */
  public double getMaxHeight()
  {
    return maxHeight;
  }

  /**
   * Simple getter for the screen width in pixels
   * @return screen width
   */
  public double getMaxWidth()
  {
    return maxWidth;
  }

  /**
   * Gets the height of one of the grid spaces in the draft layout
   * @return height of a grid box in pixels
   */
  public double getBoxHeight()
  {
    return boxHeight;
  }

  /**
   * gets the width of one of the grid spaces in the draft layout
   * @return height of a gird box in pixels
   */
  public double getBoxWidth()
  {
    return boxWidth;
  }

  /**
   * Gets the DraftLayout to manipulate GUIOrig elements of the draft phase
   * @return The DraftLayout
   */
  public DraftLayout getDraftLayout()
  {
    return draftLayout;
  }
  public VotingLayout getVotingLayout(){return votingLayout;}
  /**
   * Returns the GraphManger. Used by nodes which displays graphs
   * @return GraphManager
   */
  public GraphManager getGraphManager()
  {
    return graphManager;
  }

  /**
   * gets the FoodType at index of the food product list
   * @param index
   * @return EnumFood at the specified index
   */
  public EnumFood getFoodType(int index)
  {
    return productList.get(index);
  }

  /**
   * Updates the GUIOrig's assigned region to the passed in region
   * @param region
   */
  public void updateAssignedRegion(EnumRegion region)
  {
    this.assignedRegion = region;
  }

  /**
   * Returns the assigned region that the user was assigned
   * @return region which the user has been assigned
   */
  public EnumRegion getAssignedRegion()
  {
    return assignedRegion;
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
