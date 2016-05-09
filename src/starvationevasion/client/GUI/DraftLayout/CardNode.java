package starvationevasion.client.GUI.DraftLayout;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;

/**
 * @author Ben Matthews
 * 
 * Creates a small Card Graphic as a ResizablePane from a
 * EnumPolicy object
 *
 */
public class CardNode extends ResizablePane
{
  public static final double STROKE_PERC = .03;
  public static final Color[] COLORS = {Color.THISTLE, Color.WHEAT, Color.TURQUOISE, Color.WHITE};
  public static final int FONT_SIZE = 40;
  public static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);
  public static final Font TEXT_FONT = Font.font("Arial", FONT_SIZE);
  public static final String TOGGLED = "-fx-background-color: linear-gradient(#666666, #BBBBBB)";
  public static final String NORM = "-fx-background-color: linear-gradient(#EEEEEE, #BBBBBB)";
  private GameCard gameCard;
  
  private Rectangle background;
  
  public boolean isDiscarded = false;
  public boolean isDrafted = false;
  
  public Label title;
  public Label gameText;
  public Label flavorText;
  public Label votes;
  public ImageView image;
  public Button discardButton;
  public Button draftButton;
  
  /**
   * CardNode constructor takes a EnumRegion and EnumPolicy object
   * 
   * @param owner - the region of the card owner
   * @param policy - the policy of this card
   */
  public CardNode (EnumRegion owner, EnumPolicy policy){
    gameCard = GameCard.create(owner, policy);
    
    background = new Rectangle();
    
    title = new Label(gameCard.getTitle());
    title.setFont(TITLE_FONT);
    title.setBackground(new Background(new BackgroundFill(new Color(.2, .2, .3, 1), null, null)));
    title.setTextFill(Color.WHITE);
    gameText = new Label(gameCard.getGameText());
    gameText.setWrapText(true);
    gameText.setFont(TITLE_FONT);
    gameText.setTextFill(Color.WHITE);
    gameText.setBackground(new Background(new BackgroundFill(new Color(.2, .2, .2, .6), null, null)));
    gameText.setMaxWidth(gameText.getText().length()*5);
    gameText.setAlignment(Pos.CENTER);
    flavorText = new Label(gameCard.getFlavorText());
    flavorText.setFont(TEXT_FONT);
    votes = new Label("" + gameCard.votesRequired());
    votes.setFont(TITLE_FONT);
    votes.setBackground(new Background((new BackgroundFill(Color.PURPLE, null, null))));
    
    image = ImageGetter.getImageForCard(policy);
    
    discardButton = new Button("Discard");
    discardButton.setStyle(NORM);
    draftButton = new Button("Draft");
    draftButton.setStyle(NORM);
    
    addElements();
    addListeners();
    setStyle();
    onResize();
  }
  
  /**
   * adds the nodes to the pane
   */
  private void addElements(){
    ObservableList<Node> ob = this.getChildren();
    ob.add(background);
    ob.add(image);
    ob.add(votes);
    ob.add(title);
    ob.add(gameText);
    ob.add(discardButton);
    ob.add(draftButton);
  }
  
  private void addListeners(){
    
    discardButton.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent arg0)
      {
        discard();
      }
    });
    
    draftButton.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent arg0)
      {
        draft();
      }
    });
  }
  
  private void setStyle(){
    background.setStroke(Color.BLACK);
    background.setStrokeWidth(this.getWidth()*STROKE_PERC);
    if(gameCard.getValidTargetFoods()!=null && gameCard.getValidTargetRegions()!=null)
    {
      background.setFill(COLORS[0]);
    }
    else if(gameCard.getValidTargetFoods()!=null)
    {
      background.setFill(COLORS[1]);
    }
    else if(gameCard.getValidTargetRegions()!=null)
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
   */
  private void draft(){
    isDiscarded = false;
    discardButton.setStyle(NORM);
    if (isDrafted){
      setStyle();
      draftButton.setStyle(NORM);
    } else {
      background.setStroke(Color.GREEN);
      background.setStrokeWidth(this.getWidth()*STROKE_PERC*2);
      draftButton.setStyle(TOGGLED);
    }
    isDrafted ^= true;
  }
  
  /**
   * executes when the discard button is pushed;
   */
  private void discard(){
    isDrafted = false;
    draftButton.setStyle(NORM);
    if (isDiscarded){
      setStyle();
      discardButton.setStyle(NORM);
    } else {
      background.setStroke(Color.RED);
      background.setStrokeWidth(this.getWidth()*STROKE_PERC*2);
      discardButton.setStyle(TOGGLED);
    }
    isDiscarded ^= true;
  }
  
  /**
   * resets all of the changes to this card
   * (discard) (draft)
   */
  public void reset(){
    isDiscarded = false;
    isDrafted = false;
    setStyle();
    draftButton.setStyle(NORM);
    discardButton.setStyle(NORM);
  }
  
  /**
   * returns the GameCard object representing the EnumPolicy
   * for this card
   * 
   * @return a GameCard Object
   */
  public GameCard getGameCard(){
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
    if (isDrafted || isDiscarded){
      borderOffset = width*STROKE_PERC*4;
    } else {
      borderOffset = width*STROKE_PERC;
    }
    background.setStrokeWidth(borderOffset);
    
    width -= borderOffset*2;
    height -= borderOffset*2;
    
    Bounds b;
    double scale;
    double aspectRatio;
    double nodeAspectRatio;
    
    {//title
      b = title.getBoundsInLocal();
      aspectRatio = (width*.8)/(height*.1);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = (width*.8)/b.getWidth();
      } else {
        scale = (height*.1)/b.getHeight();
      }
      
      title.setScaleX(scale);
      title.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      title.setTranslateX(borderOffset + diffX + (width - newWidth)/2);
      title.setTranslateY(borderOffset + diffY + (height*.1 - newHeight)/2);
      title.autosize();
    }
    
    {//votes
      b = votes.getBoundsInLocal();
      aspectRatio = (width*.8)/(height*.1);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      scale = (width*.1)/b.getWidth();
      
      votes.setScaleX(scale);
      votes.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      votes.setTranslateX(diffX);
      votes.setTranslateY(diffY);
      votes.autosize();
    }
    
    {//image
      b = image.getBoundsInLocal();
      aspectRatio = (width*.9)/(height*.4);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = (width*.9)/b.getWidth();
      } else {
        scale = (height*.4)/b.getHeight();
      }
      
      image.setScaleX((width*.95)/b.getWidth());
      image.setScaleY((height*.95)/b.getHeight());
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      image.setTranslateX(borderOffset + diffX + (width - newWidth)/2);
      image.setTranslateY(borderOffset + diffY + (height - newHeight)/2);
      image.autosize();
    }
    
    {//gameText
      b = gameText.getBoundsInLocal();
      aspectRatio = (width*.9)/(height*.4);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = (width*.9)/b.getWidth();
      } else {
        scale = (height*.4)/b.getHeight();
      }
      
      gameText.setScaleX(scale);
      gameText.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      gameText.setTranslateX(borderOffset + diffX + (width - newWidth)/2);
      gameText.setTranslateY(borderOffset + height*.5 + diffY + (height*.4 - newHeight)/2);
      gameText.autosize();
    }
    
    {//draftButton
      b = draftButton.getBoundsInLocal();
      aspectRatio = (width*.3)/(height*.1);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = (width*.3)/b.getWidth();
      } else {
        scale = (height*.1)/b.getHeight();
      }
      
      draftButton.setScaleX(scale);
      draftButton.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      draftButton.setTranslateX(borderOffset + width*.1 + diffX + (width*.3 - newWidth)/2);
      draftButton.setTranslateY(borderOffset + height*.9 + diffY + (height*.1 - newHeight)/2);
      draftButton.autosize();
    }
    
    {//discardButton
      b = discardButton.getBoundsInLocal();
      aspectRatio = (width*.3)/(height*.1);
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = (width*.3)/b.getWidth();
      } else {
        scale = (height*.1)/b.getHeight();
      }
      
      discardButton.setScaleX(scale);
      discardButton.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      discardButton.setTranslateX(borderOffset + width*.6 + diffX + (width*.3 - newWidth)/2);
      discardButton.setTranslateY(borderOffset + height*.9 + diffY + (height*.1 - newHeight)/2);
      discardButton.autosize();
    }
  }
}
