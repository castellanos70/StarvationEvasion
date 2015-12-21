package Logic.geography;

import javafx.scene.paint.Color;
import starvationevasion.common.EnumRegion;

/**
 * Contains static utility methods associated with maps, regions, etc... involved with gameplay
 */
public class MapUtil implements MapConstants
{

  /**
   * Converts a string ID representation of a US region (defined in MapConstants) to it's EnumRegion equivalent
   * @param id String ID representation of the region
   * @return the EnumRegion representation of the region
   */
  public static EnumRegion regionIdToEnumRegion(String id)
  {
    if (id.equals(CALIFORNIA)) return EnumRegion.CALIFORNIA;
    else if (id.equals(SOUTHEAST)) return EnumRegion.SOUTHEAST;
    else if (id.equals(SOUTHERN_PLAINS)) return EnumRegion.SOUTHERN_PLAINS;
    else if (id.equals(NORTHERN_PLAINS)) return EnumRegion.NORTHERN_PLAINS;
    else if (id.equals(NORTHERN_CRESCENT)) return EnumRegion.NORTHERN_CRESCENT;
    else if (id.equals(MOUNTAIN)) return EnumRegion.MOUNTAIN;
    else if (id.equals(HEARTLAND)) return EnumRegion.HEARTLAND;
    else return null;
  }

  /**
   * Converts an enum region to it's String ID representation (defined in MapConstants).
   * @param enumRegion The EnumRegion representation of the region
   * @return the String ID representation of the region
   */
  public static String enumRegionToRegionId(EnumRegion enumRegion)
  {
    if (enumRegion == EnumRegion.CALIFORNIA) return CALIFORNIA;
    else if (enumRegion == EnumRegion.SOUTHEAST) return SOUTHEAST;
    else if (enumRegion == EnumRegion.SOUTHERN_PLAINS) return SOUTHERN_PLAINS;
    else if (enumRegion == EnumRegion.NORTHERN_PLAINS) return NORTHERN_PLAINS;
    else if (enumRegion == EnumRegion.NORTHERN_CRESCENT) return NORTHERN_CRESCENT;
    else if (enumRegion == EnumRegion.MOUNTAIN) return MOUNTAIN;
    else if (enumRegion == EnumRegion.HEARTLAND) return HEARTLAND;
    else return null;
  }

  /**
   * Gets the color that should be used for the default state of a map region
   * @param enumRegion region to retrieve color from
   * @return the appropriate color for provided region
   */
  public static Color getRegionColor(EnumRegion enumRegion)
  {
    if (enumRegion == EnumRegion.CALIFORNIA) return CALIFORNIA_COLOR;
    else if (enumRegion == EnumRegion.SOUTHEAST) return SOUTHEAST_COLOR;
    else if (enumRegion == EnumRegion.SOUTHERN_PLAINS) return SOUTHERN_PLAINS_COLOR;
    else if (enumRegion == EnumRegion.NORTHERN_PLAINS) return NORTHERN_PLAINS_COLOR;
    else if (enumRegion == EnumRegion.NORTHERN_CRESCENT) return NORTHERN_CRESCENT_COLOR;
    else if (enumRegion == EnumRegion.MOUNTAIN) return MOUNTAIN_COLOR;
    else if (enumRegion == EnumRegion.HEARTLAND) return HEARTLAND_COLOR;
    else return null;
  }
}
