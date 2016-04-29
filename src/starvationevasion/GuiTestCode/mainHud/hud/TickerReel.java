package starvationevasion.GuiTestCode.mainHud.hud;

import java.util.ArrayList;

import javafx.scene.text.Text;
import starvationevasion.GuiTestCode.common.NodeTemplate;

public class TickerReel extends NodeTemplate
{
  
  private ArrayList<Text> texts;
  
  public TickerReel(int width, int height){
    this.setWidth(width);
    this.setHeight(height);
  }
  
  @Override
  public void onResize()
  {
    
  }

}
