package starvationevasion.sim;

import starvationevasion.io.XMLparsers.KMLParser;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CropZoneDataIO
{
  public static final String DEFAULT_FILE = "/sim/geography/tiledata.bil";

  //public static TileManager parseFile(String resourcePath, Collection<Territory> countries) throws FileNotFoundException
  public static TileManager parseFile(String resourcePath, Territory[] territoryList) throws FileNotFoundException
  {
    TileManager dataSet = new TileManager(null);

    InputStream resourceStream = KMLParser.class.getResourceAsStream(resourcePath);
    if (resourceStream == null) throw new FileNotFoundException(resourcePath);

    BufferedInputStream inputStream = new BufferedInputStream(resourceStream);

    try
    {
      Territory lastUnit = null;

      //System.out.println("starting tiledata loading");
      byte bytes[] = new byte[LandTile.BYTE_DEF.SIZE_IN_BYTES];
      ByteBuffer buf = ByteBuffer.allocate(LandTile.BYTE_DEF.SIZE_IN_BYTES);
      LandTile tile;
      while (inputStream.read(bytes) != -1)
      {
        buf.clear();
        buf.put(bytes);
        tile = new LandTile(buf);
        dataSet.putTile(tile);

        if (lastUnit != null && lastUnit.containsMapPoint(tile.getCenter()))
        {
          lastUnit.addLandTile(tile);
          dataSet.registerCountryTile(tile);
          continue;
        }

        for (Territory agriculturalUnit : territoryList)
        {
          if (agriculturalUnit.containsMapPoint(tile.getCenter()))
          {
            agriculturalUnit.addLandTile(tile);
            dataSet.registerCountryTile(tile);
            lastUnit = agriculturalUnit;
          }
        }

      }
      //System.out.printf("read %d tiles in %dms%n", tiles, System.currentTimeMillis() - start);
    }
    catch (IOException e)
    {
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", e);
      e.printStackTrace();
    }
    return dataSet;
  }

  public static void writeCropZoneData(TileManager data, String filename)
  {
    try (FileOutputStream out = new FileOutputStream(filename))
    {
      for (LandTile t : data.allTiles())
      {
        if (t == data.NO_DATA) continue;
        byte[] array = t.toByteBuffer().array();
        out.write(array);
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private static void loadAndCheckTiles()
  {
    TileManager data;
    try {
      data = parseFile(DEFAULT_FILE, null);
    }
    catch (FileNotFoundException ex)
    {
      Logger.getGlobal().log(Level.SEVERE, "Error parsing tile data", ex);
      return;
    }

    int noTiles = 0;
    int realTiles = 0;
    float elev = 0, tmpAM = 0, tmpPM = 0, tmpMax = 0, tmpMin = 0;
    List<LandTile> tiles = data.allTiles();
    for (LandTile t : tiles)
    {
      try
      {
        if (t == TileManager.NO_DATA)
        {
          noTiles++;
        }
        else
        {
          realTiles++;
          elev += t.getElevation();
          tmpAM += t.getAvgDayTemp();
          tmpPM += t.getAvgNightTemp();
          tmpMax += t.getMaxAnnualTemp();
          tmpMin += t.getMinAnnualTemp();
        }
      }
      catch (NumberFormatException e)
      {
        System.err.println("Exception caused by");
        System.err.println(t);
      }
    }
  }
}
