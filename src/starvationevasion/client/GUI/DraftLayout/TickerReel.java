package starvationevasion.client.GUI.DraftLayout;

import java.util.LinkedList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import starvationevasion.client.GUI.ResizablePane;

/**
 * @author Ben
 * 
 * This class creates a dynamically resizable ticker reel
 * which can be fed messages that will display across the
 * screen
 *
 */
public class TickerReel extends ResizablePane
{
  private static final Color TEXT_COLOR = Color.RED;
  private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, .6);
  private static final double SCROLL_RATE = 30; //in scrolls per second
  private static final double SCROLL_AMOUNT = 4.3; //in pixels
  private static final Font FONT = Font.font("Arial", 20);
  
  private Pane parent;
  
  private Rectangle background;
  private AnimationTimer timer;
  
  private long last = System.currentTimeMillis();
  
  private double scale;
  private double textBuffer = 30;
  
  private LinkedList<String> messageQueue;
  private LinkedList<Text> displayedMessages;
  private LinkedList<Double> positionModifiers;
  
  private Text sizingText;
  
  /**
   * creates a TickerReel that resizes its width the be the entire width of the parent frame
   * and has a user defined height
   * 
   * @param parent
   * @param height
   */
  public TickerReel(Pane parent)
  {
    super();
    this.parent = parent;
    messageQueue = new LinkedList<>();
    displayedMessages = new LinkedList<>();
    positionModifiers = new LinkedList<>();
    
    background = new Rectangle();
    background.setFill(BACKGROUND_COLOR);
    background.setHeight(this.getHeight());
    background.setWidth(this.getWidth());
    
    sizingText = new Text("test");
    sizingText.setFont(FONT);
    sizingText.setVisible(false);
    sizingText.setManaged(false);
    
    getChildren().add(sizingText);
    
    Bounds b = sizingText.getBoundsInLocal();
    double textHeight = b.getHeight();
    
    timer = new AnimationTimer(){
      @Override
      public void handle(long now)
      {
        if ((now - last)/1000d > 1/SCROLL_RATE){
          scroll();
        }
      }
    };
    
    timer.start();
    
    this.getChildren().add(background);
  }
  
  /**
   * adds a string to the message queue
   * 
   * @param message - the string to be added
   */
  public void addMessage(String message){
    messageQueue.addLast(message);
  }
  
  /**
   * scrolls all of the current messages in the
   * displayedMessages
   */
  private void scroll(){
    if (displayedMessages.isEmpty()) {
      if (!messageQueue.isEmpty()){
        displayNextMessage();
      }
      return;
    }
    
    for (int i = 0; i < displayedMessages.size(); i++){
      double p = positionModifiers.get(i);
      p -= SCROLL_AMOUNT;
      displayedMessages.get(i).setTranslateX(p);
      positionModifiers.set(i, p);
    }
    
    Text last = displayedMessages.getLast();
    Bounds b = last.getBoundsInParent();
    
    if (Math.abs(last.getTranslateX()) >=  b.getWidth()*scale + textBuffer
        && !messageQueue.isEmpty()){
      displayNextMessage();
    }
    
    Text first = displayedMessages.getFirst();
    b = first.getBoundsInParent();
    
    if (Math.abs(first.getTranslateX()) >= b.getWidth()*scale + this.getWidth()){
      displayedMessages.remove(first);
      positionModifiers.removeFirst();
      this.getChildren().remove(first);
    }
  }
  
  /**
   * Pops a String off of the messageQueue and adds it to the
   * displayedMessages
   */
  private void displayNextMessage(){
    if (messageQueue.isEmpty()) return;
    
    String message = messageQueue.pop();
    Text displayText = new Text(message);
    
    displayText.setFill(TEXT_COLOR);
    displayText.setScaleY(scale);
    displayText.setScaleX(scale);
    displayText.setFont(FONT);
    displayText.setTextOrigin(VPos.TOP);
    displayText.setManaged(false);
    
    Bounds b = displayText.getBoundsInLocal();
    
    double displayHeight = b.getHeight();
    double displayWidth = b.getWidth();
    
    displayText.setLayoutX(this.getWidth());
    displayText.setLayoutY((displayHeight*scale - displayHeight)/2);
    
    displayedMessages.add(displayText);
    Double d = new Double(0);
    positionModifiers.add(d);
    this.getChildren().add(displayText);
  }

  @Override
  public void onResize()
  {
    double width = parent.getWidth();
    this.setWidth(width);
    double height = parent.getHeight();
    this.setHeight(height);
    
    
    Bounds b = sizingText.getBoundsInLocal();
    double textHeight = b.getHeight();
    scale = height/textHeight;
    textBuffer = height*2;
    
    background.setWidth(width);
    background.setHeight(height);
    
    for (Text t : displayedMessages){
      b = t.getBoundsInLocal();
      
      double displayHeight = b.getHeight();
      
      t.setX(width);
      t.setY((displayHeight*scale - displayHeight)/2);
      
    }
  }

}
