package starvationevasion.client.Driver;

//import starvationevasion.client.Logic.Client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import starvationevasion.client.Client;
import starvationevasion.client.GUIOrig.GUI;

/**
 * Created by Dayloki on 11/14/2015.
 */

public class LandingPage extends Application
{
  private int width=300;
  private int height=250;
  private Client client;
  private ServerThread serverThread;
  GridPane root = new GridPane();
  Button singlePlayer=new Button();
  Button multiPlayer=new Button();
  Button confirm = new Button();
  Button multiConfirm = new Button();
  Label unameLabel = new Label("Username");
  TextField uname = new TextField();
  Label passwdLabel = new Label("Password");
  PasswordField passwd = new PasswordField();
  Label selectAHost = new Label("Please Enter the Name of The Host Machine");
  TextField hostName = new TextField();
  Button createUser=new Button("Create new User");
  Button seeUsers=new Button("Users");
  Button createUserWithRegion=new Button("Create with Region");
  @Override
  public void start(final Stage stage) throws Exception
  {
    stage.setTitle("Starvation Evasion");
    confirm.setText("Login");
    multiConfirm.setText("Login");
    singlePlayer.setText("Single Player");
    multiPlayer.setText("MultiPlayer");

    singlePlayer.setOnAction(actionEvent -> {
      client=new Client("Nathan",2020);
      setLogin();
    });
    multiPlayer.setOnAction(e ->
    {
      client=new Client("foodgame.cs.unm.edu",5555);
      setLogin();
    });

    confirm.setOnAction(e ->
    {
     if(!client.writeToServer("login " + uname.getText() + " " + passwd.getText()))
     {
       final Stage dialog = new Stage();
       VBox dialogVbox = new VBox(20);
       dialogVbox.getChildren().add(new Label("Wrong username/password combo"));
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
     }else{
       GUI gui=new GUI(client,null);
       Stage guiStage=new Stage();
       gui.start(guiStage);
       stage.close();
     }
    });
    seeUsers.setOnAction(event1 ->
    {
      client.writeToServer("users");
    });
    createUser.setOnAction(event ->
    {
      client.writeToServer("user_create "+uname.getText()+" "+passwd.getText());
    });
    createUserWithRegion.setOnAction(event ->
    {
      Stage regionStage=new Stage();
      RegionChooser regionChooser=new RegionChooser(client,null);
      try
      {
        regionChooser.start(regionStage);
      } catch (Exception e)
      {
        e.printStackTrace();
      }
      stage.close();
    });
    root.setAlignment(Pos.CENTER);
    root.setHgap(10);
    root.setVgap(10);
    root.add(singlePlayer, 0, 5);
    root.add(multiPlayer, 0, 6);
    stage.setScene(new Scene(root, width, height));
    stage.show();
  }
  public void setLogin(){
    root.getChildren().clear();
    root.add(unameLabel, 0, 1);
    root.add(uname, 0, 2);
    root.add(passwdLabel, 0, 3);
    root.add(passwd, 0, 4);
    root.add(confirm,1,1);
    root.add(createUser,1,2);
    root.add(seeUsers,1,3);
    root.add(createUserWithRegion,1,4);
  }
  @Override
  public void stop(){
    client.closeAll();
  }
  public class ServerThread extends Thread{
    boolean singlePlayer=true;
    public void setMultiplayer()
    {
    singlePlayer=false;
    }
    public void run()
    {
      if(singlePlayer){
        client=new Client("Nathan",2020);
      }
      else new Client("peterbilt.cs.unm.edu",2021);
    }
  }
}