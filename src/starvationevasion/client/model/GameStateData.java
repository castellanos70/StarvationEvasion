package starvationevasion.client.model;


import starvationevasion.common.EnumRegion;
import starvationevasion.server.ServerState;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * This is a persisting class that holds a game state so that other parts of
 * the application can access it.
 */
public class GameStateData
{
  private long serverStartTime;
  private long phaseEndTime;//I think it's already calculated by the server. serverStartTime + length of phase
  private long phaseTime;
  private ServerState serverState;

  private Set<EnumRegion> availableRegions;
  private Map<EnumRegion, String> takenRegions;
  private ArrayList<Player> players = new ArrayList<>();

  /**
   * Returns the current {@link ServerState}, used to modify client behavior based on current game phase/connectivity.
   *
   * @return the current ServerState.
   */
  public ServerState getServerState()
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
    return phaseTime;
  }

  /**
   * Subtracts the delta from the phase time.
   *
   * @param deltaTime
   */
  public void subtractTime(float deltaTime)
  {
    phaseTime-=deltaTime;
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
   * @param serverState the {@link ServerState} to change to.
   */
  public void setServerState(ServerState serverState)
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
    phaseTime = phaseEndTime - serverStartTime;
  }

  /**
   * Sets the available {@link EnumRegion}s for player selection during the pre-game phase.
   * This is received from the server in the form of an {@link starvationevasion.common.messages.AvailableRegions}
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


  public void updatePlayers(ArrayList<Player> players)
  {
    this.players = players;
  }

  public ArrayList<Player> getPlayers()
  {
    return players;
  }
}
