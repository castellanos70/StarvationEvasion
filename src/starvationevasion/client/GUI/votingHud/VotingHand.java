package starvationevasion.client.GUI.votingHud;

import javafx.event.Event;
import javafx.event.EventHandler;
import starvationevasion.client.GUI.ResizablePane;

public class VotingHand extends ResizablePane
{
    private static final double MAX_SMALL_SIZE = .5;

    private int cardCount = 7;

    private double cardSpace;
    private double[] xLayouts = new double[cardCount];
    private double lastWidth = 0;
    private double lastHeight = 0;

    private boolean cardHasFocus = false;
    private boolean cardIsClicked = false;

    private VotingCard[] cards = new VotingCard[cardCount];
    private VotingCard focusedCard = new VotingCard();
    private VotnigButtonNode votingButtons;

    public VotingHand(double width, double height)
    {
        super(null, null);

        votingButtons = new VotnigButtonNode();
        votingButtons.setManaged(false);
        votingButtons.setVisible(false);

        this.setWidth(width);
        this.setHeight(height);
        cardSpace = 10 + (width * 3 / 4) / cardCount;
        focusedCard.setVisible(false);

        this.setOnMouseClicked(new EventHandler<Event>()
        {

            @Override
            public void handle(Event event)
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
                        cards[i].setClicked(true);
                        votingButtons.setVisible(true);
                        cardIsClicked = true;
                        noClick = false;
                    }
                }
                if (noClick)
                {
                    cardIsClicked = false;
                    votingButtons.setVisible(false);
                    for (int i = 0; i < cards.length; i++)
                    {
                        cards[i].setClicked(false);
                    }
                }
            }
        });
        this.setOnMouseMoved(new EventHandler<Event>()
        {

            @Override
            public void handle(Event event)
            {
                boolean noFocus = true;
                for (int i = 0; i < cardCount; i++)
                {

                    if (cards[i].isFocused())
                    {
                        focusedCard.setCardImage(cards[i].getCardImage());
                        focusedCard.setManaged(false);
                        noFocus = false;
                        cardHasFocus = true;
                    }
                    if (noFocus)
                    {
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
        });
        for (int i = 0; i < cards.length; i++)
        {
            cards[i] = new VotingCard(i);
            cards[i].setManaged(false);
            this.getChildren().add(cards[i]);
        }
        this.getChildren().add(focusedCard);
        this.getChildren().add(votingButtons);
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
            if (cardHeight > height * MAX_SMALL_SIZE)
            {
                cardHeight = height * MAX_SMALL_SIZE;
                cardWidth = cardHeight * card.getSizeRatio();
            }

            card.setSize(cardWidth, cardHeight);
            card.setLayoutX(cardSpace * i);
            card.setLayoutY(height - cardHeight);

            xLayouts[i] = width * i;
            lastWidth = cardWidth;
            lastHeight = cardHeight;

            focusedCard.setSize(cardWidth * 2.5, cardHeight * 2.5);
            focusedCard.setLayoutX(cardSpace * i + cardWidth + cardSpace);
            focusedCard.setLayoutY(10);

            votingButtons.setTranslateX(focusedCard.getLayoutX() + cardWidth * 2.5);
            votingButtons.setTranslateY(0);

        }

    }

}
