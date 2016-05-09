package starvationevasion.client.GUI;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import starvationevasion.client.GUI.DraftLayout.CardNode;
import starvationevasion.client.GUI.DraftLayout.HandNode;
import starvationevasion.client.GUI.DraftLayout.TickerReel;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;

public class TestLauncher extends Application
{
  private Scene scene;
  private long last;
  private double TPS = 2;
  private String alphabet = "abcde "
      + "fghij "
      + "klmno "
      + "pqrst "
      + "uvwxyz "
      + "ABCDE "
      + "FGHIJ "
      + "KLMNO "
      + "PQRST "
      + "UVWXYZ "
      + "01234 "
      + "56789 ";
  private Random random = new Random();
  
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox pane = new VBox();
//    Clock clock = new Clock(pane, new ResizeStrategy(1, 1, 1, 1));
//    pane.getChildren().add(clock);
    
//    TickerReel reel = new TickerReel(pane);
//    pane.getChildren().add(reel);
//   
    int index = random.nextInt(EnumPolicy.values().length);
    CardNode card = new CardNode(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[index]);
    pane.getChildren().add(card);
    
//    GUI gui = new GUI();
//    gui.setAssignedRegion(EnumRegion.USA_CALIFORNIA);
//    
//    HandNode hand = new HandNode(gui);
//    EnumPolicy[] policies = new EnumPolicy[7];
//    for (int i = 0; i < 7; i++){
//      int index = random.nextInt(EnumPolicy.values().length);
//      policies[i] = EnumPolicy.values()[index];
//    }
//    
//    hand.setPolicies(policies);
//    
//    pane.getChildren().add(hand);
    
    
    scene = new Scene(pane, 600, 300);
//    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);
//    stage.initStyle(StageStyle.TRANSPARENT);
    stage.show();
    
//    last = System.nanoTime();
    
//    AnimationTimer timer = new AnimationTimer(){
//      @Override
//      public void handle(long now)
//      {
//        if ((now - last)/1_000_000_000d > 1/TPS){
//          String message = "";
//          int rand = random.nextInt(40);
//          
//          for (int i = 0; i < rand; i++){
//            message += alphabet.charAt(random.nextInt(alphabet.length()));
//          }
//          reel.addMessage(message);
//          System.out.println(message);
//          last = now;
//        }
//      }
//    };
    
//    timer.start();
  }
  
  public static void main(String args[]){
    launch();
  }
}
