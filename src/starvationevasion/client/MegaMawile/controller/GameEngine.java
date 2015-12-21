package starvationevasion.client.MegaMawile.controller;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import starvationevasion.client.MegaMawile.ChartGraphics;
import starvationevasion.client.MegaMawile.StatisticReadData;
import starvationevasion.client.MegaMawile.model.*;
import starvationevasion.client.MegaMawile.net.NetworkHandler;
import starvationevasion.client.MegaMawile.view.PolicyCardImageManager;
import starvationevasion.common.*;
import starvationevasion.common.messages.ClientChatMessage;
import starvationevasion.server.ServerState;
import starvationevasion.vis.controller.EarthViewer;

import javax.swing.Timer;
import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

import static starvationevasion.client.MegaMawile.model.NetworkStatus.*;

/**
 * Main Controller of the application. This is where most objects are initialized
 * and available for the the entire application.
 */
public class GameEngine implements GameController, Initializable, EventHandler<Event>
{
  //private final Player player;
  private AbstractPlayerController playerController;

  private final int DELAY = 0;
  private final int MINUTES_DRAFT_PHASE = 2;

  private NetworkHandler network;
  private MenuController menuController;
  private ButtonOverlayController buttonOverlayController;
  private VotePopupController voteController;
  private PolicyCardImageManager policyCardImageManager = new PolicyCardImageManager();

  // private final Renderer simulationVisualization;
  // private MenuRenderer menuRenderer;
  private GameOptions options;
  private GUIDraftDrawDiscard guiDraftDrawDiscard;
  private float tryTime = 0f;
  private float loginTryTime = 0f;
  private long lengthOfTurn = 0;
  private long curTimeLeft = 0;
  private Stage stage;

  TextFlow ksd;

  /**
   * FXML field injectors for the starvationevasion.client.Aegislash.GUI fx:id's
   */
  @FXML
  private javafx.scene.control.TextArea consoleTextArea;
  @FXML
  private javafx.scene.control.Label draftTimeRemaining;
  @FXML
  private TitledPane gameInstructions, selectedPaneStats;
  @FXML
  private ImageView citrus, fruit, nuts, grains, oil, vegetables, special, feed, fish, meat, poultry, dairy;
  @FXML
  private Group california, mountainStates, heartland, northernPlains, northernCrescent, southernPlains, southeast;

  @FXML
  private StackPane stackPaneOne, stackPaneTwo, stackPaneThree, stackPaneFour, stackPaneFive, stackPaneSix, stackPaneSeven;//panes that contain the hand
  @FXML
  private ImageView cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven;

  @FXML
  private ImageView actionTable1, actionTable2, actionTable3;

  @FXML
  private ImageView current;

  @FXML
  public ImageView voteCalif, voteSoutheast, voteCrescent, voteDelta, voteHeartland, voteNorthplain, voteMountain;

  @FXML
  private Button lockButton, okActionTable, cancelActionTable;
  @FXML
  private String choice;
  @FXML
  private Group mainFeed;
  @FXML
  private TextArea mainFeedDisplay;
  @FXML
  private LineChart<String, Number> usChart;
  @FXML
  private LineChart<String, Number> worldChart;
  @FXML
  private MenuButton usRegionSelect;
  @FXML
  private MenuButton usStatSelect;
  @FXML
  private MenuButton worldRegionSelect;
  @FXML
  private MenuButton worldStatSelect;
  @FXML
  private Label usTitle;
  @FXML
  private Label worldTitle;

  @FXML
  private MenuItem artA, midA, southA, eurp, midE, subS, russ, centA, souAs, eastA, seA, oceanA;
  @FXML
  private MenuItem uHdi, uRev, uPop, uMal, uCPro, uCIn, uCEx, uCAr;
  @FXML
  private MenuItem uFPro, uFIn, uFEx, uFAr, uNPro, uNIn, uNEx, uNAr, uGPro, uGIn, uGEx, uGAr, uOPro, uOIn, uOEx, uOAr;
  @FXML
  private MenuItem uVPro, uVIn, uVEx, uVAr, uSPro, uSIn, uSEx, uSAr, uFdPro, uFdIn, uFdEx, uFdAr, uFhPro, uFhIn, uFhEx, uFhAr;
  @FXML
  private MenuItem uMPro, uMIn, uMEx, uMAr, uPPro, uPIn, uPEx, uPAr, uDPro, uDIn, uDEx, uDAr;
  @FXML
  private MenuItem cali, pacNW, nPlains, sPlains, hland, nCresc, southe, wHdi, wRev, wPop, wMal, wCPro, wCIn, wCEx, wCAr;
  @FXML
  private MenuItem wFPro, wFIn, wFEx, wFAr, wNPro, wNIn, wNEx, wNAr, wGPro, wGIn, wGEx, wGAr, wOPro, wOIn, wOEx, wOAr;
  @FXML
  private MenuItem wVPro, wVIn, wVEx, wVAr, wSPro, wSIn, wSEx, wSAr, wFdPro, wFdIn, wFdEx, wFdAr, wFhPro, wFhIn, wFhEx, wFhAr;
  @FXML
  private MenuItem wMPro, wMIn, wMEx, wMAr, wPPro, wPIn, wPEx, wPAr, wDPro, wDIn, wDEx, wDAr;
  @FXML
  private Label yearPop, dTimeRemaining;

  private double originalX;
  private double originalY;

  private StatisticReadData.USREGION myRegion = StatisticReadData.USREGION.CALIFORNIA;//default for testing
  private StatisticReadData.USREGION usSelectedRegion;
  private StatisticReadData.WORLDREGION worldSelectedRegion;
  private String usStatString, worldStatString;

  private TitledPane popupWindow;
  private Pane sliderOverlay;
  private static Stage popupStage;
  private Statistics statistics;

  private Thread thread;
  private Timer timer;
  private PrintStream printStream;
  private Console console;
  private int clockMinutes = 0;
  private int clockSeconds = 0;
  private boolean initialize = true;
  private boolean fullEarthMode = false;
  private EarthViewer earthViewer;
  private Stage earthStage = new Stage();
  private GridPane masterPane;
  private Stage startStage;
  private double originalWidth;
  private double originalHeight;
  private double scale = 2;
  private BlendMode original;
  private LinkedList<String> mainFeedHistory = new LinkedList<>();
  //private PolicySelectionManager policyManager;

  private GameStateData gameState = new GameStateData();
  private boolean state;


  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    options = new GameOptions();
    guiDraftDrawDiscard = new GUIDraftDrawDiscard();
    statistics = new Statistics();

    network = new NetworkHandler(options, gameState);
    menuController = new MenuController(options, this);
    voteController = new VotePopupController(playerController, this);
    buttonOverlayController = new ButtonOverlayController(guiDraftDrawDiscard, this);//guiDDD is an error checker/book keeper

    initializeCards();
    setUpCharts();
    mainFeedDisplay.setOnMouseClicked(this);
    // menuRenderer = new MenuRenderer(options);

  }

  /**
   * Function which initializes the statistics based charts with the default
   * settings being the Human Development Index.
   */
  private void setUpCharts()
  {
    new StatisticReadData();
    new ChartGraphics();
    usSelectedRegion = myRegion;
    worldSelectedRegion = StatisticReadData.WORLDREGION.MIDDLE_AMERICA;

    usStatString = " Human Development Index";
    worldStatString = " Human Development Index";
    usTitle.textProperty().setValue(ChartGraphics.cg.regionTypeToString(usSelectedRegion) + usStatString);
    worldTitle.textProperty().setValue(ChartGraphics.cg.regionTypeToString(worldSelectedRegion) + worldStatString);

    setMenuItemHandlers();
    //makeStartStats();
    //for Testing
    //makeJunkStats();
    //
    try
    {
      usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
        StatisticReadData.STAT_TYPE.HDI));
    }
    catch (NullPointerException e)
    {
    }
    try
    {
      worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
        StatisticReadData.STAT_TYPE.HDI));
    }
    catch (NullPointerException e)
    {
    }
  }

  @Override
  public void update(float deltaTime)
  {
    //timer reset or decrement
    if (gameState.getPhaseTime() != lengthOfTurn)
    {
      lengthOfTurn = gameState.getPhaseTime();
      curTimeLeft = lengthOfTurn;
    }
    else if (gameState.getPhaseTime() > 0)
    {
      if (curTimeLeft > 0)
      {
        curTimeLeft -= deltaTime;
      }
      else if (curTimeLeft < 0)
      {
        curTimeLeft = 0;
      }
    }
    setTimerDisplay(deltaTime);
    // network try
    if (options.getNetworkStatus() == TRY)
    {
      options.setNetworkStatus(TRYING);
      network.createClient(playerController);
    }

    // player try
    if (getPlayer().getStatus() == TRY)
    {
      getPlayer().setStatus(TRYING);
    }


    // try and connect to server every 2s
    if (options.getNetworkStatus() == TRYING)
    {
      tryTime += 1;
    }

    // try and connect to server exeyer 2s
    if (getPlayer().getStatus() == TRYING)
    {
      loginTryTime += 1;
    }


    // if 2s has passes and server trying try to connect
    if (tryTime >= 200 && options.getNetworkStatus() == TRYING)
    {
      tryTime = 0;
      network.connectToServer();
    }

    // if 2s has passed and player is trying to login retry
    if (loginTryTime >= 200 && getPlayer().getStatus() == TRYING)
    {
      loginTryTime = 0f;
      network.loginToServer();
    }

    if (options.getNetworkStatus() == DISCONNECT)
    {
      network.disconnectFromServer();
    }

    playerController.update(deltaTime);
    menuController.update(deltaTime);
    voteController.update(deltaTime);
    buttonOverlayController.update(deltaTime);
    setTimerDisplay(deltaTime);

    if (gameState.getServerState() == ServerState.DRAFTING && options.getUpdateHand())
    {
      updateHand();
    }

    mainFeed();
  }

  /**
   * Separate function used to display the mainFeed to the starvationevasion.client.Aegislash.GUI.
   */
  private void mainFeed()
  {
    String mainFeedNextLine;
    mainFeedDisplay.setWrapText(true);
    if (options.getMainFeed().peek() != null)
    {
      mainFeedNextLine = options.getMainFeed().poll();
    }
    else
    {
      return;
    }
    if (mainFeedHistory.size() < 500) //Max size of history
    {
      mainFeedHistory.add(mainFeedNextLine);
    }
    else
    {
      mainFeedHistory.removeLast();
      mainFeedHistory.add(mainFeedNextLine);
    }
    mainFeedDisplay.appendText(mainFeedHistory.poll() + "\n");
  }

  /**
   * Gets and returns the history from mainFeed up to 50 lines.
   *
   * @return mainFeedHistory
   */
  public LinkedList<String> getMainFeedHistory()
  {
    return mainFeedHistory;
  }

  /**
   * Sets the ViewPort based upon the size of the given Rectangle.
   *
   * @param viewPort
   */
  public void setViewPort(Rectangle viewPort)
  {
    //menuRenderer.setViewPort(viewPort);
  }

  /**
   * Starts the server.
   */
  public void startServer()
  {
    options.setNetworkStatus(TRY);
  }

  /**
   * Used to pass a reference for the EarthViewer in Game.
   *
   * @param earthViewer
   */
  public void setEarthViewer(EarthViewer earthViewer)
  {
    this.earthViewer = earthViewer;
  }

  /**
   * All of the MouseEvent, Event, and ActionEvent listeners from the starvationevasion.client.Aegislash.GUI.fxml file.
   */
  @FXML
  protected void handleHand(MouseEvent event)
  {
    if (gameState.getServerState() == ServerState.DRAFTING)
    {
      ImageView imageView = (ImageView) event.getSource();
      StackPane stackPane = (StackPane) imageView.getParent();
      stackPane.getChildren().add(buttonOverlayController.getView());
      buttonOverlayController.setCurrent(guiDraftDrawDiscard.getImagePolicyMap().get(imageView));
    }

  }

  @FXML
  protected void handleOkActionTable(ActionEvent e)
  {
    ArrayList<ImageView> actionImageList = guiDraftDrawDiscard.getActionTableList();
    HashMap<ImageView, PolicyCard> imageViewPolicyCardHashMap = guiDraftDrawDiscard.getImagePolicyMap();
    PolicyCard policy = imageViewPolicyCardHashMap.get(guiDraftDrawDiscard.getActionTableList().get(0));
    switch (guiDraftDrawDiscard.getCurrentMove())
    {
      case DRAFT:
        if (playerController.draftPolicy(policy)) System.out.println("good draft");
        else System.out.println("bad draft");
        break;
      case DISCARD:
        if (playerController.discard(policy)) System.out.println("good discard");
        else System.out.println("bad discard");
        break;
      case DISCARDDRAW:
        PolicyCard[] policyCards = new PolicyCard[3];
        for (int i = 0; i < policyCards.length; i++)
        {
          if(i<actionImageList.size())
          {
            policyCards[i] = imageViewPolicyCardHashMap.get(actionImageList.get(i));
          }
          else policyCards[i] = null;
        }
        if (playerController.discardDraw(policyCards)) System.out.println("good discard/draw");
        else System.out.println("bad discard/draw");
        break;
    }

    guiDraftDrawDiscard.reset();
    //submit the action in the image list
  }

  @FXML
  protected void handleCancelActionTable(ActionEvent e)
  {
    guiDraftDrawDiscard.reset();
  }

  @FXML
  protected void item(javafx.event.Event event)
  {

  }

  /**
   * Key listener that uses the TAB key to display the large EarthViewer.
   *
   * @param event
   */
  @FXML
  @Override
  public void handle(Event event)
  {
    if (event instanceof MouseEvent)
    {
      if (((MouseEvent) event).getButton() == MouseButton.SECONDARY)
      {
        getNetworkHandler().getOurClient().send(new ClientChatMessage("Slow jack", EnumRegion.US_REGIONS));
        event.consume();
      }
      return;
    }

    switch (((KeyEvent) event).getCode())
    {
      //I have the Earth view toggling on TAB, but I'm not sure if this is a requirement
      //or if you can use a button or another key of your choice
      case TAB:
      {
        if (fullEarthMode)
        {
          fullEarthMode = false;
          earthStage.close();
        }

        else
        {
          fullEarthMode = true;
          Scene earthScene = new Scene(earthViewer.updateFull(), 800, 850);
          earthViewer.addVisStyleSheet(earthScene);
          earthScene.setOnKeyPressed(this);
          earthStage.setScene(earthScene);
          earthStage.show();
        }
      }
      case ENTER:
      {
        //World Data..
        /* client passes in crop and temperature data, expecting MapPoints to be whole degrees */
        HashMap<MapPoint, Float> tempData = new HashMap<>();
        for (double lat = -90; lat < 90; lat += 1)
        {
          for (double lon = -180; lon < 180; lon += 1)
          {
            MapPoint p = new MapPoint(lat, lon);
            /* temperature test data */
            tempData.put(p, Util.rand.nextFloat() * 100);
          }
        }
        //earthViewer.updateTemperature(tempData);

        HashMap<EnumRegion, int[]> foodData = new HashMap<>();
        for (EnumRegion e : EnumRegion.values())
        {
          int[] fData = new int[EnumFood.SIZE];
          for (int i = 0; i < EnumFood.SIZE; i++)
          {
            fData[i] = Util.rand.nextInt(100);
          }
          foodData.put(e, fData);
        }
        //earthViewer.updateFoodProduced(foodData);
      }
    }
  }

  @FXML
  protected void handleCalifornia(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.CALIFORNIA;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("California");
  }

  @FXML
  protected void californiaEntered(MouseEvent event)
  {
    original = california.getBlendMode();
    california.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void californiaExited(MouseEvent event)
  {
    california.setBlendMode(original);
  }

  @FXML
  protected void handlePacificNorthwestAndMountainStates(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.PACIFIC_NW_MS;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Pacific Northwest and Mountain States");
  }

  @FXML
  protected void mountainStatesEntered(MouseEvent event)
  {
    original = mountainStates.getBlendMode();
    mountainStates.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void mountainStatesExited(MouseEvent event)
  {
    mountainStates.setBlendMode(original);
  }

  @FXML
  protected void handleHeartland(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.HEARTLAND;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Heartland");
  }

  @FXML
  protected void heartlandEntered(MouseEvent event)
  {
    original = heartland.getBlendMode();
    heartland.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void heartlandExited(MouseEvent event)
  {
    heartland.setBlendMode(original);
  }

  @FXML
  protected void handleNorthernPlains(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.N_PLAINS;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Northern Plains");
  }

  @FXML
  protected void northernPlainsEntered(MouseEvent event)
  {
    original = northernPlains.getBlendMode();
    northernPlains.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void northernPlainsExited(MouseEvent event)
  {
    northernPlains.setBlendMode(original);
  }

  @FXML
  protected void handleSouthernPlainsAndDeltaStates(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.S_PLAINS_DS;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Southern Plains and Delta States");
  }

  @FXML
  protected void southernPlainsEntered(MouseEvent event)
  {
    original = southernPlains.getBlendMode();
    southernPlains.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void southernPlainsExited(MouseEvent event)
  {
    southernPlains.setBlendMode(original);
  }

  @FXML
  protected void handleSoutheast(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.SE;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Southeast");
  }

  @FXML
  protected void southeastEntered(MouseEvent event)
  {
    original = southeast.getBlendMode();
    southeast.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void southeastExited(MouseEvent event)
  {
    southeast.setBlendMode(original);
  }

  @FXML
  protected void handleNorthernCrescent(MouseEvent event)
  {
    usSelectedRegion = StatisticReadData.USREGION.N_CRESCENT;
    usStatString = " Human Development Index";
    updateChartTitles();
    usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
      StatisticReadData.STAT_TYPE.HDI));
    System.out.println("Northern Crescent");
  }

  @FXML
  protected void northernCrescentEntered(MouseEvent event)
  {
    original = northernCrescent.getBlendMode();
    northernCrescent.setBlendMode(BlendMode.BLUE);
  }

  @FXML
  protected void northernCrescentExited(MouseEvent event)
  {
    northernCrescent.setBlendMode(original);
  }

  @FXML
  protected void handleGameInstructionsEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/InstructionPane.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(gameInstructions.getLayoutX() + gameInstructions.getWidth());
      popupStage.setY(gameInstructions.getLayoutY() - gameInstructions.getHeight());
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleGameInstructionsExited(MouseEvent event)
  {
    try
    {
      popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleCitrusEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/CitrusPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(citrus.getLayoutX() + 100);
      popupStage.setY(citrus.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleCitrusExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleFruitEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/FruitPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(fruit.getLayoutX() + 100);
      popupStage.setY(fruit.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleFruitExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleNutsEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/NutsPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(nuts.getLayoutX() + 100);
      popupStage.setY(nuts.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleNutsExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleGrainsEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/GrainsPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(grains.getLayoutX() + 100);
      popupStage.setY(grains.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleGrainsExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleOilEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/OilPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(oil.getLayoutX() + 100);
      popupStage.setY(oil.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleOilExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleVegetablesEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/VegetablesPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(vegetables.getLayoutX() + 100);
      popupStage.setY(vegetables.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleVegetablesExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleSpecialEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/SpecialPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(special.getLayoutX() + 75);
      popupStage.setY(special.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleSpecialExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleFeedEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/FeedPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(feed.getLayoutX());
      popupStage.setY(feed.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleFeedExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleFishEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/FishPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(fish.getLayoutX());
      popupStage.setY(fish.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleFishExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleMeatEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/MeatPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(meat.getLayoutX() - 50);
      popupStage.setY(meat.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleMeatExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handlePoultryEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/PoultryPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(poultry.getLayoutX() - 125);
      popupStage.setY(poultry.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handlePoultryExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  @FXML
  protected void handleDairyEntered(MouseEvent event)
  {
    try
    {
      popupStage = new Stage();
      popupWindow = FXMLLoader.load(this.getClass().getResource("../view/popups/DairyPopup.fxml"));
      popupStage.setScene(new Scene(popupWindow));
      //popupStage.initStyle(StageStyle.UNDECORATED);
      popupStage.setX(dairy.getLayoutX() - 175);
      popupStage.setY(dairy.getLayoutY() + 260);
      popupStage.show();
    }
    catch (IOException er)
    {
    }
  }

  @FXML
  protected void handleDairyExited(MouseEvent event)
  {
    try
    {
      //popupStage.close();
    }
    catch (NullPointerException er)
    {
    }
  }

  /**
   * Initializes the starting screen with login.
   */
  public void showWelcome()
  {
    menuController.show();
  }

  /**
   * Re-instantiates a new HumanPlayerController with the current state.
   *
   * @param state
   */
  public void setState(boolean state)
  {
    this.state = state;
    if (!this.state)
    {
      playerController = new HumanPlayerController(gameState, options);
    }
  }

  /**
   * Sets options for the network.
   *
   * @param port
   * @param host
   */
  public void setOptions(String port, String host)
  {
    options.setHost(host);
    options.setPort(Integer.parseInt(String.valueOf(port)));
    options.setNetworkStatus(TRY);
  }

  /**
   * Updates the hand with the next cards drawn from the deck.
   */
  private void updateHand()
  {
    options.setUpdateHand(false);
    //IDK WHAT NOW BECAUSE ARRAYS FULL OF BULLSHIT

    ArrayList<PolicyCard> hand = getPlayer().getHandList();
    HashMap<ImageView, PolicyCard> imagePolicyMap = guiDraftDrawDiscard.getImagePolicyMap();
    ArrayList<ImageView> handImageArray = guiDraftDrawDiscard.getHandImageArray();
    for (int i = 0; i < handImageArray.size(); i++)
    {
      ImageView imageView = handImageArray.get(i);
      PolicyCard policyCard = hand.get(i);
      imagePolicyMap.put(imageView, policyCard);
      EnumPolicy enumPolicy = policyCard.getCardType();
      imageView.setImage(policyCardImageManager.getPolicyCardImage(enumPolicy));
    }
    guiDraftDrawDiscard.reset();
  }

  /**
   * Initializes the List containing the cards in the player's hand.
   */
  private void initializeCards()
  {
    HashMap<ImageView, PolicyCard> imagePolicyMap = guiDraftDrawDiscard.getImagePolicyMap();
    //initialize the list containing the cards in the player's hand
    ImageView[] hold = {cardOne,
      cardTwo,
      cardThree,
      cardFour,
      cardFive,
      cardSix,
      cardSeven};
    ArrayList<ImageView> handImageArray = guiDraftDrawDiscard.getHandImageArray();
    for (ImageView imageView : hold)
    {
      handImageArray.add(imageView);
      imageView.setImage(null);//maps the hand to policy card
      imagePolicyMap.put(imageView, null);
    }
    //initialize the list containing the cards on the Action table
    hold = new ImageView[]{actionTable1, actionTable2, actionTable3};
    ArrayList<ImageView> actionTableArray = guiDraftDrawDiscard.getActionTableList();
    for (ImageView imageView : hold)
    {
      actionTableArray.add(imageView);
      imageView.setImage(null);//maps the action table to policy card
      imagePolicyMap.put(imageView, null);
    }
  }

  /**
   * Called when the user hovers over the vote card.
   *
   * @param event the MouseEvent.
   */
  public void voteCardEnter(MouseEvent event)
  {
    ImageView source = (ImageView) event.getSource();
    source.setOpacity(0.5);
  }

  /**
   * Called when the user stops hovering over the vote card.
   *
   * @param event the MouseEvent.
   */
  public void voteCardExit(MouseEvent event)
  {
    ImageView source = (ImageView) event.getSource();
    source.setOpacity(1);
  }

  /**
   * Handles a mouse click event on a PolicyCard during the drafting phase.
   *
   * @param event the mouse click event to handle.
   */
  public void handleVoteCard(MouseEvent event)
  {
    ImageView source = (ImageView) event.getSource();
    voteController.show(source.getId());
  }

  private void setMenuItemHandlers()
  {

    cali.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.CALIFORNIA;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    pacNW.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.PACIFIC_NW_MS;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    nPlains.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.N_PLAINS;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    sPlains.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.S_PLAINS_DS;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    hland.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.HEARTLAND;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    nCresc.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.N_CRESCENT;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    southe.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usSelectedRegion = StatisticReadData.USREGION.SE;
        usStatString = " Human Development Index";
        updateChartTitles();
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    artA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.ARTIC_AMERICA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    midA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.MIDDLE_AMERICA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    southA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.SOUTH_AMERICA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    eurp.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.EUROPE;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    midE.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.MIDDLE_EAST;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    subS.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.SUB_SAHARAN_AFRICA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    russ.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.RUSSIA_CAUCUSES;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    centA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.CENTRAL_ASIA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    souAs.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.SOUTH_ASIA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    eastA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.EAST_ASIA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    seA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.SOUTHEAST_ASIA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    oceanA.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldSelectedRegion = StatisticReadData.WORLDREGION.OCEANIA;
        worldStatString = " Human Development Index";
        updateChartTitles();
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
      }
    });
    uHdi.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Human Development Index";
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
        updateChartTitles();
      }
    });
    uRev.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Revenue";
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.REVENUE));
        updateChartTitles();
      }
    });
    uPop.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Population";
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.POPULATION));
        updateChartTitles();
      }
    });
    uMal.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Malnourished";
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usSelectedRegion,
          StatisticReadData.STAT_TYPE.MALNOURISHED));
        updateChartTitles();
      }
    });
    uCPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Citrus Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    uCIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Citrus Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    uCEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Citrus Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    uCAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Citrus Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    uFPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fruit Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    uFIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fruit Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    uFEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fruit Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    uFAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fruit Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    uNPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Nut Crop Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    uNIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Nut Crop Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    uNEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Nut Crop Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    uNAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Nut Crop Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    uGPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Grain Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    uGIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Grain Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    uGEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Grain Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    uGAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Grain Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    uOPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Oil Crop Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    uOIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Oil Crop Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    uOEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Oil Crop Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    uOAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Oil Crop Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    uVPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Vegetable Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    uVIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Vegetable Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    uVEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Vegetable Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    uVAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Vegetable Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    uSPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Specialty Crop Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    uSIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Specialty Crop Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    uSEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Specialty Crop Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    uSAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Specialty Crop Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    uFdPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Feed Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    uFdIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Feed Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    uFdEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Feed Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    uFdAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Feed Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    uFhPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fish Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    uFhIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fish Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    uFhEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fish Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    uFhAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Fish Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    uMPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Meat Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    uMIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Meat Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    uMEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Meat Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    uMAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Meat Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    uPPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Poultry Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    uPIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Poultry Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    uPEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Poultry Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    uPAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Poultry Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    uDPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Dairy Production";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    uDIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Dairy Income";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    uDEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Dairy Exports";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    uDAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        usStatString = " Dairy Area";
        usChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(usSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    wHdi.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Human Development Index";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.HDI));
        updateChartTitles();
      }
    });
    wRev.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Revenue";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.REVENUE));
        updateChartTitles();
      }
    });
    wPop.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Population";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.POPULATION));
        updateChartTitles();
      }
    });
    wMal.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Malnourished";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.MALNOURISHED));
        updateChartTitles();
      }
    });
    wCPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Citrus Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    wCIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Citrus Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    wCEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Citrus Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    wCAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Citrus Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.CITRUS));
        updateChartTitles();
      }
    });
    wFPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fruit Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    wFIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fruit Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    wFEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fruit Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    wFAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fruit Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.NON_CITRUS));
        updateChartTitles();
      }
    });
    wNPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Nut Crop Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    wNIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Nut Crop Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    wNEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Nut Crop Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    wNAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Nut Crop Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.NUTS));
        updateChartTitles();
      }
    });
    wGPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Grain Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    wGIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Grain Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    wGEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Grain Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    wGAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Grain Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.GRAINS));
        updateChartTitles();
      }
    });
    wOPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Oil Crop Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    wOIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Oil Crop Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    wOEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Oil Crop Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    wOAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Oil Crop Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.OIL_CROPS));
        updateChartTitles();
      }
    });
    wVPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Vegetable Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    wVIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Vegetable Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    wVEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Vegetable Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    wVAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Vegetable Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.VEGETABLES));
        updateChartTitles();
      }
    });
    wSPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Special Crops Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    wSIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Special Crops Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    wSEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Special Crop Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    wSAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Special Crop Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        updateChartTitles();
      }
    });
    wFdPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Feed Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    wFdIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Feed Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    wFdEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Feed Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    wFdAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Feed Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.FEED_CROPS));
        updateChartTitles();
      }
    });
    wFhPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fish Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    wFhIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fish Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    wFhEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fish Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    wFhAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Fish Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.FISH));
        updateChartTitles();
      }
    });
    wMPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Meat Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    wMIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Meat Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    wMEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Meat Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    wMAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Meat Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        updateChartTitles();
      }
    });
    wPPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Poultry Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    wPIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Poultry Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    wPEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Poultry Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    wPAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Poultry Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        updateChartTitles();
      }
    });
    wDPro.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Dairy Production";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    wDIn.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Dairy Income";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    wDEx.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Dairy Exports";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
    wDAr.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        worldStatString = " Dairy Area";
        worldChart.getData().setAll(ChartGraphics.cg.getRegionCropChart(worldSelectedRegion,
          StatisticReadData.STAT_TYPE.TOTAL_KM, StatisticReadData.FARMPRODUCT.DAIRY));
        updateChartTitles();
      }
    });
  }

  /**
   * Updates the titles for the displayed statistical charts.
   */
  private void updateChartTitles()
  {
    usTitle.textProperty().setValue(ChartGraphics.cg.regionTypeToString(usSelectedRegion) + usStatString);
    worldTitle.textProperty().setValue(ChartGraphics.cg.regionTypeToString(worldSelectedRegion) + worldStatString);
  }

  private void makeStartStats()
  {
    int year = 1981;
    int stat = 0;
    Random rand = new Random();
    for (StatisticReadData.FARMPRODUCT farmproduct : StatisticReadData.FARMPRODUCT.values())
    {
      year = 1981;
      StatisticReadData.srd.populateStats(year, farmproduct, stat);
    }
    for (StatisticReadData.USREGION region : StatisticReadData.USREGION.values())
    {
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.POPULATION, year, stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.HDI, year, (double) stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.REVENUE, year, stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.MALNOURISHED, year, (double) stat);
      for (StatisticReadData.FARMPRODUCT product : StatisticReadData.FARMPRODUCT.values())
      {
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_KM, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, year, stat);
      }
    }
    for (StatisticReadData.WORLDREGION region : StatisticReadData.WORLDREGION.values())
    {
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.POPULATION, year, stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.HDI, year, (double) stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.REVENUE, year, stat);
      StatisticReadData.srd.populateStats(region,
        StatisticReadData.STAT_TYPE.MALNOURISHED, year, (double) stat);
      for (StatisticReadData.FARMPRODUCT product : StatisticReadData.FARMPRODUCT.values())
      {
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_EXPORT, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.TOTAL_KM, year, stat);
        StatisticReadData.srd.populateStats(region, product,
          StatisticReadData.STAT_TYPE.NET_FARM_INCOME, year, stat);
      }
    }
  }

  /**
   * Makes junk statistics for the charts when real data is not available.
   */
  private void makeJunkStats()
  {
    int year = 1981;
    int stat;
    Random rand = new Random();
    for (StatisticReadData.FARMPRODUCT farmproduct : StatisticReadData.FARMPRODUCT.values())
    {
      year = 1981;
      while (year <= 2050)
      {
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(year, farmproduct, stat);
        year += 1;
      }
    }
    for (StatisticReadData.USREGION region : StatisticReadData.USREGION.values())
    {
      year = 1981;
      while (year <= 2050)
      {
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.POPULATION, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.HDI, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.REVENUE, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.MALNOURISHED, year, stat);
        for (StatisticReadData.FARMPRODUCT product : StatisticReadData.FARMPRODUCT.values())
        {
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_EXPORT, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_KM, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.NET_FARM_INCOME, year, stat);
        }
        year += 3;
      }
    }
    for (StatisticReadData.WORLDREGION region : StatisticReadData.WORLDREGION.values())
    {
      year = 1981;
      while (year <= 2050)
      {
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.POPULATION, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.HDI, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.REVENUE, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
          StatisticReadData.STAT_TYPE.MALNOURISHED, year, stat);
        for (StatisticReadData.FARMPRODUCT product : StatisticReadData.FARMPRODUCT.values())
        {
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_EXPORT, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.TOTAL_KM, year, stat);
          stat = rand.nextInt(100);
          StatisticReadData.srd.populateStats(region, product,
            StatisticReadData.STAT_TYPE.NET_FARM_INCOME, year, stat);
        }
        year += 3;
      }
    }
  }
  /**
   * Keeps track of and displays the time remaining for the Drafting Phase.
   */
  public void setTimerDisplay(float deltaTime)
  {
    String draftTimeLeft = "";
    gameState.subtractTime(deltaTime);
    if (gameState.getPhaseTime() > 0)
    {
      long tempTime = curTimeLeft / 1000;
      int minutes = (int) (tempTime / 60);
      int seconds = (int) (tempTime % 60);
      if (seconds > 9)
      {
        draftTimeLeft = "Drafting Phase Time Remaining : " + minutes + ":" + seconds;
      }
      else
      {
        draftTimeLeft = "Drafting Phase Time Remaining : " + minutes + ":0" + seconds;
      }
    }
    else
    {
      draftTimeLeft = "Drafting Phase Time Remaining : -:--";
    }
    //set Label text to string
    dTimeRemaining.textProperty().setValue(draftTimeLeft);
  }

  /**
   * getter that returns the Network Handler.
   */
  public NetworkHandler getNetworkHandler()
  {
    return network;
  }

  /**
   * Returns the current player.
   */
  public Player getPlayer()
  {
    return playerController.getPlayer();
  }

  /**
   * Returns the current game state.
   */
  public GameStateData getGameState()
  {
    return gameState;
  }

}
