package starvationevasion.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Networking.ClientTest;
import starvationevasion.common.EnumRegion;

/**
 * Since the AI has already been converted to use the new CommModule, this is a proof
 * of concept to show that the client can use the same module.
 *
 * This is also a first attempt at moving the client over to a single-threaded game
 * loop (built on the JavaFX thread).
 */
public class UpdateLoop extends Application
{
  private int width = 300;
  private int height = 250;
  private Stage stage;
  private long millisecondTimeStamp = System.currentTimeMillis();
  private double deltaSeconds;
  private Client client;
  private GridPane root = new GridPane();
  private Button login = new Button("Login");
  private Label usernameLabel = new Label("Username");
  private TextField username = new TextField();
  private Label passwordLabel = new Label("Password");
  private PasswordField password = new PasswordField();
  private Button createUser=new Button("Create User");

  public void notifyOfSuccessfulLogin()
  {
    System.out.println("Starting game . . .");
    GUI gui = new GUI(client, null);
    gui.start(new Stage());
    client.setGUI(gui);
    client.ready(); // Send a ready response to the server
    stage.close();
  }

  @Override
  public void start(Stage stage)
  {
    login.setOnAction((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }
      client.loginToServer(usernameLabel.getText(), passwordLabel.getText(), EnumRegion.ARCTIC_AMERICA);
    });
    createUser.setOnAction((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }
      client.createUser(usernameLabel.getText(), passwordLabel.getText(), EnumRegion.ARCTIC_AMERICA);
    });

    client = new ClientTest(this, "foodgame.cs.unm.edu", 5555);
    this.stage = stage;
    stage.setTitle("Login");
    stage.setOnCloseRequest((event) -> client.shutdown());
    //Sets up the initial stage
    root.setAlignment(Pos.CENTER);
    root.setHgap(10);
    root.setVgap(10);
    stage.setScene(new Scene(root, width, height));
    root.add(usernameLabel, 0, 0);
    root.add(username, 0, 1);
    root.add(passwordLabel, 0, 2);
    root.add(password, 0, 3);
    root.add(login,0,4);
    root.add(createUser,1,4);
    stage.show();
    startGameLoop();
  }

  private void startGameLoop()
  {
    new AnimationTimer()
    {
      @Override
      public void handle(long time)
      {
        deltaSeconds = (System.currentTimeMillis() - millisecondTimeStamp) / 1000.0;
        millisecondTimeStamp = System.currentTimeMillis();
        client.update(deltaSeconds);
        if (!client.isRunning()) stop();
      }

      public void stop()
      {
        super.stop();
        stage.close();
      }

    }.start();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
