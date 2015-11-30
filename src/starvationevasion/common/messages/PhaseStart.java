package starvationevasion.common.messages;

import starvationevasion.server.ServerState;

import java.io.Serializable;
import java.time.Instant;

/**
 * Shea Polansky
 * This is a message (server to client) indicating that a given phase has begun.
 * It will always include the phase and the current server time. It has an optional field
 * for phase end time, if the current phase is time-limited. Both times will be stored in the same
 * manner as in the ReadyToBegin class, and with the same limitations and restrictions.
 * An absent end time is represented as any value less than the current server time.
 */
public class PhaseStart implements Serializable
{
  public final ServerState currentGameState;
  public final long currentServerTime, phaseEndTime;

  public PhaseStart(ServerState currentGameState, long currentServerTime, long phaseEndTime)
  {
    this.currentGameState = currentGameState;
    this.currentServerTime = currentServerTime;
    this.phaseEndTime = phaseEndTime;
  }

  public PhaseStart(ServerState currentGameState, long currentServerTime)
  {
    this(currentGameState, currentServerTime, Long.MIN_VALUE);
  }

  /**
   * Constructs a new ServerState object with the current time, the given state,
   * and an end time based on the given length. If the length is less than zero,
   * a PhaseStart instance representing a non-time-limited state will be instantiated.
   * @param state the phase we are entering.
   * @param stateLength the length of a phase, in milliseconds, or any negative value for a
   *                    non-time-limited phase
   * @return the constructed PhaseStart object
   */
  public static PhaseStart constructPhaseStart(ServerState state, long stateLength)
  {
    final Instant now = Instant.now();
    return stateLength < 0 ?
        new PhaseStart(state, now.getEpochSecond()) :
        new PhaseStart(state, now.getEpochSecond(), now.plusMillis(stateLength).getEpochSecond());
  }
}
