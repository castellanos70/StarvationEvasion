package starvationevasion.io;


// import org.apache.commons.csv.CSVRecord;
// import org.apache.commons.csv.CSVParser;
// import org.apache.commons.csv.CSVFormat;

import starvationevasion.io.CSVReader.CSVRecord;
import spring2015code.model.geography.Region;
import starvationevasion.common.EnumFood;
import spring2015code.model.geography.AgriculturalUnit;
import starvationevasion.io.CSVhelpers.CSVParsingException;
import starvationevasion.io.CSVhelpers.CountryCSVDataGenerator;
import spring2015code.common.EnumGrowMethod;
import spring2015code.common.AbstractScenario;

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
  private static final int START_YEAR = AbstractScenario.START_YEAR;

  private Map<String, Region> regions;        // Regions discovered parsing csv
  private Collection<AgriculturalUnit> countries;        // collection populated by parsing csv
  private Collection<AgriculturalUnit> masterUnits; // collection passed in (i.e., after parsing xml)
  private File csvFile;
  private List<CSVRecord> records;
  private String[] headers;
  
  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public CountryCSVLoader(Collection<AgriculturalUnit> countriesToMerge)
  {
    this.masterUnits = countriesToMerge;
    countries = new ArrayList<AgriculturalUnit>();
    regions = new HashMap<String, Region>();
  }

  /**
   * Parses csv file with predetermined path; uses data from csv file to populate fields of
   * country objects passed in constructor.
   * @return  same country objects passed in, with fields populated from csv data (if possible)
   */
  public ParsedData getCountriesFromCSV() throws FileNotFoundException
  {
    boolean parsedOk = false;
    // create collection of countries from CSV
    while (parsedOk == false) parsedOk = parseCountries();
    // merge those countries with the ones passed in
    for (AgriculturalUnit xmlCountry: masterUnits)
    {
      String xmlCountryName = xmlCountry.getName();
      boolean countryFound = false;
      Iterator<AgriculturalUnit> csvItr = countries.iterator();
      while (csvItr.hasNext())
      {
        AgriculturalUnit csvCountry = csvItr.next();
        if (xmlCountryName.equals(csvCountry.getName()))
        {
          // copy data from csv country object into xml country object
          xmlCountry = copyCountryData(xmlCountry, csvCountry);
          // remove country from csv list after copying it
          csvItr.remove();

          // The XML object is considered the final object.  Map it to the region.
          //
          String region = csvCountry.getGameRegion();
          Region r = regions.get(region);
          if (r == null)
          {
            r = new Region(region);
            regions.put(region, r);
          }

          r.addRegion(xmlCountry);

          countryFound = true;
          break;
        }
      }

      if (countryFound == false)
      {
        //todo add method (String nameOf AgriculturalUnit) -> offending xml file, -> load in XML editor.
        System.err.print("CSV data not found for country " + xmlCountryName + "\n");
      }
    }
    
    // if not all csv data copied, print message
    if (countries.isEmpty() == false)
    {
      for (AgriculturalUnit country:countries)
      {
        System.err.print("XML data not found for country "+country.getName()+"\n");
      }
    }

    return new ParsedData(masterUnits, regions.values());
  }

  /**
   * Parses country data from DATA_FILE, adds countries to countries list.
   */ 
  private boolean parseCountries() throws FileNotFoundException
  {
    boolean parsedOk = false;
    ArrayList<AgriculturalUnit> tempCountryList = new ArrayList<AgriculturalUnit>();
    getRecords();

    for (CSVRecord record:records)
    {
      AgriculturalUnit unit;

      // The spring 2015 data file has a line immediately following the header that
      // indicates the data type of each column.  This line has been removed from
      // the Fall 2015 data file.
      //
      // My temporary Fall 2015 file has an extra row of labels following header that
      // indicates the contents of each column.
      //
      if (record.getRecordNumber() == 1) continue; // skip line

      // if name in file, make country
      try
      {
        String name = record.get("country");
        if (name != null && name.isEmpty() == false)
        { // We use the generic AgUnit here.
          //
          unit = new AgriculturalUnit(name);
        }
        else throw new CSVParsingException("country", record, this.csvFile);

        // The Fall 2015 data adds regions.
        //
        String region = record.get("region");
        if (region == null || region.isEmpty()) region = "Not assigned";
        unit.setGameRegion(region);

        setEssentialFields(unit, record);
        setNonessentialFields(unit,record);
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
  
   
  /**
   * Set population and land area
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setEssentialFields(AgriculturalUnit country, CSVRecord record)
  {
    // The Spring 2015 data file has the field 'population'.  This has been
    // removed from the Fall data file.
    //
    String population = record.get("population");
    if (population != null && population.isEmpty() == false) {
      try {
        int value = Integer.parseInt(record.get("population"));
        if (value > 0) country.setPopulation(START_YEAR, value);
        else throw new IllegalArgumentException();
      } catch (IllegalArgumentException e)
      { // PAB : Log bad records, but don't throw an exception.
        Logger.getGlobal().log(Level.SEVERE, "CSVLoader: " + country.getName() + " Invalid population.");
        // throw new CSVParsingException("population", record, this.csvFile);
      }
    }

    String landArea = record.get("landArea");
    try
    {
      double value = Double.parseDouble(landArea);
      if (value > 0) country.setLandTotal(START_YEAR, value);
      else throw new IllegalArgumentException();
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
  private void setNonessentialFields(AgriculturalUnit country, CSVRecord record)
  {
    Map<String,String> recordMap = record.toMap();
    setDemographicData(country,recordMap);    
    // crop data; note can't do this before setting population
    setCropData(country, recordMap);
    // grow method percentages
    setGrowMethodData(country, recordMap);
  }
  
  /**
   * Set averageAge, birthRate, mortality, migration, undernourish, and arableOpen fields.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setDemographicData(AgriculturalUnit country, Map<String,String> recordMap)
  {
    String[] demographicFields = {"averageAge", "birthRate", "mortality", "migration", "undernourish",
                                  "arableOpen"};
    
    for (int i = 0; i < demographicFields.length; i++)
    {
      String field = demographicFields[i];
      String value = recordMap.get(field);

      try
      {
        switch (field)
        {
          case "averageAge":
            int intValue = Integer.parseInt(value);
            if (intValue > 0) country.setMedianAge(intValue);
            else throw new IllegalArgumentException(); 
            break;
          case "birthRate":
            double numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue < 2000) country.setBirthRate(numValue);
            else throw new IllegalArgumentException();
            break;
          case "mortality":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= 1000) country.setMortalityRate(START_YEAR, numValue);
            else throw new IllegalArgumentException();
            break;
          case "migration":
            numValue = Double.parseDouble(value);
            if (numValue >= -1000 && numValue <= 1000) country.setMigrationRate(numValue);
            else throw new IllegalArgumentException();
            break;
          case "undernourish":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= 100) country.setUndernourished(START_YEAR, numValue/100); //divide int by 100
            else throw new IllegalArgumentException();
            break;
          case "arableOpen":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= country.getLandTotal(START_YEAR)) country.setArableLand(START_YEAR, numValue);
            else throw new IllegalArgumentException();
            break;
          default: ;
        }
      }
      catch (NumberFormatException e)
      {
        Logger.getGlobal().log(Level.SEVERE,
                "CSVLoader: " + country.getName() + " Illegal value " + field + " = " + value);

        CountryCSVDataGenerator.fixDemographic(country, field);
      }
      catch (IllegalArgumentException | NullPointerException e)
      {
        // need to assign default value
        Logger.getGlobal().log(Level.INFO, "CSVLoader: " + country.getName() + " No value for " + field);
        CountryCSVDataGenerator.fixDemographic(country, field);
      }
    }
  }
  
  /**
   * Set production, exports, imports, and land fields for each crop type in EnumFood.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setCropData(AgriculturalUnit country, Map<String,String> recordMap)
  {
    boolean hasError = false;
    String[] cropFields = {"Production", "Exports", "Imports", "Land"};
    cropLoop:
    for (EnumFood crop : EnumFood.values())
    {
      Double production = null; // initialize as null objects to get rid of annoying error msg
      Double exports = null;
      Double imports = null;
      Double land = null;
      String cropString;

      // PAB : The Phase 2 code had a special type for 'other' that is no longer supported.
      //
      // if (crop == EnumFood.OTHER_CROPS) cropString = "other";
      // else cropString = crop.toString().toLowerCase();

      cropString = crop.toString().toLowerCase();

      for (int i = 0; i < cropFields.length; i++)
      {
        // concatenate to cornProduction, cornExports, etc.
        String cropField = cropFields[i];
        String key = cropString + cropFields[i];
        String data = recordMap.get(key);
        try
        {
          Double value = Double.parseDouble(data);
          if (value < 0) throw new IllegalArgumentException();
          switch (cropField)
          {
            case "Production":
               production = value;
              break;

            case "Exports":
              exports = value;
              break;

            case "Imports":
              imports = value;
              break;

            case "Land":
              if (value <= country.getLandTotal(START_YEAR)) land  = value;
              else throw new IllegalArgumentException();
              break;

            default:
              break;
          }
        }
        catch (NumberFormatException e)
        {
          Logger.getGlobal().log(Level.SEVERE,
                  "CSVLoader: " + country.getName() + " Illegal value " + key + " = " + data);

          CountryCSVDataGenerator.fixCropData(country, crop);
          hasError = true;
        }
        catch (IllegalArgumentException | NullPointerException e)
        {
          // need to assign default value
          Logger.getGlobal().log(Level.INFO, "CSVLoader: " + country.getName() + " No value for " + key);
          CountryCSVDataGenerator.fixCropData(country, crop);
          hasError = true;
        }
      }

      // PAB : Some fields may be null while we're integrating the new data file.
      // We've already emitted a message to that effect.  Now just ignore the data.
      //
      //
      double yield = 0., tonsConsumed = 0.;
      if (hasError == false) {
        yield = production / land;
        tonsConsumed = production + imports - exports;

        // set values
        country.setCropProduction(START_YEAR, crop, production);
        country.setCropExport(START_YEAR, crop, exports);
        country.setCropImport(START_YEAR, crop, imports);
        country.setCropLand(START_YEAR, crop, land);
        country.setCropYield(START_YEAR, crop, yield);
        country.setCropNeedPerCapita(crop, tonsConsumed, country.getUndernourished(START_YEAR));
      }
    }
  }
  
  /**
   * Set percentage for each method in EnumGrowMethod.
   * @param agriculturalUnit     agriculturalUnit object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    agriculturalUnit's CSVRecord
   */
  private void setGrowMethodData(AgriculturalUnit agriculturalUnit, Map<String,String> recordMap)
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
          agriculturalUnit.setMethodPercentage(START_YEAR, method, value);
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
      System.err.println("AgriculturalUnit data file not found");
    }
  }
  
  private AgriculturalUnit copyCountryData(AgriculturalUnit countryFinal, AgriculturalUnit countryTemp)
  {
    //countryTemp values
     int population = countryTemp.getPopulation(START_YEAR);
     double medianAge = countryTemp.getMedianAge(START_YEAR);
     double birthRate = countryTemp.getBirthRate(START_YEAR);
     double mortalityRate = countryTemp.getMortalityRate(START_YEAR);
     double migrationRate = countryTemp.getMigrationRate(START_YEAR);
     double undernourished = countryTemp.getUndernourished(START_YEAR);
     double landTotal = countryTemp.getLandTotal(START_YEAR);
     double landArable = countryTemp.getArableLand(START_YEAR);
     
     // copy everything
     countryFinal.setPopulation(START_YEAR, population);
     countryFinal.setMedianAge(medianAge);
     countryFinal.setBirthRate(birthRate);
     countryFinal.setMortalityRate(START_YEAR, mortalityRate);
     countryFinal.setMigrationRate(migrationRate);
     countryFinal.setUndernourished(START_YEAR, undernourished);
     countryFinal.setLandTotal(START_YEAR, landTotal);
     countryFinal.setArableLand(START_YEAR, landArable);
     
     copyCropValues(countryFinal, countryTemp);
    
     for (EnumGrowMethod method:EnumGrowMethod.values())
     {
       double percentage = countryTemp.getMethodPercentage(START_YEAR,method);
       countryFinal.setMethodPercentage(START_YEAR, method, percentage);
     }

     return countryFinal;
  }
  
  private void copyCropValues(AgriculturalUnit countryFinal, AgriculturalUnit agriculturalUnitTemp)
  {
    for (EnumFood crop:EnumFood.values())
    {
      double production = agriculturalUnitTemp.getCropProduction(START_YEAR, crop);
      double imports = agriculturalUnitTemp.getCropImport(START_YEAR, crop);
      double exports = agriculturalUnitTemp.getCropExport(START_YEAR, crop);
      double land = agriculturalUnitTemp.getCropLand(START_YEAR, crop);
      double yield = agriculturalUnitTemp.getCropYield(START_YEAR, crop);
      double need = agriculturalUnitTemp.getCropNeedPerCapita(crop);

      countryFinal.setCropProduction(START_YEAR, crop, production);
      countryFinal.setCropImport(START_YEAR, crop, imports);
      countryFinal.setCropExport(START_YEAR, crop, exports);
      countryFinal.setCropLand(START_YEAR, crop, land);
      countryFinal.setCropYield(START_YEAR, crop, yield);
      countryFinal.setCropNeedPerCapita(crop, need);
    }
  }
  
  
  public static void main(String[] args)
  {
    // First make sure that the data file can be seen from the class loader.
    //
    InputStream in = CountryCSVLoader.class.getResourceAsStream("/SomeTextFile.txt");


    ArrayList<AgriculturalUnit> fakeXmlList = new ArrayList<AgriculturalUnit>();
    fakeXmlList.add(new AgriculturalUnit("Afghanistan"));
    //fakeXmlList.add(new AgriculturalUnit("Albania"));
    fakeXmlList.add(new AgriculturalUnit("Algeria"));
    fakeXmlList.add(new AgriculturalUnit("Vatican City"));
    CountryCSVLoader testLoader = new CountryCSVLoader(fakeXmlList);
    Collection<AgriculturalUnit> countryList;
    //List<AgriculturalUnit> countryList = new ArrayList<AgriculturalUnit>();

    ParsedData data;
    try {
      data = testLoader.getCountriesFromCSV();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return;
    }

    countryList = data.territories;

    System.out.println("Testing - main method in CountryCSVLoader");
    for (AgriculturalUnit ctry:countryList)
    {
      System.out.println(ctry.getName()+" "+ctry.getMethodPercentage(START_YEAR,EnumGrowMethod.ORGANIC));
      //System.out.println(ctry.getName()+" "+ctry.getPopulation(START_YEAR));
      //System.out.println(ctry.getName()+" "+ctry.getCropProduction(START_YEAR,EnumFood.GRAIN));
    }
  }

  public static class ParsedData
  {
    final public Collection<AgriculturalUnit> territories;
    final public Collection<Region> regions;

    protected ParsedData(final Collection<AgriculturalUnit> territories, final Collection<Region> regions)
    {
      this.territories = territories;
      this.regions = regions;
    }
  }
}
