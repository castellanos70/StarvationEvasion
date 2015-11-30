package starvationevasion.io;

import starvationevasion.sim.Territory;
import starvationevasion.sim.Region;
import starvationevasion.sim.World;
import starvationevasion.sim.CropZoneDataIO;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.io.XMLparsers.KMLParser;
import starvationevasion.sim.GeographicArea;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.TileManager;
import starvationevasion.sim.util.EquirectangularConverter;
import starvationevasion.sim.util.MapConverter;

import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles loading data and all configurations.
 */
public class WorldLoader
{
  private static boolean DEBUG = false;

  public static final String BG_DATA_PATH = "/sim/geography/ne_50m_land.kml";

  private Collection<Territory> territories;

  /**
   * Constructor for game, handles all init logic.
   */
  public WorldLoader(Region[] regionList)
  {
    Collection<GeographicArea> modelGeography;
    Collection<Territory> agricultureUnits;
    TileManager tileManager;

    try {
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
      data = csvLoader.getCountriesFromCSV(regionList);
    } catch (Exception ex)
    {
      // TODO : Throw some kind of error for the calling object.
      //
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", ex);
      return;
    }

    territories = data.territories;

    Calendar startingDate = Calendar.getInstance();
    startingDate.set(Calendar.YEAR,  2014);

    World.makeWorld(modelGeography, agricultureUnits, tileManager, startingDate);

    World world = World.getWorld();

    tileManager.setWorld(world);
  }

  public Collection<Territory> getTerritories()
  {
    return territories;
  }

  public static void printRegions(Region[] regions, boolean verbose)
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
}
