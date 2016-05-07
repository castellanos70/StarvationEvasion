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
import starvationevasion.client.GUI.DraftLayout.TickerReel;

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
    
    TickerReel reel = new TickerReel(pane);
    pane.getChildren().add(reel);
    
    scene = new Scene(pane, 1377, 80);
    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.show();
    
    last = System.nanoTime();
    
    AnimationTimer timer = new AnimationTimer(){
      @Override
      public void handle(long now)
      {
        if ((now - last)/1_000_000_000d > 1/TPS){
          String message = "";
          int rand = random.nextInt(40);
          
          for (int i = 0; i < rand; i++){
            message += alphabet.charAt(random.nextInt(alphabet.length()));
          }
          reel.addMessage(message);
          System.out.println(message);
          last = now;
        }
      }
    };
    
    timer.start();
  }
  
  public static void main(String args[]){
    launch();
  }
}
