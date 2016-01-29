package starvationevasion.client.Aegislash.io;


import starvationevasion.common.EnumRegion;
import starvationevasion.common.SpecialEventData;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;

/**
 * CropCSVLoader contains methods for parsing Crop data in csv file, creating a list
 * data objects.
 *
 * @author  jessica
 * @version Mar-21-2015
 */
public class SpecialEventCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(CountryCSVLoader.class.getName());
  private static final String PATH = "/sim/SpecialEvents1980To2014.csv";
  private ArrayList<SpecialEventData> eventDatum;

  private enum EnumHeader
  {
    event, year, latitude, longitude, type, region, totalProductionInDollarsPriorToEvent, damageFromEventInDollars, severity;
    public static final int SIZE = values().length;
  }

  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public SpecialEventCSVLoader() throws FileNotFoundException
  {

    CSVReader fileReader = new CSVReader(PATH, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);
    String currentField;
    System.out.println("ENUM HEADER SIZE "+EnumHeader.SIZE);
    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      currentField = fieldList[i];
      System.out.println("Header Name "+header.name()+" fieldList "+currentField);

      if (!currentField.equalsIgnoreCase(header.name()))
      {
        LOGGER.severe("**ERROR** Reading " + PATH +
                "Expected header[" + i + "]=" + header + ", Found: " + fieldList[i]);
        return;
      }
    }
    System.out.println("Events HEADER VALIDATED");
    fileReader.trashRecord();

    // Implementation notes : The CSV file contains 2014 numbers for production, etc. Each row
    // includes a column at the end that converts 2014 production and farm income to 1981.
    // It is an int percentage to be multiplied onto the 2014 production and income by to get
    // the corrosponding value for 1981.  For example CA is 61, so in 1981 they had 61% of
    // their current production and income.
    //
    eventDatum = new ArrayList<>();

    while ((fieldList = fileReader.readRecord(EnumHeader.SIZE)) != null)
    {
      currentField = fieldList[0];
      SpecialEventData sed = new SpecialEventData(currentField);
      System.out.println("SPECIAL EVENT RECORD " + currentField);
      for (EnumHeader header : EnumHeader.values())
      {
        //System.out.println("SPECIAL EVENT RECORD " + currentField + " HEADER "+header.name());
        String value = null;
        switch (header)
        {
          case year:
            value = fieldList[EnumHeader.year.ordinal()];
            //System.out.println("\tYear " + value);
            if (!value.equals("")) sed.setYear(Integer.parseInt(value));
            break;

          case latitude:
            value = fieldList[EnumHeader.latitude.ordinal()];
            //System.out.println("\tLatitude " + value);
            if (!value.equals("")) sed.setLatitude(Float.parseFloat(value));
            //sed.setLatitude(Float.parseFloat(fieldList[EnumHeader.latitude.ordinal()]));
            break;

          case longitude:
            value = fieldList[EnumHeader.longitude.ordinal()];
            //System.out.println("\tLongitude " + value);
            if (!value.equals("")) sed.setLongitude(Float.parseFloat(value));
            break;

          case type:
            value = fieldList[EnumHeader.type.ordinal()];
            //System.out.println("\tType " + value);
            if (!value.equals("")) sed.setType(SpecialEventData.EnumSpecialEvent.valueOf(value));
            break;

          case region:
            String reg = fieldList[EnumHeader.region.ordinal()];
            //System.out.println("Regions "+reg);

            if (reg.contains("/"))
            {

              //the event spanned multiple regions
              String[] regions = reg.split("/");
              for (String region : regions)
              {
                //System.out.println("Multiple Regions "+region);
                sed.addRegion(EnumRegion.valueOf(region.toUpperCase()));
              }

            }
            else
            {
              //single region
              //System.out.println("Single Regions "+reg.toUpperCase());
              sed.addRegion(EnumRegion.valueOf(reg.toUpperCase()));
            }

            break;

          case totalProductionInDollarsPriorToEvent:

            break;
          /**
           * These Last two cases are breaking. The Parser will sometimes
           * have the indexes for these items. Intermittent error, cause
           * undetermined.
           */
          case damageFromEventInDollars:
            //System.out.println("Damage Fields List size "+fieldList.length+" Ord "+EnumHeader.damageFromEventInDollars.ordinal());
            //value = fieldList[EnumHeader.damageFromEventInDollars.ordinal()];
            //System.out.println("Damage "+value);
            //if (!value.equals("")) sed.setDollarsInDamage(Long.parseLong(value));
            break;

          case severity:
            //System.out.println("Severity Fields List size "+fieldList.length+" Ord "+EnumHeader.severity.ordinal());
            //value = fieldList[EnumHeader.severity.ordinal()];
            //System.out.println("Severity "+value);
            //if (!value.equals("")) sed.setSeverity(Float.parseFloat(value));
            break;
        }
      }
      eventDatum.add(sed);
    }

  }

  public ArrayList<SpecialEventData> getEventData()
  {
    return eventDatum;
  }
}
