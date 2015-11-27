package starvationevasion.io.XMLparsers;

/**
 * Created by winston on 1/20/15.
 * phase 1
 * CS 351 spring 2015
 */

import starvationevasion.geography.GeographicArea;
import starvationevasion.geography.MapPoint;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The class handles the parsing of the region data from XML into region objects.
 */
public class RegionParserHandler extends DefaultHandler
{
  private List<GeographicArea> regionList;
  private Locator locator;
  private GeographicArea tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private boolean nameTag;


  /**
   * The method is to be called AFTER this class has been used in parsing.
   * Other wise this returns null. So check for null exceptions!
   *
   * @return list of regions from the last file parsed.
   */

  public List<GeographicArea> getRegionList()
  {
    return regionList;
  }

  /**
   * This is used to extract the line number from a parsing error
   *
   * @return Locator representing a location in file.
   */
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
    regionList = new ArrayList<>();
    tmpPerimeterSet = new LinkedList<>();
  }

  @Override
  public void startElement(String namespaceURI, String localName,
                           String qName, Attributes atts)
  throws SAXException
  {
    switch (qName)
    {
    /*
     * entering a new area tag.
     */
      case "area":
        tmpRegion = new GeographicArea();
        break;
    /*
     * sets flag to extract content of the same tag.
     */
      case "name":
        nameTag = true;
        break;

    /*
     * because the vertex tag only has atts and no content, we do not need
     * to set a flag as we did above.
     */
      case "vertex":
        double lat = 0;
        double lon = 0;
        try
        {
          lat = Double.parseDouble(atts.getValue("lat"));
          lon = Double.parseDouble(atts.getValue("lon"));
        }
        catch (Exception e)
        {
          System.out.println(locator.getLineNumber());
          fatalError(new SAXParseException("Could not parse lat/lon.", locator));
        }
        tmpPerimeterSet.add(new MapPoint(lon, lat));
        break;

      case "region":  // no nothing, this is a just a container tag.
        break;

      default:
        String msg = qName + " is not a recognized tag.";
        fatalError(new SAXParseException(msg, locator));
    }
  }


  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (nameTag && tmpRegion != null)
    {
      nameTag = false;
      tmpRegion.setName(new String(ch, start, length));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  throws SAXException
  {
    if (qName.equals("area"))
    {
      // save and reset....
      tmpRegion.setPerimeter(new ArrayList<MapPoint>(tmpPerimeterSet));
      regionList.add(tmpRegion);
      tmpPerimeterSet.clear();
    }
  }
}


