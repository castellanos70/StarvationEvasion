package starvationevasion.GuiTestCode.mainHud.chat;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;
import starvationevasion.GuiTestCode.common.NodeTemplate;

public class ChatNode extends NodeTemplate
{
  private static final Color BACK_COLOR = new Color(.1, .1, .1, .5);
  private static final Color CONTROL_COLOR = Color.WHITE;
//  private static final double 
  
  private Rectangle scrollBar;
  private Line scrollLine;
  private TextFlow chatLog;
  private TextField input;
  
  private int width;
  private int height;
  
  public ChatNode(int width, int height){
    super();
    this.height = height;
    this.width = width;
    
    this.setWidth(width);
    this.setHeight(height);
    
    scrollBar = new Rectangle(0, 0, 0, 0);
    scrollBar.setFill(CONTROL_COLOR);
//    scrollBar.setarc
    
  }

  @Override
  public void onResize()
  {
    
  }
  
  private void resizeScroll(){
    
  }
}
