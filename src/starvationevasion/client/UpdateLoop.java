package starvationevasion.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
  private Client client;
  GridPane root = new GridPane();
  Button singlePlayer=new Button();
  Button multiPlayer=new Button();
  Button confirm = new Button();
  Button multiConfirm = new Button();
  Label unameLabel = new Label("Username");
  TextField uname = new TextField();
  Label passwdLabel = new Label("Password");
  PasswordField passwd = new PasswordField();
  Button createUser=new Button("Create new User");
  Button loginAsAdmin =new Button("Login as Admin");
  Button createUserWithRegion=new Button("Create with Region");
  ArrayList<EnumRegion> regions=new ArrayList<>(Arrays.asList(EnumRegion.US_REGIONS));
  ObservableList<EnumRegion> regionList= FXCollections.observableArrayList(regions);
  ComboBox comboBox=new ComboBox(regionList);
  @Override
  public void start(Stage stage)
  {
    stage.setTitle("Login");
  }
}
