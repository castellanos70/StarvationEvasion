package starvationevasion.sim.io;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import starvationevasion.common.GeographicArea;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Model;
import starvationevasion.sim.Territory;
import starvationevasion.util.Picture;

import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by winston on 3/21/15.
 * Reads in the country XML data and produces a set of regions.
 * <p>
 * also provides supporting method to link regions to countries and generate
 * a set of liked countries form regions.
 */
public class GeographyXMLparser extends DefaultHandler
{
  private static String BORDERS_DIR = "/sim/geography";
  private static String BORDERS_INDEX = BORDERS_DIR + "/PolygonBoarders.txt";

  private Model model;
  private Locator locator;
  private String territoryName;
  private Territory territory;
  private ArrayList<MapPoint> tmpPerimeterSet;
  private boolean name;
  private int islandCount;

  //private Picture pic;  //for debugging only
  //private Color[] colorList = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN,
  //  Color.BLUE, Color.MAGENTA}; //for debugging only

  public GeographyXMLparser(Model model)
  {
    this.model = model;
    //pic = new Picture(1600, 1000); //for debugging only.
    try
    {
      XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      xmlReader.setContentHandler(this);

      ArrayList<String> filesToRead = readIndex(BORDERS_INDEX);
      //System.out.println("GeographyXMLparser.generateRegions() filesToRead="+filesToRead.size());
      for (String fileName : filesToRead)
      {
        //System.out.println("     fileName="+fileName);
        String resourcePath = BORDERS_DIR + '/' + fileName;
        InputStream resourceStream = GeographyXMLparser.class.getResourceAsStream(resourcePath);

        BufferedInputStream inputStream = new BufferedInputStream(resourceStream);
        xmlReader.parse(new InputSource(inputStream));
      }
    } catch (Exception e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }


  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  @Override
  public void startDocument() throws SAXException
  {
    tmpPerimeterSet = new ArrayList<>();
    territoryName = null;
    islandCount = 0;
  }

  @Override
  public void startElement(String uri, String localName,
                           String qName, Attributes attributes) throws SAXException
  {
    qName = qName.toLowerCase();

    switch (qName)
    {
      case "country":
      case "state":
        //regionType = qName;
        break;
      case "area":
      case "hole":
        break;

      case "name":
        name = true;
        break;

      case "vertex":
        float lat = 0;
        float lon = 0;
        try
        {
          lat = Float.parseFloat(attributes.getValue("lat"));
          lon = Float.parseFloat(attributes.getValue("lon"));
        } catch (Exception e)
        {
          System.out.println(locator.getLineNumber());
          fatalError(new SAXParseException("Could not parse lat/lon.", locator));
        }
        tmpPerimeterSet.add(new MapPoint(lat, lon));
        break;

      default:
        String msg = qName + " is not a recognized tag.";
        fatalError(new SAXParseException(msg, locator));


    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (name)
    {
      name = false;

      if (territoryName != null)
      {
        String msg = "(!) Duplicate name tag. no good.";
        fatalError(new SAXParseException(msg, locator));
      }

      territoryName = new String(ch, start, length);
      //System.out.println("GeographyXMLparser.startElement(case=name): territoryName="+territoryName);

      territory = model.getTerritory(territoryName);
      if (territory == null)
      {
        System.out.println("*******ERROR*****: GeographyXMLparser.characters(): Territory Not Found: " + territoryName);
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  {
    try
    {
      //For Debugging only
      /*
      if (qName.equals("area") || qName.equals("hole"))
      {
        islandCount++;
        if (territory.getName().equals("Canada") || territory.getName().equals("US-Alaska"))
        {
          int colorIdx = Math.min(islandCount - 1, colorList.length - 1);
          Color edgeColor = colorList[colorIdx];

          if (territory.getName().equals("Canada")) edgeColor = Color.WHITE;
          else if (territory.getName().equals("US-Alaska")) edgeColor = Color.RED;

          drawBoundaryUsingMapPoints(pic, tmpPerimeterSet, edgeColor, -142, -128, 51,70);
        }
      }
      */


      if (qName.equals("area"))
      {
        territory.getGeographicArea().addToPerimeter(new ArrayList<>(tmpPerimeterSet), GeographicArea.BoundaryType
          .ISLAND);
        tmpPerimeterSet.clear();
      }
      else if (qName.equals("hole"))
      {
        territory.getGeographicArea().addToPerimeter(new ArrayList<>(tmpPerimeterSet), GeographicArea.BoundaryType
          .HOLE);
        tmpPerimeterSet.clear();
      }
    } catch (Exception ex)
    {
      ex.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, "Error parsing region list", ex);
      System.exit(0);
    }
  }


  public static ArrayList<String> readIndex(String indexPath)
  {
    ArrayList<String> files = new ArrayList<>();

    try
    {
      System.out.println("IOHelpers.readIndex(" + indexPath + ")");
      InputStream resourceStream = GeographyXMLparser.class.getResourceAsStream(indexPath);
      BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));


      String entry;
      while ((entry = reader.readLine()) != null)
      {
        files.add(entry);
        //System.out.println("      "+entry);
      }


      reader.close();
      resourceStream.close();

    } catch (IOException ex)
    {
      Logger.getGlobal().log(Level.SEVERE, "Error parsing geography index", ex);
      ex.printStackTrace();
      System.exit(1);
    }

    return files;
  }


  /**
   * This method is used only for testing the geographic boundaries.<br>
   * Given a Picture frame containing a Mollweide would map projection and a territory,
   * it draws the boundary of that territory on the map using different colors for
   * disconnected segments (islands) of the territory.
   */
  private static void drawBoundaryUsingMapPoints(Picture pic, ArrayList<MapPoint> island, Color color,
                                                 float minLon, float maxLon, float minLat, float maxLat)
  {

    float scaleX = pic.getImageWidth() / (maxLon - minLon);
    float scaleY = pic.getImageHeight() / (maxLat - minLat);
    float scale = Math.min(scaleX, scaleY);

    Graphics2D gfx = pic.getOffScreenGraphics();
    gfx.setColor(color);

    int lastX = Integer.MAX_VALUE;
    int lastY = Integer.MAX_VALUE;

    for (MapPoint mapPoint : island)
    {
      int x = (int) ((mapPoint.longitude - minLon) * scale);
      int y = pic.getImageHeight() - (int) ((mapPoint.latitude - minLat) * scale);

      if (lastX != Integer.MAX_VALUE)
      {
        gfx.drawLine(lastX, lastY, x, y);
      }
      lastX = x;
      lastY = y;
    }
    pic.repaint();
  }
}

