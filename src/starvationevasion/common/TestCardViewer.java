package starvationevasion.common;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.CardView;

public class TestCardViewer extends Application
{
  private Scene scene;
  private Random random = new Random();
  
  @Override
  public void start(Stage stage) throws Exception
  {
    int index = random.nextInt(EnumPolicy.values().length);

    CardView card = new CardView(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[index]);
    
    scene = new Scene(card.getCardView(), 370, 520);
    stage.setScene(scene);
    stage.show();
  }
  
  public static void main(String args[]){
    launch();
  }
}
