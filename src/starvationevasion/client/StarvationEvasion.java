package starvationevasion.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point
 */
public class StarvationEvasion extends Application
{
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    Parent root = FXMLLoader.load(this.getClass().getResource("gui/fxml/Hand.fxml"));
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.setScene(new Scene(root, 650, 500));
    primaryStage.show();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
