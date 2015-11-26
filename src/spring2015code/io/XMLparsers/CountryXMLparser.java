package spring2015code.io.XMLparsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import spring2015code.io.RegionValidator;
import spring2015code.model.geography.AgriculturalUnit;
import spring2015code.model.geography.GeographicArea;
import spring2015code.model.MapPoint;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

import static spring2015code.io.IOHelpers.convertToFileURL;
import static spring2015code.io.IOHelpers.getFilesInDir;

/**
 * Created by winston on 3/21/15.
 * Reads in the country XML data and produces a set of regions.
 *
 * also provides supporting method to link regions to countries and generate
 * a set of liked countries form regions.
 */
public class CountryXMLparser extends DefaultHandler
{
  // this flag should be turned on when development team receives finalized
  // country xml data.
  private static boolean REGION_VALIDATION = false;
  private static String COUNTRY_DIR = "resources/countries";

  // PAB : This was a hook into last semester's XML editor.
  //
  // private XMLEditor editor;

  private Collection<GeographicArea> regionList;
  private Locator locator;
  private String countryName;
  private GeographicArea tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private RegionValidator regionValidator = new RegionValidator();
  private boolean name;

  /**
   * generates a set of Countries from a list of regions. liked properly together.
   * @param regions list to derive countries from
   * @return collection of countries created form the given regions.
   */
  public static Collection<AgriculturalUnit> RegionsToCountries(Collection<GeographicArea> regions)
  {
    HashMap<String, AgriculturalUnit> nameToCountry = new HashMap<>();

    for (GeographicArea region : regions)
    {
      if ( ! nameToCountry.containsKey(region.getName()))
      {
        AgriculturalUnit country = new AgriculturalUnit(region.getName());
        nameToCountry.put(region.getName(), country);
      }
      region.setAgriculturalUnit(nameToCountry.get(region.getName()));
    }

    return nameToCountry.values();
  }

  public static void main(String[] args)
  {
    System.out.println(new CountryXMLparser().getCountries().size());
  }

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
    countryName = null;
  }

  @Override
  public void startElement(String uri, String localName,
                           String qName, Attributes attributes) throws SAXException
  {
    qName = qName.toLowerCase();

    switch (qName)
    {
      case "country":
        //do nothing...
        break;

      case "area":
        tmpRegion = new GeographicArea();
        tmpRegion.setName(countryName);
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
        tmpPerimeterSet.add(new MapPoint(lon, lat));
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

      if (countryName != null)
      {
        String msg = "(!) Duplicate name tag. no good.";
        fatalError(new SAXParseException(msg, locator));
      }

      countryName = new String(ch, start, length);
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

  public Collection<GeographicArea> getRegionList()
  {
    if (regionList == null)
    {
      try
      {
        generateRegions();
      }
      catch (ParserConfigurationException e)
      {
        e.printStackTrace();
      }
      catch (SAXException e)
      {
        e.printStackTrace();
      }
    }
    return regionList;
  }

  /* private method to generate the set of regions*/
  private void generateRegions()
  throws ParserConfigurationException, SAXException
  {
    regionList = new ArrayList<>();
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    xmlReader.setContentHandler(this);

    List<String> filesToRead = getFilesInDir(COUNTRY_DIR);

    while (!filesToRead.isEmpty())
    {
      String file = filesToRead.remove(0);
      try
      {
        xmlReader.parse(convertToFileURL(file));
      } catch (SAXException e)
      {
        String errorMessage = "";

        // PAB : It looks like the Phase 2 project allowed the user to select
        // if (editor == null) editor = new XMLEditor(); // to be lazy
        // editor.loadFile(file);

        // Locator locator = getLocator();

        // if (locator.getLineNumber() != -1)
        // {
        //  // we know the line that the error happened on
        //  editor.highlightLine(locator.getLineNumber() - 1);
        //  editor.setCaretToLine(locator.getLineNumber() - 1);
        //  errorMessage = "line " + locator.getLineNumber() + ": ";
        // }
        // editor.setTitle("editing: " + file);
        // editor.setErrorMessage(errorMessage + e.getMessage());
        // editor.setVisible(true);

        // if (!editor.getIgnoreFile())
        // {
        //   filesToRead.add(0, file);
        // }

        filesToRead.add(0, file);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * convenience method, simply composes the following two functions:
   *
   * RegionsToCountries() of getRegionList()
   *
   * @return
   */
  public Collection<AgriculturalUnit> getCountries()
  {
    return RegionsToCountries(getRegionList());
  }
}
