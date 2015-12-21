package starvationevasion.client.Aegislash.Driver;

import Logic.Client;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import starvationevasion.server.Server;

/**
 * Created by Dayloki on 11/14/2015.
 */
public class LandingPage extends Application
{
  private int width=300;
  private int height=250;
  private Client client;

  @Override
  public void start(final Stage stage) throws Exception
  {
    GridPane root = new GridPane();
    stage.setTitle("Starvation Evasion");
    Button singlePlayer=new Button();
    Button multiPlayer=new Button();
    Button confirm = new Button();
    Button multiConfirm = new Button();
    confirm.setText("Confirm");
    multiConfirm.setText("Confirm");
    Label unameLabel = new Label("Username");
    TextField uname = new TextField();
    Label passwdLabel = new Label("Password");
    PasswordField passwd = new PasswordField();
    Label selectAHost = new Label("Please Enter the Name of The Host Machine");
    TextField hostName = new TextField();
    singlePlayer.setText("Single Player");
    multiPlayer.setText("MultiPlayer");
    client = new Client(false);

    singlePlayer.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent actionEvent)
      {
        //TODO:Handle Server Stuff
        Thread serverThread = new Thread(new Task<Void>()
        {
          String seperator = System.getProperty("file.separator");
          String pathToJar = System.getProperty("user.dir") + seperator + "AI.jar";
          String path = System.getProperty("java.home") + seperator + "bin" + seperator + "java";
          public Void call()
          {
            String[] command = {"example_password_file.tsv",
              path, "-jar", pathToJar};
            Server.main(command);
            return null;
          }
        });
        serverThread.start();
        client.init();
        new Thread(client.listener).start();
        root.add(unameLabel, 0, 1);
        root.add(uname, 0, 2);
        root.add(passwdLabel, 0, 3);
        root.add(passwd, 0, 4);
        root.add(confirm,0,5);
        root.getChildren().remove(singlePlayer);
        root.getChildren().remove(multiPlayer);
      }
    });
    multiPlayer.setOnAction(e ->
    {
        root.add(selectAHost, 0 , 1);
        root.add(hostName, 0, 2);
        root.add(multiConfirm, 0 , 3);
        root.getChildren().remove(singlePlayer);
        root.getChildren().remove(multiPlayer);
    });
    confirm.setOnAction(e ->
    {
      client.sendLogin(uname.getText(), passwd.getText());
      while (!client.didServerReceiveLogin());
      //Success!
      if (client.isLoginSuccessful())
      {
        client.startSelection();
        stage.close();
      }
      //Failure :(
      else
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
        client.setReceivedLoginAttempt(false);

      }
    });
    root.setAlignment(Pos.CENTER);
    root.setHgap(10);
    root.setVgap(10);

    root.add(singlePlayer, 0, 5);
    root.add(multiPlayer, 0, 6);
    stage.setScene(new Scene(root, width, height));
    stage.show();
  }
}
