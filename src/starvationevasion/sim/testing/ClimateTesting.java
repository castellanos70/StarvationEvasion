package starvationevasion.sim.testing;

import starvationevasion.common.EnumRegion;
import starvationevasion.io.WorldLoader;
import starvationevasion.sim.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by peter on 12/3/2015.
 */
public class ClimateTesting {

  public static void pruneTiles(Region[] regionList) throws IOException
  { FileOutputStream fos = new FileOutputStream(new File("c:/temp/tiles.dat"));
    for (Region region : regionList) {
        for (Territory territory : region.getTerritories()) {
            for (LandTile tile : territory.getLandTiles()) {
                if (tile == TileManager.NO_DATA) continue;
                byte[] array = tile.toByteBuffer().array();
                fos.write(array);
            }
        }
    }

    fos.flush();
    fos.close();
  }

  /**
   * This entry point is for testing only. <br><br>
   *
   * This test shows how to instantiate the simulator and how to tell it
   * to deal each player a hand of cards.
   * @param args ignored.
   */
  public static void main(String[] args) {
    // Parse and load a world structure.
    //
    // The load() operation is very time consuming.
    //
    Region[] regionList = new Region[EnumRegion.SIZE];
    for (int i = 0; i < EnumRegion.SIZE; i++) {
      regionList[i] = new Region(EnumRegion.values()[i]);
    }

    WorldLoader loader = new WorldLoader(regionList);
    World world = loader.getWorld();

    if (false)
    {
        // Dump the data to disk for only the regions in play,
        //
        try {
            pruneTiles(regionList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TileManager tileManager = world.getTileManager();
    world.getTileManager().setClimate(1981);

    // Albuquerque (ish) ...
    //  lat [-90.0 to 90.0], South latitudes are less than 0.
    //  lon [-180.0 to 180.0], West longitudes are less than 0.
    float lat = 35.1107f;
    float lon = 106.6100f;

    LandTile tile = tileManager.getTile(lon, lat);
    System.out.println(tile.toDetailedString());

    float maxT = tileManager.getTemperatureMax(lat, lon);
    float minT = tileManager.getTemperatureMin(lat, lon);
    float dayT = tileManager.getTemperatureDay(lat, lon);
    float nightT = tileManager.getTemperatureNight(lat, lon);
    float rainFall = tileManager.getRainfall(lat, lon);

    // The Fall 2015 project needs 1981-2013 values, which is not supported by the
    // Spring 2015 project data.  Joel has asked that we simply use 1994's values for
    // those years.
    //
    for (int year = 1981; year < 2050; year += 1)
    {
      world.getTileManager().setClimate(year);

      float dMax = tileManager.getTemperatureMax(lat, lon) - maxT;
      float dMin = tileManager.getTemperatureMin(lat, lon) - minT;
      float dDay = tileManager.getTemperatureDay(lat, lon) - dayT;
      float dNight = tileManager.getTemperatureNight(lat, lon) - nightT;
      float dRain = tileManager.getRainfall(lat, lon) - rainFall;

      System.out.println("Year " + year +
          ", dRainfall=" + dRain +
          ", davgNightTemp=" + dNight +
          ", davgDayTemp=" + dDay +
          ", dminAnnualTemp=" + dMin +
          ", dmaxAnnualTemp=" + dMax +
          '}'
      );
    }
  }
}
