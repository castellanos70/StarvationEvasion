package starvationevasion.io;


// import org.apache.commons.csv.CSVRecord;
// import org.apache.commons.csv.CSVParser;
// import org.apache.commons.csv.CSVFormat;

import spring2015code.model.geography.Territory;
import starvationevasion.common.EnumRegion;
import starvationevasion.io.CSVReader.CSVRecord;
import spring2015code.model.geography.Region;
import starvationevasion.common.EnumFood;
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
          xmlCountry = copyCountryData(xmlCountry, csvCountry);
          // remove country from csv list after copying it
          csvItr.remove();

          // The XML object is considered the final object.  Map it to the region.
          //
          String region = csvCountry.getGameRegion().name();
          EnumRegion enumRegion = EnumRegion.valueOf(region);
          Region r = regions.get(region);
          if (r == null)
          {
            r = new Region(enumRegion);
            regions.put(region, r);
          }

          r.addRegion(xmlCountry);

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

    return new ParsedData(masterUnits, regions.values());
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
      country.setLandTotal(START_YEAR, Double.parseDouble(landArea));
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
  
  /**
   * Set averageAge, birthRate, mortality, migration, undernourish, and arableOpen fields.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setDemographicData(Territory country, Map<String,String> recordMap)
  {
    String[] demographicFields = {"averageAge", "birthRate", "mortality", "migration", "undernourish", "arableOpen"};
    
    for (int i = 0; i < demographicFields.length; i++)
    {
      String field = demographicFields[i];
      String value = recordMap.get(field);

      try
      {
        switch (field)
        {
          case "averageAge":
            country.setMedianAge(Integer.parseInt(value));
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
            if (numValue >= 0 && numValue <= 100) country.setUndernourished(START_YEAR, numValue / 100); // Convert to percent.
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
  private void setCropData(Territory country, Map<String,String> recordMap)
  {
    int[] incomePerCategory = new int[EnumFood.SIZE];
    int[] productionPerCategory = new int[EnumFood.SIZE];
    double[] incomeToCategoryPercentages = new double[EnumFood.SIZE];
    double[] adjustmentFactors = new double[EnumFood.SIZE];

    int totalFarmLand = (int) getValue(recordMap, "farmLand");

    incomePerCategory[EnumFood.CITRUS.ordinal()] = (int) getValue(recordMap, "incomeCitrusFruits");
    productionPerCategory[EnumFood.CITRUS.ordinal()] = (int) getValue(recordMap, "productionCitrusFruits");

    incomePerCategory[EnumFood.FRUIT.ordinal()] = (int) getValue(recordMap, "incomeNonCitrusFruits");
    productionPerCategory[EnumFood.FRUIT.ordinal()] = (int) getValue(recordMap, "productionNonCitrusFruits");

    incomePerCategory[EnumFood.NUT.ordinal()] = (int) getValue(recordMap, "incomeNuts");
    productionPerCategory[EnumFood.NUT.ordinal()] = (int) getValue(recordMap, "productionNuts");

    incomePerCategory[EnumFood.GRAIN.ordinal()] = (int) getValue(recordMap, "incomeGrains");
    productionPerCategory[EnumFood.GRAIN.ordinal()] = (int) getValue(recordMap, "productionGrains");

    incomePerCategory[EnumFood.OIL.ordinal()] = (int) getValue(recordMap, "incomeOilCrops");
    productionPerCategory[EnumFood.OIL.ordinal()] = (int) getValue(recordMap, "productionOilCrops");

    incomePerCategory[EnumFood.VEGGIES.ordinal()] = (int) getValue(recordMap, "incomeVegetables");
    productionPerCategory[EnumFood.VEGGIES.ordinal()] = (int) getValue(recordMap, "productionVegetables");

    incomePerCategory[EnumFood.SPECIAL.ordinal()] = (int) getValue(recordMap, "incomeSpecialtyCrops");
    productionPerCategory[EnumFood.SPECIAL.ordinal()] = (int) getValue(recordMap, "productionSpecialtyCrops");

    incomePerCategory[EnumFood.FEED.ordinal()] = (int) getValue(recordMap, "incomeFeedCrops");
    productionPerCategory[EnumFood.FEED.ordinal()] = (int) getValue(recordMap, "productionFeedCrops");

    incomePerCategory[EnumFood.FISH.ordinal()] = (int) getValue(recordMap, "incomeFish");
    productionPerCategory[EnumFood.FISH.ordinal()] = (int) getValue(recordMap, "productionFish");

    incomePerCategory[EnumFood.MEAT.ordinal()] = (int) getValue(recordMap, "incomeMeatAnimales");
    productionPerCategory[EnumFood.MEAT.ordinal()] = (int) getValue(recordMap, "productionMeatAnimales");

    incomePerCategory[EnumFood.POULTRY.ordinal()] = (int) getValue(recordMap, "incomePoultryAndEggs");
    productionPerCategory[EnumFood.POULTRY.ordinal()] = (int) getValue(recordMap, "productionPoultryAndEggs");

    incomePerCategory[EnumFood.DAIRY.ordinal()] = (int) getValue(recordMap, "incomeDairy");
    productionPerCategory[EnumFood.DAIRY.ordinal()] = (int) getValue(recordMap, "productionDairy");

    double totalIncome = 0., totalProduction = 0.;

    // 1. Tally total income
    for (int i = 0; i < EnumFood.SIZE; i++)
    { totalIncome += incomePerCategory[i];
      totalProduction += productionPerCategory[i];
    }

    for (int i = 0; i < EnumFood.SIZE; i++)
    { EnumFood food = EnumFood.values()[i];
      incomeToCategoryPercentages[i] = (double) incomePerCategory[i] / (double) totalIncome;

      double p = 0.;
      if (totalProduction != 0.) p = (double) productionPerCategory[i] / totalProduction;

      // This is an initial naive estimate.  Per Joel there will eventually be a multiplier
      // applied that gives a more realistic estimate.
      //
      double land = p * totalFarmLand /* * multiplier[food] */;
      country.setCropProduction(START_YEAR, food, productionPerCategory[i]);
      country.setCropLand(START_YEAR, food, land);

      double yield = productionPerCategory[i] / land;
      country.setCropYield(START_YEAR, food, yield);
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
      System.err.println("Territory data file not found");
    }
  }
  
  private Territory copyCountryData(Territory countryFinal, Territory countryTemp)
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
  
  private void copyCropValues(Territory countryFinal, Territory agriculturalUnitTemp)
  {
    for (EnumFood crop:EnumFood.values())
    {
      // double imports = agriculturalUnitTemp.getCropImport(START_YEAR, crop);
      // double exports = agriculturalUnitTemp.getCropExport(START_YEAR, crop);
      double production = agriculturalUnitTemp.getCropProduction(START_YEAR, crop);
      double land = agriculturalUnitTemp.getCropLand(START_YEAR, crop);
      double yield = agriculturalUnitTemp.getCropYield(START_YEAR, crop);
      double need = agriculturalUnitTemp.getCropNeedPerCapita(crop);

      // countryFinal.setCropImport(START_YEAR, crop, imports);
      // countryFinal.setCropExport(START_YEAR, crop, exports);
      countryFinal.setCropProduction(START_YEAR, crop, production);
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


    ArrayList<Territory> fakeXmlList = new ArrayList<Territory>();
    fakeXmlList.add(new Territory("Afghanistan"));
    //fakeXmlList.add(new Territory("Albania"));
    fakeXmlList.add(new Territory("Algeria"));
    fakeXmlList.add(new Territory("Vatican City"));
    CountryCSVLoader testLoader = new CountryCSVLoader(fakeXmlList);
    Collection<Territory> countryList;
    //List<Territory> countryList = new ArrayList<Territory>();

    ParsedData data;
    try {
      data = testLoader.getCountriesFromCSV();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return;
    }

    countryList = data.territories;

    System.out.println("Testing - main method in CountryCSVLoader");
    for (Territory ctry:countryList)
    {
      System.out.println(ctry.getName()+" "+ctry.getMethodPercentage(START_YEAR,EnumGrowMethod.ORGANIC));
      //System.out.println(ctry.getName()+" "+ctry.getPopulation(START_YEAR));
      //System.out.println(ctry.getName()+" "+ctry.getCropProduction(START_YEAR,EnumFood.GRAIN));
    }
  }

  public static class ParsedData
  {
    final public Collection<Territory> territories;
    final public Collection<Region> regions;

    protected ParsedData(final Collection<Territory> territories, final Collection<Region> regions)
    {
      this.territories = territories;
      this.regions = regions;
    }
  }
}
