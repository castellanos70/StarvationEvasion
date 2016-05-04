package starvationevasion.client;

import javafx.application.Application;
import javafx.stage.Stage;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.Networking.Client;
import starvationevasion.client.Networking.ClientTest;
import starvationevasion.client.Setup.StartAnimation;

public class Main extends Application
{
  public static void main(String args[]) throws Exception
  {
    launch(args);
  }

  @Override
  public void start(Stage arg0) throws Exception
  {
    Client client = new ClientTest(null, "localhost", 5555);
    GUI gui = new GUI(client, null);
    gui.start(new Stage());
    client.setGUI(gui);
  }
}
