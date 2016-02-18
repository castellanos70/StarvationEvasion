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

public class TileManager
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
  public static final double DX_KM = 20.; // Constant.EARTH_CIRCUMFERENCE / COLS;
  public static final double DY_KM = 20.; // Constant.EARTH_CIRCUMFERENCE / ROWS * 0.5;
  
  /* max radius from selected tiles to add noise to each year */
  public static final double NOISE_RADIUS = 100; /* in km */


  private LandTile[][] tiles = new LandTile[COLS][ROWS];

  public TileManager()
  {
    for(LandTile[] arr : tiles) Arrays.fill(arr, NO_DATA);
  }



  /**
   Get the max temperature at a location in the current simulation year.
   
   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @return  the max temperature at the coordinates, either estimated or exact,
            depending on the year
   */
  public float getTemperatureMax(float lat, float lon)
  {
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    return tile.getMaxAnnualTemp();
  }

  /**
   Get the min temperature at a location in the current simulation year.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @return  the min temperature at the coordinates, either estimated or exact,
   depending on the year
   */
  public float getTemperatureMin(float lat, float lon)
  {
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    return tile.getMinAnnualTemp();
  }

  /**
   Get the average daytime temperature at a location in the current simulation year.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @return  the average daytime temperature at the coordinates, either estimated
   or exact, depending on the year
   */
  public float getTemperatureDay(float lat, float lon)
  {
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    return tile.getAvgDayTemp();
  }

  /**
   Get the average nighttime temperature at a location in the current simulation year.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @return  the average night temperature at the coordinates, either estimated 
   or exact, depending on the year
   */
  public float getTemperatureNight(float lat, float lon)
  {
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    return tile.getAvgNightTemp();
  }

  /**
   Get the annual rainfall at a location in the current simulation year.

   @param lat [-90.0 to 90.0], South latitudes are less than 0.
   @param lon [-180.0 to 180.0], West longitudes are less than 0.
   @return  the annual rainfall at the coordinates, either estimated or exact,
   depending on the year
   */
  public float getRainfall(float lat, float lon)
  {
    LandTile tile = getTile(lon, lat);
    if(tile == NO_DATA)
    {
      throw new NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
      
    return tile.getRainfall();
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
