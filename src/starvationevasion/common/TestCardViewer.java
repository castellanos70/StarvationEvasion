package starvationevasion.common;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import starvationevasion.client.GUI.DraftLayout.CardView;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.EnumPolicy;

public class TestCardViewer extends Application
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
    HBox pane = new HBox();
//    Clock clock = new Clock(pane, new ResizeStrategy(1, 1, 1, 1));
//    pane.getChildren().add(clock);
    
//    TickerReel reel = new TickerReel(pane);
//    pane.getChildren().add(reel);
//   
    int index = random.nextInt(EnumPolicy.values().length);
//    CardNode card = new CardNode(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[index]);
    CardView card = new CardView(EnumRegion.USA_CALIFORNIA, EnumPolicy.values()[index]);
//    pane.getChildren().add(card);
    
//    GUI gui = new GUI();
//    gui.setAssignedRegion(EnumRegion.USA_CALIFORNIA);
//    
//    HandNode hand = new HandNode(gui);
//    EnumPolicy[] card = new EnumPolicy[7];
//    for (int i = 0; i < 7; i++){
//      int index = random.nextInt(EnumPolicy.values().length);
//      card[i] = EnumPolicy.values()[index];
//    }
//    
//    hand.setPolicies(card);
//    
//    pane.getChildren().add(hand);
    
    
    scene = new Scene(card.getCardView(), 370, 520);
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
