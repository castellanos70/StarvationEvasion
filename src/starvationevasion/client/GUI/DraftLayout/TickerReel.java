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
import starvationevasion.client.GUI.ResizeStrategy;

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
  private static final Color TEXT_COLOR = Color.WHITE;
  private static final Color BACKGROUND_COLOR = Color.BLACK;
  private static final double SCROLL_RATE = 30; //in scrolls per second
  private static final double SCROLL_AMOUNT = 3; //in pixels
  private static final Font FONT = Font.font("Arial", 20);
  
  
  private Rectangle background;
  private AnimationTimer timer;
  
  private long last = System.currentTimeMillis();
  
  private double scale;
  
  private LinkedList<String> messageQueue;
  private LinkedList<Text> displayedMessages;
  private LinkedList<Double> positionModifiers;
  
  private Text sizingText;
  
  public TickerReel(Pane parent, ResizeStrategy strategy)
  {
    super(parent, strategy);
    messageQueue = new LinkedList<>();
    displayedMessages = new LinkedList<>();
    positionModifiers = new LinkedList<>();
    
    background = new Rectangle();
    background.setFill(BACKGROUND_COLOR);
    
    scale = 0;
    
    sizingText = new Text("test");
    sizingText.setFont(FONT);
    sizingText.setVisible(false);
    
    timer = new AnimationTimer(){
      @Override
      public void handle(long now)
      {
        if ((now - last)/1000d > SCROLL_RATE/1){
          scroll();
        }
      }
    };
  }
  
  /**
   * adds a string to the message queue
   * 
   * @param message - the string to be added
   */
  public void addMessage(String message){
    messageQueue.push(message);
  }
  
  /**
   * scrolls all of the current messages in the
   * displayedMessages
   */
  private void scroll(){
    for (int i = 0; i < positionModifiers.size(); i++){
      double pos = positionModifiers.get(i);
      pos -= SCROLL_AMOUNT;
      
      Text t = displayedMessages.get(i);
      t.setTranslateX(pos);
      
      positionModifiers.set(i, pos);
    }
  }
  
  private void displayNextMessage(){
    String message = messageQueue.pop();
    Text displayText = new Text(message);
    
    displayText.setFill(TEXT_COLOR);
    displayText.setScaleX(scale);
    displayText.setFont(Font.font("Arial", 20));
    displayText.setTextOrigin(VPos.TOP);
    
  }

  @Override
  public void onResize()
  {
    Bounds b = sizingText.getBoundsInLocal();
    
    double height = this.getHeight();
    double textHeight = b.getHeight();
    
    scale = height/textHeight;
    
  }

}
