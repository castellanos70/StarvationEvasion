package starvationevasion.sim;

import starvationevasion.common.EnumRegion;

/**
 * RegionData contains data used by the Simulator and shared with the Clients (via the Server)
 * for each player and non-Player region.<br><br>
 * Each Player region is a group of state territories.<br>
 * Each Non-Player region is a group of countries (and/or Alaska and Hawaii).
 */
public class RegionData extends TerritoryData
{
  protected final EnumRegion playerRegion;


  /**
   * Class constructor
   *
   * @param playerRegion the player's region.
   */
  public RegionData(EnumRegion region)
  {
    this.playerRegion = region;
  }
}
