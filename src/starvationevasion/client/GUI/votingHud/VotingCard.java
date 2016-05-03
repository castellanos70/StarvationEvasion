package starvationevasion.client.GUI.votingHud;

import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import starvationevasion.client.GUI.ResizablePane;

public class VotingCard extends ResizablePane
{
  private static final double LINE_PERC = .02;

  private ImageView display;
  private Image cardImage;
  private static Random random = new Random();
  private Line[] borders = new Line[4];
  private boolean isClicked = false;

  public VotingCard()
  {
    this(random.nextInt(7));
  }

  public VotingCard(int i)
  {
	  super(null, null);
    cardImage = new Image(
        getClass().getResource("/starvationevasion/GuiTestCode/resources/card" + i + ".png").toString());
    display = new ImageView(cardImage);
    for (int j = 0; j < 4; j++)
    {
      Line line = new Line();
      line.setVisible(false);
      line.setStroke(Color.YELLOW);
      borders[j] = line;

    }
    this.getChildren().add(display);
    this.getChildren().addAll(borders);

    this.setCursor(Cursor.HAND);
    this.setOnMouseEntered(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent arg0)
      {
        setFocused(true);
        for (Line l : borders)
        {
          l.setVisible(true);
        }
      }
    });

    this.setOnMouseExited(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent arg0)
      {
        setFocused(false);
        for (Line l : borders)
        {
          l.setVisible(false);
        }
      }
    });
  }

  @Override
  public void onResize()
  {
    double cardWidth = cardImage.getWidth();
    double cardHeight = cardImage.getHeight();

    double scaleX = this.getWidth() / cardWidth;
    double scaleY = this.getHeight() / cardHeight;

    double translateX = (cardWidth * scaleX - cardWidth) / 2;
    double translateY = (cardHeight * scaleY - cardHeight) / 2;

    display.setScaleX(scaleX);
    display.setScaleY(scaleY);
    display.setTranslateX(translateX);
    display.setTranslateY(translateY);

    adjustBorders();
  }

  private void adjustBorders()
  {
    for (Line l : borders)
    {
      l.setStrokeWidth(this.getWidth() * LINE_PERC);
    }

    double width = this.getWidth();
    double height = this.getHeight();

    borders[0].setStartX(0);
    borders[0].setStartY(0);
    borders[0].setEndX(0);
    borders[0].setEndY(height);

    borders[1].setStartX(0);
    borders[1].setStartY(0);
    borders[1].setEndX(width);
    borders[1].setEndY(0);

    borders[2].setStartX(width);
    borders[2].setStartY(0);
    borders[2].setEndX(width);
    borders[2].setEndY(height);

    borders[3].setStartX(0);
    borders[3].setStartY(height);
    borders[3].setEndX(width);
    borders[3].setEndY(height);
  }

  public double getSizeRatio()
  {
    return cardImage.getWidth() / cardImage.getHeight();
  }
  public Image getCardImage()
  {
    return cardImage;
  }
  public void setCardImage(Image image)
  {
    display.setImage(image);
    onResize();
  }
  public void setClicked(boolean clicked)
  {
    isClicked = clicked;
  }
  public boolean isClicked()
  {
    return isClicked;
  }
}
