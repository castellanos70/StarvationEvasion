package starvationevasion.GuiTestCode.mainHud;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import starvationevasion.GuiTestCode.mainHud.hand.HandNode;
import starvationevasion.GuiTestCode.mainHud.map.MapNode;

public class HudLauncher extends Application
{
  
  @Override
  public void start(Stage stage) throws Exception
  {
//    CardNode card = new CardNode();
//    HandNode hand = new HandNode(300, 600);
    MapNode map = new MapNode(500, 700);
    Scene scene = new Scene(map);
    stage.setScene(scene);
    stage.setFullScreen(true);
    stage.show();
  }
  
  public static void main(String[] args){
    launch();
  }
}
