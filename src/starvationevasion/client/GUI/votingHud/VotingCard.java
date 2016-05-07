package starvationevasion.client.GUI.votingHud;

import java.io.File;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class VotingCard extends NodeTemplate
{

  private ImageView display;
  private Image cardImage;
  private static Random random = new Random();
  private Line[] borders = new Line[4];
  private boolean isClicked = false;
  private int votes;
  private int votesNeeded;
  private int status;
  private boolean yea = false;
  private boolean abstain = false;
  private boolean nay = false;
  private boolean voteSet = false;

  public VotingCard()
  {
    this(random.nextInt(7));
  }

  public VotingCard(int i)
  {

    super();

    votes = 0;
    votesNeeded = i;

    File file = new File("src/starvationevasion/client/GUI/votingHud/testImages/" + i + ".png");
    cardImage = new Image(file.toURI().toString());
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
        setBorders(true);
      }
    });

    this.setOnMouseExited(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent arg0)
      {
        setFocused(false);
        setBorders(false);
      }
    });
  }

  public void setBorders(Boolean show)
  {
    if (show)
    {
      for (Line l : borders)
      {
        if (voteSet)
        {
          if (getVotes() >= getVotesNeeded())
          {
            l.setStroke(Color.GOLD);
            l.setStrokeWidth(7);
          }
          else if (getStatus() == 0)
          {
            l.setStroke(Color.LIGHTGREEN);
            l.setStrokeWidth(7);
          }
          else if (getStatus() == 1)
          {
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(7);
          }
          else if (getStatus() == 2)
          {
            l.setStroke(Color.RED);
            l.setStrokeWidth(7);
          }
        }
        else
        {
          l.setStrokeWidth(2);
          l.setStroke(Color.YELLOW);
        }
        l.setVisible(true);
      }
    }

    else if (!voteSet)
    {
      for (Line l : borders)

      {
        l.setVisible(false);
      }
    }
  }

  public void setHasBeenVoted(boolean set)
  {
    voteSet = set;
    setBorders(set);
  }
  public boolean getHasBeenVoted()
  {
    return voteSet;
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
    // for (Line l : borders)
    // {
    // l.setStrokeWidth(this.getWidth() * LINE_PERC);
    // }

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

    setBorders(false);
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

  public int getVotes()
  {
    return votes;
  }

  public int getVotesNeeded()
  {
    return votesNeeded;
  }

  public void setVotes(int i)
  {

    if (!nay && i == -1)
    {
      votes += i;
      setNay();
    }
    else if (!yea && i == 1)
    {
      votes += i;
      setYea();
    }
    else if (!abstain && i == 10)
    {
      setAbstain();
    }
    if (votes < 0) votes = 0;
  }

  public void setYea()
  {
    yea = true;
    abstain = false;
    nay = false;
    status = 0;
  }

  public void setAbstain()
  {
    yea = false;
    abstain = true;
    nay = false;
    status = 1;
  }

  public void setNay()
  {
    yea = false;
    abstain = false;
    nay = true;
    status = 2;
  }

  public boolean getYea()
  {
    return yea;
  }

  public boolean getAbstain()
  {
    return abstain;
  }

  public boolean getNay()
  {
    return nay;
  }

  public int getStatus()
  {
    return status;
  }

  public void resetCard()
  {
    this.abstain = false;
    this.yea = false;
    this.nay = false;
    this.votes = 0;
    this.voteSet = false;
    this.setClicked(false);
    this.setFocused(false);
    onResize();
  }

  public void setTargetCard(VotingCard card)
  {
    setCardImage(card.getCardImage());
    votes = card.getVotes();
    setVotesNeeded(card.getVotesNeeded());
    setHasBeenVoted(card.getHasBeenVoted());
    setBorders(true);
  }

  public void setVotesNeeded(int need)
  {
    votesNeeded = need;
  }
}
