package starvationevasion.sim;

import starvationevasion.common.EnumRegion;

/**
 * Each Player region is a group of state territories.<br>
 * Each Non-Player region is a group of countries (and/or Alaska and Hawaii).
 */
public class Region extends Territory
{
  protected final EnumRegion playerRegion;


  /**
   * Class constructor
   *
   * @param region the player's region.
   */
  public Region(EnumRegion region)
  {
    this.playerRegion = region;
  }
}
