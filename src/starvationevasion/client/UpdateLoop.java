package starvationevasion.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.Arrays;

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
  private Client client;
  private GridPane root = new GridPane();
  private Button login = new Button("Login");
  private Label usernameLabel = new Label("Username");
  private TextField username = new TextField();
  private Label passwordLabel = new Label("Password");
  private PasswordField password = new PasswordField();
  private Button createUser=new Button("Create User");
  @Override
  public void start(Stage stage)
  {
    stage.setTitle("Login");
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
  }
}
