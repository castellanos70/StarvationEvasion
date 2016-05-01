package starvationevasion.client.Setup;

//import starvationevasion.client.Logic.Client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Networking.DeprecatedClient;
import starvationevasion.common.EnumRegion;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the window that the users interacts with
 * Here you can select if your playing on a remote server or local,
 * Also handles login and creating new users
 */

public class LandingPage extends Application
{
  private int width = 300;
  private int height = 250;
  private final String WRONG_COMBO = "Wrong username/password combo";
  private final String NO_HOST = "Could not connect to host, try again";

  private Client client;
  private Pane root = new Pane();
  private Button singlePlayer = new Button();
  private  Button multiPlayer = new Button();
  private Button confirm = new Button("confirm");
  private Button multiConfirm = new Button();
  private Label unameLabel = new Label("Username");
  private TextField uname = new TextField();
  private Label passwdLabel = new Label("Password");
  private PasswordField passwd = new PasswordField();
  private Button createUser = new Button("Create new User");
  private Button loginAsAdmin = new Button("Login as Admin");
  private Button createUserWithRegion = new Button("Create with Region");
  private ArrayList<EnumRegion> regions = new ArrayList<>(Arrays.asList(EnumRegion.US_REGIONS));
  private ObservableList<EnumRegion> regionList = FXCollections.observableArrayList(regions);
  private ComboBox comboBox = new ComboBox(regionList);
  private Button startGame = new Button("startGame");
  private Stage stage;
  private Image background = new Image("file:assets/visResources/DIFFUSE_MAP.jpg");
  private Screen screen;
  static Rectangle2D bounds;
  private Menu menu;

  @Override
  public void start(Stage primaryStage) throws Exception
  { unameLabel.setTextFill(Color.WHITE);
    passwdLabel.setTextFill(Color.WHITE);
    screen = Screen.getPrimary();
    bounds = screen.getVisualBounds();

    primaryStage.setX(bounds.getMinX());
    primaryStage.setY(bounds.getMinY());
    primaryStage.setWidth(bounds.getWidth());
    primaryStage.setHeight(bounds.getHeight());

    Pane menuRoot = new Pane();
    menuRoot.setPrefSize(bounds.getWidth(), bounds.getHeight());
    Image logo = new Image("file:assets/visResources/TempLogo.png");


    ImageView ivLogo = new ImageView(logo);
    ivLogo.setFitHeight(bounds.getHeight() / 7);
    ivLogo.setFitWidth(bounds.getWidth() / 7);

    ImageView imgView = new ImageView(background);
    imgView.setFitWidth(bounds.getWidth());
    imgView.setFitHeight(bounds.getHeight());

    ivLogo.setTranslateX(bounds.getWidth() / 5 * 3.25);
    ivLogo.setTranslateY(bounds.getHeight() / 5 * 3);
    menu = new Menu();

    createUserWithRegion.setOnAction(event ->
    {
      openRegionChooser();
    });
    startGame.setOnAction(event ->
    {
      client.ready();
      ((DeprecatedClient)client).openGUI();
      stage.close();
    });
    confirm.setText("Login");
    multiConfirm.setText("Login");
    confirm.setOnAction(e ->
    {
      if(uname.getText().equals("")||passwd.getText().equals(""))
      {
        errorMessage(WRONG_COMBO);
      }
      else if(!client.loginToServer(uname.getText(), passwd.getText(), null))
      {
        errorMessage(WRONG_COMBO);
      }else
      {
        setSelectRegion();
        starvationevasion.client.GUI.GUI gui=new starvationevasion.client.GUI.GUI(client,null);
        Stage guiStage=new Stage();
        gui.start(guiStage);
        stage.close();
        openRegionChooser();
      }
    });
    createUser.setOnAction(event ->
    {
      if (!(uname.getText().equals("")) || !passwd.getText().equals(""))
      {
        //if(comboBox.getValue()==null)
        {
          //errorMessage("NEED REGION");
        }
        //else
        {
          client.createUser(uname.getText(), passwd.getText(), (EnumRegion) comboBox.getValue());
        }
      } else errorMessage(WRONG_COMBO);
    });
    singlePlayer.setOnAction(actionEvent -> {
      try
      {
        client = new DeprecatedClient("localhost", 5555);
        setBasicLogin();
      } catch (Exception e)
      {
        errorMessage(NO_HOST);
      }
    });

    multiPlayer.setOnAction(e ->
    {
      client = new DeprecatedClient("localhost", 5555);
      setBasicLogin();
    });

    

    loginAsAdmin.setOnAction(event1 ->
    {
      setAdminLogin();
      // client.getUsers();
    });

    createUser.setOnAction(event ->
    {
      if (!(uname.getText().equals("")) || !passwd.getText().equals(""))
      {
        //if(comboBox.getValue()==null)
        {
          //errorMessage("NEED REGION");
        }
        //else
        {
          client.createUser(uname.getText(), passwd.getText(), (EnumRegion) comboBox.getValue());
        }
      } else errorMessage(WRONG_COMBO);
    });

    root.getChildren().addAll(imgView, menu, ivLogo);

    Scene menuScene = new Scene(root);
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.setScene(menuScene);
    primaryStage.show();

  }




//    //Sets up the initial stage
//    root.setAlignment(Pos.CENTER);
//    root.setHgap(10);
//    root.setVgap(10);
//    root.add(singlePlayer, 0, 5);
//    root.add(multiPlayer, 0, 6);
//    stage.setScene(new Scene(root, width, height));
//    stage.show();
//  }


  private class Menu extends Parent
  {
    private Menu()
    {
      VBox menu0 = new VBox(1);

      menu0.setTranslateX(50);
      menu0.setTranslateY(bounds.getHeight() / 4 * 3);

      MenuButton btnSinglePlayer = new MenuButton("  SINGLE PLAYER");
      btnSinglePlayer.setOnMouseClicked(event ->
      {
        try
        {
          client = new DeprecatedClient("localhost", 5555);
          setBasicLogin();
        } catch (Exception e)
        {
          errorMessage(NO_HOST);
        }
      });

      MenuButton btnHostNetwork = new MenuButton("   JOIN SERVER");
      btnHostNetwork.setOnMouseClicked(event ->
      {
      });

      MenuButton btnJoinNetwork = new MenuButton("  MULTIPLAYER");
      btnJoinNetwork.setOnMouseClicked(event ->
      {
        client = new DeprecatedClient("localhost", 5555);
        setBasicLogin();
      });

      MenuButton btnOptions = new MenuButton("  OPTIONS");
      btnOptions.setOnMouseClicked(event ->
      {
        FadeTransition ft = new FadeTransition(Duration.seconds(.5), this);
        ft.setFromValue(1);
        ft.setToValue(0);
        // ft.setOnFinished(evt -> this.setVisible(false));
        ft.play();
      });

      MenuButton btnTutorial = new MenuButton("  Tutorial");
      btnTutorial.setOnMouseClicked(event ->
      {
        FadeTransition ft = new FadeTransition(Duration.seconds(.5), this);
        ft.setFromValue(1);
        ft.setToValue(0);
        // ft.setOnFinished(evt -> this.setVisible(false));
        ft.play();
      });

      MenuButton btnExit = new MenuButton("  QUIT");
      btnExit.setOnMouseClicked(event ->
      {
        FadeTransition ft = new FadeTransition(Duration.seconds(.5), this);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(evt -> System.exit(0));
        ft.play();

      });

      confirm.setOnAction(e ->
      {
        if(uname.getText().equals("")||passwd.getText().equals(""))
        {
          errorMessage(WRONG_COMBO);
        }
        else if(!client.loginToServer(uname.getText(), passwd.getText(), null))
        {
          errorMessage(WRONG_COMBO);
        }else
        {
          setSelectRegion();
          starvationevasion.client.GUI.GUI gui=new starvationevasion.client.GUI.GUI(client,null);
          Stage guiStage=new Stage();
          gui.start(guiStage);
          stage.close();
          openRegionChooser();
        }
      });

      loginAsAdmin.setOnAction(event1 ->
      {
        setAdminLogin();
        // client.getUsers();
      });

      createUser.setOnAction(event ->
      {
        if (!(uname.getText().equals("")) || !passwd.getText().equals(""))
        {
          if (comboBox.getValue() == null)
          {
            errorMessage("NEED REGION");
          } else
          {
            client.createUser(uname.getText(), passwd.getText(), (EnumRegion) comboBox.getValue());
          }
        } else errorMessage(WRONG_COMBO);
      });

      createUserWithRegion.setOnAction(event ->
      {
        openRegionChooser();
      });
      startGame.setOnAction(event ->
      {
        client.ready();
        ((DeprecatedClient)client).openGUI();
        stage.close();
      });

      menu0.getChildren().addAll(btnSinglePlayer, btnJoinNetwork, btnHostNetwork, btnOptions, btnTutorial, btnExit);
      Rectangle bg = new Rectangle(bounds.getWidth(), bounds.getHeight());
      bg.setFill(Color.GRAY);
      bg.setOpacity(0.15);

      getChildren().addAll(bg, menu0);
    }
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
      setRotate(-0.5);
      getChildren().addAll(bg, text);
      text.setTranslateX(-10);
      bg.setTranslateX(-10);

      this.setOnMouseEntered(event ->
      {
        bg.setTranslateX(10);
        text.setTranslateX(10);
        bg.setFill(Color.WHITE);
        text.setFill(Color.BLACK);
      });

      this.setOnMouseExited(event ->
      {
        bg.setTranslateX(-10);
        text.setTranslateX(-10);
        bg.setFill(Color.BLACK);
        text.setFill(Color.WHITE);
      });

      DropShadow drop = new DropShadow(50, Color.WHITE);

      drop.setInput(new Glow());

      this.setOnMousePressed(event -> setEffect(drop));
      this.setOnMouseReleased(event -> setEffect(null));
    }
  }


  private void openRegionChooser()
  {
    Stage regionStage = new Stage();
    RegionChooser regionChooser = new RegionChooser(client, null);
    try
    {
      regionChooser.start(regionStage);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
   // stage.close();
  }

  private void setLogin()
  {

    root.getChildren().remove(menu);
    GridPane temp = new GridPane();
    temp.add(unameLabel, 0, 1);
    temp.add(uname, 0, 2);
    temp.add(passwdLabel, 0, 3);
    temp.add(passwd, 0, 4);
    temp.add(confirm, 1, 1);
    temp.add(createUser, 1, 2);
    temp.add(loginAsAdmin, 1, 3);
    temp.add(createUserWithRegion, 1, 4);
    temp.add(comboBox, 1, 5);
    temp.setTranslateY((int) bounds.getHeight() / 8 * 3);
    temp.setTranslateX((int) bounds.getWidth() / 8 * 3);
    //root.getChildren().clear();
    root.getChildren().add(temp);
  }

  private void setBasicLogin()
  {
    root.getChildren().remove(menu);
    GridPane temp = new GridPane();
    temp.getChildren().clear();
    temp.add(unameLabel, 0, 1);
    temp.add(uname, 0, 2);
    temp.add(passwdLabel, 0, 3);
    temp.add(passwd, 0, 4);
    temp.add(confirm, 1, 1);
    temp.add(createUser, 1, 2);
    temp.setTranslateY((int) bounds.getHeight() / 8 * 3);
    temp.setTranslateX((int) bounds.getWidth() / 8 * 3);
    temp.add(loginAsAdmin, 1, 3);
    temp.add(createUserWithRegion, 1, 4);
    temp.add(comboBox, 1, 5);
    //root.getChildren().clear();
    root.getChildren().add(temp);

  }

  private Slider numberOfPlayers;

  private void setAdminLogin()
  {

  }

  private void setSelectRegion()
  {
    //regions = client.getAvailableRegion();
    regionList = FXCollections.observableArrayList(regions);
    comboBox = new ComboBox(regionList);
    GridPane temp = new GridPane();
    //root.getChildren().clear();
    temp.add(comboBox,0,0);
    temp.add(startGame, 0, 1);
    root.getChildren().addAll(temp,startGame);


  }

  @Override
  public void stop(){
    client.shutdown();
  }

  private void errorMessage(String message)
  {
    final Stage dialog = new Stage();
    VBox dialogVbox = new VBox(20);
    dialogVbox.getChildren().add(new Label(message));
    Scene dialogScene = new Scene(dialogVbox, 300, 80);
    dialog.setScene(dialogScene);
    dialog.setTitle("ERROR");
    dialog.show();
    Button btn = new Button();
    btn.setText("Ok");
    dialogVbox.getChildren().addAll(btn);
    btn.setOnAction(event ->
    {
      dialog.close();
    });
  }
}