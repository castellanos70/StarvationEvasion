package starvationevasion.io.XMLparsers;

import org.xml.sax.*;
import starvationevasion.sim.GeographicArea;
import starvationevasion.common.MapPoint;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author david
 *         created: 2015-01-29<br>
 *         description:
 */
public class KMLParser extends RegionParserHandler
{
  private List<GeographicArea> regions;
  
  private String regName;
  private String cleanCoordString;
  
  private boolean nameTag;
  private boolean coordTag;
  
  
  @Override
  public List<GeographicArea> getRegionList()
  {
    return regions;
  }

  @Override
  public void startDocument() throws SAXException
  {
    regions = new ArrayList<>();
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
  {
    switch(qName)
    {
      case "name":
        nameTag = true;
        break;
      case "coordinates":
        coordTag = true;
        cleanCoordString = "";
        break;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if(nameTag)
    {
      regName = new String(ch, start, length);
    }
    if(coordTag)
    {
      cleanCoordString += new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    switch (qName)
    {
      case "name":
        nameTag = false;
        break;
      case "coordinates":
        coordTag = false;
        GeographicArea r = new GeographicArea();
        r.setName(regName);
        r.setPerimeter(parseCoordString());
        regions.add(r);
    }
  }
  
  private List<MapPoint> parseCoordString()
  {
    List <MapPoint> l = new ArrayList<>();
    
    for(String s : cleanCoordString.trim().split("\\s+"))
    {
      String nums[] = s.split(",");
      double lon = Double.parseDouble(nums[0]);
      double lat = Double.parseDouble(nums[1]);
      l.add(new MapPoint(lon, lat));
    }
    return l;
  }

  public static Collection<GeographicArea> getRegionsFromFile(String filePath)
  {
    try
    {
      XMLReader kmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      KMLParser kmlParser = new KMLParser();
      kmlReader.setContentHandler(kmlParser);

      InputStream resourceStream = KMLParser.class.getResourceAsStream(filePath);
      BufferedInputStream inputStream = new BufferedInputStream(resourceStream);

      kmlReader.parse(new InputSource(inputStream));
      return kmlParser.getRegionList();
      
    } 
    /* I had no idea this was legal.
     * Bad form, but works for proof of kml parsing *  */
    catch (SAXException | ParserConfigurationException | IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
