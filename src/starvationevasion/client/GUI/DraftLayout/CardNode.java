package starvationevasion.client.GUI.DraftLayout;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;

/**
 * @author Ben Matthews
 * 
 *         Creates a small Card Graphic as a ResizablePane from a EnumPolicy
 *         object
 *
 */
public class CardNode extends ResizablePane
{
  public static final double STROKE_PERC = .03;
  public static final Color[] COLORS =
  { Color.THISTLE, Color.WHEAT, Color.TURQUOISE, Color.WHITE };
  public static final int FONT_SIZE = 40;
  public static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);
  public static final Font TEXT_FONT = Font.font("Arial", FONT_SIZE);
  public static final String TOGGLED = "-fx-background-color: linear-gradient(#666666, #BBBBBB);"
      + " -fx-background-radius: 3 3 3 3;";
  public static final String NORM = "-fx-background-color: linear-gradient(#EEEEEE, #BBBBBB);"
      + " -fx-background-radius: 3 3 3 3;";
  private GameCard gameCard;
  private ArrayList<CardNode> cards;

  private Rectangle background;

  public boolean isDiscarded = false;
  public boolean isDrafted = false;
  private EnumPolicy ePolicy;
  private EnumRegion region;
  public boolean isClicked = false;
  public Label title;
  public Label gameText;
  public Label flavorText;
  public Label votes;
  public ImageView image;
  public Label discardButton;
  public Label draftButton;

  private int status;
  private boolean yea = false;
  private boolean abstain = false;
  private boolean nay = false;
  private int cardVotes = 0;
  private boolean voteSet = false;

  /**
   * CardNode constructor takes a EnumRegion and EnumPolicy object
   * 
   * @param owner
   *          - the region of the card owner
   * @param policy
   *          - the policy of this card
   */
  public CardNode(EnumRegion owner, EnumPolicy policy)
  {
    super();
    gameCard = GameCard.create(owner, policy);
    ePolicy = policy;
    region = owner;

    background = new Rectangle();
    background.setStrokeType(StrokeType.INSIDE);
    title = new Label(gameCard.getTitle());
    title.setFont(TITLE_FONT);
    title.setBackground(new Background(new BackgroundFill(new Color(.2, .2, .3, 1), null, null)));
    title.setTextFill(Color.WHITE);
    gameText = new Label(gameCard.getGameText());
    gameText.setWrapText(true);
    gameText.setFont(TITLE_FONT);
    gameText.setTextFill(Color.WHITE);
    gameText.setBackground(new Background(new BackgroundFill(new Color(.2, .2, .2, .6), null, null)));
    gameText.setMaxWidth(gameText.getText().length() * 5);
    gameText.setTextAlignment(TextAlignment.CENTER);
    flavorText = new Label(gameCard.getFlavorText());
    flavorText.setFont(TEXT_FONT);
    votes = new Label("" + gameCard.votesRequired());
    votes.setFont(TITLE_FONT);
    votes.setBackground(new Background((new BackgroundFill(Color.PURPLE, null, null))));
    image = ImageGetter.getImageForCard(policy);
    discardButton = new Label(" Discard ");
    discardButton.setStyle(NORM);
    draftButton = new Label("Draft");
    draftButton.setStyle(NORM);

    addElements();
    addListeners();
//    setStyle();
    onResize();
  }

  /**
   * adds the nodes to the pane
   */
  private void addElements()
  {
    ObservableList<Node> ob = this.getChildren();
    ob.add(background);
    ob.add(image);
    ob.add(votes);
    ob.add(title);
    ob.add(gameText);
    ob.add(discardButton);
    ob.add(draftButton);
  }

  private void addListeners()
  {

    discardButton.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent arg0)
      {
        discard();
      }
    });

    draftButton.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent arg0)
      {
        draft();
      }
    });
  }

  private void setStyle()
  {
    background.setStroke(Color.BLACK);
    background.setStrokeWidth(this.getWidth() * STROKE_PERC);
    if (gameCard.getValidTargetFoods() != null && gameCard.getValidTargetRegions() != null)
    {
      background.setFill(COLORS[0]);
    }
    else if (gameCard.getValidTargetFoods() != null)
    {
      background.setFill(COLORS[1]);
    }
    else if (gameCard.getValidTargetRegions() != null)
    {
      background.setFill(COLORS[2]);
    }
    else
    {
      background.setFill(COLORS[3]);
    }
  }

  /**
   * executes when the draft button is pushed
<<<<<<< HEAD
   * 
   * removes draft status in other cards if the card list has been instantiated,
   * and there are over the max number of drafts
=======
>>>>>>> branch 'master' of https://github.com/bmatthews1/StarvationEvasion.git
   */
  private void draft()
  {
    isDiscarded = false;
    discardButton.setStyle(NORM);
    if (isDrafted)
    {
      setStyle();
      draftButton.setStyle(NORM);
    }
    else
    {
      background.setStroke(Color.GREEN);
      background.setStrokeWidth(this.getWidth() * STROKE_PERC * 2);
      draftButton.setStyle(TOGGLED);
    }
    isDrafted ^= true;

    if (cards == null) return;

    int drafts = 0;
    for (CardNode card : cards)
    {
      if (card.isDrafted)
      {
        drafts++;
      }
    }

    if (drafts > 2)
    {
      for (CardNode card : cards)
      {
        if (card.isDrafted && !card.equals(this))
        {
          card.setDrafted(false);
          drafts--;
          if (drafts <= 2) break;
        }
      }
    }
  }

  @Override
  /**
   * checks for equality by comparing if the object is an instance of CardNode
   * and calling the GameCard equals method
   */
  public boolean equals(Object o)
  {
    if (!(o instanceof CardNode)) return false;
    CardNode card = (CardNode) o;
    return card.gameCard.equals(this.gameCard);
  }

  /**
   * executes when the discard button is pushed;
<<<<<<< HEAD
   * 
   * removes discard status in other cards if the card list has been
   * instantiated, and there are over the max number of discards
=======
>>>>>>> branch 'master' of https://github.com/bmatthews1/StarvationEvasion.git
   */
  private void discard()
  {
    isDrafted = false;
    draftButton.setStyle(NORM);
    if (isDiscarded)
    {
      setStyle();
      discardButton.setStyle(NORM);
    }
    else
    {
      background.setStroke(Color.RED);
      background.setStrokeWidth(this.getWidth() * STROKE_PERC * 2);
      discardButton.setStyle(TOGGLED);
    }
    isDiscarded ^= true;

    if (cards == null) return;

    int discards = 0;
    for (CardNode card : cards)
    {
      if (card.isDiscarded)
      {
        discards++;
      }
    }

    if (discards > 3)
    {
      for (CardNode card : cards)
      {
        if (card.isDiscarded && !card.equals(this))
        {
          card.setDiscarded(false);
          discards--;
          if (discards <= 3) break;
        }
      }
    }
  }

  /**
   * Sets the draft state of this card
   * 
   * @param drafted
   */
  public void setDrafted(boolean drafted)
  {
    isDrafted = !drafted;
    draft();
  }

  /**
   * sets the discard state of this card
   * 
   * @param discarded
   */
  public void setDiscarded(boolean discarded)
  {
    isDiscarded = !discarded;
    discard();
  }

  /**
   * passes a reference to an ArrayList of CardNode objects
   * 
   * @param cards
   *          - the ArrayList of CardNodes
   */
  public void setCards(ArrayList<CardNode> cards)
  {
    this.cards = cards;
  }

  /**
   * resets all of the changes to this card (discard) (draft)
   */
  public void reset()
  {
    isDiscarded = false;
    isDrafted = false;
    setStyle();
    draftButton.setStyle(NORM);
    discardButton.setStyle(NORM);
  }

  /**
   * returns the GameCard object representing the EnumPolicy for this card
   * 
   * @return a GameCard Object
   */
  public GameCard getGameCard()
  {
    return gameCard;
  }

  @Override
  public void onResize()
  {
    double width = this.getWidth();
    double height = this.getHeight();

    double borderOffset = 0;

    background.setWidth(width);
    background.setHeight(height);
    if (isDrafted || isDiscarded)
    {
      borderOffset = width * STROKE_PERC * 4;
    }
    else
    {
      borderOffset = width * STROKE_PERC;
    }
    background.setStrokeWidth(borderOffset);

    width -= borderOffset * 2;
    height -= borderOffset * 2;

    Bounds b;
    double scale;
    double aspectRatio;
    double nodeAspectRatio;

    {// title
      b = title.getBoundsInLocal();
      aspectRatio = (width * .8) / (height * .1);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      if (aspectRatio <= nodeAspectRatio)
      {
        scale = (width * .8) / b.getWidth();
      }
      else
      {
        scale = (height * .1) / b.getHeight();
      }

      title.setScaleX(scale);
      title.setScaleY(scale);

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      title.setTranslateX(borderOffset + diffX + (width - newWidth) / 2);
      title.setTranslateY(borderOffset + diffY + (height * .1 - newHeight) / 2);
      title.autosize();
    }

    {// votes
      b = votes.getBoundsInLocal();
      aspectRatio = (width * .8) / (height * .1);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      scale = (width * .1) / b.getWidth();

      votes.setScaleX(scale);
      votes.setScaleY(scale);

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      votes.setTranslateX(diffX);
      votes.setTranslateY(diffY);
      votes.autosize();
    }

    {// image
      b = image.getBoundsInLocal();
      aspectRatio = (width * .9) / (height * .4);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      if (aspectRatio <= nodeAspectRatio)
      {
        scale = (width * .9) / b.getWidth();
      }
      else
      {
        scale = (height * .4) / b.getHeight();
      }

      image.setScaleX((width * .95) / b.getWidth());
      image.setScaleY((height * .95) / b.getHeight());

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      image.setTranslateX(borderOffset + diffX + (width - newWidth) / 2);
      image.setTranslateY(borderOffset + diffY + (height - newHeight) / 2);
      image.autosize();
    }

    {// gameText
      b = gameText.getBoundsInLocal();
      aspectRatio = (width * .9) / (height * .4);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      if (aspectRatio <= nodeAspectRatio)
      {
        scale = (width * .9) / b.getWidth();
      }
      else
      {
        scale = (height * .4) / b.getHeight();
      }

      gameText.setScaleX(scale);
      gameText.setScaleY(scale);

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      gameText.setTranslateX(borderOffset + diffX + (width - newWidth) / 2);
      gameText.setTranslateY(borderOffset + height * .5 + diffY + (height * .4 - newHeight) / 2);
      gameText.autosize();
    }

    {// draftButton
      b = draftButton.getBoundsInLocal();
      aspectRatio = (width * .3) / (height * .1);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      if (aspectRatio <= nodeAspectRatio)
      {
        scale = (width * .3) / b.getWidth();
      }
      else
      {
        scale = (height * .1) / b.getHeight();
      }

      draftButton.setScaleX(scale);
      draftButton.setScaleY(scale);

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      draftButton.setTranslateX(borderOffset + width * .1 + diffX + (width * .3 - newWidth) / 2);
      draftButton.setTranslateY(borderOffset + height * .9 + diffY + (height * .1 - newHeight) / 2);
      draftButton.autosize();
    }

    {// discardButton
      b = discardButton.getBoundsInLocal();
      aspectRatio = (width * .3) / (height * .1);
      nodeAspectRatio = b.getWidth() / b.getHeight();

      if (aspectRatio <= nodeAspectRatio)
      {
        scale = (width * .3) / b.getWidth();
      }
      else
      {
        scale = (height * .1) / b.getHeight();
      }

      discardButton.setScaleX(scale);
      discardButton.setScaleY(scale);

      double newWidth = b.getWidth() * scale;
      double newHeight = b.getHeight() * scale;
      double diffX = (newWidth - b.getWidth()) / 2;
      double diffY = (newHeight - b.getHeight()) / 2;
      discardButton.setTranslateX(borderOffset + width * .6 + diffX + (width * .3 - newWidth) / 2);
      discardButton.setTranslateY(borderOffset + height * .9 + diffY + (height * .1 - newHeight) / 2);
      discardButton.autosize();
    }
  }

  public EnumPolicy getPolicy()
  {
    return ePolicy;
  }

  public EnumRegion getRegion()
  {
    return region;
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

  public void setClicked(boolean clicked)
  {
    isClicked = clicked;
  }

  public boolean isClicked()
  {
    return isClicked;
  }

  public void setVotes(int i)
  {

    if (!nay && i == -1)
    {
      cardVotes += i;
      setNay();
    }
    else if (!yea && i == 1)
    {
      cardVotes += i;
      setYea();
    }
    else if (!abstain && i == 10)
    {
      setAbstain();
    }
    if (cardVotes < 0) cardVotes = 0;
  }
  public void setHasBeenVoted(boolean set)
  {
    voteSet = set;
  }
  public boolean getHasBeenVoted()
  {
    return voteSet;
  }

}
