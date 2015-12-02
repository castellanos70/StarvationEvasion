package starvationevasion.sim;


import starvationevasion.common.Constant;
import starvationevasion.common.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 @author: david
 cs351
 WorldFoodGame project
 
 description:
 
 TileManager wraps the collection of LandTiles and encapsulates the
 equal area projection used to define the tile space.
 It provides an interface for mutating the climate data in the tiles by year and
 obtaining various useful subsets of tiles and/or individual tiles and data by
 coordinates.
 */

public class TileManager extends AbstractClimateData
{
  /*
    Tiles represent 100 sq. km areas on the globe, defined by the
    Lambert Equal Area Projection.
    Their dimensions are defined as rectangles within the
    projection, with the following base assumptions:
    Earth Surface Area : 600,000,000 sq. km
    Earth Circumference: 40,000 km

    This implies there must be 6 million tiles.
    Since the projection is cylindrical and linear in X, it is simple to divide
    the rectangular map it produces into 4000 columns, which implies 1500 rows
    to achieve 6 million total tiles.  Since the aspect ratio of the map is PI,
    these tiles are not too far from square.
   */

  public static final LandTile NO_DATA = new LandTile(-180,0); /* in pacific */

  public static final int ROWS = 1500;
  public static final int COLS = 4000;

  
  public static final double MIN_LAT = -90;
  public static final double MAX_LAT = 90;
  public static final double MIN_LON = -180;
  public static final double MAX_LON = 180;
  public static final double LAT_RANGE = MAX_LAT - MIN_LAT;
  public static final double LON_RANGE = MAX_LON - MIN_LON;
  
  public static final double DLON = LON_RANGE/COLS;
  public static final double DLAT = LAT_RANGE/ROWS;
  
  /* these are fairly rough estimates for the distance between two tiles on the
   X and Y axes.  Should be acceptable for our purposes */
  public static final double DX_KM = Constant.EARTH_CIRCUMFERENCE / COLS;
  public static final double DY_KM = Constant.EARTH_CIRCUMFERENCE / ROWS * 0.5;
  
  /* max radius from selected tiles to add noise to each year */
  public static final double NOISE_RADIUS = 100; /* in km */

  private World world;

  private LandTile[][] tiles = new LandTile[COLS][ROWS];


  private List<LandTile> countryTiles = new ArrayList<>();
  private List<LandTile> allTiles;
  private List<LandTile> dataTiles;

  public TileManager(World world)
  {
    this.world = world;
    for(LandTile[] arr : tiles) Arrays.fill(arr, NO_DATA);
  }

  
  public TileManager()
  {this(null);}

  /**
   Get the max temperature at a location, given a year.  If the year is the
   current year in the World, the temperature is the actual model temperature at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.
   
   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the max temperature at the coordinates, either estimated or exact,
            depending on the year
   */
  @Override
  public float getTemperatureMax(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getMaxAnnualTemp();
    
    float cur = tile.getMaxAnnualTemp();
    float proj = tile.getProj_maxAnnualTemp();
    int slices = Constant.LAST_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices, sliceNum);
  }

  /**
   Get the min temperature at a location, given a year.  If the year is the
   current year in the World, the temperature is the actual model temperature at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the min temperature at the coordinates, either estimated or exact,
   depending on the year
   */
  @Override
  public float getTemperatureMin(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getMinAnnualTemp();

    float cur = tile.getMinAnnualTemp();
    float proj = tile.getProj_minAnnualTemp();
    int slices = Constant.LAST_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices, sliceNum);
  }

  /**
   Get the average daytime temperature at a location, given a year.  If the year
   is the current year in the World, the temperature is the actual model 
   temperature at that location.  Otherwise it is an interpolated estimate that
   will NOT include the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the average daytime temperature at the coordinates, either estimated
   or exact, depending on the year
   */
  @Override
  public float getTemperatureDay(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getAvgDayTemp();

    float cur = tile.getAvgDayTemp();
    float proj = tile.getProj_avgDayTemp();
    int slices = Constant.LAST_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices, sliceNum);
  }

  /**
   Get the average nighttime temperature at a location, given a year.  If the year
   is the current year in the World, the temperature is the actual model 
   temperature at that location.  Otherwise it is an interpolated estimate that
   will NOT include the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the average night temperature at the coordinates, either estimated 
   or exact, depending on the year
   */
  @Override
  public float getTemperatureNight(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getAvgNightTemp();

    float cur = tile.getAvgNightTemp();
    float proj = tile.getProj_avgNightTemp();
    int slices = Constant.LAST_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices, sliceNum);
  }

  /**
   Get the annual rainfall at a location, given a year.  If the year is the
   current year in the World, the rainfall is the actual model rainfall at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the annual rainfall at the coordinates, either estimated or exact,
   depending on the year
   */
  @Override
  public float getRainfall(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getRainfall();

    float cur = tile.getRainfall();
    float proj = tile.getProj_rainfall();
    int slices =  Constant.LAST_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices, sliceNum);
  }


  /**
   Mutates all the tile data based on projections maintained within each tile
   and noise added randomly.
   */
  public void stepTileData()
  {
    List<LandTile> tiles = dataTiles();
    for(LandTile tile : tiles) tile.stepTile( Constant.LAST_YEAR - world.getCurrentYear());
    
    /* shuffle tiles before adding noise */
    Collections.shuffle(tiles);
    
    /* take ten percent of tiles, add noise */
    for(LandTile tile : tiles.subList(0,tiles.size()/10))
    {
      addNoiseByTile(tile);
    }
  }

  /* adds noise to the parameters of all the tiles within the NOISE_RADIUS of 
    a given tile */
  private void addNoiseByTile(LandTile tile)
  {
    int centerRow = latToRow(tile.getLat());
    int centerCol = lonToCol(tile.getLon());
    
    /* calculate min and max row and column based on radius of noise addition and
      the current tile's location in the data */
    int minRow = centerRow - (int) (NOISE_RADIUS / DY_KM);
    int maxRow = centerRow + (int) (NOISE_RADIUS / DY_KM);
    int minCol = centerCol - (int) (NOISE_RADIUS / DX_KM);
    int maxCol = centerCol + (int) (NOISE_RADIUS / DX_KM);

    /* get the source tile's data.  
      All noise is added as a function of these values */
    float minTemp = tile.getMinAnnualTemp();
    float maxTemp = tile.getMaxAnnualTemp();
    float pmTemp = tile.getAvgNightTemp();
    float amTemp = tile.getAvgDayTemp();
    float rain = tile.getRainfall();
    
    /* noise is also a function of two random numbers in range [0,1]
    (generated once per source?) */
    double r1 = Util.rand.nextDouble();
    double r2 = Util.rand.nextDouble();
    
    float dMaxMinTemp = calcTileDelta(minTemp, maxTemp, r1, r2);
    float dAMPMTemp = calcTileDelta(pmTemp, amTemp, r1, r2);
    float dRainfall = calcTileDelta(0, rain, r1, r2);
    
    LandTile neighbor;
    
    for (int r = minRow; r < maxRow; r++)
    {
      for (int c = minCol; c < maxCol; c++)
      {
        /* allow overlap in data to account for the sphere */
        int colIndex = c < 0 ? COLS + c : c%COLS;
        int rowIndex = r < 0 ? ROWS + r : r%ROWS;
        if (colIndex < 0 || rowIndex < 0)
        {
          System.out.printf("c: %d, colIndex: %d, r: %d, rowIndex: %d", c, colIndex, r, rowIndex);
        }
        if(tiles[colIndex][rowIndex] == NO_DATA) continue;
        
        neighbor = tiles[colIndex][rowIndex];

        double xDist = DX_KM * (centerCol - c);
        double yDist = DY_KM * (centerRow - r);
        
        double dist = Math.sqrt(xDist * xDist + yDist * yDist);
        
        if(dist < NOISE_RADIUS)
        {
          
          double r3 = Util.rand.nextDouble()*2;
          
          float toAddMaxMin = scaleDeltaByDistance(dMaxMinTemp, dist, r3);
          float toAddAMPM = scaleDeltaByDistance(dAMPMTemp, dist, r3);
          float toAddRain = scaleDeltaByDistance(dRainfall, dist, r3);
          
          neighbor.setMaxAnnualTemp(neighbor.getMaxAnnualTemp() + toAddMaxMin);
          neighbor.setMinAnnualTemp(neighbor.getMinAnnualTemp() + toAddMaxMin);
          neighbor.setAvgDayTemp(neighbor.getAvgDayTemp() + toAddAMPM);
          neighbor.setAvgNightTemp(neighbor.getAvgNightTemp() + toAddAMPM);
          neighbor.setRainfall(neighbor.getRainfall() + toAddRain);
        }
      }
    }
  }

  /**
   Calculates the delta value used to add noise to tiles, given a maximum and
   minimum value for the parameter, and two random numbers in range [0,1]
   To use this for the annual rainfall delta, use 0 as the minimum.

   @param min   minimum value in the range of the data
   @param max   maximum value in the range of the data
   @param r1    first random number between 0 and 1
   @param r2    second random number between 0 and 1
   @return      the delta value used to randomize climate data
   */
  public float calcTileDelta(double min, double max, double r1, double r2)
  {
    return (float)(0.1 * (max - min)  * (r1 - r2));
  }


  /**
   Calculates the actual amount to add to a given parameter in a LandTile, given
   the delta value, the distance between the tile from which noise is being added
   and a random number in range [0,2]

   @param delta calculated delta value
   @param distance
   @param r3
   @return
   */
  public float scaleDeltaByDistance(double delta, double distance, double r3)
  {
    return (float)(delta/(Math.log(Math.E + distance * r3)));
  }
  

  /**
   Get a tile by longitude and latitude
   
   @param lon degrees longitude
   @param lat degrees latitude
   @return the tile into which the specified longitude and latitude coordinates
   fall.  If no tile exists at that point, NO_DATA is returned
   */
  public LandTile getTile (double lon, double lat)
  {
    if(!coordsInBounds(lon, lat))
    {
      throw new IllegalArgumentException(
        String.format("coordinates out of bounds. lon: %.3f, lat: %.3f", lon, lat)
      );
    }
    
    /* equal area projection is encapsulated here */
    int col = lonToCol(lon);
    int row = latToRow(lat);
    LandTile tile = tiles[col][row];
    return tile == null? NO_DATA : tile;
  }


  /**
   Add a given tile to the data set.
   This should really only be used when reading in a new set of tiles from
   a file.
   
   @param tile  LandTile to add
   */
  public void putTile(LandTile tile)
  {
    double lon = tile.getLon();
    double lat = tile.getLat();
    tiles[lonToCol(lon)][latToRow(lat)] = tile;
  }


  /**
   Registers a tile as having been associated with a country.  Due to gaps in the
   country data, if a set of tiles covering all the land is desired, use dataTiles()
   @param tile  tile to register
   */
  public void registerCountryTile(LandTile tile)
  {
    countryTiles.add(tile);
  }


  /**
   Returns a Collection of tiles that have been registered with a country.
   This is dependent on the usage of registerCountryTile() at initial data
   creation. (Also maybe should be refactored to another location?)
   
   @return Collection of those LandTiles that have been registered with a Territory
   */
  public List<LandTile> countryTiles()
  {
    return countryTiles;
  }

  /**
   Returns a Collection of the tiles held by this TileManager that actually
   contain data.  This, in effect, excludes tiles that would be over ocean and
   those at the extremes of latitude.  For all tiles, use allTiles();
   
   @return  a Collection holding only those tiles for which there exists raster
            data.
   */
  public List<LandTile> dataTiles()
  {
    if(null == dataTiles)
    {
      dataTiles = new ArrayList<>();
      for(LandTile t : allTiles())
      {
        if(NO_DATA != t) dataTiles.add(t);
      }
    }
    return dataTiles;
  }

  /**
   @return  all the tiles in this manager in a List
   */
  public List<LandTile> allTiles()
  {
    if(allTiles == null)
    {
      allTiles = new ArrayList<>();
      for(LandTile[] arr : tiles) allTiles.addAll(Arrays.asList(arr));
    }
    return allTiles;
  }


  /**
   remove a given tile from the underlying set of tiles.  This has the effect
   of placing a NO_DATA tile at the location of the given tile in the full
   projection (assuming the given tile was found)
   @param tile tile to remove
   @return  true if the tile was found and removed
   */
  public boolean removeTile(LandTile tile)
  {
    int col = lonToCol(tile.getLon());
    int row = latToRow(tile.getLat());

    /* check if tile is in the right position */
    boolean ret = tiles[col][row] == tile;

    if(!ret) /* only search if the tile is not in its proper place */
    {
      loop:
      for (int i = 0; i < tiles.length; i++)
      {
        for (int j = 0; j < tiles[i].length; j++)
        {
          if(ret = (tiles[i][j] == tile))
          {
            /* "remove" first instance of tile from underlying array */
            tiles[i][j] = NO_DATA;
            break loop;
          }
        }
      }
    }
    /* tile is in the right place, remove it */
    else tiles[col][row] = NO_DATA;

    /* pull all tiles from method ref, to guarantee the list exists */
    ret = ret || allTiles().remove(tile);
    ret = ret || countryTiles.remove(tile);

    /* ret is true iff the tile was a member of one of the underlying structures */
    return ret;
  }


  /**
   set the World for this TileManager.  TileManager needs access to World-specific
   data for current year and randomization percentages
   @param world   world to set for the manager
   */
  public void setWorld(World world)
  {
    this.world = world;
  }
  
  /* check given row and column indices for validity */
  private boolean indicesInBounds(int row, int col)
  {
    return col >= 0 && col < COLS && row >= 0 && row < ROWS;
  }

  /* check given longitude and latitude coordinates for validity */
  private boolean coordsInBounds(double lon, double lat)
  {
    return lon >= MIN_LON && lon <= MAX_LON && lat >= MIN_LAT && lat <= MAX_LAT; 
  }

  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private static int latToRow(double lat)
  {
    /* sine of latitude, shifted into [0,2] */
    double sinShift = Math.sin(Math.toRadians(lat)) + 1;
    double row = ROWS * sinShift / 2;
    
    /* take minimum of row and max row value, for the outlier lat = 90 */
    return (int)Math.min(row, ROWS - 1);
  }


  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private static int lonToCol(double lon)
  {
    return (int)Math.min((COLS * (lon + MAX_LON) / LON_RANGE), COLS - 1);
  }
  
  /* return the theoretical center latitude line of tiles in a given row */
  public static double rowToLat(int row)
  {
    /* bring the row index into floating point range [-1,1] */
    double measure = (row * 2d / ROWS) - 1;
    
    /* arcsin brings measure into spherical space.  Convert to degrees, and shift
      by half of DLAT (~latitude lines covered per tile)  */
    return Math.toDegrees(Math.asin(measure)) + 0.5 * DLAT;
  }


  /* return the theoretical center longitude line of tiles in a given column */
  public static double colToLon(int col)
  {
    return LON_RANGE * ((double)col) / COLS - MAX_LON + 0.5*DLON;
  }

  /* initialize a new tile set with proper latitude and longitude center points.
    This is really only used for making tiles for a new data set*/
  private static void initTiles(LandTile [][] tileset)
  {
    for (int col = 0; col < COLS; col++)
    {
      for (int row = 0; row < ROWS; row++)
      {
        double lon = colToLon(col);
        double lat = rowToLat(row);
        tileset[col][row] = new LandTile(lon, lat);
      }
    }
  }

  /* Used to create and write a new tile set.
     Be careful using this with the current data's filepath.  If a backup is not
     made, that data will be overwritten and must be re-generated from the raw
     data from www.worldclim.org (See BioClimDataParser)
   */
  private static void writeNewTileSet(String filePath)
  {
    TileManager data = new TileManager();
    initTiles(data.tiles);
    
    try(FileOutputStream out = new FileOutputStream(filePath))
    {
      for(LandTile t : data.allTiles())
      {
        byte[] array = t.toByteBuffer().array();
        out.write(array);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   Exception used if accessors from the AbstractClimateData class are given
   invalid coordinates
   */
  static class NoDataException extends IllegalArgumentException
  {

    public NoDataException(String msg)
    {
      super(msg);
    }
  }
}
