package worldfoodgame.io;

import worldfoodgame.model.TileManager;
import worldfoodgame.model.LandTile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static worldfoodgame.io.BioClimDataParser.FIELDS.*;

/**
 @author david
 created: 2015-03-22

 description: */
public class BioClimDataParser
{

  private static final double STEP = 0.041666666;
  private static final int DATA_ROWS = 3600;
  private static final int DATA_COLS = 8640;

  public enum FIELDS
  {
    ANL_MEAN_TEMP("bio1.bil",-9999),
    DIURNAL_RANGE("bio2.bil",-9999),
    MAX_TEMP("bio5.bil",-9999),
    MIN_TEMP("bio6.bil",-9999),
    ANL_PRECIP("bio12.bil",-9999),
    PROJ_ANL_MEAN_TEMP("bio1proj.bil", -32768),
    PROJ_DIURNAL_RANGE("bio2proj.bil", -32768),
    PROJ_MAX_TEMP("bio5proj.bil", -32768),
    PROJ_MIN_TEMP("bio6proj.bil", -32768),
    PROJ_ANL_PRECIP("bio12proj.bil", -32768),
    ELEVATION("alt.bil",-9999);

    public static final int SIZE = values().length;
    public static final float SCALE = 0.1f;
    String filename;
    int noData;

    FIELDS(String filename, int noData)
    {
      this.filename = filename;
      this.noData = noData;
    }

    int ind()
    {
      return ordinal();
    }
  }

  private final String root;
  private final TileManager dataSet =
    CropZoneDataIO.parseFile("resources/data/tiledata.bil", null);
  private final Map<LandTile, AgrPoint> map = new HashMap<>();


  public BioClimDataParser(String fileRoot)
  {
    root = fileRoot;
  }

  private static int twoByteToSignedInt(byte first, byte second)
  {
  /* raster data is little-endian, two-byte signed ints */
    return (short) ((second << 8) | (first & 0xff));
  }

  private void writeAll()
  {
    int count = 0;
    long start = System.currentTimeMillis();
    for (LandTile t : dataSet.allTiles())
    {
      if(count%10000 == 0) System.out.println(count + " of " + 6000000);
      count++;
      AgrPoint p = map.get(t);
      if(p == null || !p.hasAllData())
      {
        dataSet.removeTile(t);
        continue;
      }

      float data;
      float dayRange = p.getData(DIURNAL_RANGE.ind());
      float anlMean = p.getData(ANL_MEAN_TEMP.ind());
//      if(dayRange == DIURNAL_RANGE.noData || anlMean == ANL_MEAN_TEMP.noData)
//      {
//        dataSet.removeTile(t);
//        continue;
//      }

      float avgAM = SCALE * (anlMean + dayRange / 2f);
      t.setAvgDayTemp(avgAM);

      float avgPM = SCALE * (anlMean - dayRange / 2f);
      t.setAvgNightTemp(avgPM);

      data = p.getData(MAX_TEMP.ind());
//      if(data == MAX_TEMP.noData) { dataSet.removeTile(t); continue; }
      float anlMax = SCALE * data;
      t.setMaxAnnualTemp(anlMax);

      data = p.getData(MIN_TEMP.ind());
//      if(data == MIN_TEMP.noData) { dataSet.removeTile(t); continue; }
      float anlMin = SCALE * data;
      t.setMinAnnualTemp(anlMin);

      data = p.getData(ANL_PRECIP.ind());
//      if(data == ANL_PRECIP.noData) { dataSet.removeTile(t); continue; }
      float precip = SCALE * data;
      t.setRainfall(precip);

      float p_avgAM = SCALE * p.getData(PROJ_ANL_MEAN_TEMP.ind()) + p.getData(PROJ_DIURNAL_RANGE.ind()) / 2f;
      t.setProj_avgDayTemp(p_avgAM);

      float p_avgPM = SCALE * p.getData(PROJ_ANL_MEAN_TEMP.ind()) - p.getData(PROJ_DIURNAL_RANGE.ind()) / 2f;
      t.setProj_avgNightTemp(p_avgPM);

      float p_anlMax = SCALE * p.getData(PROJ_MAX_TEMP.ind());
      t.setProj_maxAnnualTemp(p_anlMax);

      float p_anlMin = SCALE * p.getData(PROJ_MIN_TEMP.ind());
      t.setProj_minAnnualTemp(p_anlMin);

      float p_precip = SCALE * p.getData(PROJ_ANL_PRECIP.ind());
      t.setProj_rainfall(p_precip);
      
      float elev = p.getData(ELEVATION.ind());
      t.setElev(elev);
    }
    System.out.printf("setting data took: %dms%n", System.currentTimeMillis() - start);
    CropZoneDataIO.writeCropZoneData(dataSet, "/Users/david/cs351/Food_Project/tiledata.bil");
  }

  public void readMaxTemps()
  {
    try (FileInputStream in = new FileInputStream(root + '/' + MAX_TEMP.filename))
    {
      for (int row = 0; row < DATA_ROWS; row++)
      {
        if(row%100 == 0) System.out.println("row: " + row);
        for (int col = 0; col < DATA_COLS; col++)
        {
          double lat = 90 - row * STEP;
          double lon = -180 + col * STEP;

          LandTile tile = dataSet.getTile(lon, lat);
          if (!map.containsKey(tile)) map.put(tile, new AgrPoint());

          AgrPoint point = map.get(tile);

          byte first = (byte) in.read();
          byte second = (byte) in.read();
          float data = twoByteToSignedInt(first, second);
          
          if(!(data == MAX_TEMP.noData)) point.putData(MAX_TEMP.ind(), data);
        }
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void readAll()
  {
    long start = System.currentTimeMillis();
    try
    {
      FileInputStream streams[] = new FileInputStream[FIELDS.SIZE];
      for (int i = 0; i < FIELDS.SIZE; i++)
      {
        streams[i] = new FileInputStream(root + '/' + FIELDS.values()[i].filename);
      }

      for (int row = 0; row < DATA_ROWS; row++)
      {
        if(row%100 == 0) System.out.println("row: " + row);
        
        for (int col = 0; col < DATA_COLS; col++)
        {
          double lat = 90 - row * STEP;
          double lon = -180 + col * STEP;

          LandTile tile = dataSet.getTile(lon, lat);
          if (!map.containsKey(tile)) map.put(tile, new AgrPoint());

          AgrPoint point = map.get(tile);

          for (int i = 0; i < streams.length; i++)
          {
            FileInputStream s = streams[i];

            byte first = (byte) s.read();
            byte second = (byte) s.read();

            float data = twoByteToSignedInt(first, second);
            if(!(data == FIELDS.values()[i].noData)) point.putData(i, data);

          }
        }
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.printf("reading and averaging data took: %dms%n",System.currentTimeMillis()-start);
  }

  static class AgrPoint
  {
    float fields[] = new float[FIELDS.SIZE];

    int points[] = new int[FIELDS.SIZE];
    boolean hasData[] = new boolean[FIELDS.SIZE];

    public AgrPoint()
    {
      Arrays.fill(fields, 0);
      Arrays.fill(points, 0);
      Arrays.fill(hasData, false);
    }

    public void putData(int i, float data)
    {
      hasData[i] = true;
      fields[i] = (fields[i] * points[i] + data) / (points[i] + 1);
      points[i]++;
    }

    public float getData(int i)
    {
      return (points[i] > 0 ? fields[i] : -Float.MAX_VALUE);
    }
    
    public boolean hasAllData()
    {
      boolean val = true;
      for(boolean b : hasData) val = val && b;
      return val;
    }
  }

  public static void main(String[] args)
  {
    BioClimDataParser parser = new BioClimDataParser("/Users/david/cs351/Food_Project/bins");
    parser.readAll();
    parser.writeAll();
  }
}
