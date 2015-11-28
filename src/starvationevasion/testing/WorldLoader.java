package starvationevasion.testing;

import spring2015code.common.EnumGrowMethod;
import spring2015code.model.geography.Territory;
import spring2015code.model.geography.Region;
import spring2015code.model.geography.World;
import starvationevasion.common.EnumFood;
import starvationevasion.io.CountryCSVLoader;
import spring2015code.io.CropZoneDataIO;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.io.XMLparsers.KMLParser;
import starvationevasion.geography.GeographicArea;
import starvationevasion.geography.LandTile;
import starvationevasion.geography.TileManager;
import starvationevasion.util.EquirectangularConverter;
import starvationevasion.util.MapConverter;

import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the 'game'. Handles loading data and all configurations.
 * @author david winston
 *         created: 2015-02-04
 *         <p/>
 *         description:
 */
public class WorldLoader
{
  private static boolean DEBUG = false;

  public static final String MODEL_DATA_PATH = "/sim/geography/ne_10m_admin_1_states_provinces.kml";
  public static final String BG_DATA_PATH = "/sim/geography/ne_50m_land.kml";

  private Collection<Territory> territories;
  private Collection<Region> regions;

  /**
   * Constructor for game, handles all init logic.
   */
  public WorldLoader() {
  }

  public Collection<Territory> getTerritories()
  {
    return territories;
  }

  public Collection<Region> getRegions()
  {
    return regions;
  }

  /**
   * set it ALL up.
   */
  public void load()
  {
    Collection<GeographicArea> background;
    Collection<GeographicArea> modelGeography;
    Collection<Territory> agricultureUnits;
    TileManager tileManager;

    try {
      background = KMLParser.getRegionsFromFile(BG_DATA_PATH);
      modelGeography = new GeographyXMLparser().getGeography();
      agricultureUnits = GeographyXMLparser.geograpyToAgriculture(modelGeography);
      tileManager = CropZoneDataIO.parseFile(CropZoneDataIO.DEFAULT_FILE, agricultureUnits);
    } catch (Exception ex)
    {
      // TODO : Throw some kind of error for the calling object.
      //
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", ex);
      return;
    }

    // add data from csv to agricultureUnits
    CountryCSVLoader csvLoader = new CountryCSVLoader(agricultureUnits);
    CountryCSVLoader.ParsedData data;
    try {
      data = csvLoader.getCountriesFromCSV();
    } catch (Exception ex)
    {
      // TODO : Throw some kind of error for the calling object.
      //
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", ex);
      return;
    }

    territories = data.territories;
    regions = data.regions;
    // printRegions(regions, false);

    Calendar startingDate = Calendar.getInstance();
    startingDate.set(Calendar.YEAR,  2014);

    World.makeWorld(modelGeography, agricultureUnits, tileManager, startingDate);

    World world = World.getWorld();
    MapConverter converter = new EquirectangularConverter();

    tileManager.setWorld(world);
  }

  public static void printRegions(Collection<Region> regions, boolean verbose)
  {
    for (Region region : regions)
    {
      System.out.println("Region : " + region.getName());
      for (Territory unit : region.getAgriculturalUnits())
      {
        System.out.println("\t" + unit.toString());
		if (verbose == false) continue;

        for (LandTile tile : unit.getLandTiles())
        {
          System.out.println("\t\t" + tile.toString());
        }
      }
    }
  }

  //*******
  // MAIN *
  //*******
  public static void main(String[] args)
  {
    new WorldLoader().load();
  }
}
