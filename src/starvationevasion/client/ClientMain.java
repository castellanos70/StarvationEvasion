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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Networking.ClientTest;
import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;
/**
 * Update loop starts up the home screen for the client. From here the user is able
 * to launch a game and connect to a single or multiplayer. When the user clicks
 * buttonLogin, it initializes a client object and tries to connect to the
 * selected server.
 * <p>
 * Moves the client over to a single-threaded game
 * loop (built on the JavaFX thread).
 */
public class ClientMain extends Application
{
  private int width  = 300;
  private int height = 250;
  private Stage stage;
  private long millisecondTimeStamp = System.currentTimeMillis();
  private double deltaSeconds;
  private Client client;
  private Pane root = new Pane();
  private GridPane gridRoot = new GridPane();
  private TextField editServer = new TextField("foodgame.cs.unm.edu");
  private MenuButton buttonConnect = new MenuButton("  CONNECT");
  private MenuButton buttonLogin = new MenuButton("  LOGIN");
  private Label labelUsername = new Label("Username");
  private TextField editUsername = new TextField();
  private Label labelPassword = new Label("Password");
  private PasswordField editPassword = new PasswordField();
  private MenuButton buttonCreateUser = new MenuButton("  CREATE USER");
  private MenuButton credits = new MenuButton("  CREDITS");
  private MenuButton tutorial = new MenuButton("  TUTORIAL");
  private MenuButton exit = new MenuButton("  EXIT");

  private Screen screen;
  static Rectangle2D bounds;
  private Menu menu;
  private Image background = new Image("file:assets/LoginBackground.png");
  private AnimationTimer timer;
  private Scene scene2;
  private Stage newStage;
  private FlowPane pane2;
  private Label labelForScene2;
  private AudioClip clip;

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

    IntroVideo video = new IntroVideo();

    try
    {
      video.start(primaryStage);
    } catch (Exception e)
    {
      e.printStackTrace();
    }

    startTimer(primaryStage);
  }


  /**
   * Menu button attributes are in this method
   */
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

      this.setOnMouseEntered(event ->
      {
        bg.setFill(Color.WHITE);
        text.setFill(Color.BLACK);
      });

      this.setOnMouseExited(event ->
      {
        bg.setFill(Color.BLACK);
        text.setFill(Color.WHITE);
      });

      DropShadow drop = new DropShadow(50, Color.WHITE);

      drop.setInput(new Glow());

      this.setOnMousePressed(event -> setEffect(drop));
      this.setOnMouseReleased(event -> setEffect(null));
    }
  }

  /**
   * Method starts the audio file for background music
   */
  private void playMusic()
  {
    try
    {
      // Maxwell Sanchez: This resource doesn't appear to exist any more, and attempting to play it creates a NullPointerException.
      /* final java.net.URL resource = getClass().getResource("WhenThe3574792685754392ffffffffffffff.wav");
      clip = new AudioClip(resource.toString());
      clip.play(); */
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  private void pauseMusic()
  {
    try
    {
      // Maxwell Sanchez: Also caused a NullPointerException because the clip is not running.
      // clip.stop();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  /**
   * This method allows the first intro video to play without
   * locking the window up. Once a time limit or the skip
   * button has been invoked, the primary Stage then shows
   * the start menu (buttonLogin page)
   *
   * @param primaryStage
   */
  private void startTimer(Stage primaryStage)
  {
    timer = new AnimationTimer()
    {
      int seconds    = 150;
      long startTime = System.currentTimeMillis();
      long endTime   = System.currentTimeMillis() + (seconds * 100);
      long temp      = startTime + 150;

      @Override
      public void handle(long l)
      {
        temp++;

        if(MediaControl.getFlag_To_ExitVideo()) showStartMenu(primaryStage);
        else if(temp > endTime)
        {
          showStartMenu(primaryStage);
        }
      }
    };

    timer.start();
  }

  /**
   * When invoked, this method will show the start menu and
   * all of the elements on the page, such as user buttonLogin,
   * editPassword, create user etc.
   *
   * @param primaryStage
   */
  public void showStartMenu(Stage primaryStage)
  {
    timer.stop();

    editUsername.setPromptText("USER NAME");
    editPassword.setPromptText("PASSWORD");
    screen = Screen.getPrimary();
    bounds = screen.getVisualBounds();

    primaryStage.setX(bounds.getMinX());
    primaryStage.setY(bounds.getMinY());
    primaryStage.setWidth(bounds.getWidth());
    primaryStage.setHeight(bounds.getHeight());

    stage = primaryStage;

    Pane root = new Pane();
    root.setPrefSize(bounds.getWidth(), bounds.getHeight());


    ImageView imgView = new ImageView(background);
    imgView.setFitWidth(bounds.getWidth());
    imgView.setFitHeight(bounds.getHeight());

    root.getChildren().addAll(imgView);
    menu = new Menu();

    buttonConnect.setOnMouseClicked((event) ->
      {
    //client = new ClientTest(this, "foodgame.cs.unm.edu", 5555);
    //client = new ClientTest(this, "localhost", 5555);
    String host = editServer.getText();
    client = new ClientTest(this, host, Constant.SERVER_PORT);
      });


    buttonLogin.setOnMouseClicked((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }

        client.loginToServer(editUsername.getText(),editPassword.getText(), EnumRegion.USA_CALIFORNIA);
    });

    buttonCreateUser.setOnMouseClicked((event) ->
    {
      if (!client.isRunning())
      {
        System.err.println("ERROR: Not connected to server");
        return;
      }

      System.out.println("ClientMain.buttonCreateUser.setOnMouseClicked:" +
        "editUsername.getText()="+editUsername.getText());



      client.createUser(editUsername.getText(), editPassword.getText(), EnumRegion.USA_CALIFORNIA);
    });

    credits.setOnMouseClicked(event -> {

    });
    exit.setOnMouseClicked(event -> {
      client.shutdown();
    });

    credits.setOnMouseClicked((event) ->
    {
      newStage.showAndWait();
    });

    tutorial.setOnMouseClicked((event) ->
    {

      pauseMusic();

      TutorialVideo video = new TutorialVideo();
      FlowPane pane  = new FlowPane();
      Scene scene = new Scene(pane, 1300, 2000);


      //make another stage for scene2
      Stage newStage = new Stage();
      newStage.setScene(scene);

      //tell stage it is meant to pop-up (Modal)
      newStage.initModality(Modality.APPLICATION_MODAL);
      newStage.setTitle("Tutorial");
      //newStage.addEventHandler(eventType, eventHandler);

      try
      {
        video.start(newStage);
      } catch (Exception e)
      {
        e.printStackTrace();
      }

    });





    this.stage = stage;
    stage.setMaximized(true);
    stage.setTitle("Login");
    stage.setOnCloseRequest((event) -> client.shutdown());
    //Sets up the initial stage
    gridRoot.setVgap(5);
    stage.setScene(new Scene(root, width, height));
    stage.setMaximized(true);


    //The following code is for the credits

    labelForScene2 = new Label(showCreditText());

    pane2  = new FlowPane();
    scene2 = new Scene(pane2, 350, 500);

    pane2.getChildren().addAll(labelForScene2);

    //make another stage for scene2
    newStage = new Stage();
    newStage.setScene(scene2);

    //tell stage it is meant to pop-up (Modal)
    newStage.initModality(Modality.APPLICATION_MODAL);
    newStage.setTitle("Credits");


    editUsername.setFocusTraversable(false);
    editPassword.setFocusTraversable(false);
    gridRoot.add(editServer, 0, 1); gridRoot.add(buttonConnect, 1, 1);
    gridRoot.add(editUsername, 0, 2);
    gridRoot.add(editPassword, 0, 3);
    gridRoot.add(buttonLogin, 0, 4);
    gridRoot.add(buttonCreateUser, 0, 5);
    gridRoot.add(tutorial,0,6);
    gridRoot.add(credits,0,7);
    gridRoot.add(exit,0,8);
    root.getChildren().add(gridRoot);
    gridRoot.setTranslateY(bounds.getHeight()/5*3);
    gridRoot.setTranslateX(50);

    stage.show();
    startGameLoop();

    playMusic();
  }



  /**
   * This string gets passed into one of the button labels
   * to show the credits.
   *
   * @return A string with all of the team members
   */
  public String showCreditText(){

    String text = "Music:"
      +"\n \"When the Sun Goes Down\""
      +"\n produced by DJ MAC n' Cheese"

      +"\n\n\t\t\t\t\t   Developers"
      +"\n\n Project Lead:"
      +"\n Joel Castellanos"
      +"\n\n Intro Screen:"
      +"\n Christopher Sanchez, Isaiah Waltemire, Miri Ryu, Scott Cooper,"
      +"\n\n Data Collection, Data Preprocessing:"
      +"\n Chris Wu, James Green, Rob Spidle, Tommy Manzanares"
      +"\n\n Simulator:"
      +"\n John Clark, Elijah Griffo-Black, Jesus Lopez"
      +"\n\n Policy Card Development:"
      +"\n Atle Olson, Michael Martin, Stephen Sagartz"
      +"\n\n Server and Client Communication:"
      +"\n Javier Chavez, Justin Hall, George Boujaoude"
      +"\n\n Client User Interface (GUI):"
      +"\n Ben Matthews, Brian Downing, Christian Seely, Nate Gonzales"
      +"\n\n Client Artificial Intelligence:"
      +"\n Antonio Griego, Ederin Igharoro, Jeff McCall"
      +"\n\n Testing and Integration:"
      +"\n Max Sanchez";

    return text;
  }

  /***************************************************************************************/

  private void startGameLoop()
  {
    new AnimationTimer()
    {
      @Override
      public void handle(long time)
      {
        deltaSeconds = (System.currentTimeMillis() - millisecondTimeStamp) / 1000.0;
        millisecondTimeStamp = System.currentTimeMillis();
        if (client != null)
        { client.update(deltaSeconds);
          if (!client.isRunning()) stop();
        }
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
