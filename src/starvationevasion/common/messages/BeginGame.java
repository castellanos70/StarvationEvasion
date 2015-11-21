package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;

import java.util.Collections;
import java.util.Map;

/**
 * Shea Polansky
 * A message indicating the game is beginning and teams are finalized.
 */
public class BeginGame
{
  /**
   * An unmodifiable (i.e. will throw if modified) Map mapping regions to player names
   */
  public final Map<EnumRegion, String> finalRegionChoices;

  public BeginGame(Map<EnumRegion, String> finalRegionChoices)
  {
    this.finalRegionChoices = Collections.unmodifiableMap(finalRegionChoices);
  }
}
