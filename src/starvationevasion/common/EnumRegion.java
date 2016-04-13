package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.util.ResourceBundle;

/**
 * Enumerates each of the geographic regions used in the game: both Player
 * and non-Player regions.<br><br>
 * The ordinal of US regions are continuous and less than all non-Player world regions.
 */

public enum EnumRegion implements Sendable
{
  /**
   *  The US states in this region are: CA
   */
  USA_CALIFORNIA,


  /**
   *  The US states in this region are: IA, MO, IL, IN
   */
  USA_HEARTLAND,

  /**
   * The US states in this region are: ND, SD, NE, KS, MN
   */
  USA_NORTHERN_PLAINS,

  /**
   * The US states in this region are: FL, MS, AL, GA, TN, NC, SC, KY, WV, VA
   */
  USA_SOUTHEAST,

  /**
   * The US states in this region are: ME, VT, HN, MA, RI, CT, NJ, NY, PA, DE, MD, OH, WI, MI
   */
  USA_NORTHERN_CRESCENT,

  /**
   * The US states in this region are: TX, LA, OK, AR
   */
  USA_SOUTHERN_PLAINS,


  /**
   * The US states in this region are: WA, OR, ID, NV, MT, WY, UT, CO, AZ, NM
   */
  USA_MOUNTAIN,
  ARCTIC_AMERICA, MIDDLE_AMERICA, SOUTH_AMERICA, EUROPE, MIDDLE_EAST, SUB_SAHARAN,
  RUSSIA, CENTRAL_ASIA, SOUTH_ASIA, EAST_ASIA, OCEANIA;

  public static final int SIZE = values().length;

  /**
   * Array of all player regions used in game.
   */
  public static final EnumRegion[] US_REGIONS =
  {USA_CALIFORNIA, USA_HEARTLAND, USA_NORTHERN_PLAINS, USA_SOUTHEAST, USA_NORTHERN_CRESCENT, USA_SOUTHERN_PLAINS, USA_MOUNTAIN
  };

  /**
   * Array of all non-player regions used in game.
   */
  public static final EnumRegion[] WORLD_REGIONS =
  { ARCTIC_AMERICA, MIDDLE_AMERICA, SOUTH_AMERICA, EUROPE, MIDDLE_EAST, SUB_SAHARAN,
    RUSSIA, CENTRAL_ASIA, SOUTH_ASIA, EAST_ASIA, OCEANIA
  };

  private final ResourceBundle res = ResourceBundle.getBundle("strings_enum");
  private final String shortName = res.getString("EnumRegion." + name() + ".shortName");

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    _json.setString("short-name", shortName);
    _json.setBoolean("is-us", isUS());

    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public Type getType ()
  {
    return Type.REGION;
  }

  /**
   * @return Returns an int with one bit set corresponding to the region's
   * ordinal.
   */
  public int getBit()
  {
    return 1 << ordinal();
  }

  public String toString() { return shortName; }

  public static int allUSRegionBits()
  {
    return USA_CALIFORNIA.getBit() |
      USA_HEARTLAND.getBit() |
      USA_NORTHERN_PLAINS.getBit() |
      USA_SOUTHEAST.getBit() |
      USA_NORTHERN_CRESCENT.getBit() |
      USA_SOUTHERN_PLAINS.getBit() |
      USA_MOUNTAIN.getBit();
  }


  public static EnumRegion getRegion(String name)
  {
    for (EnumRegion region : EnumRegion.values())
    {
      //System.out.println("region"+region + ", " + region.toString());
      if (region.name().equals(name)) return region;
    }
    return null;
  }

  /**
   * @return true if and only of the region is a player region of the United States of America.
   */
  public boolean isUS()
  {
    if ((getBit() | allUSRegionBits()) != 0) return true;
    return false;
  }
}
