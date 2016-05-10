package starvationevasion.client.GUI.votingHud;

import java.io.File;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.DraftLayout.CardNode;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.DraftTimer;
import starvationevasion.client.GUI.DraftLayout.GraphNode;
import starvationevasion.client.GUI.DraftLayout.TickerReel;
import starvationevasion.client.GUI.DraftLayout.WorldMap;
import starvationevasion.client.GUI.Popups.GraphDisplay;
import starvationevasion.client.GUI.VotingLayout.VotingNode;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.GameCard;

/**
 * 
 * @author Brian Downing
 *
 *         This is the container for all of the GUI elements of the VOting Phase
 *         for the Game NodeTemplate is a Pane that has some added resizing
 *         functionality.
 */
public class VotingLayout extends NodeTemplate
{
  private static final int SCROLL_MOD = 4;
  private double lastX;
  private double lastY;
  private double width;
  private double height;

  private boolean receivedCards = false;

  private VBox tickerReel;
  private TickerReel reel;

  private ChatNode chatNode;

  private ImageView imageView;

  // private WorldMap worldMap;

  private Image oldMap;
  private Image borderMap;
  private boolean borders = false;
  private Rectangle2D viewPort;
  private VotingHand hand;
  private DraftTimer votingTimer;
  private ArrayList<VotingNode> votingNodes = new ArrayList<>();
  private ImageView view;

  private GUI gui;

  public VotingLayout(GUI gui2)
  {
    super();

    // File f = new
    // File("StarvationEvasion/src/starvationevasion/client/GUI/votingHud/testImages/WorldMap_MollweideProjection.png");
    // Image i = new Image(f.toURI().toString());
    // ImageView iv = new ImageView(i);
    // iv.fitHeightProperty().bind(this.widthProperty());
    // iv.fitHeightProperty().bind(this.heightProperty());
    // this.getChildren().add(iv);

    this.gui = gui2;
    this.setSize(width, height);
    this.width = gui2.getPrimaryStage().getWidth();
    this.height = gui2.getPrimaryStage().getHeight();

    // File f = new
    // File("StarvationEvasion/src/starvationevasion/client/GUI/votingHud/testImages/WorldMap_MollweideProjection.png");
    // ImageView iv = new ImageView(new Image(f.toURI().toString()));

    /*
     * Hand is a nodeTemplate and is the container that holds the cards and the
     * other voting buttons and elements
     */
    hand = new VotingHand(gui2, width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    hand.setManaged(false);
    hand.setVisible(true);
    hand.setPickOnBounds(false);

    view = new ImageView(gui.getImageGetter().getWorldMap());
    view.setOnMouseMoved(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        hand.mouseMovedEvent(event);

      }
    });
    view.setOnMouseClicked(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        hand.mouseClickedEvent(event);

      }
    });

    this.getChildren().add(view);

    chatNode = new ChatNode(gui);
    chatNode.setVotingMode(true);

    viewPort = new Rectangle2D(0, 0, width, height);

    tickerReel = new VBox();
    reel = new TickerReel(tickerReel);
    tickerReel.getChildren().add(reel);
    reel.setMinWidth(this.width);
    reel.setMinHeight(this.height / 16);

    /**
     * votingTimer counts down from 2 minutes and sets votingFinished when time
     * is out.
     */
    votingTimer = new DraftTimer();
    votingTimer.draftTimer.setTime(120000);
    votingTimer.setMinWidth(gui.getPrimaryStage().getWidth() / 10);
    votingTimer.setMinHeight(gui.getPrimaryStage().getHeight() / 9);

    this.getChildren().add(tickerReel);
    this.getChildren().add(hand);
    this.getChildren().add(votingTimer);
    this.getChildren().add(chatNode);

    onResize();
  }

  public void update()
  {
    // worldMap.getBoardersManager().update();
  }

  @Override
  public void onResize()
  {
    this.height = this.gui.getPrimaryStage().getHeight();
    this.width = this.gui.getPrimaryStage().getWidth();

    double x = viewPort.getMinX();
    double y = viewPort.getMinY();

    viewPort = new Rectangle2D(x, y, this.getWidth(), this.getHeight());

    width = this.getWidth();
    height = this.getHeight();

    hand.setSize(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);

    view.fitWidthProperty().bind(this.widthProperty());
    view.fitHeightProperty().bind(this.heightProperty());

    // if (worldMap != null)
    // {
    // if (worldMap != null)
    // {
    // worldMap.setMaxHeight(this.getHeight());
    // worldMap.setMaxWidth(this.getWidth());
    // }
    // }

    votingTimer.setMinWidth(gui.getPrimaryStage().getWidth() / 10);
    votingTimer.setTranslateX(gui.getPrimaryStage().getWidth() - 1.5 * votingTimer.getMinWidth());
    votingTimer.setTranslateY(votingTimer.getMinHeight() / 2);

    chatNode.setMinWidth(gui.getPrimaryStage().getWidth() / 5.5);
    chatNode.setMinHeight(gui.getPrimaryStage().getHeight() / 3);
    chatNode.setTranslateX(gui.getPrimaryStage().getWidth() - chatNode.getMinWidth() * 1.5);
    chatNode.setTranslateY(gui.getPrimaryStage().getHeight() - chatNode.getMinHeight() * 1.2);

    hand.onResize();
  }

  private int getIndexOfRegion(EnumRegion region)
  {
    for (int i = 0; i < EnumRegion.US_REGIONS.length; i++)
    {
      if (region.equals(EnumRegion.US_REGIONS[i])) return i;
    }
    return -1;
  }

  public boolean hasReceivedCards()
  {
    return receivedCards;
  }

  public ChatNode getChatNode()
  {
    return chatNode;
  }

  public void resetVotingLayout()
  {
    receivedCards = false;
    hand.clearCards();
  }

  public void setChatNode(ChatNode chatNode2)
  {
    chatNode = chatNode2;
    onResize();

  }

  public VotingHand getVotingHand()
  {
    return hand;
  }

  // public void setWorldMap(WorldMap map)
  // {
  // worldMap = map;
  // worldMap.setMaxHeight(gui.getPrimaryStage().getHeight());
  // worldMap.setMaxWidth(gui.getPrimaryStage().getWidth());
  // worldMap.setOnMouseMoved(new EventHandler<Event>()
  // {
  //
  // @Override
  // public void handle(Event event)
  // {
  // hand.mouseMovedEvent(event);
  // }
  // });
  // worldMap.setOnMouseClicked(new EventHandler<Event>()
  // {
  //
  // @Override
  // public void handle(Event event)
  // {
  // hand.mouseClickedEvent(event);
  //
  // }
  // });
  //
  // this.getChildren().add(worldMap);
  // worldMap.toBack();
  // }
  //
  // }
}