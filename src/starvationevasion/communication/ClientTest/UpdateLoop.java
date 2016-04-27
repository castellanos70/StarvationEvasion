package starvationevasion.communication.ClientTest;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import starvationevasion.common.EnumRegion;

public class UpdateLoop extends Application
{
  private int width=300;
  private int height=250;
  private final String WRONG_COMBO="Wrong username/password combo";
  private final String NO_HOST="Could not connect to host, try again";

  private Client client;
  private GridPane root = new GridPane();
  private Button login = new Button("Login");
  private Label usernameLabel = new Label("Username");
  private TextField usernameTextField = new TextField();
  private Label passwordLabel = new Label("Password");
  private PasswordField passwordTextField = new PasswordField();

  public void notifyOfLoginSuccess()
  {
    new AnimationTimer()
    {

      /**
       * This method needs to be overridden by extending classes. It is going to
       * be called in every frame while the {@code AnimationTimer} is active.
       *
       * @param now The timestamp of the current frame given in nanoseconds. This
       *            value will be the same for all {@code AnimationTimers} called
       *            during one frame.
       */
      @Override
      public void handle(long now)
      {
        System.out.println("I'm running!!!!!!!!!!");
        client.update();
      }
    }.start();
  }

  /**
   * The main entry point for all JavaFX applications.
   * The start method is called after the init method has returned,
   * and after the system is ready for the application to begin running.
   * <p>
   * <p>
   * NOTE: This method is called on the JavaFX Application Thread.
   * </p>
   *
   * @param stage the primary stage for this application, onto which
   *                     the application scene can be set. The primary stage will be embedded in
   *                     the browser if the application was launched as an applet.
   *                     Applications may create other stages, if needed, but they will not be
   *                     primary stages and will not be embedded in the browser.
   */
  @Override
  public void start(Stage stage) throws Exception
  {
    stage.setTitle("Login");
    stage.setOnCloseRequest(this::closeWindow);
    client = new Client(this, "localhost", 5555);
    if (!client.isRunning()) System.exit(-1);
    login.setOnAction((event) ->
    {
      client.login(usernameLabel.getText(), passwordLabel.getText(), EnumRegion.ARCTIC_AMERICA);
    });
    root.setAlignment(Pos.CENTER);
    root.setHgap(10);
    root.setVgap(10);
    root.getChildren().clear();
    root.add(usernameLabel, 0, 1);
    root.add(usernameTextField, 0, 2);
    root.add(passwordLabel, 0, 3);
    root.add(passwordTextField, 0, 4);
    root.add(login,0,5);
    stage.setScene(new Scene(root, width, height));
    stage.show();
  }

  private void closeWindow(WindowEvent e)
  {
    client.shutdown();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
