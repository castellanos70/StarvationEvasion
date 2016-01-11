package starvationevasion.client.Rayquaza.network;

import starvationevasion.client.MegaMawile.net.AbstractClient;
import starvationevasion.client.Rayquaza.engine.Engine;

/**
 * Created by c on 1/10/2016.
 */
public class NetworkHandler
{
  Engine engine;

  public NetworkHandler(Engine engine)
  {
    this.engine = engine;
  }

  /**
   * Connects the main client to a desired server.
   * This needs to happen first because we need to know the status
   * of the server, specifically how many people are on it! if there are
   * already filled then we need to figure out how to handle AI.
   */
  public void createClient()
  {
  }

  public AbstractClient getOurClient()
  {
    return null;
  }

  /**
   * Connects the client to the server.
   */
  public void connectToServer()
  {

  }

  /**
   * Logs the player into the server.
   */
  public void loginToServer()
  {

  }

  /**
   * Disconnects a player from the server.
   */
  public void disconnectFromServer()
  {
  }
}
