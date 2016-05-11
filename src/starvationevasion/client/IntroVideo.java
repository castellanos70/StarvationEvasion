package starvationevasion.client;


import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


/**
 * Launches the intro video right before the start menu
 * @author christopher sanchez
 *
 */
public class IntroVideo extends Application
{

  private AnimationTimer timer;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    //The location of your file
    Media media = new Media(new File("assets/Media/Intro.mp4").toURI().toString());

    MediaPlayer mediaPlayer = new MediaPlayer(media);
   
    mediaPlayer.setAutoPlay(true);
    
    MediaView mediaView = new MediaView(mediaPlayer);

    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(mediaView);

    borderPane.setStyle("-fx-background-color: Black");

    //Add controls, mainly the "skip intro" option
    MediaControl mediaControl = new MediaControl(mediaPlayer);

    Scene scene = new Scene(borderPane, 850, 520);

    scene.setRoot(mediaControl);
    
    primaryStage.setTitle("Starvation Evasion!");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

