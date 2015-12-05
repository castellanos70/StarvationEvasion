package starvationevasion.io.XMLparsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import starvationevasion.sim.Territory;
import starvationevasion.io.GeographyValidator;
import starvationevasion.sim.GeographicArea;
import starvationevasion.common.MapPoint;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static starvationevasion.io.IOHelpers.readIndex;

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

  private List<GeographicArea> regionList;
  private Locator locator;
  private String regionName;
  private String regionType;
  private GeographicArea tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private GeographyValidator regionValidator = new GeographyValidator();
  private boolean name;

  public Locator getLocator()
  {
    return locator;
  }

  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  @Override
  public void startDocument() throws SAXException
  {
    tmpPerimeterSet = new LinkedList<>();
    regionName = null;
    regionType = null;
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
        regionType = qName;
        break;

      case "area":
        tmpRegion = new GeographicArea();
        tmpRegion.setName(regionName);
        tmpRegion.setType(regionType);
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

      if (regionName != null)
      {
        String msg = "(!) Duplicate name tag. no good.";
        fatalError(new SAXParseException(msg, locator));
      }

      regionName = new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  throws SAXException
  {
    if (qName.equals("area"))
    {
      // save and reset....
      tmpRegion.setPerimeter(new ArrayList<>(tmpPerimeterSet));

      if (REGION_VALIDATION) regionValidator.validate(tmpRegion);
      regionList.add(tmpRegion);
      tmpPerimeterSet.clear();
    }
  }

  public List<GeographicArea> getGeography()
  {
    if (regionList == null)
    {
      try
      {
        // Generate the sorted list of territories.
        //
        generateRegions();
      }
      catch (ParserConfigurationException | SAXException | IOException ex)
      {
        Logger.getGlobal().log(Level.SEVERE, "Error parsing region list", ex);
      }
    }
    return regionList;
  }

  /* private method to generate the set of geographical regions
  */
  private void generateRegions() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException
  {
    regionList = new ArrayList<>();
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    xmlReader.setContentHandler(this);

    // List<String> filesToRead = getFilesInDir(BORDERS_DIR);
    List<String> filesToRead = readIndex(BORDERS_INDEX);
    while (!filesToRead.isEmpty())
    {
      String file = filesToRead.remove(0);
      try
      {
        String resourcePath = BORDERS_DIR + '/' + file;
        InputStream resourceStream = KMLParser.class.getResourceAsStream(resourcePath);
        if (resourceStream == null) throw new FileNotFoundException(resourcePath);

        BufferedInputStream inputStream = new BufferedInputStream(resourceStream);
        xmlReader.parse(new InputSource(inputStream));
      } catch (SAXException | IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
