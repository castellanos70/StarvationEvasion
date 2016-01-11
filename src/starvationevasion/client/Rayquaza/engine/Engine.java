package starvationevasion.client.Rayquaza.engine;

import javafx.fxml.Initializable;
import starvationevasion.client.Rayquaza.gui.GUIController;
import starvationevasion.client.Rayquaza.model.ModelController;
import starvationevasion.client.Rayquaza.network.NetworkHandler;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Entry point
 */
public class Engine implements Initializable
{
  ModelController modelController;//interacts with Player, Statistics, and GameState
  GUIController guiController;
  NetworkHandler networkHandler;

  public void initEngine()
  {
    modelController = new ModelController(this);
    guiController = new GUIController(this);
    networkHandler = new NetworkHandler(this);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
  }

  public void update(double deltaTime)
  {

  }
}
