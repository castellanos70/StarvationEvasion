package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import javafx.scene.paint.Color;
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

  public Color getColor()
  {
    return REGION_COLOR[ordinal()];
  }
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
    return (getBit() & allUSRegionBits()) != 0;
  }

  private Color[] REGION_COLOR =
  {
    Color.rgb(177, 149, 198),//USA_CALIFORNIA,
    Color.rgb(253, 235, 157),//USA_HEARTLAND,
    Color.rgb(77, 169, 196),//USA_NORTHERN_PLAINS,
    Color.rgb(247, 175, 99),//USA_SOUTHEAST,
    Color.rgb(200, 212, 164),//USA_NORTHERN_CRESCENT,
    Color.rgb(243, 153, 152),//USA_SOUTHERN_PLAINS,
    Color.rgb(182, 146, 114),//USA_MOUNTAIN,
    Color.rgb(179, 205, 227),//ARCTIC_AMERICA,
    Color.rgb(200, 212, 164),// MIDDLE_AMERICA,
    Color.rgb(118, 162, 135),// SOUTH_AMERICA,
    Color.rgb(77, 169, 196),// EUROPE,
    Color.rgb(182, 146, 114),// MIDDLE_EAST,
    Color.rgb(223, 143, 135),// SUB_SAHARAN,
    Color.rgb(113, 179, 123),//RUSSIA,
    Color.rgb(247, 175, 99),// CENTRAL_ASIA,
    Color.rgb(245, 212, 121),// SOUTH_ASIA,
    Color.rgb(219, 82, 89),// EAST_ASIA,
    Color.rgb(179, 131, 179)// OCEANIA;
  };
}
