package starvationevasion.client.GUI.votingHud;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import starvationevasion.client.GUI.ResizablePane;

public class VotingNode extends ResizablePane
{
  private static final int SCROLL_MOD = 4;

  private ImageView imageView;
  private Image map;
  private Image borderMap;
  private boolean borders = false;
  private Rectangle2D viewPort;
  private VotingHand hand;


  private double lastX;
  private double lastY;

  private double width;
  private double height;

  public VotingNode(double width, double height)
  {
	  super(null, null);
    this.setSize(width, height);
    this.width = width;
    this.height = height;
    
    

    hand = new VotingHand(width * (2 / 3d), height / 2);
    hand.setLayoutX(20);
    hand.setLayoutY(height / 2 - 20);
    hand.setManaged(false);
    hand.setVisible(true);

    map = new Image(getClass().getResource("/starvationevasion/GuiTestCode/resources/map.png").toString());
    borderMap = new Image(getClass().getResource("/starvationevasion/GuiTestCode/resources/map2.png").toString());

    imageView = new ImageView(map);
    imageView.setManaged(false);
    imageView.setPreserveRatio(false);

    viewPort = new Rectangle2D(0, 0, width, height);
    imageView.setViewport(viewPort);

    this.getChildren().add(imageView);
    this.getChildren().add(hand);

    setHandlers();
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
  }


  private void checkViewBounds()
  {

    imageView.setViewport(viewPort);
  }

  public void toggleBorders()
  {
    if (borders)
    {
      imageView.setImage(map);
    }
    else
    {
      imageView.setImage(borderMap);
    }
    borders ^= true;
  }
}
