package starvationevasion.common;

/**
 * Enumerates each of the geographic regions used in the game: both Player
 * and non-Player regions.
 */

public enum EnumRegion
{
  /**
   *  The US states in this region are: CA
   */
  CALIFORNIA
  {
    public String toString() {return "California";}
    public boolean isUS() {return true;}
  },


  /**
   *  The US states in this region are: IA, MO, IL, IN
   */
  HEARTLAND
  {
    public String toString() {return "Heartland";}
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: ND, SD, NE, KS, MN
   */
  NORTHERN_PLAINS
  {
    public String toString() {return "Northern Plains";}
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: FL, MS, AL, GA, TN, NC, SC, KY, WV, VA
   */
  SOUTHEAST
  {
    public String toString() {return "Southeast";}
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: ME, VT, HN, MA, RI, CT, NJ, NY, PA, DE, MD, OH, WI, MI
   */
  NORTHERN_CRESCENT
  {
    public String toString() {return "Northern Crescent";}
    public boolean isUS() {return true;}
  },

  /**
   * The US states in this region are: TX, LA, OK, AR
   */
  SOUTHERN_PLAINS
  {
    public String toString() {return "Southern Plains & Delta States";}
    public boolean isUS() {return true;}
  },


  /**
   * The US states in this region are: WA, OR, ID, NV, MT, WY, UT, CO, AZ, NM
   */
  MOUNTAIN
  {
    public String toString() {return "Pacific Northwest & Mountain States";}
    public boolean isUS() {return true;}
  },


  ARCTIC_AMERICA
  {
    public String toString() {return "Arctic America";}
    public boolean isUS() {return false;}
  },

  MIDDLE_AMERICA
  {
    public String toString() {return "Middle America";}
    public boolean isUS() {return false;}
  },

  SOUTH_AMERICA
  {
    public String toString() {return "South America";}
    public boolean isUS() {return false;}
  },

  EUROPE
  {
    public String toString() {return "Europe";}
    public boolean isUS() {return false;}
  },

  MIDDLE_EAST
  {
    public String toString() {return "Middle East";}
    public boolean isUS() {return false;}
  },

  SUB_SAHARAN
  {
    public String toString() {return "Sub-Saharan Africa";}
    public boolean isUS() {return false;}
  },

  RUSSIA
  {
    public String toString() {return "Russia & Caucasus";}
    public boolean isUS() {return false;}
  },

  CENTRAL_ASIA
  {
    public String toString() {return "Central Asia";}
    public boolean isUS() {return false;}
  },

  SOUTH_ASIA
  {
    public String toString() {return "South Asia";}
    public boolean isUS() {return false;}
  },

  EAST_ASIA
  {
    public String toString() {return "East Asia";}
    public boolean isUS() {return false;}
  },

  SOUTHEAST_ASIA
  {
    public String toString() {return "Southeast Asia";}
    public boolean isUS() {return false;}
  },

  OCEANIA
  {
    public String toString() {return "Oceania";}
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

  public int allUSRegionBits()
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
