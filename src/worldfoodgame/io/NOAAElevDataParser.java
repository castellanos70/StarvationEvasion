package worldfoodgame.io;

import worldfoodgame.model.TileManager;
import worldfoodgame.model.LandTile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static worldfoodgame.io.NOAAElevDataParser.INDICIES.*;
/**
 @author david
 created: 2015-03-05

 #####################
 IF YOU RUN THIS WITH THE NOAA DATA, IT WILL REQUIRE -Xmx4g AS A JVM OPTION JUST
 TO PARSE ONE FILE
 This will be fixed with the next version.
 #####################
 
 this should be a temporary class, used to parse the binary data files
 NOAA
 provides and write them to a more meaningful format

 files are expected to have names "[a-p]10g" with no extension.  The
 format
 and naming convention of these files is detailed in the PDF found at

 http://www.ngdc.noaa.gov/mgg/topo/report/globedocumentationmanual.pdf

 In brief:
 Each file is simply a binary file holding a number of 16-bit integers,
 each representing the elevation at a specific latitude and longitude
 defined by their place in the conceptual array defined by the number of
 rows and columns for that file.  The table below details the contents
 and
 bounds of each file

        Latitude      Longitude    Elevation     Data
 Name   min   max     min   max    min    max    cols   rows
 -----------------------------------------------------------
 A10G    50    90    -180   -90      1   6098    10800  4800
 B10G    50    90     -90     0      1   3940    10800  4800
 C10G    50    90       0    90    -30   4010    10800  4800
 D10G    50    90      90   180      1   4588    10800  4800
 E10G     0    50    -180   -90    -84   5443    10800  6000
 F10G     0    50     -90     0    -40   6085    10800  6000
 G10G     0    50       0    90   -407   8752    10800  6000
 H10G     0    50      90   180    -63   7491    10800  6000
 I10G   -50     0    -180   -90      1   2732    10800  6000
 J10G   -50     0     -90     0   -127   6798    10800  6000
 K10G   -50     0       0    90      1   5825    10800  6000
 L10G   -50     0      90   180      1   5179    10800  6000
 M10G   -90   -50    -180   -90      1   4009    10800  4800
 N10G   -90   -50     -90     0      1   4743    10800  4800
 O10G   -90   -50       0    90      1   4039    10800  4800
 P10G   -90   -50      90   180      1   4363    10800  4800


 description: */
public class NOAAElevDataParser
{
  public enum INDICIES
  {
    NAME, MINLAT, MAXLAT, MINLON, MAXLON, MINEL, MAXEL, COLS, ROWS
  }

  final String[][] fileDef = new String[][]
    {
      {"A10G", "50", "90", "-180", "-90", "1", "6098", "10800", "4800"},
      {"B10G", "50", "90", "-90", "0", "1", "3940", "10800", "4800"},
      {"C10G", "50", "90", "0", "90", "-30", "4010", "10800", "4800"},
      {"D10G", "50", "90", "90", "180", "1", "4588", "10800", "4800"},
      {"E10G", "0", "50", "-180", "-90", "-84", "5443", "10800", "6000"},
      {"F10G", "0", "50", "-90", "0", "-40", "6085", "10800", "6000"},
      {"G10G", "0", "50", "0", "90", "-407", "8752", "10800", "6000"},
      {"H10G", "0", "50", "90", "180", "-63", "7491", "10800", "6000"},
      {"I10G", "-50", "0", "-180", "-90", "1", "2732", "10800", "6000"},
      {"J10G", "-50", "0", "-90", "0", "-127", "6798", "10800", "6000"},
      {"K10G", "-50", "0", "0", "90", "1", "5825", "10800", "6000"},
      {"L10G", "-50", "0", "90", "180", "1", "5179", "10800", "6000"},
      {"M10G", "-90", "-50", "-180", "-90", "1", "4009", "10800", "4800"},
      {"N10G", "-90", "-50", "-90", "0", "1", "4743", "10800", "4800"},
      {"O10G", "-90", "-50", "0", "90", "1", "4039", "10800", "4800"},
      {"P10G", "-90", "-50", "90", "180", "1", "4363", "10800", "4800"}
    };

  String root;
  Map<LandTile, ElevPoint> map = new HashMap<>();
  TileManager dataSet = CropZoneDataIO.parseFile("resources/data/tiledata", null);

  /**
   @param rootPath
   absolute path to location of NOAA files.  See class comment
   */
  public NOAAElevDataParser(String rootPath)
  {
    root = rootPath;
  }

  /*
    reads all the files in the first field of the fileDef array
   */
  public void readAll()
  {
    for (String listing[] : fileDef)
    {
      readFile(listing[NAME.ordinal()]);
    }
  }
  
  public void writeData(String filename)
  {
    CropZoneDataIO.writeCropZoneData(dataSet, filename);
  }
  
  public void writeData()
  {
    writeData("resources/data/tiledata");
  }
    
    
  /*
    reads a single file from the NOAA data and returns the List of ElevPoints
    parsed from that file
   */
  private void readFile(String filename)
  {
    LandTile tile;

    try(FileInputStream in = new FileInputStream(root + "/" + filename))
    {
      String[] listing = getListingByName(filename);
      byte[] bytes = new byte[2];

      int rows = Integer.parseInt(listing[ROWS.ordinal()]);
      int cols = Integer.parseInt(listing[COLS.ordinal()]);

      double minLat = Double.parseDouble(listing[MINLAT.ordinal()]);
      double minLon = Double.parseDouble(listing[MINLON.ordinal()]);

      double maxLat = Double.parseDouble(listing[MAXLAT.ordinal()]);
      double maxLon = Double.parseDouble(listing[MAXLON.ordinal()]);

      double latStep = (maxLat - minLat) / cols;
      double lonStep = (maxLon - minLon) / rows;
      
      int minElev = Integer.parseInt(listing[MINEL.ordinal()]);
      int maxElev = Integer.parseInt(listing[MAXEL.ordinal()]);

      int elev;
      double lat;
      double lon;

      for (int row = 0; row < rows; row++)
      {
        for (int col = 0; col < cols; col++)
        {

          bytes[0] = (byte)in.read();
          bytes[1] = (byte)in.read();
          
          /* data is stored in big-endian two-byte integers;
             shift bits accordingly */
          elev = (bytes[1] << 8) & bytes[0];
          assert(elev > minElev && elev < maxElev);

          lat = row * latStep + minLat;
          lon = col * lonStep + minLon;
          
          tile = dataSet.getTile(lon, lat);
          if(null == tile) 
          {
            dataSet.hashCode();
          }
          if(!map.containsKey(tile))
          {
            map.put(tile, new ElevPoint());
          }
          map.get(tile).addDataPoint(elev);
        }
      }
    } 
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void setElevData()
  {
    for(LandTile t : map.keySet())
    {
      t.setElev((float) map.get(t).elev);
    }
  }

  /*
    given a filename, return the corresponding listing that defines that file's
    constraints
   */
  private String[] getListingByName(String filename) throws IOException
  {
    for(String listing[] : fileDef) 
    {
      if(filename.endsWith(listing[NAME.ordinal()])) return listing;
    }
    throw new IOException("listing not found");
  }

  /*
    temporary class used for storing an elevation point
   */
  static class ElevPoint
  {
    private int dataPoints = 0;
    private double elev = 0;
    
    public void addDataPoint(double data)
    {
     elev = (elev * dataPoints + data) / (++dataPoints);
    }
  }

  
  /*
    convert data to project specific raster format
   */
  public static void main(String[] args)
  {
    NOAAElevDataParser p =
      new NOAAElevDataParser("/Users/david/cs351/Food_Project/all10");
    
    p.readAll();
    p.setElevData();
    p.writeData();
  }
}
