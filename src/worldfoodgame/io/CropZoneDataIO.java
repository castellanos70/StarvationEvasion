package worldfoodgame.io;

import worldfoodgame.model.geography.AgriculturalUnit;
import worldfoodgame.model.TileManager;
import worldfoodgame.model.LandTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;

/**
 * @author david
 *         created: 2015-03-21
 *         <p/>
 *         description:
 */
public class CropZoneDataIO
{
  public static final String DEFAULT_FILE = "resources/data/tiledata.bil";

  public static TileManager parseFile(String filename, Collection<AgriculturalUnit> countries)
  {
    TileManager dataSet = new TileManager(null);


    try (FileInputStream in = new FileInputStream(filename))
    {
      AgriculturalUnit lastCountry = null;

      //System.out.println("starting tiledata loading");
      byte bytes[] = new byte[LandTile.BYTE_DEF.SIZE_IN_BYTES];
      ByteBuffer buf = ByteBuffer.allocate(LandTile.BYTE_DEF.SIZE_IN_BYTES);
      LandTile tile;
      int tiles = 0;
      long start = System.currentTimeMillis();
      while (in.read(bytes) != -1)
      {
        tiles++;
        buf.clear();
        buf.put(bytes);
        tile = new LandTile(buf);
        dataSet.putTile(tile);

        if (lastCountry != null && lastCountry.containsMapPoint(tile.getCenter()))
        {
          lastCountry.addLandTile(tile);
          dataSet.registerCountryTile(tile);
          continue;
        }

        for (AgriculturalUnit agriculturalUnit : countries)
        {
          if (agriculturalUnit.containsMapPoint(tile.getCenter()))
          {
            agriculturalUnit.addLandTile(tile);
            dataSet.registerCountryTile(tile);
            lastCountry = agriculturalUnit;
          }
        }

      }
      //System.out.printf("read %d tiles in %dms%n", tiles, System.currentTimeMillis() - start);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
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
    TileManager data = parseFile(DEFAULT_FILE, null);


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
    elev /= realTiles;
    tmpAM /= realTiles;
    tmpPM /= realTiles;
    tmpMax /= realTiles;
    tmpMin /= realTiles;
    System.out.printf("tiles: %d, NO_DATA: %d%n" +
        "avg elev: %f%n" +
        "avg tmpAM: %f%n" +
        "avg tmpPM: %f%n" +
        "avg tmpMax: %f%n" +
        "avg tmpMin: %f",
      realTiles, noTiles, elev, tmpAM, tmpPM, tmpMax, tmpMin);
  }

  public static void main(String[] args)
  {
    loadAndViewTiles();
  }

  public static void loadAndViewTiles()
  {
    final TileManager data = parseFile(DEFAULT_FILE, null);
    final JFrame win = new JFrame();
    win.setSize(1080, 600);
    win.setResizable(false);
    final DataPanel contentPane = new DataPanel(data.allTiles());
    contentPane.init();
    win.setContentPane(contentPane);
    win.setVisible(true);
    win.pack();
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    new Timer(50, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        contentPane.repaint();
      }
    }).start();

  }

  private static class DataPanel extends JPanel
  {
    public Rectangle2D.Double tileRect;
    public final int ELEV = 0;
    public final int DAY = 1;
    public final int NIGHT = 2;
    public final int MAX = 4;
    public final int MIN = 8;
    public final int PREC = 16;

    public float
      minElev, maxElev, minDay, maxDay, minNight, maxNight,
      minMax, maxMax, minMin, maxMin, minPrec, maxPrec;

    public AffineTransform transform;
    public int mode = ELEV;

    List<LandTile> tiles;

    public final Color C_ELEV = new Color(155, 155, 155);
    public final Color C_DAY = Color.pink;
    public final Color C_NIGHT = Color.green;
    public final Color C_MAX = Color.red;
    public final Color C_MIN = Color.blue;
    public final Color C_PREC = Color.cyan;

    public final Rectangle R_ELEV = new Rectangle(20, 560, 100, 20);
    public final Rectangle R_DAY = new Rectangle(120, 560, 100, 20);
    public final Rectangle R_NIGHT = new Rectangle(220, 560, 100, 20);
    public final Rectangle R_MAX = new Rectangle(320, 560, 100, 20);
    public final Rectangle R_MIN = new Rectangle(420, 560, 100, 20);
    public final Rectangle R_PREC = new Rectangle(520, 560, 100, 20);

    MouseAdapter mouseAdapter = new MouseAdapter()
    {
      @Override
      public void mouseMoved(MouseEvent e)
      {
        super.mouseMoved(e);
      }

      @Override
      public void mouseClicked(MouseEvent e)
      {
        super.mouseClicked(e);
        Point loc = e.getPoint();
        if (R_ELEV.contains(loc)) mode = ELEV;
        if (R_DAY.contains(loc)) mode = DAY;
        if (R_NIGHT.contains(loc)) mode = NIGHT;
        if (R_MAX.contains(loc)) mode = MAX;
        if (R_MIN.contains(loc)) mode = MIN;
        if (R_PREC.contains(loc)) mode = PREC;
      }
    };

    public DataPanel(List<LandTile> tileset)
    {
      tiles = tileset;
      init();
    }

    public void init()
    {
      transform = new AffineTransform();
      transform.translate(540, 270);
      transform.scale(3, -3);
      addMouseListener(mouseAdapter);
      setLims();
      Dimension size = new Dimension(1080, 600);
      setPreferredSize(size);
      setMaximumSize(size);
      setMinimumSize(size);
      setSize(size);
    }

    private void setLims()
    {
      boolean set = true;
      for (LandTile t : tiles)
      {
        if (t != TileManager.NO_DATA)
        {
          if (set)
          {
            set = false;
            minElev = maxElev = t.getElevation();
            minDay = maxDay = t.getAvgDayTemp();
            minNight = maxNight = t.getAvgNightTemp();
            minMax = maxMax = t.getMaxAnnualTemp();
            minMin = maxMin = t.getMinAnnualTemp();
            minPrec = maxPrec = t.getRainfall();
          }
          else
          {
            minElev = Math.min(minElev, t.getElevation());
            maxElev = Math.max(maxElev, t.getElevation());
            minDay = Math.min(minDay, t.getAvgDayTemp());
            maxDay = Math.max(maxDay, t.getAvgDayTemp());
            minNight = Math.min(minNight, t.getAvgNightTemp());
            maxNight = Math.max(maxNight, t.getAvgNightTemp());
            minMax = Math.min(minMax, t.getMaxAnnualTemp());
            maxMax = Math.max(maxMax, t.getMaxAnnualTemp());
            minMin = Math.min(minMin, t.getMinAnnualTemp());
            maxMin = Math.max(maxMin, t.getMinAnnualTemp());
            minPrec = Math.min(minPrec, t.getRainfall());
            maxPrec = Math.max(maxPrec, t.getRainfall());
          }
        }
      }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      drawRects(g2);
      drawTiles(g2);
    }

    private void drawTiles(Graphics2D g2)
    {
      Color c = new Color(0xffffff);
      Color toDraw = c;
   
   /* these colors don't really look great */
//   switch(mode){
//    case ELEV: c = C_ELEV; break;
//    case DAY: c = C_DAY; break;
//    case NIGHT: c = C_NIGHT; break;
//    case MAX: c = C_MAX; break;
//    case MIN: c = C_MIN; break;
//    case PREC: c = C_PREC; break;
//   }

      for (LandTile t : tiles)
      {
        float data;
        switch (mode)
        {
          case ELEV:
            data = t.getElevation();
            toDraw = scaleColor(data, minElev, maxElev, c);
            break;
          case DAY:
            data = t.getAvgDayTemp();
            toDraw = scaleColor(data, minDay, maxDay, c);
            break;
          case NIGHT:
            data = t.getAvgNightTemp();
            toDraw = scaleColor(data, minNight, maxNight, c);
            break;
          case MAX:
            data = t.getMaxAnnualTemp();
            toDraw = scaleColor(data, minMax, maxMax, c);
            break;
          case MIN:
            data = t.getMinAnnualTemp();
            toDraw = scaleColor(data, minMin, maxMin, c);
            break;
          case PREC:
            data = t.getRainfall();
            toDraw = scaleColor(data, minPrec, maxPrec, c);
            break;
        }
        Point2D p = new Point2D.Double(t.getLon(), t.getLat());
        Point2D dst = new Point2D.Double();
        transform.transform(p, dst);
        tileRect = new Rectangle2D.Double(dst.getX(), dst.getY(), 2, 2);
        g2.setColor(toDraw);
        g2.fill(tileRect);
      }
    }

    private Color scaleColor(float data, float min, float max, Color c)
    {
      float rng = max - min;
      float scale = (data - min) / rng;
      int r = (int) (c.getRed() * scale);
      int g = (int) (c.getGreen() * scale);
      int b = (int) (c.getBlue() * scale);
      return new Color(r, g, b);
    }

    private void drawRects(Graphics2D g2)
    {
      g2.setColor(Color.blue);
      g2.draw(R_ELEV);
      g2.drawString("ELEV", (int) R_ELEV.getX() + 5, (int) R_ELEV.getMaxY() - 5);
      g2.draw(R_DAY);
      g2.drawString("DAY", (int) R_DAY.getX() + 5, (int) R_DAY.getMaxY() - 5);
      g2.draw(R_NIGHT);
      g2.drawString("NIGHT", (int) R_NIGHT.getX() + 5, (int) R_NIGHT.getMaxY() - 5);
      g2.draw(R_MAX);
      g2.drawString("MAX", (int) R_MAX.getX() + 5, (int) R_MAX.getMaxY() - 5);
      g2.draw(R_MIN);
      g2.drawString("MIN", (int) R_MIN.getX() + 5, (int) R_MIN.getMaxY() - 5);
      g2.draw(R_PREC);
      g2.drawString("PRECIP", (int) R_PREC.getX() + 5, (int) R_PREC.getMaxY() - 5);
    }
  }

}
