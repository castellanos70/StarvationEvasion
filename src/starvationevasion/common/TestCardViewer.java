package starvationevasion.common;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.CardView;

import java.util.Random;

public class TestCardViewer extends Application
{
  private Scene scene;
  private Random random = new Random();



  @Override
  public void start(Stage stage) throws Exception
  {
    EnumPolicy.load();
    MenuBar menuBar = new MenuBar();

    Menu menuPolicyCards = new Menu("Policy Cards");
    menuBar.getMenus().addAll(menuPolicyCards);

    int index = random.nextInt(EnumPolicy.values().length);

    CardView card = new CardView(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[index]);
    scene = new Scene(card, 370, 520);
    //scene.getRoot().getChildren().addAll(menuBar);
    stage.setScene(scene);
    stage.show();
  }
  
  public static void main(String args[]){
    launch();
  }
}
