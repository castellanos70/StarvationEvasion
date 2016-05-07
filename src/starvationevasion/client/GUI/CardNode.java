package starvationevasion.client.GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
  public static final double STROKE_PERC = .05;
  public static final Color[] COLORS = {Color.DARKRED, Color.DARKGREEN, Color.DARKBLUE, Color.WHITE};
  public static final int FONT_SIZE = 12;
  public static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);
  public static final Font TEXT_FONT = Font.font("Arial", FONT_SIZE);
  private GameCard gameCard;
  private Pane parent;
  
  private Rectangle background;
  
  private boolean discarded = false;
  private boolean drafted = false;
  
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
  public CardNode (EnumRegion owner, EnumPolicy policy, Pane parent){
    gameCard = GameCard.create(owner, policy);
    this.parent = parent;
    
    background = new Rectangle();
    
    title = new Label(gameCard.getTitle());
    title.setFont(TITLE_FONT);
    gameText = new Label(gameCard.getGameText());
    gameText.setWrapText(true);
    gameText.setFont(TEXT_FONT);
    flavorText = new Label(gameCard.getFlavorText());
    flavorText.setFont(TEXT_FONT);
    votes = new Label("" + gameCard.votesRequired());
    votes.setFont(TITLE_FONT);
    
    image = ImageGetter.getImageForCard(policy);
    
    discardButton = new Button("Discard");
    draftButton = new Button("Draft");
    
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
//    ob.add(discardButton);
//    ob.add(draftButton);
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
    background.setStroke(Color.TRANSPARENT);
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
    discarded = false;
    if (drafted){
      setStyle();
    } else {
      background.setStroke(Color.GREEN);
    }
    drafted ^= true;
  }
  
  /**
   * executes when the discard button is pushed;
   */
  private void discard(){
    drafted = false;
    if (discarded){
      setStyle();
    } else {
      background.setStroke(Color.RED);
    }
    discarded ^= true;
  }
  
  @Override
  public void onResize()
  {
    double width = parent.getWidth();
    double height = parent.getHeight();
    
    background.setWidth(width);
    background.setHeight(height);
    background.setStrokeWidth(width*STROKE_PERC);
    
    Bounds b;
    double scale;
    double aspectRatio;
    double nodeAspectRatio;
    
    {//title
      b = title.getBoundsInLocal();
      aspectRatio = width/height*.1;
      nodeAspectRatio = b.getWidth()/b.getHeight();
      
      if (aspectRatio <= nodeAspectRatio){
        scale = width/b.getWidth();
      } else {
        scale = height/b.getHeight();
      }
      
      title.setScaleX(scale);
      title.setScaleY(scale);
      
      double newWidth = b.getWidth()*scale;
      double newHeight = b.getHeight()*scale;
      double diffX = (newWidth - b.getWidth())/2;
      double diffY =  (newHeight - b.getHeight())/2;
      title.setTranslateX(diffX + (width - newWidth)/2);
      title.setTranslateY(diffY + (height*.1 - newHeight)/2);
    }
  }
}
