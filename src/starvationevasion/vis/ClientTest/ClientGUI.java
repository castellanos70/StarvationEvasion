package starvationevasion.vis.ClientTest;

//Imports are listed in full to show what's being used
//could just import javafx.*
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Created by Tess Daughton on 11/15/15.
 * Class for testing purposes - simulate what a client's GUI might be like and ensure that our application
 * is function properly within their GUI
 */


public class ClientGUI extends Application implements EventHandler<KeyEvent> {

  //I made my own class called customLayout, it doesn't matter which layout manager you choose to use however
  private CustomLayout customLayout;
  private Scene scene;

  @Override
  public void start(Stage primaryStage) throws Exception{
    primaryStage.setWidth(800);
    primaryStage.setHeight(600);

    customLayout = new CustomLayout();
    //initialize your scene with the layout manager you have chosen
    scene = new Scene(customLayout);
    //add an EventHandler to "toggle" between Earth view modes , mini, or full
    //example of this method "switchEarthView" in CustomLayout class
    scene.setOnKeyPressed(this);
    scene.setRoot(customLayout);

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
        customLayout.switchEarthView();
      }
    }
  }


  public static void main(String[] args) {
    launch(args);
  }
}
