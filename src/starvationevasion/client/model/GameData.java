package starvationevasion.client.model;


import starvationevasion.client.net.common.NetworkStatus;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.State;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * GameOptions stores all of the information we need to find the server we want to connect to, authenticate our login
 * credentials, and pick a region to begin playing.
 */
public class GameData
{
  private int port = 27015;
  private String host = "localhost";

  private NetworkStatus networkStatus = NetworkStatus.NOT_CONNECTED;
  private String loginNonce;

  private Queue<String> mainFeed = new ConcurrentLinkedQueue<String>();



  public GameData()
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

  private long serverStartTime;
  private float phaseEndTime;//I think it's already calculated by the server. serverStartTime + length of phase
  private State serverState;

  private Set<EnumRegion> availableRegions;
  private Map<EnumRegion, String> takenRegions;

  /**
   * Returns the current {@link State}, used to modify client behavior based on current game phase/connectivity.
   *
   * @return the current State.
   */
  public State getServerState()
  {
    return serverState;
  }

  /**
   * Returns the length remaining of the current phase.
   *
   * @return the length of the current phase.
   */
  public long getPhaseTime()
  {
    return (long) (phaseEndTime - serverStartTime);
  }

  /**
   * Returns the currently available {@link EnumRegion}s for player selection during the pre-game phase.
   *
   * @return the Set of currently available regions.
   */
  public Set<EnumRegion> getAvailableRegions(){ return availableRegions; }

  /**
   * Changes the current server state. Used to handle client-side behavior in response to changes in server state.
   *
   * @param serverState the {@link State} to change to.
   */
  public void setServerState(State serverState)
  {
    this.serverState = serverState;
  }

  /**
   * Sets the current time, relative to when we connected to the {@link starvationevasion.server.Server}
   *
   * @param serverStartTime the time the server started.
   * @param phaseEndTime the time the current phase will end.
   */
  public void setTime(long serverStartTime, long phaseEndTime)
  {
    this.serverStartTime = serverStartTime;
    this.phaseEndTime = phaseEndTime;
  }

  /**
   * Sets the available {@link EnumRegion}s for player selection during the pre-game phase.

   * message, and then set here by the client.
   *
   * @param availableRegions the set of regions not yet assigned to any players.
   */
  public void setAvailableRegions(Set<EnumRegion> availableRegions){ this.availableRegions = availableRegions; }


  public void setTakenRegions(Map<EnumRegion, String> takenRegions)
  {
    this.takenRegions = takenRegions;
  }

  public Map<EnumRegion, String> getTakenRegions()
  {
    return takenRegions;
  }

}
