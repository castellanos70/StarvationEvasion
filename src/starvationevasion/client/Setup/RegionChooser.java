package starvationevasion.client.Setup;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.GUI.DraftLayout.map.ConfigPhaseMapController;
import starvationevasion.client.GUI.DraftLayout.map.Map;
import starvationevasion.client.GUI.DraftLayout.map.MapController;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Logic.LocalDataContainer;

/**
 * Created by Dayloki on 11/15/2015.
 */
public class RegionChooser extends Application
{
  LocalDataContainer localDataContainer;
  Client client;

  public final Map map;

  public static Text selectionText;
  public Stage primaryStage;


  public RegionChooser(Client client, LocalDataContainer localDataContainer)
  {
    super();
    this.localDataContainer = localDataContainer;
    this.client = client;
    this.map = new Map();

  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Choose a Region");
    GridPane gridPane = new GridPane();
    VBox login=new VBox();
    HBox userName=new HBox();
    Label unameLabel = new Label("Username");
    TextField uname = new TextField();
    userName.getChildren().addAll(unameLabel,uname);
    HBox password=new HBox();
    Label passwdLabel = new Label("Password");
    PasswordField passwd = new PasswordField();
    password.getChildren().addAll(passwdLabel,passwd);
    Button createUser=new Button("Create new User");
    login.getChildren().addAll(userName,password,createUser);
    gridPane.add(login,0,1,1,1);

    gridPane.setPrefSize(1000, 200);
    gridPane.setAlignment(Pos.CENTER);


    selectionText = new Text();
    selectionText.setTranslateX(50);
    selectionText.setTranslateY(-100);
    selectionText.setWrappingWidth(200);
    selectionText.setTextAlignment(TextAlignment.CENTER);
    selectionText.setText("Select a Region");
    selectionText.setStyle("-fx-font-size: 30;");
    gridPane.add(selectionText, 0, 0, 1, 1);


    MapController.setCurrentController(ConfigPhaseMapController.class);
    Node mapNode = map.getConfigMapNode(client);
    gridPane.add(mapNode, 1, 0, 1, 1);

    Scene scene = new Scene(gridPane);

    Button button = new Button();
    button.setText("Lock in!");
    button.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        GUI gui=new GUI(client,null);
        Stage guiStage=new Stage();
        gui.start(guiStage);
        primaryStage.close();
        button.setDisable(true);
      }
    });

    StackPane buttonPane = new StackPane();
    buttonPane.getChildren().add(button);
    gridPane.add(buttonPane, 0, 2, 1, 1);

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
