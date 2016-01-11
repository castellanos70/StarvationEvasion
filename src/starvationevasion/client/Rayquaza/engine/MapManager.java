/**
 * @author Mohammad R. Yousefi
 * Essentially a resource bundle for the EnumFood for the values used by the client.
 * @param SMALL_ICON The size of the image assigned to the image path.
 * @param LARGE_ICON The size of the image assigned to the image path.
 * @param US_ZONE_ID The default prefix id for javaFX nodes used in the program. Can use any string prefixed with the
 * prefix to get the appropriate EnumRegion for US regions.
 * @param WORLD_ZONE_ID The default prefix id for javaFX nodes used in the program. Can use any string prefixed with the
 * prefix to get the appropriate EnumRegion for world regions.
 */
package starvationevasion.client.Rayquaza.engine;

import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import starvationevasion.common.EnumRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MohammadR on 12/4/2015.
 * Manages the String and Enum conversions from IDs used in code.
 */
public class MapManager
{
  private final static Map<EnumRegion, String> displayNames;
  private final static Map<String, EnumRegion> regions;
  private final static Map<EnumRegion, String> zoneId;
  public final static String[] US_ZONE_ID = {"mapZoneCA", "mapZoneMTN", "mapZoneNP", "mapZoneSP", "mapZoneHL",
      "mapZoneNC", "mapZoneSE"};


  public final static String[] WORLD_ZONE_ID = {"worldZoneAA", "worldZoneMA", "worldZoneSA", "worldZoneEU",
      "worldZoneME", "worldZoneSS", "worldZoneRU", "worldZoneCA", "worldZoneSOA", "worldZoneEA", "worldZoneSEA",
      "worldZoneOC"};

  static
  {
    regions = new HashMap<>();
    regions.put(US_ZONE_ID[0], EnumRegion.CALIFORNIA);
    regions.put(US_ZONE_ID[1], EnumRegion.MOUNTAIN);
    regions.put(US_ZONE_ID[2], EnumRegion.NORTHERN_PLAINS);
    regions.put(US_ZONE_ID[3], EnumRegion.SOUTHERN_PLAINS);
    regions.put(US_ZONE_ID[4], EnumRegion.HEARTLAND);
    regions.put(US_ZONE_ID[5], EnumRegion.NORTHERN_CRESCENT);
    regions.put(US_ZONE_ID[6], EnumRegion.SOUTHEAST);
    regions.put(WORLD_ZONE_ID[0], EnumRegion.ARCTIC_AMERICA);
    regions.put(WORLD_ZONE_ID[1], EnumRegion.MIDDLE_AMERICA);
    regions.put(WORLD_ZONE_ID[2], EnumRegion.SOUTH_AMERICA);
    regions.put(WORLD_ZONE_ID[3], EnumRegion.EUROPE);
    regions.put(WORLD_ZONE_ID[4], EnumRegion.MIDDLE_EAST);
    regions.put(WORLD_ZONE_ID[5], EnumRegion.SUB_SAHARAN);
    regions.put(WORLD_ZONE_ID[6], EnumRegion.RUSSIA);
    regions.put(WORLD_ZONE_ID[7], EnumRegion.CENTRAL_ASIA);
    regions.put(WORLD_ZONE_ID[8], EnumRegion.SOUTH_ASIA);
    regions.put(WORLD_ZONE_ID[9], EnumRegion.EAST_ASIA);
    regions.put(WORLD_ZONE_ID[10], EnumRegion.SOUTHEAST_ASIA);
    regions.put(WORLD_ZONE_ID[11], EnumRegion.OCEANIA);

    displayNames = new HashMap<>();
    displayNames.put(EnumRegion.CALIFORNIA, "California");
    displayNames.put(EnumRegion.MOUNTAIN, "Mountains");
    displayNames.put(EnumRegion.NORTHERN_PLAINS, "Northern Plains");
    displayNames.put(EnumRegion.SOUTHERN_PLAINS, "Southern Plains");
    displayNames.put(EnumRegion.HEARTLAND, "Heartland");
    displayNames.put(EnumRegion.NORTHERN_CRESCENT, "Northern Crescent");
    displayNames.put(EnumRegion.SOUTHEAST, "Southeast");
    displayNames.put(EnumRegion.ARCTIC_AMERICA, "Arctic America");
    displayNames.put(EnumRegion.MIDDLE_AMERICA, "Middle America");
    displayNames.put(EnumRegion.SOUTH_AMERICA, "South America");
    displayNames.put(EnumRegion.EUROPE, "Europe");
    displayNames.put(EnumRegion.MIDDLE_EAST, "Middle East");
    displayNames.put(EnumRegion.SUB_SAHARAN, "Sub-Saharan");
    displayNames.put(EnumRegion.RUSSIA, "Russia");
    displayNames.put(EnumRegion.CENTRAL_ASIA, "Central Asia");
    displayNames.put(EnumRegion.SOUTH_ASIA, "South Asia");
    displayNames.put(EnumRegion.EAST_ASIA, "East Asia");
    displayNames.put(EnumRegion.SOUTHEAST_ASIA, "Southeast Asia");
    displayNames.put(EnumRegion.OCEANIA, "Oceania");

    zoneId = new HashMap<>();
    zoneId.put(EnumRegion.CALIFORNIA, US_ZONE_ID[0]);
    zoneId.put(EnumRegion.MOUNTAIN, US_ZONE_ID[1]);
    zoneId.put(EnumRegion.NORTHERN_PLAINS, US_ZONE_ID[2]);
    zoneId.put(EnumRegion.SOUTHERN_PLAINS, US_ZONE_ID[3]);
    zoneId.put(EnumRegion.HEARTLAND, US_ZONE_ID[4]);
    zoneId.put(EnumRegion.NORTHERN_CRESCENT, US_ZONE_ID[5]);
    zoneId.put(EnumRegion.SOUTHEAST, US_ZONE_ID[6]);
    zoneId.put(EnumRegion.ARCTIC_AMERICA, WORLD_ZONE_ID[0]);
    zoneId.put(EnumRegion.MIDDLE_AMERICA, WORLD_ZONE_ID[1]);
    zoneId.put(EnumRegion.SOUTH_AMERICA, WORLD_ZONE_ID[2]);
    zoneId.put(EnumRegion.EUROPE, WORLD_ZONE_ID[3]);
    zoneId.put(EnumRegion.MIDDLE_EAST, WORLD_ZONE_ID[4]);
    zoneId.put(EnumRegion.SUB_SAHARAN, WORLD_ZONE_ID[5]);
    zoneId.put(EnumRegion.RUSSIA, WORLD_ZONE_ID[6]);
    zoneId.put(EnumRegion.CENTRAL_ASIA, WORLD_ZONE_ID[7]);
    zoneId.put(EnumRegion.SOUTH_ASIA, WORLD_ZONE_ID[8]);
    zoneId.put(EnumRegion.EAST_ASIA, WORLD_ZONE_ID[9]);
    zoneId.put(EnumRegion.SOUTHEAST_ASIA, WORLD_ZONE_ID[10]);
    zoneId.put(EnumRegion.OCEANIA, WORLD_ZONE_ID[11]);
  }

  /**
   * Returns the default lookup prefix string for region variables.
   *
   * @return Prefix string.
   */
  public static String getZoneId(EnumRegion region)
  {
    if (region == null) return "";
    return "#" + zoneId.get(region);
  }

  public static EnumRegion getRegionEnumById(String zoneId)
  {
    for (String key : US_ZONE_ID)
    {
      if (zoneId.startsWith(key)) return regions.get(key);
    }
    for (String key : WORLD_ZONE_ID)
    {
      if (zoneId.startsWith(key)) return regions.get(key);
    }
    return null;
  }

  /**
   * Adjusts the brightness of the given root. It is primarily used by the maps (Groups of SVGPaths).
   *
   * @param root The root node of the visual objects.
   */
  public static void setGlow(Group root)
  {
    root.getChildren().forEach(child -> child.setEffect(new Glow(0.6)));
  }

  /**
   * Adjusts the brightness of the given root. It is primarily used by the maps (Groups of SVGPaths).
   *
   * @param root The root node of the visual objects.
   */
  public static void removeGlow(Group root)
  {
    root.getChildren().forEach(child -> child.setEffect(null));
  }

  public static String getDisplayName(EnumRegion region)
  {
    if (displayNames.containsKey(region)) return displayNames.get(region);
    else return "None";
  }

  /**
   * Returns the default associated color to each EnumRegion as a "#ABCDEF" representation.
   *
   * @return The color string of the region.
   */
  public static String getRegionFXStyleString(EnumRegion region)
  {
    if (region == null) return "#c0c0c0";
    switch (region)
    {
      case CALIFORNIA:
        return "#ff7f2a";
      case HEARTLAND:
        return "#bc5fd3";
      case NORTHERN_PLAINS:
        return "#71c837";
      case SOUTHEAST:
        return "#ffdd55";
      case NORTHERN_CRESCENT:
        return "#aa0044";
      case SOUTHERN_PLAINS:
        return "#5fd3bc";
      case MOUNTAIN:
        return "#d38d5f";
      default:
        return "#c0c0c0";
    }
  }

  /*Testing Only*/
  @SuppressWarnings("ConstantConditions")
  public static void main(String[] args)
  {
    EnumRegion[] array = EnumRegion.values();
    for (int i = 0; i < array.length; i++)
    {
      System.out.println(String.format("Zone: %d = [%s] [%s] [%s] [%s]", i, array[i].name(), displayNames.get
          (array[i]), zoneId.get(array[i]), getRegionEnumById(zoneId.get(array[i])).name()));
    }
  }
}
