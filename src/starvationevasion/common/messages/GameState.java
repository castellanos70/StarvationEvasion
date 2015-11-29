package starvationevasion.common.messages;

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;

import java.io.Serializable;

/**
 * Shea Polansky
 * Message for transmitting simulator state data
 */
public class GameState implements Serializable
{
  /**
   * The current world state
   */
  public final WorldData worldData;

  /**
   * The current hand of the region this GameState will be sent to
   */
  public final EnumPolicy[] hand;

  public GameState(WorldData worldData, EnumPolicy[] hand)
  {
    this.worldData = worldData;
    this.hand = hand;
  }
}
