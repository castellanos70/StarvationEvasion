package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;

import java.util.*;

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
  private static World theOneWorld;
  private Random random = new Random(44);
  private Collection<GeographicArea> world;
  //private Collection<Territory> politicalWorld;
  private Territory[] territoryList;
  private TileManager tileManager;
  private Calendar currentDate;
  private List<TradingOptimizer.TradePair>[] lastTrades;
  private boolean DEBUG = false;
  private TileManager[] idealCropZone = new TileManager[EnumFood.SIZE];

  private AbstractClimateData climateData;

  //private World(Collection<GeographicArea> world, Collection<Territory> regions, Calendar cal)
  private World(Collection<GeographicArea> world, Territory[] territoryList, Calendar cal)
  {
    this.world = world;
    this.territoryList = territoryList;
    this.currentDate = cal;
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
  public static void makeWorld(Collection<GeographicArea> world,
                               Territory[] territories,
                               TileManager allTheLand,
                               Calendar cal)
  {
    if (theOneWorld != null)
    {
      new RuntimeException("Make World can only be called once!");
    }

    // TODO : The tile optimization function will only work if we have the
    // CropClimateData structure correctly populated for each of the crops.
    //
    // calculate OTHER_CROPS temp & rain requirements for each country
    for (Territory state : territories)
    {
      // The loader loads 2014 data.  We need to adjust the data for 1981.  Joel's first estimate is
      // to simply multiply all of the territorial data by 50%
      //
      state.estimateInitialYield();
      state.scaleInitialStatistics(.50);
      CropOptimizer optimizer = new CropOptimizer(Constant.FIRST_YEAR, state);
      optimizer.optimizeCrops();
    }

    theOneWorld = new World(world, territories, cal);
    theOneWorld.tileManager = allTheLand;
  }

  /**
   * used to return the world to singleton design pattern.
   * @return  the world
   */
  public static World getWorld()
  {
    if (theOneWorld == null)
    {
      throw new RuntimeException("WORLD HAS NOT BEEN MADE YET!");
    }
    return theOneWorld;
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

  public Territory[] getCountries()
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
    updateEcoSystems();
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

  /*
    Mutate the LandTile data through the TileManger.  This steps climate data,
    interpolating based on 2050 predictions with random noise added.
   */
  private void updateEcoSystems()
  {
    tileManager.stepTileData();
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
  public List<LandTile> dataTiles()
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
