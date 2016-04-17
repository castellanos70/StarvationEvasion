package starvationevasion.sim.io.XMLparsers;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import starvationevasion.common.GeographicArea;
import starvationevasion.common.MapPoint;

/**
 * Created by winston on 3/21/15.
 * Reads in the country XML data and produces a set of regions.
 *
 * also provides supporting method to link regions to countries and generate
 * a set of liked countries form regions.
 */
public class GeographyXMLparser extends DefaultHandler
{
  // this flag should be turned on when development team receives finalized
  // country xml data.
  private static boolean REGION_VALIDATION = false;
  private static String BORDERS_DIR = "/sim/geography";
  private static String BORDERS_INDEX = BORDERS_DIR + "/PolygonBoarders.txt";

  private ArrayList<GeographicArea> geographicAreaList;
  private Locator locator;
  private String territoryName;

  private GeographicArea tmpGeographicArea;
  private List<MapPoint> tmpPerimeterSet;
  private boolean name;


  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  @Override
  public void startDocument() throws SAXException
  {
    tmpPerimeterSet = new LinkedList<>();
    territoryName = null;
  }

  @Override
  //public void startElement(String uri, String localName,
  //                         String qName, Attributes attributes) throws SAXException
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
        tmpGeographicArea = new GeographicArea(territoryName);
        break;

      case "name":
        name = true;
        break;

      case "vertex":
        double lat = 0;
        double lon = 0;
        try
        {
          lat = Double.parseDouble(attributes.getValue("lat"));
          lon = Double.parseDouble(attributes.getValue("lon"));
        }
        catch (Exception e)
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
      //System.out.println("!!!!!!!!!!!!!!!! GeographyXMLparser.characters() territoryName="+territoryName);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  {
    try
    {
      if (qName.equals("area"))
      {
        // save and reset....
        tmpGeographicArea.setPerimeter(new ArrayList<>(tmpPerimeterSet));

        if (REGION_VALIDATION) GeographicArea.validate(tmpGeographicArea);
        geographicAreaList.add(tmpGeographicArea);
        tmpPerimeterSet.clear();
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, "Error parsing region list", ex);
      System.exit(0);
    }
  }

  public ArrayList<GeographicArea> getGeography()
  {
    if (geographicAreaList == null)
    {
      try
      {
        // Generate the sorted list of territories.
        //
        generateRegions();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Logger.getGlobal().log(Level.SEVERE, "Error parsing region list", ex);
        System.exit(0);
      }
    }
    return geographicAreaList;
  }

  /**
  * private method to generate the set of geographical regions
  **/
  private void generateRegions()
  {
    try
    {
      geographicAreaList = new ArrayList<>();
      XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      xmlReader.setContentHandler(this);

      ArrayList<String> filesToRead = readIndex(BORDERS_INDEX);
      //System.out.println("GeographyXMLparser.generateRegions() filesToRead="+filesToRead.size());
      for (String fileName: filesToRead)
      {
        //System.out.println("     fileName="+fileName);
        String resourcePath = BORDERS_DIR + '/' + fileName;
        InputStream resourceStream = GeographyXMLparser.class.getResourceAsStream(resourcePath);

        BufferedInputStream inputStream = new BufferedInputStream(resourceStream);
        xmlReader.parse(new InputSource(inputStream));
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
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
}

