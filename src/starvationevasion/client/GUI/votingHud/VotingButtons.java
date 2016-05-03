package starvationevasion.client.GUI.votingHud;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import starvationevasion.client.GUI.ResizablePane;

public class VotingButtons extends ResizablePane
{
  ImageView up;
  ImageView abs;
  ImageView down;
  ImageView[] buttons = new ImageView[3];

  public VotingButtons()
  {
	  super(null, null);
    up = new ImageView(
        new Image(getClass().getResource("/starvationevasion/GuiTestCode/resources/up.png").toString()));
    abs = new ImageView(
        new Image(getClass().getResource("/starvationevasion/GuiTestCode/resources/abs.png").toString()));
    down = new ImageView(
        new Image(getClass().getResource("/starvationevasion/GuiTestCode/resources/down.png").toString()));

    double width = up.getLayoutX();
    double height = up.getLayoutY();
//    for(int i = 0; i<buttons.length; i ++)
//    {
//      buttons[i].setManaged(false);
//      buttons[i].setLayoutX(width);
//      buttons[i].setLayoutY(height);
//      buttons[i].setTranslateY(i*height);
//    }
    
    this.getChildren().addAll(up, abs, down);
    
    onResize();
  }

  @Override
  public void onResize()
  {
    
    
    
    for(int i = 0; i<buttons.length; i ++)
    {
      
    }
    // TODO Auto-generated method stub

  }

}
