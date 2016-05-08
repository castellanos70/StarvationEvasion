package starvationevasion.client.GUI.votingHud;

import java.io.File;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import starvationevasion.client.GUI.DraftLayout.ChatNode;
import starvationevasion.client.GUI.DraftLayout.WorldMap;
import starvationevasion.client.GUI.DraftLayout.map.Map;
import starvationevasion.client.GUI.VotingLayout.VotingTimer;
import starvationevasion.common.gamecards.GameCard;

public class VotingLayout extends NodeTemplate
{
  private static final int SCROLL_MOD = 4;

  private ChatNode chatNode;
  private boolean receivedCards = false;
  private ImageView imageView;

  private WorldMap worldMap;

  private Image oldMap;
  private Image borderMap;
  private boolean borders = false;
  private Rectangle2D viewPort;
  private VotingHand hand;

  private double lastX;
  private double lastY;

  private double width;
  private double height;
  private starvationevasion.client.GUI.GUI gui;

  public VotingLayout(starvationevasion.client.GUI.GUI gui2)
  {
    super();
    this.gui = gui2;
    this.setSize(width, height);
    this.width = gui2.getPrimaryStage().getWidth();
    this.height = gui2.getPrimaryStage().getHeight();

    hand = new VotingHand(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    hand.setManaged(false);
    hand.setVisible(true);
    hand.setPickOnBounds(false);

    File file = new File("src/starvationevasion/client/GUI/votingHud/testImages/WorldMap_MollweideProjection.png");
    oldMap = new Image(file.toURI().toString());
    imageView = new ImageView(oldMap);
    imageView.setManaged(false);
    imageView.setPreserveRatio(false);
    viewPort = new Rectangle2D(0, 0, width, height);
    imageView.setViewport(viewPort);
//    this.getChildren().add(imageView);

    worldMap = new WorldMap(gui);
    worldMap.setMaxHeight(gui.getPrimaryStage().getHeight());
    worldMap.setMaxWidth(gui.getPrimaryStage().getWidth());
    
    this.getChildren().add(worldMap);


    this.getChildren().add(hand);
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
    // this.height = this.gui.getPrimaryStage().getHeight();
    // this.width = this.gui.getPrimaryStage().getWidth();
    if (imageView == null) return;

    imageView.setFitHeight(this.getHeight());
    imageView.setFitWidth(this.getWidth());

    double x = viewPort.getMinX();
    double y = viewPort.getMinY();

    viewPort = new Rectangle2D(x, y, this.getWidth(), this.getHeight());
    checkViewBounds();

    width = this.getWidth();
    height = this.getHeight();

    hand.setSize(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    
    worldMap.setMaxHeight(this.getHeight());
    worldMap.setMaxWidth(this.getWidth());

  }

  private void checkViewBounds()
  {

    imageView.setViewport(viewPort);
  }

  public void toggleBorders()
  {
    if (borders)
    {
      imageView.setImage(oldMap);
    }
    else
    {
      imageView.setImage(borderMap);
    }
    borders ^= true;
  }

  public boolean hasReceivedCards()
  {
    return receivedCards;
  }

  public void setCards(GameCard[] cards)
  {

  }

  public ChatNode getChatNode()
  {
    return chatNode;
  }

  public void updateCardSpaces(ArrayList<GameCard> votingCards)
  {
    // TODO Auto-generated method stub
    
  }

}
