package starvationevasion.io;


// import org.apache.commons.csv.CSVRecord;
// import org.apache.commons.csv.CSVParser;
// import org.apache.commons.csv.CSVFormat;

import starvationevasion.common.Constant;
import starvationevasion.sim.Territory;
import starvationevasion.common.EnumRegion;
import starvationevasion.io.CSVReader.CSVRecord;
import starvationevasion.sim.Region;
import starvationevasion.common.EnumFood;
import starvationevasion.io.CSVhelpers.CSVParsingException;
import starvationevasion.io.CSVhelpers.CountryCSVDataGenerator;
import starvationevasion.sim.EnumGrowMethod;

import java.io.*;
import java.util.*;
import java.lang.Integer;
import java.lang.Double;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CountryCSVLoader contains methods for parsing country data in csv file, creating list of
 * ag unit objects.
 * @author  jessica
 * @version Mar-21-2015
 */
public class CountryCSVLoader
{
  private static final String DATA_DIR_PATH = "/sim/WorldData/";
  private static final String DATA_FILE = "TerritoryFarmAreaAndIncome-2014.csv";

  private Collection<Territory> countries;        // collection populated by parsing csv
  private Collection<Territory> masterUnits; // collection passed in (i.e., after parsing xml)
  private File csvFile;
  private List<CSVRecord> records;
  private String[] headers;
  
  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public CountryCSVLoader(Collection<Territory> countriesToMerge)
  {
    this.masterUnits = countriesToMerge;
    countries = new ArrayList<Territory>();
  }

  /**
   * Parses csv file with predetermined path; uses data from csv file to populate fields of
   * country objects passed in constructor.
   * @return  same country objects passed in, with fields populated from csv data (if possible)
   */
  public ParsedData getCountriesFromCSV(Region[] regionList) throws FileNotFoundException
  {
    boolean parsedOk = false;
    // create collection of countries from CSV
    while (parsedOk == false) parsedOk = parseCountries();
    // merge those countries with the ones passed in
    for (Territory xmlCountry: masterUnits)
    {
      String xmlCountryName = xmlCountry.getName();
      boolean countryFound = false;
      Iterator<Territory> csvItr = countries.iterator();
      while (csvItr.hasNext())
      {
        Territory csvCountry = csvItr.next();
        if (xmlCountryName.equals(csvCountry.getName()))
        {
          // copy data from csv country object into xml country object
          xmlCountry.copyInitialValuesFrom(csvCountry);
          // remove country from csv list after copying it
          csvItr.remove();

          // The XML object is considered the final object.  Map it to the region.
          //
          String regionStr = csvCountry.getGameRegion().name();
          EnumRegion enumRegion = EnumRegion.valueOf(regionStr);
          regionList[enumRegion.ordinal()].addTerritory(xmlCountry);

          countryFound = true;
          break;
        }
      }

      if (countryFound == false)
      {
        //todo add method (String nameOf Territory) -> offending xml file, -> load in XML editor.
        System.err.print("CSV data not found for country " + xmlCountryName + "\n");
      }
    }
    
    // if not all csv data copied, print message
    if (countries.isEmpty() == false)
    {
      for (Territory country:countries)
      {
        System.err.print("XML data not found for country "+country.getName()+"\n");
      }
    }

    return new ParsedData(masterUnits, regionList);
  }

  /**
   * Parses country data from DATA_FILE, adds countries to countries list.
   */ 
  private boolean parseCountries() throws FileNotFoundException
  {
    boolean parsedOk = false;
    ArrayList<Territory> tempCountryList = new ArrayList<Territory>();
    getRecords();

    for (CSVRecord record:records)
    {
      Territory unit;

      // The spring 2015 data file has a line immediately following the header that
      // indicates the data type of each column.  This line has been removed from
      // the Fall 2015 data file.
      //
      // My temporary Fall 2015 file has an extra row of labels following header that
      // indicates the contents of each column.
      //
      if (record.getRecordNumber() == 1) continue; // skip line

      // if name in file, make country
      try {
        String name = record.get("territory");
        if (name != null && name.isEmpty() == false) { // We use the generic AgUnit here.
          //
          unit = new Territory(name);
        } else throw new CSVParsingException("territory", record, this.csvFile);

        // The Fall 2015 data adds regions.
        //
        String regionName = record.get("region");
        if (regionName != null && regionName.isEmpty() == false)
        {
          EnumRegion region = EnumRegion.valueOf(regionName);
          if (region != null) unit.setGameRegion(region);
          else Logger.getGlobal().log(Level.SEVERE, "CSV record " + name + " undefined region " + regionName);
        }
        else
        { Logger.getGlobal().log(Level.SEVERE, "CSV record " + name + " is missing region");
        }

        setEssentialFields(unit, record);
        setNonessentialFields(unit, record);

        tempCountryList.add(unit);
      }
      // if name or essential fields empty, edit file
      catch (CSVParsingException exception)
      {
        // PAB : This used to invoke an error message in the GUI.
        //
        exception.printStackTrace();
        // callEditor(exception);
        return parsedOk;
      }
    }
    countries.addAll(tempCountryList);
    return true;
  }

  private double getValue(Map<String, String> record, String key)
  {
    String data = record.get(key);

	if (data == null || data.isEmpty()) return 0.;

	return Double.parseDouble(data);
  }
   
  /**
   * Set population and land area
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setEssentialFields(Territory country, CSVRecord record)
  {
    String landArea = record.get("landArea");
    try
    {
      country.setLandTotal(Constant.FIRST_YEAR, Double.parseDouble(landArea));
    }
    catch (IllegalArgumentException e)
    {
      Logger.getGlobal().log(Level.SEVERE, "CSVLoader: " + country.getName() + " Invalid land area");
      // throw new CSVParsingException("landArea", record, this.csvFile);
    }
  }

  /**
   * Set fields other than name, population, and total land area.
   * If any field missing, its value must be determined.
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setNonessentialFields(Territory country, CSVRecord record)
  {
    Map<String,String> recordMap = record.toMap();
    setDemographicData(country,recordMap);    
    // crop data; note can't do this before setting population
    setCropData(country, recordMap);
    // grow method percentages
    setGrowMethodData(country, recordMap);
  }

  /** Linear interpolate population.
  */
  private void interpolatePopulation(Territory territory, int year0, int year1)
  {
    int y0 = territory.getPopulation(year0);
    int y1 = territory.getPopulation(year1);

    for (int i = year0 + 1 ; i < year1 ; i += 1)
    {
      double y = y0 + (y1 - y0) * (((double) i - year0) / (year1 - year0));
      territory.setPopulation(i, (int) y);
    }
  }

  /**
   * Set averageAge, births, mortality, migration, undernourish fields.
   * @param territory     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setDemographicData(Territory territory, Map<String,String> recordMap)
  {
    String[] demographicFields = {
            "population1981", "population1990", "population2000", "population2010", "population2014", "population2025", "population2050",
            "averageAge", "births", "mortality", "migration", "undernourish"
    };
    
    for (int i = 0; i < demographicFields.length; i++)
    {
      String field = demographicFields[i];
      String value = recordMap.get(field);

      // Per Joel : Don't complain if the value is missing. We can see missing value in Excel.
      //
      if (value == null || value.isEmpty()) continue;

      try
      {
        switch (field)
        {
          case "population1981":
            territory.setPopulation(1981, Integer.parseInt(value));
            break;

          case "population1990":
            territory.setPopulation(1990, Integer.parseInt(value));
            break;

          case "population2000":
            territory.setPopulation(2000, Integer.parseInt(value));
            break;

          case "population2010":
            territory.setPopulation(2010, Integer.parseInt(value));
            break;

          case "population2014":
            territory.setPopulation(2014, Integer.parseInt(value));
            break;

          case "population2050":
            territory.setPopulation(2050, Integer.parseInt(value));
            break;

          case "averageAge":
            territory.setMedianAge(Integer.parseInt(value));
            break;

          case "births":
            double numValue = Double.parseDouble(value);
            territory.setBirths(numValue);
            break;

          case "mortality":
            numValue = Double.parseDouble(value);
            territory.setMortality(Constant.FIRST_YEAR, numValue);
            break;

          case "migration":
            numValue = Double.parseDouble(value);
            territory.setMigration(numValue);
            break;

          case "undernourish":
            numValue = Double.parseDouble(value);
            territory.setUndernourished(Constant.FIRST_YEAR, numValue / 100); // Convert to percent.
            break;

          case "arableOpen":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= territory.getLandTotal(Constant.FIRST_YEAR)) territory.setArableLand(Constant.FIRST_YEAR, numValue);
            else throw new IllegalArgumentException();
            break;

          default: ;
        }
      }
      catch (NumberFormatException e)
      {
        Logger.getGlobal().log(Level.SEVERE,
                "CSVLoader: " + territory.getName() + " Illegal value " + field + " = " + value);

        CountryCSVDataGenerator.fixDemographic(territory, field);
      }
      catch (IllegalArgumentException e)
      {
        // need to assign default value
        Logger.getGlobal().log(Level.INFO, "CSVLoader: {0} bad value for {1} {2}",
                new Object[] {territory.getName(), field, value});
        CountryCSVDataGenerator.fixDemographic(territory, field);
      }
    }

    // Linear interpolate population. Do this as needed rather than
    // generating a big data structure;
    //
    interpolatePopulation(territory, 1981, 1990);
    interpolatePopulation(territory, 1990, 2000);
    interpolatePopulation(territory, 2000, 2010);
    interpolatePopulation(territory, 2010, 2014);
    interpolatePopulation(territory, 2014, 2050);
  }
  
  /**
   * Set production, exports, imports, and land fields for each crop type in EnumFood.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setCropData(Territory country, Map<String,String> recordMap)
  {
    int[] income = new int[EnumFood.SIZE];
    int[] production = new int[EnumFood.SIZE];

    int totalFarmLand = (int) getValue(recordMap, "farmLand");

    income[EnumFood.CITRUS.ordinal()] = (int) getValue(recordMap, "incomeCitrusFruits");
    production[EnumFood.CITRUS.ordinal()] = (int) getValue(recordMap, "productionCitrusFruits");

    income[EnumFood.FRUIT.ordinal()] = (int) getValue(recordMap, "incomeNonCitrusFruits");
    production[EnumFood.FRUIT.ordinal()] = (int) getValue(recordMap, "productionNonCitrusFruits");

    income[EnumFood.NUT.ordinal()] = (int) getValue(recordMap, "incomeNuts");
    production[EnumFood.NUT.ordinal()] = (int) getValue(recordMap, "productionNuts");

    income[EnumFood.GRAIN.ordinal()] = (int) getValue(recordMap, "incomeGrains");
    production[EnumFood.GRAIN.ordinal()] = (int) getValue(recordMap, "productionGrains");

    income[EnumFood.OIL.ordinal()] = (int) getValue(recordMap, "incomeOilCrops");
    production[EnumFood.OIL.ordinal()] = (int) getValue(recordMap, "productionOilCrops");

    income[EnumFood.VEGGIES.ordinal()] = (int) getValue(recordMap, "incomeVegetables");
    production[EnumFood.VEGGIES.ordinal()] = (int) getValue(recordMap, "productionVegetables");

    income[EnumFood.SPECIAL.ordinal()] = (int) getValue(recordMap, "incomeSpecialtyCrops");
    production[EnumFood.SPECIAL.ordinal()] = (int) getValue(recordMap, "productionSpecialtyCrops");

    income[EnumFood.FEED.ordinal()] = (int) getValue(recordMap, "incomeFeedCrops");
    production[EnumFood.FEED.ordinal()] = (int) getValue(recordMap, "productionFeedCrops");

    income[EnumFood.FISH.ordinal()] = (int) getValue(recordMap, "incomeFish");
    production[EnumFood.FISH.ordinal()] = (int) getValue(recordMap, "productionFish");

    income[EnumFood.MEAT.ordinal()] = (int) getValue(recordMap, "incomeMeatAnimals");
    production[EnumFood.MEAT.ordinal()] = (int) getValue(recordMap, "productionMeatAnimals");

    income[EnumFood.POULTRY.ordinal()] = (int) getValue(recordMap, "incomePoultryAndEggs");
    production[EnumFood.POULTRY.ordinal()] = (int) getValue(recordMap, "productionPoultryAndEggs");

    income[EnumFood.DAIRY.ordinal()] = (int) getValue(recordMap, "incomeDairy");
    production[EnumFood.DAIRY.ordinal()] = (int) getValue(recordMap, "productionDairy");

    // double[] incomeToCategoryPercentages = new double[EnumFood.SIZE];
    // double[] adjustmentFactors = new double[EnumFood.SIZE];
    for (int i = 0; i < EnumFood.SIZE; i++)
    { EnumFood food = EnumFood.values()[i];

      country.setCropIncome(Constant.FIRST_YEAR, food, income[i]);
      country.setCropProduction(Constant.FIRST_YEAR, food, production[i]);
    }


    // PAB : This was the Spring 2015 computation.
    //
    //
    // double yield = production / land;
    // double tonsConsumed = production + imports - exports;

    // set values
    // country.setCropProduction(START_YEAR, crop, production);
    // country.setCropLand(START_YEAR, crop, land);
    // country.setCropYield(START_YEAR, crop, yield);
    // country.setCropNeedPerCapita(crop, tonsConsumed, country.getUndernourished(START_YEAR));
  }
  
  /**
   * Set percentage for each method in EnumGrowMethod.
   * @param agriculturalUnit     agriculturalUnit object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    agriculturalUnit's CSVRecord
   */
  private void setGrowMethodData(Territory agriculturalUnit, Map<String,String> recordMap)
  {
    double sum = 0;
    for (EnumGrowMethod method : EnumGrowMethod.values()) 
    {
      try
      {
        double value = 0;
        String methodString = method.toString().toLowerCase();
        value = Double.parseDouble(recordMap.get(methodString));
        if (value >= 0 && value <= 1)
        {
          agriculturalUnit.setMethodPercentage(Constant.FIRST_YEAR, method, value);
          sum += value;
        }
        else throw new IllegalArgumentException();
      }
      catch (IllegalArgumentException e) 
      {
        CountryCSVDataGenerator.fixGrowMethods(agriculturalUnit);
        return;
      }
    }
    if (sum != 1) CountryCSVDataGenerator.fixGrowMethods(agriculturalUnit);
  }

  /**
   * Parse the csv file specified by DATA_DIR_PATH+DATA_FILE. Use it
   * to populate a list of CSVRecords, assign list to records member variable.
   */
  private void getRecords() throws FileNotFoundException
  {
    try
    {
      String csvPath = DATA_DIR_PATH + DATA_FILE;
      InputStream stream = this.getClass().getResourceAsStream(csvPath);
      if (stream == null) throw new FileNotFoundException(csvPath);

      CSVReader reader = new CSVReader();
      reader.read(stream);
      // csvFile = new File(DATA_DIR_PATH + DATA_FILE);
      // reader.read(new FileInputStream(csvFile));
      // CSVFormat format;
      // CSVParser parser;
      // format = CSVFormat.DEFAULT.withHeader();
      // parser = CSVParser.parse(csvFile, StandardCharsets.US_ASCII, format);
      headers = reader.getHeaders();
      records = reader.getRecords();
      reader.close();
    }
    catch (IOException e)
    {
      System.err.println("Territory data file not found");
    }
  }
  
  private void copyCropValues(Territory countryFinal, Territory agriculturalUnitTemp)
  {
    for (EnumFood crop:EnumFood.values())
    {
      // double imports = agriculturalUnitTemp.getCropImport(START_YEAR, crop);
      // double exports = agriculturalUnitTemp.getCropExport(START_YEAR, crop);
      double production = agriculturalUnitTemp.getCropProduction(Constant.FIRST_YEAR, crop);
      double land = agriculturalUnitTemp.getCropLand(Constant.FIRST_YEAR, crop);
      double yield = agriculturalUnitTemp.getCropYield(Constant.FIRST_YEAR, crop);
      double need = agriculturalUnitTemp.getCropNeedPerCapita(crop);

      // countryFinal.setCropImport(START_YEAR, crop, imports);
      // countryFinal.setCropExport(START_YEAR, crop, exports);
      countryFinal.setCropProduction(Constant.FIRST_YEAR, crop, production);
      countryFinal.setCropLand(Constant.FIRST_YEAR, crop, land);
      countryFinal.setCropYield(Constant.FIRST_YEAR, crop, yield);
      countryFinal.setCropNeedPerCapita(crop, need);
    }
  }
  


  public static class ParsedData
  {
    final public Collection<Territory> territories;
    final public Region[] regionList;

    protected ParsedData(final Collection<Territory> territories, Region[] regionList)
    {
      this.territories = territories;
      this.regionList = regionList;
    }
  }
}
