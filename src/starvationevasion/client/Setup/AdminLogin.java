package starvationevasion.client.Setup;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import starvationevasion.client.Networking.Client;

/**
 * Opens a new window for admin settings
 * This will be were you can set the number of players, AIs, ect
 *
 */
public class AdminLogin extends Application
{
  private Slider numberOfPlayer;
  private Button startGame=new Button("Start");
  private Button restartGame=new Button("RestartGame");
  private Client client;
  private Stage primaryStage;
  public  AdminLogin(Client client)
  {
    this.client=client;

  }
  @Override
  public void start(Stage primaryStage) throws Exception
  {
      this.primaryStage=primaryStage;

  }
}
