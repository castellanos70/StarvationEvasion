package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Shea Polansky
 * A listing of which regions are available, and which are taken.
 * An instance of this message will be transmitted at least, but possibly more than,
 * once every time a player changes their chosen region.
 */
public class AvailableRegions implements Serializable
{
  /**
   * An *unmodifiable* (as in, will throw if modified) Map mapping taken regions
   * to player names.
   */
  public final Map<EnumRegion, String> takenRegions;

  /**
   * An *unmodifiable* (as in, will throw if modified) Set containing all the available regions.
   */
  public final Set<EnumRegion> availableRegions;

  public AvailableRegions(Map<EnumRegion, String> takenRegions, Set<EnumRegion> availableRegions)
  {
    this.takenRegions = Collections.unmodifiableMap(takenRegions);
    this.availableRegions = Collections.unmodifiableSet(availableRegions);
  }
}
