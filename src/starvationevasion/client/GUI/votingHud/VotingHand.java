package starvationevasion.client.GUI.votingHud;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VotingHand extends NodeTemplate
{
  private static final double MAX_SMALL_SIZE = .5;

  private int cardCount = 11;

  private double[] xLayouts = new double[cardCount];
  private double minWidth = 100;

  private boolean cardHasFocus = false;
  private boolean cardIsClicked = false;

  private VotingCard[] cards = new VotingCard[cardCount];
  private VotingCard focusedCard = new VotingCard();
  private VotnigButtonNode votingButtons;
  private File f = new File("src/starvationevasion/client/GUI/votingHud/testImages/FinishedVotingButton.png");
  private Image votingFin = new Image(f.toURI().toString());
  private ImageView viv = new ImageView(votingFin);
  private Button finishedButton;

  public VotingHand(double width, double height)
  {
    super();
    viv.setPreserveRatio(true);
    finishedButton = new Button("", viv);
    finishedButton.setStyle("-fx-background-color: transparent;");
    finishedButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        System.out.println("Finished Button Pressed");
        resetCards();
      }

    });

    votingButtons = new VotnigButtonNode();
    votingButtons.setManaged(false);
    votingButtons.setVisible(false);

    this.setWidth(width);
    this.setHeight(height);
    focusedCard.setVisible(false);

    this.setOnMouseClicked(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        mouseClickedEvent();
      }

    });
    this.setOnMouseMoved(new EventHandler<Event>()
    {

      @Override
      public void handle(Event event)
      {
        mouseMovedEvent();
      }

    });
    for (int i = 0; i < cards.length; i++)
    {
      cards[i] = new VotingCard(i);
      cards[i].setManaged(false);
      this.getChildren().add(cards[i]);
    }
    this.getChildren().add(focusedCard);
    this.getChildren().add(votingButtons);
    this.getChildren().add(finishedButton);
    onResize();
  }

  @Override
  public void onResize()
  {
    double width = this.getWidth() / cardCount;
    double height = this.getHeight();

    for (int i = 0; i < cards.length; i++)
    {
      VotingCard card = cards[i];
      if (card == null) return;
      double cardWidth = width * (7d / 8d);
      double cardHeight = cardWidth / card.getSizeRatio();
      double votingY = height - 20 - cardHeight - focusedCard.getHeight();
      if (cardHeight > height * MAX_SMALL_SIZE)
      {
        cardHeight = height * MAX_SMALL_SIZE;
        cardWidth = cardHeight * card.getSizeRatio();
      }

      card.setSize(cardWidth, cardHeight);
      card.setLayoutX((cardWidth / 4) * 3 * i);

      if (card.isClicked())
      {
        card.setLayoutY(height - cardHeight);
        card.setTranslateY(-10);
      }
      else
      {

        card.setLayoutY(height - cardHeight);
      }
      xLayouts[i] = width * i;

      if (this.getWidth() / 3.8 < minWidth)
      {
        focusedCard.setSize(minWidth, minWidth / focusedCard.getSizeRatio());
      }
      else focusedCard.setSize(this.getWidth() / 3.8, (this.getWidth() / 3.8) / focusedCard.getSizeRatio());
      focusedCard.setLayoutX(10);
      focusedCard.setLayoutY(votingY);

      votingButtons.setLayoutX(focusedCard.getWidth() + votingButtons.getWidth());
      votingButtons.setLayoutY(votingY);
      votingButtons.setSize(this.getWidth() / 18.6, this.getWidth() / 18.6);

      finishedButton.setLayoutY(height - finishedButton.getHeight() - 10);
      finishedButton.setLayoutX(20 + (cardWidth / 4) * 3 * cards.length);
    }

  }

  private void resetCards()
  {
    for (int i = 0; i < cards.length; i++)
    {
      cards[i].resetCard();
    }
    focusedCard.resetCard();

    cardIsClicked = false;
    votingButtons.setVisible(false);
    focusedCard.setVisible(false);

  }

  public void mouseClickedEvent()
  {
    boolean noClick = true;

    for (int i = 0; i < cards.length; i++)
    {
      cards[i].setClicked(false);
      focusedCard.setClicked(false);
      if (focusedCard.isFocused())
      {
        focusedCard.setClicked(true);
        cardIsClicked = true;
        votingButtons.setVisible(true);
        noClick = false;
      }
      if (cards[i].isFocused())
      {
        votingButtons.setCard(cards[i]);
        votingButtons.setVisible(true);
        votingButtons.setVotes(0);

        cards[i].setTranslateY(-10);
        cards[i].setClicked(true);

        cardIsClicked = true;
        noClick = false;

      }
    }

    if (noClick)
    {
      cardIsClicked = false;
      votingButtons.setVisible(false);
      focusedCard.setVisible(false);
      for (int i = 0; i < cards.length; i++)
      {
        cards[i].setClicked(false);
      }
    }
  }

  public void mouseMovedEvent()
  {
    boolean noFocus = true;
    for (int i = 0; i < cardCount; i++)
    {

      if (cards[i].isFocused())
      {
        cards[i].setTranslateY(-10);
        focusedCard.setTargetCard(cards[i]);
        focusedCard.setManaged(false);
        noFocus = false;
        cardHasFocus = true;
      }
      else if (cards[i].isClicked() == false)
      {
        cards[i].setTranslateY(0);
      }
      if (noFocus)
      {
        cards[i].setTranslateY(0);
        cardHasFocus = false;
        for (int j = 0; j < cards.length; j++)
        {
          if (cards[i].isClicked())
          {
            focusedCard.setCardImage(cards[i].getCardImage());
          }
        }
      }

    }

    if (cardHasFocus || cardIsClicked)
    {
      focusedCard.setVisible(true);
    }
    else
    {
      focusedCard.setVisible(false);
    }
    onResize();
  }

}
