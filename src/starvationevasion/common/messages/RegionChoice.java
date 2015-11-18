package starvationevasion.common.messages;

import starvationevasion.common.EnumRegion;

/**
 * Shea Polansky
 * A class representing a player's region choice.
 * This is not guaranteed; in the event two players transmit
 * the same choice, regions will be assigned on a first-come first-server basis.
 * In the event that you transmit a choice that is already taken, the server is *not*
 * required to reply with anything other than the standard Response.OK. If the player's
 * choice does change the chosen countries, an AvailableRegions response *is* guaranteed.
 * A region choice of 'null' will unassign a client from any region.
 * Be wary of race conditions in cases when two clients both try to claim the same region.
 */
public class RegionChoice
{
  /**
   * The chosen region, or null if no region is chosen.
   */
  public final EnumRegion region;

  public RegionChoice(EnumRegion region)
  {
    this.region = region;
  }
}
