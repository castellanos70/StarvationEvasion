package starvationevasion.io;


import starvationevasion.sim.CropZoneData;
import starvationevasion.sim.Territory;
import starvationevasion.common.EnumFood;

import java.io.FileNotFoundException;
import java.util.*;
import java.lang.Integer;
import java.util.logging.Logger;

/**
 * CropCSVLoader contains methods for parsing Crop data in csv file, creating a list
 * data objects.
 *
 * @author  jessica
 * @version Mar-21-2015
 */
public class CropCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(CountryCSVLoader.class.getName());
  private static final String PATH = "/sim/CropData.csv";
  private ArrayList<CropZoneData> categoryData;

  private enum EnumHeader
  {
    category, price, land, water, fertilizerN, fertilizerP2O5, fertilizerK2O, growing, temperature, ideal, high, max;
    public static final int SIZE = values().length;
  };

  private enum EnumCategories
  {
    CITRUS, FRUIT, NUT, GRAIN, OIL, VEGGIES, SPECIAL, FEED, FISH, MEAT, POULTRY, DAIRY;
    public static final int SIZE = values().length;
  };

  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public CropCSVLoader() throws FileNotFoundException
  {
    CSVReader fileReader = new CSVReader(PATH, 0);

    //Check header
    String[] fieldList = fileReader.readRecord(EnumHeader.SIZE);
    String currentField;
    //System.out.println("ENUM HEADER SIZE "+EnumHeader.SIZE);
    for (EnumHeader header : EnumHeader.values())
    {
      int i = header.ordinal();
      currentField = fieldList[i];
      //System.out.println("Header Name "+header.name()+" fieldList "+currentField);
      if (!currentField.contains(header.name()))
      {
        LOGGER.severe("**ERROR** Reading " + PATH +
                "Expected header[" + i + "]=" + header + ", Found: " + fieldList[i]);
        return;
      }
    }
    //System.out.println("HEADER VALIDATED");
    fileReader.trashRecord();

    // Implementation notes : The CSV file contains 2014 numbers for production, etc. Each row
    // includes a column at the end that converts 2014 production and farm income to 1981.
    // It is an int percentage to be multiplied onto the 2014 production and income by to get
    // the corrosponding value for 1981.  For example CA is 61, so in 1981 they had 61% of
    // their current production and income.
    //
    categoryData = new ArrayList<>();

    for (EnumCategories category : EnumCategories.values())
    {

      fieldList = fileReader.readRecord(EnumHeader.SIZE);
      //create data object
      CropZoneData zoneData = new CropZoneData(EnumFood.valueOf(category.name()));
      for (EnumHeader header : EnumHeader.values())
      {

        //create data object
        int i = header.ordinal();
        int value = 0;
        double value_d = 0.0;
        //catch for blank fields
        if (fieldList[i].equals("")) continue;

        if (i > 0)
        {

          //set fields for newest data objects. NOT COMPLETE
          switch (header)
          {
            case price:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setPricePerMetricTon(value);
              break;
            case land:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setTonsPerKM2(value);
              break;
            case water:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setLitersPerKG(value);
              break;
            case fertilizerN:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setFertilizerNPerKM2(value);
              break;
            case fertilizerK2O:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setFertilizerK2OPerKM2(value);
              break;
            case fertilizerP2O5:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setFertilizerP2O5PerKM2(value);
              break;
            case growing:
              value = Integer.parseInt(fieldList[i]);
              zoneData.setDaysGrownPerYear(value);
              break;
            case temperature:
              value_d = Double.parseDouble(fieldList[i]);
              zoneData.setAnnualMinimumTemperature(value_d);
              break;
            case ideal:
              value_d = Double.parseDouble(fieldList[i]);
              zoneData.setIdealLowTemperature(value_d);
              break;
            case high:
              value_d = Double.parseDouble(fieldList[i]);
              zoneData.setIdealHighTemperature(value_d);
              break;
            case max:
              value_d = Double.parseDouble(fieldList[i]);
              zoneData.setAnnualMaximumTemperature(value_d);
              break;
          }
        }

      }
      categoryData.add(zoneData);
    }
  }

  public ArrayList<CropZoneData> getCategoryData()
  {
    return categoryData;
  }

  private void copyCropValues(Territory countryFinal, Territory agriculturalUnitTemp)
  {
    for (EnumFood crop : EnumFood.values())
    {
      long imports = agriculturalUnitTemp.getCropImport(crop);
      long exports = agriculturalUnitTemp.getCropExport(crop);
      long production = agriculturalUnitTemp.getCropProduction(crop);
      int land = agriculturalUnitTemp.getCropLand(crop);
      double yield = agriculturalUnitTemp.getCropYield(crop);
      double need = agriculturalUnitTemp.getCropNeedPerCapita(crop);

      countryFinal.setCropImport(crop, imports);
      countryFinal.setCropExport(crop, exports);
      countryFinal.setCropProduction(crop, production);
      countryFinal.setCropLand(crop, land);
      countryFinal.setCropYield(crop, yield);
      countryFinal.setCropNeedPerCapita(crop, need);
    }
  }
}