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
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.DraftTimer;
import starvationevasion.client.GUI.DraftLayout.TickerReel;
import starvationevasion.client.GUI.DraftLayout.WorldMap;
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

  private ChatNode chatNode;
  private boolean receivedCards = false;
  private ImageView imageView;

  private WorldMap worldMap;

  private Image oldMap;
  private Image borderMap;
  private boolean borders = false;
  private Rectangle2D viewPort;
  private VotingHand hand;
  private DraftTimer votingTimer;
  private ArrayList<VotingNode> votingNodes = new ArrayList<>();

  private starvationevasion.client.GUI.GUI gui;

  public VotingLayout(starvationevasion.client.GUI.GUI gui2)
  {
    super();
    this.gui = gui2;
    this.setSize(width, height);
    this.width = gui2.getPrimaryStage().getWidth();
    this.height = gui2.getPrimaryStage().getHeight();

    /*
     * Hand is a nodeTemplate and is the container that holds the cards and the
     * other voting buttons and elements
     */
    hand = new VotingHand(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    hand.setManaged(false);
    hand.setVisible(true);
    hand.setPickOnBounds(false);

    chatNode = new ChatNode(gui);
    chatNode.setVotingMode(true);

    viewPort = new Rectangle2D(0, 0, width, height);

    /*
     * worldMap loads the map and the region border polygons
     */

    /**
     * votingTimer counts down from 2 minutes and sets votingFinished when time
     * is out.
     */
    votingTimer = new DraftTimer();
    votingTimer.draftTimer.setTime(120000);
    votingTimer.setMinWidth(gui.getPrimaryStage().getWidth() / 10);
    votingTimer.setMinHeight(gui.getPrimaryStage().getHeight() / 9);

    this.getChildren().add(hand);
    this.getChildren().add(votingTimer);
    this.getChildren().add(chatNode);

    setHandlers();
  }

  public void update()
  {
    worldMap.getBoardersManager().update();
  }
  
  /**
   * 
   * @return Reference to the world map.
   */
  public WorldMap getWorldMap()
  {
    return worldMap;
  }

  private void setHandlers()
  {
    this.setOnMouseMoved(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        lastX = e.getX();
        lastY = e.getY();
      }
    });

    this.setOnMouseDragged(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        double xDiff = e.getX() - lastX;
        double yDiff = e.getY() - lastY;

        double x = viewPort.getMinX() - xDiff;
        double y = viewPort.getMinY() - yDiff;
        double width = viewPort.getWidth();
        double height = viewPort.getHeight();

        viewPort = new Rectangle2D(x, y, width, height);
        checkViewBounds();

        lastX = e.getX();
        lastY = e.getY();
      }
    });

    this.setOnScroll(new EventHandler<ScrollEvent>()
    {
      @Override
      public void handle(ScrollEvent scroll)
      {

        double change = scroll.getDeltaY() * SCROLL_MOD * -1;

        double heightDim = height / width;

        double x = viewPort.getMinX() - change;
        double y = viewPort.getMinY() - change * heightDim;
        double vWidth = viewPort.getWidth() + change * 2;
        double vHeight = viewPort.getHeight() + change * 2 * heightDim;

        if (vWidth <= 0) vWidth = 3;
        if (vHeight <= 0) vHeight = 3;

        viewPort = new Rectangle2D(x, y, vWidth, vHeight);
        checkViewBounds();
      }
    });

    this.setOnMousePressed(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        setCursor(Cursor.MOVE);
      }
    });

    this.setOnMouseReleased(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        setCursor(Cursor.DEFAULT);
      }
    });
  }

  @Override
  public void onResize()
  {
    this.height = this.gui.getPrimaryStage().getHeight();
    this.width = this.gui.getPrimaryStage().getWidth();

    double x = viewPort.getMinX();
    double y = viewPort.getMinY();

    viewPort = new Rectangle2D(x, y, this.getWidth(), this.getHeight());
    checkViewBounds();

    width = this.getWidth();
    height = this.getHeight();

    hand.setSize(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    
    if(worldMap!=null)
    {
    if (worldMap != null)
    {
      worldMap.setMaxHeight(this.getHeight());
      worldMap.setMaxWidth(this.getWidth());
    }
    }

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
  }

  private void checkViewBounds()
  {
    // TODO Auto-generated method stub
  }

  public void setCards(GameCard[] cards)
  {
    // TODO Auto-generated method stub
  }

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
          // if (cardSpacesSecondRow.get(index).getCard() == null)
          // {
          // cardSpacesSecondRow.get(index).setCard(card.getOwner(),
          // card.getCardType(), gui);
          // }
          // else cardSpacesFirstRow.get(index).setCard(card.getOwner(),
          // card.getCardType(), gui);
        }
        else
        {
          Button[] buttons = votingNodes.get(index).addVotingButtons();
          buttons[0].setOnAction(event ->
          {
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });
          buttons[1].setOnAction(event ->
          {
            buttons[0].setDisable(true);
            buttons[1].setDisable(true);
          });
        }
      }
    }
  }

  public void setChatNode(ChatNode chatNode2)
  {
    chatNode = chatNode2;
    onResize();

  }

  public void setWorldMap(WorldMap map)
  {
    worldMap = map;
    worldMap.setMaxHeight(gui.getPrimaryStage().getHeight());
    worldMap.setMaxWidth(gui.getPrimaryStage().getWidth());
    worldMap.setOnMouseMoved(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        hand.mouseMovedEvent();
      }
    });
    worldMap.setOnMouseClicked(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        hand.mouseClickedEvent();

      }
    });
    
    this.getChildren().add(worldMap);
    worldMap.toBack();
  }

}
