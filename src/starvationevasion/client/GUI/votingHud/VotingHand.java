package starvationevasion.client.GUI.votingHud;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.DraftLayout.CardNode;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;

public class VotingHand extends NodeTemplate
{
  private static final double MAX_SMALL_SIZE = .5;
  private static final double CARD_RATIO = .77;

  private EnumPolicy[] policies;

  private int cardCount;

  private double[] xLayouts = new double[cardCount];
  private double minWidth = 100;

  private boolean cardHasFocus = false;
  private boolean cardIsClicked = false;

  private CardNode[] cardNode;

  private CardNode focusedCard = new CardNode(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[0]);
  private VotnigButtonNode votingButtons;
  private File f = new File("src/starvationevasion/client/GUI/votingHud/testImages/FinishedVotingButton.png");
  private Image votingFin = new Image(f.toURI().toString());
  private ImageView viv = new ImageView(votingFin);
  private Button finishedButton;
  private boolean testCards = true;

  public VotingHand(GUI gui, double width, double height)
  {
    super();
    policies = EnumPolicy.values();

    if (testCards)
    {
      ArrayList<GameCard> cardList = new ArrayList<>();
      cardList.add(GameCard.create(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[0]));
      cardList.add(GameCard.create(EnumRegion.USA_HEARTLAND, EnumPolicy.values()[1]));
      cardList.add(GameCard.create(EnumRegion.USA_MOUNTAIN, EnumPolicy.values()[2]));
      cardList.add(GameCard.create(EnumRegion.USA_NORTHERN_CRESCENT, EnumPolicy.values()[3]));
      cardList.add(GameCard.create(EnumRegion.USA_NORTHERN_PLAINS, EnumPolicy.values()[4]));
      setVotingCards(cardList);
    }

    viv.setPreserveRatio(true);
    finishedButton = new Button("", viv);
    finishedButton.setStyle("-fx-background-color: transparent;");
    finishedButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        System.out.println("Finished Button Pressed");
        for (int i = 0; i < cardNode.length; i++)
        {
          if (cardNode[i].isDrafted)
          {
            gui.getClient().voteUp(cardNode[i].getGameCard());
          }
          else if (cardNode[i].isDiscarded)
          {
            gui.getClient().voteDown(cardNode[i].getGameCard());
          }
        }
        gui.getClient().done();
      }

    });

    votingButtons = new VotnigButtonNode();
    votingButtons.setManaged(false);
    votingButtons.setVisible(false);

    this.setWidth(width);
    this.setHeight(height);

    focusedCard.discardButton.setVisible(false);
    focusedCard.draftButton.setVisible(false);
    focusedCard.setVisible(false);
    focusedCard.setManaged(false);

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

    if (cardNode != null && votingButtons!=null)
    {
      for (int i = 0; i < cardNode.length; i++)
      {
        CardNode card = cardNode[i];
        if (card == null) return;
        double cardWidth = width * (7d / 8d);
        double cardHeight = cardWidth / CARD_RATIO;

        if (cardHeight > height * MAX_SMALL_SIZE)
        {
          cardHeight = height * MAX_SMALL_SIZE;
          cardWidth = cardHeight * CARD_RATIO;
        }

        card.setSize(cardWidth, cardHeight);
        card.setLayoutX((cardWidth / 4) * 3 * i);
        card.setLayoutY(height - cardHeight - 10);
        cardNode[i].onResize();

        if (this.getWidth() / 3.8 < minWidth)
        {
          focusedCard.setSize(minWidth, minWidth / CARD_RATIO);
        }
        else focusedCard.setSize(this.getWidth() / 3.8, (this.getWidth() / 3.8) / CARD_RATIO);
        focusedCard.setLayoutX(10);
        focusedCard.setLayoutY(this.getHeight() - 20 - cardHeight - (this.getWidth() / 3.8) / CARD_RATIO);
        //
        votingButtons.setLayoutX(focusedCard.getWidth() + votingButtons.getWidth());
        votingButtons.setLayoutY(this.getHeight() - 20 - cardHeight - (this.getWidth() / 3.8) / CARD_RATIO);
        votingButtons.setSize(this.getWidth() / 18.6, this.getWidth() / 18.6);

        finishedButton.setLayoutX(10 * cardCount + (cardWidth / 4) * 3 * cardCount);
        finishedButton.setLayoutY(height - finishedButton.getHeight() - 10);
      }
    }
  }

  private void resetCards()
  {
    // focusedCard.resetCard();

    cardIsClicked = false;
    votingButtons.setVisible(false);
    focusedCard.setVisible(false);

  }

  public void mouseClickedEvent(Event event)
  {
    if (cardNode != null)
    {
      boolean noClick = true;

      for (int i = 0; i < cardNode.length; i++)
      {
        cardNode[i].isClicked = false;
        if (focusedCard.isFocused())
        {
          cardIsClicked = true;
          votingButtons.setVisible(true);
          noClick = false;
        }
        if (event.getSource().equals(cardNode[i]))
        {
          votingButtons.setCard(cardNode[i]);
          votingButtons.setVisible(true);

          cardNode[i].setTranslateY(-10);
          cardNode[i].setClicked(true);

          cardIsClicked = true;
          noClick = false;

        }
      }

      if (noClick)
      {
        cardIsClicked = false;
        votingButtons.setVisible(false);
        focusedCard.setVisible(false);
        for (int i = 0; i < cardNode.length; i++)
        {
          cardNode[i].setClicked(false);
        }
      }
    }
  }

  public void mouseMovedEvent(Event event)
  {
    if (cardNode != null)
    {
      boolean noFocus = true;
      for (int i = 0; i < cardNode.length; i++)
      {
        if (event.getSource().equals(cardNode[i]))
        {
          setFocusedCard(cardNode[i]);
          focusedCard.onResize();
          cardNode[i].setTranslateY(-10);
          noFocus = false;
          cardHasFocus = true;
        }
        else if (cardNode[i].isClicked() == false)
        {
          cardNode[i].setTranslateY(0);
        }
        if (noFocus)
        {
          cardNode[i].setTranslateY(0);
          cardHasFocus = false;
          for (int j = 0; j < cardNode.length; j++)
          {
            if (cardNode[i].isClicked())
            {
              setFocusedCard(cardNode[i]);
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

  private void setFocusedCard(CardNode card)
  {
    focusedCard.image.setImage(card.image.getImage());
    focusedCard.gameText.setText(card.getGameCard().getGameText());
    focusedCard.title.setText(card.getGameCard().getTitle());
    focusedCard.votes.setText("" + card.getGameCard().votesRequired());
  }

  public void setVotingCards(ArrayList<GameCard> cardInfo)
  {
    clearCards();
    cardNode = new CardNode[cardInfo.size()];
    cardCount = cardInfo.size();
    if (cardInfo != null)
    {
      for (int i = 0; i < cardInfo.size(); i++)
      {
        for (int j = 0; j < policies.length; j++)
        {
          if (cardInfo.get(i).getPolicyName().equals(policies[j].name()))
          {
            cardNode[i] = new CardNode(cardInfo.get(i).getOwner(), policies[j]);
            this.getChildren().add(cardNode[i]);
            cardNode[i].setManaged(false);
            cardNode[i].setPickOnBounds(false);
            cardNode[i].draftButton.setVisible(false);
            cardNode[i].discardButton.setVisible(false);
            cardNode[i].setOnMouseMoved(new EventHandler<Event>()
            {
              @Override
              public void handle(Event event)
              {
                mouseMovedEvent(event);
              }
            });
            cardNode[i].setOnMouseClicked(new EventHandler<Event>()
            {
              @Override
              public void handle(Event event)
              {
                mouseClickedEvent(event);
              }
            });

          }
        }

      }
    }
    onResize();
    
  }

  public void clearCards()
  {

    if (cardNode == null) return;
    for (int i = 0; i < cardNode.length; i++)
    {
      this.getChildren().remove(cardNode[i]);
      cardNode[i] = null;
    }

  }
}
