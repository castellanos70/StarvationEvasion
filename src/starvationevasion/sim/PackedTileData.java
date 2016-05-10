package starvationevasion.sim;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.EnumCropZone;
import starvationevasion.common.EnumFood;
import starvationevasion.server.io.JSON;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

/**
 * Wrapper class for packed tile data. Tile coordinates are packed into a 32 bit integer.
 * the first 16 bits hold the latitude, the last 16 bits hold the longitude.
 * the lat and lon are rounded to two decimal places and multiplied by 100 before being packed
 * Crop ratings for all 12 foods are packed into 3 bytes per tile.
 * 2 bits per food * 12 foods = 24 bits = 3 bytes;
 *
 * How to unpack:
 * index from 0 to # of land tiles:
 * PackedTileData.unpackLatitude(PACKED_TILE_COORDINATES, index);
 * PackedTileData.unpackLongitude(PACKED_TILE_COORDINATES, index);
 * PackedTileData.unpackCropRatings(PACKED_CROP_RATINGS, index);
 */
public class PackedTileData implements Sendable
{
  public byte[] PACKED_CROP_RATINGS;
  public  int[] PACKED_TILE_COORDINATES;

  private static int lowMask = loadMask(0,16);
  private static int highMask = loadMask(16,32);


  public PackedTileData(int totalTiles)
  {
    PACKED_CROP_RATINGS = new byte[totalTiles * 3];
    PACKED_TILE_COORDINATES = new int[totalTiles];
  }

  private static int loadMask(int start, int end)
  {
    int mask = 0;
    for(int i = start ; i< end;i++)
    {
      mask |= 1<< i;
    }
    return mask;
  }

  /**
   * packs coordinates and crop ratings for a single tile into data arrays
   * @param tile   the tile to pack
   * @param index  the index of the tile
   */
  public void packData(LandTile tile, int index)
  {
    packRatings(tile, index);
    packCoordinates(tile,index);
  }

  private void packRatings(LandTile tile, int index)
  {
      //3 bytes to store 12 crop ratings
      byte packedRating1 = 0;
      byte packedRating2 = 0;
      byte packedRating3 = 0;

      for(int i = 0; i < 4; i++)
      {
        packedRating1 |= (tile.getCropRatings()[i].ordinal() << (6 - 2*i));
        packedRating2 |= (tile.getCropRatings()[i+4].ordinal() << (6 - 2*i));
        packedRating3 |= (tile.getCropRatings()[i+8].ordinal() << (6 - 2*i));
      }

      int realIndex = index * 3;
      PACKED_CROP_RATINGS[realIndex] = packedRating1;
      PACKED_CROP_RATINGS[realIndex+1] = packedRating2;
      PACKED_CROP_RATINGS[realIndex+2] = packedRating3;

    }

  private void packCoordinates(LandTile tile, int index)
  {
    //lat and lon rounded to 2 decimal places
    int packedLatLon = 0;
    int roundedLat = Math.round(tile.getLatitude()  * 100);
    int roundedLon = Math.round(tile.getLongitude() * 100);
    //pack lat onto first 16 bits, lon onto next 16 bits
    packedLatLon |= (roundedLat << 0);
    packedLatLon |= (roundedLon << 16);
    PACKED_TILE_COORDINATES[index] = packedLatLon;
  }

  /**
   * unpacks crop ratings for a single land tile.
   * example of use:
   * cropRatings[EnumFood.CROP_FOODS[EnumFood.CITRUS.ordinal()]] returns rating for citrus
   * @param    index    index of tile to unpack
   * @return   an array of ratings for each crop
   */
  public static EnumCropZone[] unpackCropRatings(byte[] packedData, int index)
  {
    int realIndex = index * 3;
    EnumCropZone[] cropRatings = new EnumCropZone[EnumFood.CROP_FOODS.length];
    for(int i = 0; i < 4; i++)
    {
      for(int j = 0; j < 3; j++)
      {
        int enumIndex = (packedData[realIndex + j] & (3 << (6 - 2*i))) >> (6 - 2*i);
        cropRatings[i + 4*j] = EnumCropZone.values()[enumIndex];
      }

    }
    return cropRatings;
  }

  /**
   * unpacks latitude of a single landTile, rounded to 2 decimal places.
   * @param    packedData   integer array storing packed data
   * @param    index        index of packedData to unpack
   * @return  the latitude of the given landTile
   */
  public static double unpackLatitude(int[] packedData, int index)
  {
    return (packedData[index] & lowMask) / 100.0 ;
  }

  /**
   * unpacks longitude of a single landTile, rounded to 2 decimal places.
   * @param    packedData   integer array storing packed data
   * @param    index        index of packedData to unpack
   * @return  the longitude of the given landTile
   */
  public static double unpackLongitude(int[] packedData, int index)
  {
    return ((packedData[index] & highMask) >> 16) / 100.0 ;
  }

  @Override
  public Type getType()
  {
    return null;
  }

  /**
   * Convert class into json
   *
   * @return JSON representation of class
   */
  @Override
  public JSONDocument toJSON()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setNumber("total_crop_tiles", PACKED_CROP_RATINGS.length);
    json.setNumber("total_coord_tiles", PACKED_TILE_COORDINATES.length);
    for (int i = 0; i < PACKED_CROP_RATINGS.length; i++)
    {
      json.setNumber("crop " + i, PACKED_CROP_RATINGS[i]);
      if (i < PACKED_TILE_COORDINATES.length) json.setNumber("coord " + i, PACKED_TILE_COORDINATES[i]);
    }
    return json;
  }

  /**
   * Convert String into JSON
   *
   * @param doc object to be converted
   */
  @Override
  public void fromJSON(Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(doc);
    PACKED_CROP_RATINGS = new byte[json.getNumber("total_crop_tiles").intValue()];
    PACKED_TILE_COORDINATES = new int[json.getNumber("total_coord_tiles").intValue()];
    for (int i = 0; i < PACKED_CROP_RATINGS.length; i++)
    {
      PACKED_CROP_RATINGS[i] = json.getNumber("crop " + i).byteValue();
      if (i < PACKED_TILE_COORDINATES.length) PACKED_TILE_COORDINATES[i] = json.getNumber("coord " + i).intValue();
    }
  }
}
