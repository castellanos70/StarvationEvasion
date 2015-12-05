package starvationevasion.io;

import starvationevasion.sim.Territory;
import starvationevasion.sim.Region;
import starvationevasion.sim.World;
import starvationevasion.sim.CropZoneDataIO;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.TileManager;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles loading data and all configurations.
 */
public class WorldLoader
{
  private static boolean DEBUG = false;

  public static final String BG_DATA_PATH = "/sim/geography/ne_50m_land.kml";

  private World world;
  private Territory[] territories;

  /**
   * Constructor for game, handles all init logic.
   */
  public WorldLoader(Region[] regionList)
  {
    TileManager tileManager;
    List<GeographicArea> geography;

    try {
      geography = new GeographyXMLparser().getGeography();
      territories = Territory.parseTerritories(geography);
      tileManager = CropZoneDataIO.parseFile(CropZoneDataIO.DEFAULT_FILE, territories);
    } catch (Exception ex)
    {
      // TODO : Throw some kind of error for the calling object.
      //
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", ex);
      return;
    }

    // add data from csv to agricultureUnits
    CountryCSVLoader csvLoader;
    try {
      csvLoader = new CountryCSVLoader(territories, regionList);
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("The world model can not be populated.");
    }

    Calendar startingDate = Calendar.getInstance();
    startingDate.set(Calendar.YEAR,  2014);

    world = World.makeWorld(geography, territories, tileManager, startingDate);

    tileManager.setWorld(world);
  }

  public World getWorld()
  {
    return world;
  }

  public Territory[] getTerritories()
  {
    return territories;
  }

  public static void printRegions(Region[] regions, boolean verbose)
  {
    for (Region region : regions)
    {
      System.out.println("Region : " + region.getName());
      for (Territory unit : region.getTerritories())
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
}
