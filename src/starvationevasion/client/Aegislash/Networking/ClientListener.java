package starvationevasion.client.Aegislash.Networking;

import Logic.Client;
import javafx.concurrent.Task;
import starvationevasion.common.messages.*;
import java.io.Serializable;

/**
 * Listens and type checks on incoming client messages from the server. Determines the behavior of and
 * where messages should be forwarded.
 */
public class ClientListener extends Task
{
  /* local reference to the client */
  public Client client;

  /**
   * Constructor for the ClientListener class
   * @param client Client to construct the ClientListener with
   */
  public ClientListener(Client client)
  {
    this.client = client;
  }

  /**
   * The run method of client should be continuously listening to
   * the server for new information. This includes move validation,
   * whether another player has made a move, and fulfilling requests
   * for statistics.
   */
  public Void call()
  {
    try
    {
      for (;;)
      {
        parseServerMessages();
      }
    } catch (InterruptedException ex)
    {
      return null;
    }
  }

  /**
   * This method uses the clients Messenger object to listen
   * for Server messages. It also parses what kind of message
   * it is based on its class and passes it off to the
   * appropriate method.
   * @throws InterruptedException In case of connection issues.
   */
  private void parseServerMessages() throws InterruptedException
  {
    Serializable response = null;
    try
    {
      response = client.messenger.receive();
    } catch (InterruptedException ex)
    {
      ex.printStackTrace();
      throw new InterruptedException();
    }

    if (response != null)
    {
      if (response instanceof Hello)
      {
        System.out.println("login nonce " + ((Hello) response).loginNonce +
          " Server version " + ((Hello) response).serverVersion);
        client.setLoginSalt(((Hello) response).loginNonce);
        if(client.isAI) client.sendLogin(System.getenv("SEUSERNAME"),System.getenv("SEPASSWORD"));
      }
      else if (response instanceof LoginResponse)
      {
        System.out.println(((LoginResponse) response).responseType);
        client.parseLoginMessage((LoginResponse) response);
      }
      else if(response instanceof AvailableRegions)
      {
        System.out.println("Available Regions: " + ((AvailableRegions) response).availableRegions);
        client.setAvailableRegionInfo(((AvailableRegions) response));
      }
      else if(response instanceof GameState)
      {
        System.out.println("GameState updated");
        client.localDataContainer.updateGameState((GameState) response);
      }
      else if(response instanceof BeginGame)
      {
        System.out.println("Beginning Game");
        client.beginGame((BeginGame) response);
      }
      else if(response instanceof PhaseStart)
      {
        System.out.println("Phase start " + ((PhaseStart) response).currentGameState);
        client.handlePhaseStart((PhaseStart) response);
      }
      else if(response instanceof ActionResponse)
      {
        client.localDataContainer.parseActionResponse((ActionResponse) response);
      }
      else if(response instanceof Response)
      {
        System.out.println("Response " + ((Response) response).isError());
      }
      else if(response instanceof VoteStatus)
      {
        client.HandleVoteStatus((VoteStatus) response);
      }
      else System.out.println("unknown response " + response.toString());
    }
  }
}
