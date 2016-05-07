package starvationevasion.client.GUI.DraftLayout;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;

/**
 * VisNode is the GUI element responsible for allowing the user to view and interact with the visualizer
 * This is the node which talks to all the stuff the visualizer team has done
 */
public class VisNode extends StackPane
{
  Stage primaryStage;
  boolean fullEarth=false;
  //EarthViewer earthViewer;
  Stage popUpEarth;
  Scene earthScene;
  GUI gui;
  public VisNode(Stage stage,GUI gui,DraftLayout draftLayout)
  {
    this.gui=gui;
    primaryStage = stage;
    //earthViewer=new EarthViewer(100, 250);
    //getChildren().add(earthViewer.updateMini());


    setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        switchEarthView();
      }
    });

            draftLayout.setOnKeyPressed(new EventHandler<KeyEvent>()
            {
              @Override
              public void handle(KeyEvent event)
              {
                switch (event.getCode())
                {
                  case TAB:
                    switchEarthView();
                    break;
                }
              }
            });
  }
  public synchronized void switchEarthView()
  {
    //This method will switch between Earth "modes", the modes being either Mini or Large
    //Needs to be synchronized, since it will be removing and adding nodes while the program is running
    //In my example, I am using BorderLayout and I switching between displaying the mini Earth in the
    //left pane and the large Earth in the center.
    //The large Earth responds to scrolling, the mini Earth does not and should simply rotate continuously.
    if (fullEarth)
    {
      popUpEarth.close();
      fullEarth = false;
    }
    else
    {
      popUpEarth=new Stage();
      //Scene earthScene = new Scene(earthViewer.updateFull(),700,700);
      //earthViewer.addVisStyleSheet(earthScene);

      popUpEarth.setScene(earthScene);
      popUpEarth.show();

      earthScene.setOnKeyPressed(new EventHandler<KeyEvent>()
      {
        @Override
        public void handle(KeyEvent event)
        {
          switch (event.getCode())
          {
            case TAB:
              switchEarthView();
              break;
          }
        }
      });
      fullEarth = true;
    }
  }
}
