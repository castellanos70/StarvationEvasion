package starvationevasion.client;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;


/**
 * Launches the tutorial video 
 * @author christopher sanchez
 * @author scott cooper
 *
 */
public class TutorialVideo extends Application
{

  private AnimationTimer timer;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    //The location of your file
    Media media = new Media(new File("assets/Media/tutorial.mp4").toURI().toString());

    MediaPlayer mediaPlayer = new MediaPlayer(media);
   
    mediaPlayer.setAutoPlay(true);
    
    MediaView mediaView = new MediaView(mediaPlayer);

    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(mediaView);
    borderPane.setStyle("-fx-background-color: Black");

    MediaControlTutorial mediaControlTutorial = new MediaControlTutorial(mediaPlayer);

    Scene scene = new Scene(borderPane, 1200, 700);

    scene.setRoot(mediaControlTutorial);
    
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

