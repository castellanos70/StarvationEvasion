package starvationevasion.client.MegaMawile;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import starvationevasion.client.MegaMawile.controller.GameEngine;
import starvationevasion.vis.controller.EarthViewer;


/**
 * The entry point for the team Mega Mawile implementation of the StarvationEvasion client.
 *
 * @author Javier Chavez
 * @author Keira Haskins
 * @author Evan King
 * @author Mark Mitchell
 * @author Christopher Wu
 */
public class MainClient_MegaMawile extends Application
{
  private GameEngine gameEngine;
  private boolean running = true;
  private EarthViewer earth;
  private boolean headless = true;

  /**
   * The entry point for the game.
   *
   * @param args  command line arguments.
   */
  public static void main(String[] args) { launch(args); }

  /**
   * Returns a boolean, indicating whether or not the gameEngine is running. Does not correlate to the gameEngine process,
   * but instead whether or not the engine is actively being updated.
   *
   * @return a boolean, <code>true</code> if the gameEngine is running, <code>false</code> otherwise.
   */
  public boolean isRunning() { return running; }

  /**
   * Sets the current running state of the gameEngine.
   *
   * @param running  the running state of the gameEngine, <code>true</code> if running.
   */
  public void setRunning(boolean running) { this.running = running; }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    // TODO update this GUI file
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("gui/GUI.fxml"));
    GridPane pane = loader.load();

    gameEngine = loader.getController();

    /*
    String s = getParameters().getNamed().get("headless");
    if (s != null && s.equalsIgnoreCase("true"))
    {
      headless = true;
      String port = getParameters().getNamed().get("port");
      String host = getParameters().getNamed().get("host");
      gameEngine.setOptions(port, host);
    }

    String usr = getParameters().getNamed().get("SEUSERNAME");

    if(usr != null)
    {
      headless = true;
      gameEngine.setState(headless);

      String pass = getParameters().getNamed().get("SEPASSWORD");
      gameEngine.getPlayer().setUsername(usr);
      gameEngine.getPlayer().setPassword(pass);

      String port = getParameters().getNamed().get("SEPORT");
      String host = getParameters().getNamed().get("SEHOSTNAME");
      gameEngine.setOptions(port, host);
    }
    else
    {
      headless = false;
      gameEngine.setState(headless);
    }*/

    headless = false;
    gameEngine.setState(headless);

    if (!headless)
    {
//      earth = new EarthViewer(100,200);
//      gameEngine.setEarthViewer(earth);
//      pane.add(earth.updateMini(), 4, 0);
//      Scene newScene = new Scene(pane);
//      newScene.setOnKeyPressed(event->{gameEngine.handle(event);});
//      primaryStage.setScene(newScene);
      primaryStage.setScene(new Scene(pane));
      primaryStage.setTitle("Starvation Evasion");
      primaryStage.show();

      gameEngine.showWelcome();
      //gameEngine.setOptions("27015", "localhost");
    }

    run();

    primaryStage.setOnCloseRequest(event -> setRunning(false));
  }

  //Creates a new Thread to run the Game update loop in.
  private void run()
  {
    Task task = new Task<Class<Void>>()
    {
      @Override
      public Class<Void> call() throws Exception
      {
        long delta = 0l;
        while (running)
        {
          long lastTime = System.nanoTime();
          final long finalDelta = delta;

          Platform.runLater(() -> gameEngine.update(finalDelta / 1000000000.0f));

          delta = System.nanoTime() - lastTime;
          if (delta < 20000000L)
          {
            try
            {
              Thread.sleep((20000000L - delta) / 1000000L);
            }
            catch (Exception ignored)
            {
            }
          }
        }
        return Void.TYPE;
      }
    };
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }
}
