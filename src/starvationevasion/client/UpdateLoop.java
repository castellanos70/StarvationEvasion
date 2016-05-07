package starvationevasion.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Networking.ClientTest;
import starvationevasion.common.EnumRegion;

/**
 * Update loop starts up the home screen for the client. From here the user is able
 * to launch a game and connect to a single or multiplayer. When the user clicks
 * login, it initializes a client object and tries to connect to the
 * selected server.
 * <p>
 * Moves the client over to a single-threaded game
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
  private Pane root = new Pane();
  private GridPane gridRoot = new GridPane();
  private MenuButton login = new MenuButton("  LOGIN");
  private Label usernameLabel = new Label("Username");
  private TextField username = new TextField();
  private Label passwordLabel = new Label("Password");
  private PasswordField password = new PasswordField();
  private MenuButton createUser = new MenuButton("  CREATE USER");
  private MenuButton options = new MenuButton("  OPTIONS");
  private MenuButton exit = new MenuButton("  EXIT");
  private boolean single;

  private Screen screen;
  static Rectangle2D bounds;
  private Menu menu;
  private Image background = new Image("file:assets/visResources/DIFFUSE_MAP.jpg");

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
  public void start(Stage primaryStage)
  {


    username.setPromptText("USER NAME");
    password.setPromptText("PASSWORD");
    screen = Screen.getPrimary();
    bounds = screen.getVisualBounds();

    primaryStage.setX(bounds.getMinX());
    primaryStage.setY(bounds.getMinY());
    primaryStage.setWidth(bounds.getWidth());
    primaryStage.setHeight(bounds.getHeight());

    stage = primaryStage;

    Pane root = new Pane();
    root.setPrefSize(bounds.getWidth(), bounds.getHeight());
    Image logo = new Image("file:assets/visResources/TempLogo.png");


    ImageView ivLogo = new ImageView(logo);
    ivLogo.setFitHeight(bounds.getHeight() / 7);
    ivLogo.setFitWidth(bounds.getWidth() / 7);

    ImageView imgView = new ImageView(background);
    imgView.setFitWidth(bounds.getWidth());
    imgView.setFitHeight(bounds.getHeight());

    ivLogo.setTranslateX(bounds.getWidth() / 5 * 3.25);
    ivLogo.setTranslateY(bounds.getHeight() / 5 * 3);
    root.getChildren().addAll(imgView,ivLogo);
    menu = new Menu();




    login.setOnMouseClicked((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }
      client.loginToServer(usernameLabel.getText(), passwordLabel.getText(), EnumRegion.USA_CALIFORNIA);
    });
    createUser.setOnMouseClicked((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }
      client.createUser(usernameLabel.getText(), passwordLabel.getText(), EnumRegion.USA_CALIFORNIA);
    });
    options.setOnMouseClicked(event -> {

    });
    exit.setOnMouseClicked(event -> {
      client.shutdown();
    });




    //client = new ClientTest(this, "foodgame.cs.unm.edu", 5555);
    client = new ClientTest(this, "localhost", 5555);
    this.stage = stage;
    stage.setMaximized(true);
    stage.setTitle("Login");
    stage.setOnCloseRequest((event) -> client.shutdown());
    //Sets up the initial stage
    gridRoot.setVgap(5);
    stage.setScene(new Scene(root, width, height));
    stage.setMaximized(true);
    username.setFocusTraversable(false);
    password.setFocusTraversable(false);
    gridRoot.add(username, 0, 1);
    gridRoot.add(password, 0, 2);
    gridRoot.add(login, 0, 3);
    gridRoot.add(createUser, 0, 4);
    gridRoot.add(options,0,5);
    gridRoot.add(exit,0,6);
    root.getChildren().add(gridRoot);
    gridRoot.setTranslateY(bounds.getHeight()/5*3);
    gridRoot.setTranslateX(50);
    stage.show();
    startGameLoop();
  }


  private static class MenuButton extends StackPane
  {

    private Text text;

    private MenuButton(String name)
    {
      Rectangle bg = new Rectangle(250, 30);

      text = new Text(name);
      text.setFont(text.getFont().font(20));

      text.setFill(Color.WHITE);
      text.setTranslateY(-2);
      bg.setOpacity(0.6);
      bg.setFill(Color.BLACK);
      bg.setEffect(new GaussianBlur(3.5));

      setAlignment(Pos.CENTER_LEFT);
      getChildren().addAll(bg, text);
      //text.setTranslateX(-10);
      //bg.setTranslateX(-10);

      this.setOnMouseEntered(event ->
      {
        //bg.setTranslateX(10);
        //text.setTranslateX(10);
        bg.setFill(Color.WHITE);
        text.setFill(Color.BLACK);
      });

      this.setOnMouseExited(event ->
      {
        //bg.setTranslateX(-10);
        //text.setTranslateX(-10);
        bg.setFill(Color.BLACK);
        text.setFill(Color.WHITE);
      });

      DropShadow drop = new DropShadow(50, Color.WHITE);

      drop.setInput(new Glow());

      this.setOnMousePressed(event -> setEffect(drop));
      this.setOnMouseReleased(event -> setEffect(null));
    }
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