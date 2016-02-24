package starvationevasion.client.net;


import starvationevasion.client.controller.AbstractPlayerController;
import starvationevasion.client.model.GameOptions;
import starvationevasion.client.net.common.NetworkStatus;
import starvationevasion.client.model.Player;
import starvationevasion.client.model.GameStateData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

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
   * to connect to the server. {@link GameStateData} allows the client to respond to changes in {@link starvationevasion.server.model.State}. The
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
//    for (EnumRegion USRegion : EnumRegion.US_REGIONS)
//    {
//      if (USRegion.toString().equals(region))
//      {
//        send(new RegionChoice(USRegion));
//      }
//    }
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

   */
  public void disconnect()
  {
    System.out.println("Closing client");

    // send(new Goodbye("Client(" + playerString + ") disconnecting."));

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
    // send(new Login(player.getUsername(), options.getLoginNonce(), player.getPassword()));
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
      catch (IOException e)
      {
      }
      catch(ClassNotFoundException e)
      {

      }
    }

    /**
     * Deserialize a message to do stuff to it
     * @param message
     */
    private void typeOfMSG(Object message)
    {
      //go to deserializer
    }
  }
}
