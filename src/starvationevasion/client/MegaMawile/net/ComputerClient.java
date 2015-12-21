package starvationevasion.client.MegaMawile.net;


import starvationevasion.client.MegaMawile.model.GameOptions;
import starvationevasion.client.MegaMawile.model.NetworkStatus;
import starvationevasion.client.MegaMawile.model.Player;
import starvationevasion.client.MegaMawile.model.GameStateData;

/**
 * The ComputerClient is used by an AI player running the game in headless mode to interact with the server. Based on
 * changes to {@link starvationevasion.server.ServerState}, namely current game phase, the ComputerClient modifies its
 * behavior.
 */
public class ComputerClient extends AbstractClient
{
  private boolean loggedIn;

  /**
   * Creates a new ComputerClient for use by an AI player running the game in headless mode.
   *
   * @param options {@link GameOptions} to use for logging in.
   * @param player an {@link Player} to use for making moves/viewing our hand, etc
   * @param gameState the {@link GameStateData}, which influences the computer client's behavior depending on connectivity,
   *                  current phase, etc.
   */
  public ComputerClient(GameOptions options, Player player, GameStateData gameState)
  {
    super(options, player, gameState);
  }

  @Override
  public void run()
  {
    listenToComputerRequests();
  }

  private void listenToComputerRequests()
  {
    while(options.getNetworkStatus() != NetworkStatus.CONNECTED)
    {
      connect();
      if(isConnected()) options.setNetworkStatus(NetworkStatus.CONNECTED);
    }

    while(isConnected())
    {
      if (options.getNetworkStatus() != NetworkStatus.LOGGED_IN && options.getLoginNonce() != null)
      {
        login();
        player.setStatus(NetworkStatus.LOGGED_IN);
        options.setNetworkStatus(NetworkStatus.LOGGED_IN);
      }
    }
  }
}
