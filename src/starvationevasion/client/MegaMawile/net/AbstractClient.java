package starvationevasion.client.MegaMawile.net;


import starvationevasion.client.MegaMawile.StatisticReadData;
import starvationevasion.client.MegaMawile.controller.AbstractPlayerController;
import starvationevasion.client.MegaMawile.model.Ballot;
import starvationevasion.client.MegaMawile.model.GameOptions;
import starvationevasion.client.MegaMawile.model.NetworkStatus;
import starvationevasion.client.MegaMawile.model.Player;
import starvationevasion.client.MegaMawile.model.GameStateData;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.messages.*;
import starvationevasion.server.ServerState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A client that will communicate with server. Responsible for holding relevant
 * attributes communication tasks that apply to both Human and Computer.
 */
public abstract class AbstractClient extends Thread
{
  private Socket clientSocket;

  // write to socket
  private ObjectOutputStream write;
  // read the socket
  private ObjectInputStream reader;

  // time of server start
  private long startNanoSec;
  private volatile long lastUpdate;

  // writes to user
  private ClientListener listener;
  protected Player player;

  protected GameStateData gameState;
  protected GameOptions options;

  private volatile boolean isRunning = false;

  private String playerString;

  /**
   * Creates a new generic client (for either a human or AI connection to the server) with the given options, state, and
   * a controller. {@link GameOptions} are used for the player's login credentials, as well as the information we'll need
   * to connect to the server. {@link GameStateData} allows the client to respond to changes in {@link ServerState}. The
   * {@link AbstractPlayerController} allows us to get/set player state information based on messages from the server.
   *
   * @param options   GameOptions to reference for server location/login credentials.
   * @param player    The AbstractPlayerController we'll use to control the player in response to server messages.
   * @param gameState The GameState to modify/respond to as a result of server messages.
   */
  public AbstractClient(GameOptions options, Player player, GameStateData gameState)
  {
    this.options = options;
    this.player = player;
    this.gameState = gameState;


    playerString = player.toString().substring(player.toString().lastIndexOf('.') + 1);
  }

  public void connect()
  {

    if (openConnection(options.getHost(), options.getPort()))
    {
      listener = (this instanceof ComputerClient) ? new ClientListener(true) : new ClientListener();

      System.out.println("Client(" + playerString + "): Starting listener = : " + listener);
      listener.start();
      options.setNetworkStatus(NetworkStatus.CONNECTED);
      player.setStatus(NetworkStatus.NOT_CONNECTED);
    }

  }

  /**
   * Requests the passed region, in String form, from the server. Does not guarantee that the region will be assigned.
   *
   * @param region the region to request, in String form.
   */
  public void requestRegion(String region)
  {
    for (EnumRegion USRegion : EnumRegion.US_REGIONS)
    {
      if (USRegion.toString().equals(region))
      {
        send(new RegionChoice(USRegion));
      }
    }
  }

  /**
   * Returns whether or not the client is currently connected to the server.
   *
   * @return <code>true</code> if connected.
   */
  public boolean isConnected()
  {
    return isRunning;
  }

  /**
   * Changes connection status based on the passed boolean value.
   *
   * @param isConnected <code>true</code> if connected.
   */
  public void setIsConnected(boolean isConnected)
  {
    this.isRunning = isConnected;
  }

  /**
   * Disconnects the client from the server. Closes both reader and writer and sets the player
   * that is with the client to disconnected. We also send the server a {@link Goodbye} message.
   */
  public void disconnect()
  {
    System.out.println("Closing client");

    send(new Goodbye("Client(" + playerString + ") disconnecting."));

    if (write != null)
    {
      try
      {
        write.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    if (reader != null)
    {
      try
      {
        reader.close();
        clientSocket.close();
      }
      catch (IOException e)
      {
        System.err.println("Client Error: Could not close");
        e.printStackTrace();
      }
    }
    setIsConnected(false);
    player.setStatus(NetworkStatus.NOT_CONNECTED);
    options.setNetworkStatus(NetworkStatus.NOT_CONNECTED);
  }

  /**
   * Logs the client into the server, using the client's currently assigned username, pass, and login nonce. These
   * credentials are set in {@link GameOptions} and retrieved here at login.
   */
  public void login()
  {
    if (player.getUsername().isEmpty() || player.getPassword().isEmpty())
    {
      return;
    }
    player.setStatus(NetworkStatus.TRYING);
    send(new Login(player.getUsername(), options.getLoginNonce(), player.getPassword()));
  }

  /**
   * Sends a message to the server. See starvationevasion.common.messages for allowable Serializable messages
   *
   * @param message a Serializable message object
   */
  public void send(Serializable message)
  {
    try
    {
      write.writeObject(message);
    }
    catch (IOException e)
    {
      System.out.println("There was an error!");
    }
  }

  //Opens a connection with the given host:portNumber. This instantiates our Socket, as well as input and output streams.
  private boolean openConnection(String host, int portNumber)
  {
    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      isRunning = false;
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
        + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      write = new ObjectOutputStream(clientSocket.getOutputStream());
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open output stream");
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new ObjectInputStream(clientSocket.getInputStream());
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open input stream");
      e.printStackTrace();
      return false;
    }
    isRunning = true;
    return true;
  }

  /**
   * The ClientListener is used to respond to server messages received through the {@link ObjectInputStream}, as well
   * as print useful information to the console.
   */
  class ClientListener extends Thread
  {
    boolean isAi = false;

    ClientListener()
    {

    }

    ClientListener(boolean isAi)
    {
      this.isAi = isAi;
    }

    /**
     * Starts the ClientListener Thread. We call read in a loop to actively listen to server messages.
     */
    public void run()
    {
      while (isRunning)
      {
        read();
      }
    }

    /**
     * Reads serialized object messages from the server and passes them to the handler when received.
     */
    private void read()
    {
      try
      {
        Object msg = reader.readObject();

        if (msg == null)
        {
          System.out.println("Lost server.");
          isRunning = false;
          player.setStatus(NetworkStatus.LOST);
          return;
        }
        typeOfMSG(msg);//has all the type of instances from the Message package

      }
      catch (IOException | ClassNotFoundException ignored)
      {
      }
    }

    /**
     * Takes a server message as an Object and casts it to the appropriate type, then directs the program to a
     * handler method to take proper action.
     *
     * @param message the generic server message to respond to, in Object form.
     */
    private void typeOfMSG(Object message)
    {
      if (message instanceof ActionResponse)
      {
        handleActionResponse((ActionResponse) message);
      }
      if (message instanceof AvailableRegions)
      {
        handleAvailableRegions((AvailableRegions) message);
      }
      if (message instanceof BeginGame)
      {
        handleBeginGame((BeginGame) message);
      }
      if (message instanceof ClientChatMessage)
      {
        handleClientChatMessage((ClientChatMessage) message);
      }
      if (message instanceof GameState)
      {
        handleGameState((GameState) message);
      }
      if (message instanceof Goodbye)
      {
        handleGoodbye((Goodbye) message);
      }
      if (message instanceof Hello)
      {
        handleHello((Hello) message);
      }
      if (message instanceof LoginResponse)
      {
        handleLoginResponse((LoginResponse) message);
      }
      if (message instanceof PhaseStart)
      {
        handlePhaseStart((PhaseStart) message);
      }
      if (message instanceof ReadyToBegin)
      {
        handleReadyToBegin((ReadyToBegin) message);
      }
      if (message instanceof Response)
      {
        handleResponse((Response) message);
      }
      if (message instanceof ServerChatMessage)
      {
        handleServerChatMessage((ServerChatMessage) message);
      }
      if (message instanceof VoteStatus)
      {
        handleVoteStatus((VoteStatus) message);
      }
    }

    /**
     * Changes the player's hand once an ok for the action sent is received.
     *
     * @param actionResponse the server's {@link ActionResponse} to the client's previous action.
     */
    private void handleActionResponse(ActionResponse actionResponse)
    {
      options.addToMainFeed("Action response type: " + actionResponse.responseType);
      if (actionResponse.responseType == ActionResponseType.OK)
      {
        player.setHand(actionResponse.playerHand);
        options.setUpdateHand(true);
      }
      else
      {
        System.out.println(actionResponse.responseMessage);
        options.addToMainFeed(actionResponse.responseMessage + "\n");
      }
    }

    /**
     * Saves a client-side copy of the currently available regions and prints out the currently taken regions.
     *
     * @param availableRegionMsg the server's {@link AvailableRegions} message to handle.
     * @since 12/3/15
     */
    private void handleAvailableRegions(AvailableRegions availableRegionMsg)
    {
      gameState.setAvailableRegions(availableRegionMsg.availableRegions);

      for (Map.Entry<EnumRegion, String> entry : availableRegionMsg.takenRegions.entrySet())
      {
        if (player.getUsername().equals(entry.getValue()))
        {
          player.setRegion(entry.getKey());
        }
      }
      gameState.setTakenRegions(availableRegionMsg.takenRegions);

      availableRegionMsg.takenRegions.forEach((k, v) -> {
        options.addToMainFeed("Region : " + k + " Owner : " + v + "\n");
        options.addToMainFeed(k + " is available\n");
      });

    }

    /**
     * Handles a message from the server indicating that the game is about to begin.
     *
     * @param beginGame the server's {@link BeginGame} message to handle.
     */
    private void handleBeginGame(BeginGame beginGame)
    {
      System.out.println("Begin Game: " + beginGame.toString());
      options.addToMainFeed("Begin Game: " + beginGame.toString() + "\n");
      for (Map.Entry<EnumRegion, String> entry : beginGame.finalRegionChoices.entrySet())
      {
        if (entry.getValue().equals(player.getUsername()))
        {
          player.setRegion(entry.getKey());
        }
        else
        {
          gameState.getPlayers().add(new Player(entry.getValue(), entry.getKey()));
        }
      }

    }

    /**
     * Prints out client chat messages to the console.
     *
     * @param clientChatMessage the server's {@link ClientChatMessage} to print.
     * @since 12/1/15
     */
    private void handleClientChatMessage(ClientChatMessage clientChatMessage)
    {
      System.out.println(clientChatMessage.toString());
      options.addToMainFeed(clientChatMessage.toString() + "\n");
    }

    /**
     * Handles game state messages from the server by updating the client's {@link StatisticReadData} with the
     * information received from the server.
     *
     * @param messageGameState the server's {@link starvationevasion.common.messages.GameState} message to use to populate
     *                         the statistics data set.
     * @since 12/1/15
     */
    private void handleGameState(starvationevasion.common.messages.GameState messageGameState)
    {
      if (!isAi) StatisticReadData.srd.populateStats(messageGameState.worldData);
      player.setHand(messageGameState.hand);
      options.setUpdateHand(true);
    }

    /**
     * Handles a goodbye message from the server by printing it to the console.
     *
     * @param goodbye the server's {@link Goodbye} message to respond to.
     * @since 12/1/15
     */
    private void handleGoodbye(Goodbye goodbye)
    {
      options.addToMainFeed(goodbye.disconnectMessage + "\n");
      disconnect();
    }

    /**
     * Handles the server's hello message by saving the client's login nonce and printing confirmation to the console.
     *
     * @param hello the server's {@link Hello} message to pull the login nonce from.
     * @since 12/1/15
     */
    private void handleHello(Hello hello)
    {
      //connect message
      options.addToMainFeed("Recieved:" + hello.loginNonce + "\n");
      //connect to server

      options.setLoginNonce(hello.loginNonce);
      options.setNetworkStatus(NetworkStatus.CONNECTED);
    }

    /**
     * Handles the login response from the server by either assigning the selected region (specified in the presets
     * stored in the server's password file) or notifying the player of available regions to select.
     *
     * @param loginResponse the server's {@link LoginResponse} message to handle.
     * @since 12/1/15
     */
    private void handleLoginResponse(LoginResponse loginResponse)
    {
      System.out.println("You have been assigned to " + loginResponse.assignedRegion);
      System.out.println(loginResponse.responseType + " response type");
      options.addToMainFeed("You have been assigned to " + loginResponse.assignedRegion + "\n");
      options.addToMainFeed(loginResponse.responseType + " response type\n");
      if (loginResponse.responseType == LoginResponse.ResponseType.ASSIGNED_REGION
        || loginResponse.responseType == LoginResponse.ResponseType.REJOIN
        || loginResponse.responseType == LoginResponse.ResponseType.CHOOSE_REGION)
      {
        player.setRegion(loginResponse.assignedRegion);
        player.setStatus(NetworkStatus.LOGGED_IN);
      }
      if (loginResponse.responseType == LoginResponse.ResponseType.ACCESS_DENIED || loginResponse.responseType == LoginResponse.ResponseType.DUPLICATE)
      {
        // options.setNetworkStatus(NetworkStatus.AUTH_ERROR);
        player.setStatus(NetworkStatus.AUTH_ERROR);
      }
    }

    /**
     * Handles the phase start message from the server, indicating that a new phase is about to begin. We update
     * the client's {@link GameStateData} accordingly, as well as reset the counters in our.
     *
     * @param phaseStart the server's {@link PhaseStart} message to handle.
     * @since 12/2/15
     */
    private void handlePhaseStart(PhaseStart phaseStart)
    {
      System.out.println("Current game state: " + phaseStart.currentGameState + " Server time: " + phaseStart.currentServerTime + " End time: " + phaseStart.phaseEndTime);
      options.addToMainFeed("Current game state: " + phaseStart.currentGameState + " Server time: " + phaseStart.currentServerTime + " End time: " + phaseStart.phaseEndTime + "\n");
      gameState.setServerState(phaseStart.currentGameState);//sets the current state
      gameState.setTime(phaseStart.currentServerTime, phaseStart.phaseEndTime);
    }

    /**
     * Handles the server's ready to begin message.
     *
     * @param readyToBegin the server's {@link ReadyToBegin} message to handle.
     * @since 12/2/15
     */
    private void handleReadyToBegin(ReadyToBegin readyToBegin)
    {
      System.out.println("Ready: " + readyToBegin.isReady);
      options.addToMainFeed("Ready: " + readyToBegin.isReady + "\n");

    }

    /**
     * Handles generic server response messages by printing them to the console.
     *
     * @param response the server's {@link Response} message to handle.
     * @since 12/2/15
     */
    private void handleResponse(Response response)
    {
      System.out.println("Response: " + response.toString());
      options.addToMainFeed("Response: " + response.toString() + "\n");
    }

    /**
     * Handles server chat messages.
     *
     * @param serverChatMessage the server's {@link ServerChatMessage} to handle.
     * @since 12/2/15
     */
    private void handleServerChatMessage(ServerChatMessage serverChatMessage)
    {
      System.out.println(serverChatMessage.toString());
      options.addToMainFeed(serverChatMessage.message + "\n");
    }

    /**
     * Handles the server's vote status message by creating a new {@link Ballot} and assigning it to our
     * {@link AbstractPlayerController} to be filled out during the voting phase.
     *
     * @param voteStatus the server's {@link VoteStatus} message to handle.
     * @since 12/3/15
     */
    private void handleVoteStatus(VoteStatus voteStatus)
    {
      options.addToMainFeed("Got voting");
      ArrayList<PolicyCard> card = new ArrayList<>();
      for (PolicyCard currentCard : voteStatus.currentCards)
      {
        card.add(currentCard);
      }
      Ballot newBallot = new Ballot(card);
      player.setBallot(newBallot);
      gameState.setServerState(ServerState.VOTING);

    }
  }
}
