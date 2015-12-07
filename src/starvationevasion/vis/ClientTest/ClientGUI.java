package starvationevasion.vis.ClientTest;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import starvationevasion.vis.controller.EarthViewer;

/**
 * Created by Tess Daughton on 11/15/15.
 * Class for testing purposes - simulate what a client's GUI might be like and ensure that our application
 * is function properly within their GUI
 */

public class ClientGUI extends Application implements EventHandler<KeyEvent> {

  private CustomLayout customLayout;
  private Scene scene;
  private Stage earthStage = new Stage();
  private final int MINI_EARTH_RADIUS = 100;
  private final int LARGE_EARTH_RADIUS = 300;
  private boolean fullEarthMode = false;
  private EarthViewer earthViewer = new EarthViewer(MINI_EARTH_RADIUS, LARGE_EARTH_RADIUS);


  @Override
  public void start(Stage primaryStage) throws Exception{
    primaryStage.setWidth(800);
    primaryStage.setHeight(600);

    customLayout = new CustomLayout(earthViewer);
    scene = new Scene(customLayout);
    scene.setOnKeyPressed(this);
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void handle(KeyEvent event)
  {
    switch(event.getCode())
    {
      //I have the Earth view toggling on TAB, but I'm not sure if this is a requirement
      //or if you can use a button or another key of your choice
      case TAB:
      {
        if(fullEarthMode)
        {
          fullEarthMode=false;
          earthStage.close();
        }

        else
        {
          fullEarthMode=true;
          Scene earthScene = new Scene(earthViewer.updateFull(),800,850);
          earthViewer.addVisStyleSheet(earthScene);
          earthScene.setOnKeyPressed(this);
          earthStage.setScene(earthScene);
          earthStage.show();
        }
      }
    }
  }


  public static void main(String[] args) {
    launch(args);
  }
}
