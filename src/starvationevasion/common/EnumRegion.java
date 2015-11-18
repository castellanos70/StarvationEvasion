package starvationevasion.common;

import java.util.ResourceBundle;

/**
 * Enumerates each of the geographic regions used in the game: both Player
 * and non-Player regions.<br><br>
 * The ordinal of US regions are continuous and less than all non-Player world regions.
 */

public enum EnumRegion
{
  /**
   *  The US states in this region are: CA
   */
  CALIFORNIA
  {
    public boolean isUS() {return true;}
  },


  /**
   *  The US states in this region are: IA, MO, IL, IN
   */
  HEARTLAND
  {
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: ND, SD, NE, KS, MN
   */
  NORTHERN_PLAINS
  {
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: FL, MS, AL, GA, TN, NC, SC, KY, WV, VA
   */
  SOUTHEAST
  {
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: ME, VT, HN, MA, RI, CT, NJ, NY, PA, DE, MD, OH, WI, MI
   */
  NORTHERN_CRESCENT
  {
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: TX, LA, OK, AR
   */
  SOUTHERN_PLAINS
  {
    public boolean isUS() {return true;}
  },


  /**
   * The US states in this region are: WA, OR, ID, NV, MT, WY, UT, CO, AZ, NM
   */
  MOUNTAIN
  {
    public boolean isUS() {return true;}
  },


  ARCTIC_AMERICA
  {
    public boolean isUS() {return false;}
  },

  MIDDLE_AMERICA
  {
    public boolean isUS() {return false;}
  },

  SOUTH_AMERICA
  {
    public boolean isUS() {return false;}
  },

  EUROPE
  {
    public boolean isUS() {return false;}
  },

  MIDDLE_EAST
  {
    public boolean isUS() {return false;}
  },

  SUB_SAHARAN
  {
    public boolean isUS() {return false;}
  },

  RUSSIA
  {
    public boolean isUS() {return false;}
  },

  CENTRAL_ASIA
  {
    public boolean isUS() {return false;}
  },

  SOUTH_ASIA
  {
    public boolean isUS() {return false;}
  },

  EAST_ASIA
  {
    public boolean isUS() {return false;}
  },

  SOUTHEAST_ASIA
  {
    public boolean isUS() {return false;}
  },

  OCEANIA
  {
    public boolean isUS() {return false;}
  };

  public static final int SIZE = values().length;

  /**
   * Array of all player regions used in game.
   */
  public static final EnumRegion[] US_REGIONS =
  { CALIFORNIA, HEARTLAND, NORTHERN_PLAINS, SOUTHEAST, NORTHERN_CRESCENT, SOUTHERN_PLAINS, MOUNTAIN
  };

  /**
   * Array of all non-player regions used in game.
   */
  public static final EnumRegion[] WORLD_REGIONS =
  { ARCTIC_AMERICA, MIDDLE_AMERICA, SOUTH_AMERICA, EUROPE, MIDDLE_EAST, SUB_SAHARAN,
    RUSSIA, CENTRAL_ASIA, SOUTH_ASIA, EAST_ASIA, SOUTHEAST_ASIA, OCEANIA
  };

  private final ResourceBundle res = ResourceBundle.getBundle("starvationevasion.common.locales.strings");
  private final String shortName = res.getString("EnumRegion." + name() + ".shortName");

  /**
   * @return true if and only of the region is a player region of the United States of America.
   */
  public abstract boolean isUS();

  /**
   * For US regions, returns an int with one bit set corresponding to the region's
   * ordinal (0 through 6) for the 7 US regions. If not a US region, returns 0.
   * @return bit
   */
  public int getBit()
  {
    if (isUS())  return 1 << ordinal();
    else return 0;
  }

  public String toString() { return shortName; }

  public static int allUSRegionBits()
  {
    return CALIFORNIA.getBit() |
      HEARTLAND.getBit() |
      NORTHERN_PLAINS.getBit() |
      SOUTHEAST.getBit() |
      NORTHERN_CRESCENT.getBit() |
      SOUTHERN_PLAINS.getBit() |
      MOUNTAIN.getBit();
  }
}
