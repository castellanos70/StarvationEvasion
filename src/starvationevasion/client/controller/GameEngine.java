package starvationevasion.client.controller;

import javafx.fxml.Initializable;
import starvationevasion.client.model.GameStateData;
import starvationevasion.client.model.Player;
import starvationevasion.client.net.NetworkHandler;
import starvationevasion.client.model.GameOptions;


import java.awt.*;
import java.net.URL;
import java.util.*;

import static starvationevasion.client.net.common.NetworkStatus.TRY;
import static starvationevasion.client.net.common.NetworkStatus.TRYING;
import static starvationevasion.client.net.common.NetworkStatus.DISCONNECT;

/**
 * Main Controller of the application. This is where most objects are initialized
 * and available for the the entire application.
 */
public class GameEngine implements GameController, Initializable
{
  //private final Player player;
  private AbstractPlayerController playerController;

  private NetworkHandler network;

  // private final Renderer simulationVisualization;
  // private MenuRenderer menuRenderer;
  private GameOptions options;
  private float tryTime = 0f;
  private float loginTryTime = 0f;
  private long lengthOfTurn = 0;
  private long curTimeLeft = 0;

  private GameStateData gameState = new GameStateData();
  private boolean state;


  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    options = new GameOptions();

    network = new NetworkHandler(options, gameState);
  }

  @Override
  public void update(float deltaTime)
  {
    //timer reset or decrement
    if (gameState.getPhaseTime() != lengthOfTurn)
    {
      lengthOfTurn = gameState.getPhaseTime();
      curTimeLeft = lengthOfTurn;
    }
    else if (gameState.getPhaseTime() > 0)
    {
      if (curTimeLeft > 0)
      {
        curTimeLeft -= deltaTime;
      }
      else if (curTimeLeft < 0)
      {
        curTimeLeft = 0;
      }
    }
    // network try
    if (options.getNetworkStatus() == TRY)
    {
      options.setNetworkStatus(TRYING);
      network.createClient(playerController);
    }

    // player try
    if (getPlayer().getStatus() == TRY)
    {
      getPlayer().setStatus(TRYING);
    }


    // try and connect to server every 2s
    if (options.getNetworkStatus() == TRYING)
    {
      tryTime += 1;
    }

    // try and connect to server exeyer 2s
    if (getPlayer().getStatus() == TRYING)
    {
      loginTryTime += 1;
    }


    // if 2s has passes and server trying try to connect
    if (tryTime >= 200 && options.getNetworkStatus() == TRYING)
    {
      tryTime = 0;
      network.connectToServer();
    }

    // if 2s has passed and player is trying to login retry
    if (loginTryTime >= 200 && getPlayer().getStatus() == TRYING)
    {
      loginTryTime = 0f;
      network.loginToServer();
    }

    if (options.getNetworkStatus() == DISCONNECT)
    {
      network.disconnectFromServer();
    }

    playerController.update(deltaTime);
  }

  /**
   * Sets the ViewPort based upon the size of the given Rectangle.
   *
   * @param viewPort
   */
  public void setViewPort(Rectangle viewPort)
  {
    //menuRenderer.setViewPort(viewPort);
  }

  /**
   * Starts the server.
   */
  public void startServer()
  {
    options.setNetworkStatus(TRY);
  }

  /**
   * Re-instantiates a new HumanPlayerController with the current state.
   *
   * @param state
   */
  public void setState(boolean state)
  {
    this.state = state;
    if (!this.state)
    {
      playerController = new HumanPlayerController(gameState, options);
    }
  }

  /**
   * Sets options for the network.
   *
   * @param port
   * @param host
   */
  public void setOptions(String port, String host)
  {
    options.setHost(host);
    options.setPort(Integer.parseInt(String.valueOf(port)));
    options.setNetworkStatus(TRY);
  }

  /**
   * getter that returns the Network Handler.
   */
  public NetworkHandler getNetworkHandler()
  {
    return network;
  }

  /**
   * Returns the current player.
   */
  public Player getPlayer()
  {
    return playerController.getPlayer();
  }

  /**
   * Returns the current game state.
   */
  public GameStateData getGameState()
  {
    return gameState;
  }

}
