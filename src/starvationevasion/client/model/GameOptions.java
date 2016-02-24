package starvationevasion.client.model;


import starvationevasion.client.net.common.NetworkStatus;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * GameOptions stores all of the information we need to find the server we want to connect to, authenticate our login
 * credentials, and pick a region to begin playing.
 */
public class GameOptions
{
  private int port = 27015;
  private String host = "localhost";

  private NetworkStatus networkStatus = NetworkStatus.NOT_CONNECTED;
  private String loginNonce;

  private Queue<String> mainFeed = new ConcurrentLinkedQueue<String>();

  private boolean updateHand = false;

  //private ArrayList<Region>;

  public GameOptions()
  {
  }

  /**
   * Returns the IP/name of the host server we want to connect to.
   *
   * @return the name of the host, in String form.
   */
  public String getHost()
  {
    return host;
  }

  /**
   * Returns the port through which we want to connect to the host server.
   *
   * @return the port, as an <code>int</code>.
   */
  public int getPort()
  {
    return port;
  }

  /**
   * Returns the client's login nonce for logging in. The nonce is received from the server in a
   * {@link starvationevasion.common.messages.Hello} message upon successful connection to the server.
   *
   * @return the client's login nonce, as a String.
   */
  public String getLoginNonce()
  {
    return loginNonce;
  }

  /**
   * Returns the current network status, for altering client connection behavior based on connectivity.
   *
   * @return the current {@link NetworkStatus}.
   */
  public NetworkStatus getNetworkStatus()
  {
    return networkStatus;
  }

  /**
   * Returns the client's main feed queue, for use in the starvationevasion.client.Aegislash.GUI.
   *
   * @return the client's main feed.
   */
  public ConcurrentLinkedQueue<String> getMainFeed()
  {
    return (ConcurrentLinkedQueue)mainFeed;
  }

  /**
   * Sets the hostname of the server we want to connect to/are connected to.
   *
   * @param host the host IP or name, as a String.
   */
  public void setHost(String host)
  {
    this.host = host;
  }

  /**
   * Sets the port through which we'll connect to the server.
   *
   * @param port the desired server port, as a String.
   */
  public void setPort(int port)
  {
    this.port = port;
  }

  /**
   * Set's the client's login nonce. This is done after the client has received a
   * {@link starvationevasion.common.messages.Hello} message from the server upon successful connection.
   *
   * @param loginNonce the client's login nonce, as a String.
   */
  public void setLoginNonce(String loginNonce)
  {
    this.loginNonce = loginNonce;
  }

  /**
   * Sets the current network status.
   *
   * @param networkStatus the current {@link NetworkStatus}.
   */
  public void setNetworkStatus(NetworkStatus networkStatus)
  {
    this.networkStatus = networkStatus;
  }


  /**
   * Adds the passed String to client's message feed/queue. This is utilized for displaying messages
   * in the feed on the starvationevasion.client.Aegislash.GUI.
   *
   * @param message a String to add to the feed.
   */
  public void addToMainFeed(String message)
  {
    mainFeed.add(message);
  }

  public boolean getUpdateHand()
  {
    return updateHand;
  }

  public void setUpdateHand(boolean updateHand)
  {
    this.updateHand = updateHand;
  }

}
