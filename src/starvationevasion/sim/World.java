package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015<br>
 * The world is everything that is the case.
 * The world is the totality of facts, not of things.
 * The facts in logical space are the world.<BR>
 * - L. W.
 */
public class World
{
  /**
   * The random number seed is used to initialize the random number generator.  During
   * normal game play this value should be 0, indicating that the default random number
   * seed should be used.  It is useful to set this to a fixed value during debugging
   * so that events can be repeated.
   */
  public static int RANDOM_SEED = 0;
  private final Random random;
  private Collection<GeographicArea> world;
  // private Collection<Territory> politicalWorld;
  private Territory[] territoryList;
  private TileManager tileManager;
  private Calendar currentDate;
  private List<TradingOptimizer.TradePair>[] lastTrades;
  private boolean DEBUG = false;
  private TileManager[] idealCropZone = new TileManager[EnumFood.SIZE];

  private TileManager climateData;
  private World(Collection<GeographicArea> world, Territory[] territoryList, Calendar cal)
  {
    this.world = world;
    this.territoryList = territoryList;
    this.currentDate = cal;

    if (RANDOM_SEED != 0)
    {
      Logger.getGlobal().warning("World initializing used fixed random number seed.");
      random = new Random(RANDOM_SEED);
    } else random = new Random();
  }

  /**
   * This method is used to create the world object. The world object is a
   * singleton class, there is one and only one world.
   *
   * @param world    the list of geographic areas that make up the world.
   * @param territories the political entities in the world
   * @param cal      the starting date of the world.
   */
  //public static void makeWorld(Collection<GeographicArea> world,
  //                             Collection<Territory> territories,
  //                             TileManager allTheLand,
  //                             Calendar cal)
  public static World makeWorld(Collection<GeographicArea> world,
                               Territory[] territories,
                               TileManager allTheLand,
                               Calendar cal)
  {
    World theOneWorld = new World(world, territories, cal);
    theOneWorld.tileManager = allTheLand;

    return theOneWorld;
  }

  public Random getRandomGenerator()
  {
    return random;
  }

  /**
   * Get the current time of this particular world.
   *
   * @return a calendar object, with the date and time in side.
   */
  public Calendar getCurrentDate()
  {
    return currentDate;
  }

  /**
   * Set the world time to the given calendar date.
   *
   * @param currentDate date the world will be after calling this method.
   */
  public void setCurrentDate(Calendar currentDate)
  {
    this.currentDate = currentDate;
  }

  /**
   * returns the year as an int.
   *
   * @return
   */
  public int getCurrentYear()
  {
    return getCurrentDate().get(Calendar.YEAR);
  }

  public Collection<GeographicArea> getWorldRegions()
  {
    return world;
  }

  public Territory[] getTerritories()
  {
    return territoryList;
  }


  /**
   * @return world population at current world time, in millions as a double.
   */
  public double getWorldPopulationMil()
  {
    double totalPop = 0;
    int year = getCurrentYear();
    for (Territory state : territoryList)
    {
      totalPop += state.getPopulation(year);
    }
    totalPop = totalPop / 1000000;
    return totalPop;
  }

  /**
   * performs operations needed when stepping from 1 year to next
   */
  public void stepWorld()
  {
    if (DEBUG) System.out.println("\n\nStarting world stepping in " + getCurrentYear());

    long start = System.currentTimeMillis();

    if (DEBUG) System.out.println("Mutating climate data...");
    // updateEcoSystems();
    if (DEBUG) System.out.printf("climate data mutated in %dms%n", System.currentTimeMillis() - start);
    
    currentDate.add(Calendar.YEAR, 1);
    start = System.currentTimeMillis();
    if (DEBUG) System.out.println("Planting tiles...");
    plantAndHarvestCrops();       // implemented
    if (DEBUG) System.out.printf("tiles planted in %dms%n", System.currentTimeMillis() - start);
    if (DEBUG) System.out.println("Date is now " + getCurrentYear());


    start = System.currentTimeMillis();
    if (DEBUG) System.out.println("Shipping and recieving...");
    shipAndReceive();
    if (DEBUG) System.out.printf("Done shipping and receiving in: %dms%n", System.currentTimeMillis() - start);

    start = System.currentTimeMillis();
    if (DEBUG) System.out.println("Mutating country demographics...");
    if (DEBUG) System.out.printf("country demographics mutated in %dms%n", System.currentTimeMillis() - start);
    if (DEBUG) System.out.println("year stepping done");
  }




  /*
    implements the benevolent trading between regions with surpluses and
    deficits by crop through the TradingOptimizer. */
  private void shipAndReceive()
  {
    TradingOptimizer optimizer = new TradingOptimizer(territoryList, getCurrentYear());
    optimizer.optimizeAndImplementTrades();
    while(!optimizer.doneTrading());
    lastTrades = optimizer.getAllTrades();
  }

  
  private void plantAndHarvestCrops()
  {
    final int year = getCurrentYear();
    for (final Territory state :territoryList)
    {
      CropOptimizer optimizer = new CropOptimizer(year, state);
      optimizer.optimizeCrops();
    }
  }

  /**
   * @return a Collection holding all the LandTiles in the world, including those
   * not assigned to regions and those without data
   */
  public Collection<LandTile> getAllTiles()
  {
    return tileManager.allTiles();
  }

  
  /**
   * Returns a Collection of the tiles held by this TileManager that actually
   * contain data.  This, in effect, excludes tiles that would be over ocean and
   * those at the extremes of latitude.  For all tiles, use allTiles();
   * @return  a Collection holding only those tiles for which there exists raster data.
   */
  public LandTile[] dataTiles()
  {
    return tileManager.dataTiles();
  }


  /**
   * @return a Collection of all the tiles registered with Countries.
   */
  public Collection<LandTile> getAllCountrifiedTiles() { return tileManager.countryTiles(); }


  /**
   * Set the TileManager for the World
   * @param mgr TileManager to set to this World
   */
  public void setTileManager(TileManager mgr)
  {
    this.tileManager = mgr;
  }

  /**
   * @return the TileManager for the World
   */
  public TileManager getTileManager()
  {
    return tileManager;
  }

  /**
   * Return the LandTile containing given longitude and latitude coordinates.
   * See TileManager.getTile()
   *@param lon longitude of coord
   *@param lat latitude of coord
   *@return    LandTile containing the coordinates
   */
  public LandTile getTile(double lon, double lat)
  {
    return tileManager.getTile(lon, lat);
  }



  public List<TradingOptimizer.TradePair>[] getTrades()
  {
   return lastTrades;
  }
}
