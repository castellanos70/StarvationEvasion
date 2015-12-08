package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import sun.plugin.dom.exception.InvalidStateException;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


/**
 * Region class extends AbstractAgriculturalUnit, includes methods for accessing its
 * fields.
 */
public class Region extends AbstractTerritory
{
  private static final int PLAYER_START_REVENUE = 50; //million dollars
  private static final boolean VERBOSE = false;

  private EnumRegion region;
  private final Area area = new Area();
  private final Collection<Territory> territories = new ArrayList<>();

  private int revenue;

  public int ethanolProducerTaxCredit = 0;

  // all data in metric tons
  private long[] initialProduction1981 = new long[EnumFood.SIZE];
  private long[] initialImports1981 = new long[EnumFood.SIZE];
  private long[] initialExports1981 = new long[EnumFood.SIZE];
  private long[] initialProduction2014 = new long[EnumFood.SIZE];
  private long[] initialImports2014 = new long[EnumFood.SIZE];
  private long[] initialExports2014 = new long[EnumFood.SIZE];

  /**
   * Territory constructor
   *
   * @param region
   */
  public Region(EnumRegion region)
  {
    super(region.name());
    this.region = region;
    if (region.isUS())
    { revenue = PLAYER_START_REVENUE;
      ethanolProducerTaxCredit = Util.rand.nextInt(15) + Util.rand.nextInt(15);
    }
  }

  private Region(String name)
  {
    super(name);

    this.region = null;
  }

  public EnumRegion getRegionEnum()
  {
    return region;
  }

  public int getRevenue() {return revenue;}

  /**
   * @param tile Territory to add to country
   */
  public void addTerritory(Territory tile)
  {
    territories.add(tile);
    area.add(tile.getArea());
  }

  public void optimizeCrops(int year)
  {
    for (Territory t : territories) {
      CropOptimizer optimizer = new CropOptimizer(year, t);
      optimizer.optimizeCrops();
    }
  }

  /**
   * Used to link land tiles to a country.
   *
   * @param mapPoint mapPoint that is being testing for inclusing
   * @return true is mapPoint is found inside country.
   */
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (territories == null)
    {
      throw new RuntimeException("(!)REGIONS NOT SET YET");
    }

    for (Territory t : territories)
    {
      if (t.containsMapPoint(mapPoint)) return true;
    }
    return false;
  }

  /**
   * Sets the food production for the year 2014 or 1981.
   *
   * @param food type of food produced
   * @param production production of food in metric tons
   * @param year either 1981 or 2014
   */
  public void setInitialProduction(EnumFood food, long production, int year)
  {
    if (year == 2014)
    {
      initialProduction2014[food.ordinal()] = production;
    }
    else
    {
      initialProduction1981[food.ordinal()] = production;
    }
  }

  /**
   * Sets the food imports for the year 2014 or 1981.
   *
   * @param food type of food imported
   * @param imports imports of food in metric tons
   * @param year either 1981 or 2014
   */
  public void setInitialImports(EnumFood food, long imports, int year)
  {
    if (year == 2014)
    {
      initialImports2014[food.ordinal()] = imports;
    }
    else
    {
      initialImports1981[food.ordinal()] = imports;
    }
  }

  /**
   * Sets the food exports for the year 2014 or 1981.
   *
   * @param food type of food produced
   * @param exports exports of food in metric tons
   * @param year either 1981 or 2014
   */
  public void setInitialExports(EnumFood food, long exports, int year)
  {
    if (year == 2014)
    {
      initialExports2014[food.ordinal()] = exports;
    }
    else
    {
      initialExports1981[food.ordinal()] = exports;
    }
  }

  /**
   * Get the initial production for the specified food. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param food type of food to get production for
   * @param year 1981 or 2014
   * @return initial food production for type food for 1981 or 2014 (metic tons)
   */
  public long getInitialProduction(EnumFood food, int year)
  {
    if (year == 2014)
    {
      return initialProduction1981[food.ordinal()];
    }
    return initialProduction1981[food.ordinal()];
  }

  /**
   * Get the total initial production for all food in the region. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param year 1981 or 2014
   * @return total food production for 1981 or 2014 (metric tons)
   */
  public long getInitialProduction(int year)
  {
    int totalProduction = 0;
    long[] production = year == 2014 ? initialProduction2014 : initialProduction1981;
    for (int i = 0; i < production.length; i++)
    {
      totalProduction += production[i];
    }
    return totalProduction;
  }

  /**
   * Get the initial imports for the specified food. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param food type of food to get imports for
   * @param year 1981 or 2014
   * @return initial food imports for type food for 1981 or 2014 (metric tons)
   */
  public long getInitialImports(EnumFood food, int year)
  {
    if (year == 2014)
    {
      return initialImports2014[food.ordinal()];
    }
    return initialImports1981[food.ordinal()];
  }

  /**
   * Get the total initial imports for all food in the region. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param year 1981 or 2014
   * @return total food imports for 1981 or 2014 (metric tons)
   */
  public long getInitialImports(int year)
  {
    int totalImports = 0;
    long[] imports = year == 2014 ? initialImports2014 : initialImports1981;
    for (int i = 0; i < imports.length; i++)
    {
      totalImports += imports[i];
    }
    return totalImports;
  }

  /**
   * Get the initial exports for the specified food. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param food type of food to get exports for
   * @param year 1981 or 2014
   * @return initial food exports for type food for 1981 or 2014 (metric tons)
   */
  public long getInitialExports(EnumFood food, int year)
  {
    if (year == 2014)
    {
      return initialExports2014[food.ordinal()];
    }
    return initialExports1981[food.ordinal()];
  }

  /**
   * Get the total initial exports for all food in the region. This is for data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param year 1981 or 2014
   * @return total food exports for 1981 or 2014 (metric tons)
   */
  public long getInitialExports(int year)
  {
    int totalExports = 0;
    long[] exports = year == 2014 ? initialExports2014 : initialExports1981;
    for (int i = 0; i < exports.length; i++)
    {
      totalExports += exports[i];
    }
    return totalExports;
  }

  /**
   * Get the initial food consumption for the specified food. This is for 1981 or 2014 and uses data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @param food type of food to get consumption for
   * @return initial food consumption for type food for 1981 or 2014
   */
  public long getInitialConsumption(EnumFood food, int year)
  {
    return getInitialProduction(food, year) + getInitialImports(food, year) - getInitialExports(food, year);
  }

  /**
   * Get the initial total food consumption for the region. This is for 1981 data and uses data provided in
   * "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @return initial total food consumption for 1981 or 2014 in the region
   */
  public long getInitialConsumption(int year)
  {
    return getInitialProduction(year) + getInitialImports(year) - getInitialExports(year);
  }

  /**
   * Get the initial food consumption per capita for the region for the type food. This is for 1981 or 2014 and
   * uses data provided in "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @return initial food consumption per capita in 1981 or 2014 for type food
   */
  public double getInitialConsumtionPerCapita(EnumFood food, int year)
  {
    return getInitialConsumption(food, year) / getPopulation(Constant.FIRST_YEAR);
  }

  /**
   * Get the initial total food consumption per capita for the region. This is for 1981 or 2014 and uses data
   * provided in "/data/sim/WorldData/WorldFoodProduction.csv" and is populated by io.ProductionCSVLoader
   *
   * @return initial total food consumption per capita for 1981 or 2014 in the region
   */
  public double getInitialConsumptionPerCapita(int year)
  {
    return getInitialConsumption(year) / getPopulation(Constant.FIRST_YEAR);
  }

  /**
   * @return regions
   */
  public Collection<Territory> getTerritories()
  {
    return territories;
  }

  /**
   * Estimates the initial yield of all US states the US bookkeeping region.
   */
  public void estimateInitialUSYield()
  {
    // category divided by the region�s people in 1000s of people.
    //
    for (EnumFood crop : EnumFood.values())
    { // Spec section 3.4 2) c) - For each food category find the sum of all 1981
      // state incomes.
      //
      long income = 0;
      for (Territory t : territories)
      { if (t.getName().startsWith("US-")) income += t.getCropIncome(crop);
      }

      // The 1981 per capita need for each category of food per 1000 people is
      // the actual domestic consumption of the crop (excluding the underfed
      // people).
      //
      double need = getInitialConsumption(crop, Constant.FIRST_YEAR) / (population[0] - undernourished);

      // Imports & exports per capita for all regions.
      //
      double cropImport = (double) initialImports1981[crop.ordinal()] / population[0];
      double cropExport = (double) initialExports1981[crop.ordinal()] / population[0];

      for (Territory t : territories)
      { // Skip the umbrella 'Unitied States' territory object.
	    //
        if (t.getName().startsWith("US-") == false) continue;

        // Spec section 3.4 2) d) - Calculate the production of each category as total
        // US production(Constant.FIRST_YEAR) * state income / total US income.  Note that these values
        // are summed into their respective player region in a subsequent step (section
        // 'e)', sum these totals per region).
        //
        double r = (double) t.getCropIncome(crop) / income;
        t.setCropProduction(crop, (long) (initialProduction1981[crop.ordinal()] * r));

        t.setCropNeedPerCapita(crop, need);
        t.setCropImport(crop, (long) (cropImport * t.getPopulation(Constant.FIRST_YEAR)));
        t.setCropExport(crop, (long) (cropExport * t.getPopulation(Constant.FIRST_YEAR)));
      }
    }

    for (Territory t : territories) t.updateYield();
  }

  /**
   * Estimates the initial yield of all territories in the region.
   */
  public void estimateInitialYield()
  {
    if (region == null)
    {
      // At present the only Region that does not correspond to the region enum is
      // the special US bookkeeping region.  This region is special because there
      // are income numbers in the csv file that can be translated to production
      // numbers.
      //
      estimateInitialUSYield();
      return;
    }

    // For United States regions, this data will be populated when the special book-
    // keeping region is visited (above).
    //
    if (region.isUS() == true) return;

    // category divided by the region's people in 1000s of people.
    //
    for (EnumFood crop : EnumFood.values())
    {
      // The 1981 need for each region and category of food per 1000 people is
      // the domestic consumption of the crop.
      //
      double need = getInitialConsumption(crop, Constant.FIRST_YEAR) / (population[0] - undernourished);

      // Production per capita for non-US regions.
      //
      double cropProduction = (double) initialProduction1981[crop.ordinal()] / population[0];

      // Imports & exports per capita for all regions.
      //
      double cropImport = (double) initialImports1981[crop.ordinal()] / population[0];
      double cropExport = (double) initialExports1981[crop.ordinal()] / population[0];

      for (Territory t : territories)
      { 
        if (t.getName().startsWith("US-") == false) 
        {
          t.setCropNeedPerCapita(crop, need);
          t.setCropProduction(crop, (long) (cropProduction * t.getPopulation(Constant.FIRST_YEAR)));
          t.setCropImport(crop, (long) (cropImport * t.getPopulation(Constant.FIRST_YEAR)));
          t.setCropExport(crop, (long) (cropExport * t.getPopulation(Constant.FIRST_YEAR)));
        }
      }
    }

    for (Territory t : territories) t.updateYield();
  }

  /**
   * Updates the yield of all territories in the region and aggregates the values for
   * the entire region.
   */
  public void updateYield(int year)
  {
    for (Territory t : territories)
    {
      // replant crops based on the new land used for farming
      CropOptimizer cropOptimizer = new CropOptimizer(year, t);
      cropOptimizer.optimizeCrops();

      t.updateYield();
      for (EnumFood crop : EnumFood.values())
      {
        landCrop[crop.ordinal()] += t.landCrop[crop.ordinal()];
        cropProduction[crop.ordinal()] += t.cropProduction[crop.ordinal()];
        cropIncome[crop.ordinal()] += t.cropIncome[crop.ordinal()];
      }
    }

    for (EnumFood crop : EnumFood.values())
    {
      if (landCrop[crop.ordinal()] != 0)
      { cropYield[crop.ordinal()] = cropProduction[crop.ordinal()] / landCrop[crop.ordinal()];
      }
      else cropYield[crop.ordinal()] = 0;
    }
  }

  public void updateCropNeed(int year)
  {
    double undernourishedRatio = undernourished / getPopulation(year);
    for (EnumFood crop : EnumFood.values())
    {
      int idx = crop.ordinal();
      double consumed = cropProduction[idx] + cropImport[idx] - cropExport[idx];
      setCropNeedPerCapita(crop, consumed, undernourishedRatio);
    }
  }


  /**
   * Estimates the initial crop budget for a all of the territories in the region by multiplying the territory
   * consumption of the crop by its cost.
   *
   * @param cropData crop data loaded from "/data/sim/CropData.csv"
   */
  public void estimateInitialBudget(List<CropZoneData> cropData)
  {
    for (CropZoneData zoneData : cropData)
    {
      double cropConsumptionPerCapita = getInitialConsumption(zoneData.food, Constant.FIRST_YEAR) / getPopulation(Constant.FIRST_YEAR);

      for (Territory t : getTerritories())
      {
        double territoryCropConsumption = cropConsumptionPerCapita * t.getPopulation(Constant.FIRST_YEAR);
        long budget = (long) territoryCropConsumption * zoneData.pricePerMetricTon;
        t.setCropBudget(zoneData.food, budget);
      }
    }
  }

  public void estimateInitialCropLandArea(List<CropZoneData> cropData)
  {
    setRegionLandTotal();
    for (EnumFood food : EnumFood.values())
    {
      landCrop[food.ordinal()] = 0;
    }

    for (Territory t : getTerritories())
    {
      if (t.getGameRegion() != null)
      {
        double territoryFarmLand = (t.farmLand1981 / 100.0) * t.landTotal;
        double helperSum = cropLandAreaHelper(t, cropData);
        double landCropRatio = territoryFarmLand / helperSum;

        for (CropZoneData zoneData : cropData)
        {
          double cropLand = cropLandAreaHelper(t, zoneData) * landCropRatio;
          t.setCropLand(zoneData.food, (int) cropLand);
          landCrop[zoneData.food.ordinal()] += cropLand;
        }
      }
    }
  }

  // defined to be the temp function in the spec
  private double cropLandAreaHelper(Territory t, CropZoneData zoneData)
  {
    double productionYieldRatio = initialProduction1981[zoneData.food.ordinal()] / (double) zoneData.tonsPerKM2;
    double landRatio = (double) (t.landTotal * t.totalFarmLand) / (this.landTotal * this.totalFarmLand);
    return productionYieldRatio * landRatio;
  }

  // finds the sum of the temp function defined in the spec
  private double cropLandAreaHelper(Territory t, List<CropZoneData> zoneData)
  {
    double sum = 0;
    for (CropZoneData zone : zoneData)
    {
      sum += cropLandAreaHelper(t, zone);
    }
    return sum;
  }

  private void setRegionLandTotal()
  {
    landTotal = 0;
    totalFarmLand = 0;
    for (Territory t : getTerritories())
    {
      if (t.getGameRegion() != null)
      {
        landTotal += t.landTotal;
        totalFarmLand += (t.totalFarmLand * t.landTotal);
      }
    }
    totalFarmLand = (int) (((double) totalFarmLand / landTotal) * 100);
  }

  /**
   * A region is a collection of one or more territories.
   */
  public void aggregateTerritoryFields(int year)
  {
    if (VERBOSE) System.out.println("========> aggregateTerritoryFields("+year+")  territories="+ territories.size());

    population[year - Constant.FIRST_YEAR] = 0;
    births = 0;
    mortality = 0;
    migration = 0;
    undernourished = 0;
    landTotal = 0;
    for (int i=0; i<EnumFood.SIZE; i++)
    {
      cropIncome[i] = 0;
      cropProduction[i] = 0;
      landCrop[i] = 0;
      cropImport[i] = 0;
      cropExport[i] = 0;
    }

    for (Territory part : territories)
    {
      population[year - Constant.FIRST_YEAR] += part.getPopulation(year);
      births          += part.births;
      mortality       += part.mortality;
      migration       += part.migration;
      undernourished  += part.undernourished;
      landTotal       += part.landTotal;
      for (int i=0; i<EnumFood.SIZE; i++)
      {
        cropIncome[i]     += part.cropIncome[i];
        cropProduction[i] += part.cropProduction[i];
        if (VERBOSE && part.cropProduction[i] > 0)
        { System.out.println("Region.aggregateTerritoryFields(): " + region +": cropProduction["+EnumFood.values()[i]+"] ="+cropProduction[i]);
        }
        landCrop[i]       += part.landCrop[i];
        cropImport[i]     += part.cropImport[i];
        cropExport[i]     += part.cropExport[i];
      }
    }

    humanDevelopmentIndex  = (float)
            (population[year - Constant.FIRST_YEAR] - undernourished) /
            population[year - Constant.FIRST_YEAR];
  }

  /**
   * Method for calculating and setting crop need
   *
   * @param crop          EnumFood
   * @param tonsConsumed      2014 production + imports - exports
   * @param percentUndernourished 2014 percent of population undernourished
   */
  public void setCropNeedPerCapita(EnumFood crop, double tonsConsumed, double percentUndernourished)
  {
    for (Territory unit : territories)
    {
      unit.setCropNeedPerCapita(crop, tonsConsumed, percentUndernourished);
    }

    double population = getPopulation(Constant.FIRST_YEAR);
    double tonPerPerson = tonsConsumed / (population - (0.5 * percentUndernourished * population));
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Method for setting crop need when already known (e.g., when copying).
   *
   * @param crop     EnumFood
   * @param tonPerPerson 2014 ton/person
   */
  public void setCropNeedPerCapita(EnumFood crop, double tonPerPerson)
  {
    // *Changed*
    // amount is already ton per person for the region, so it shouldn't be divided up
    // based on the number of territory.
    // each territory will have the same need per capita as the region.

    // Divide it up amongst the units.
    //
    //double perUnit = tonPerPerson / territories.size();
    //double remainder = tonPerPerson % (territories.size() * perUnit);
    for (Territory unit : territories)
    {
      unit.setCropNeedPerCapita(crop, tonPerPerson);
      //remainder = 0;
    }

    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }


  /**
   * Updates the region population for the year.
   * @param year
   */
  public void updatePopulation(int year)
  {
    // Do not recompute territory undernourished statistics in the 1st year, or if
    // this is a book-keeping region.
    //
    boolean updateTerritories = region != null && year != Constant.FIRST_YEAR;
    int people = 0, underfed = 0;
    for (Territory t : territories)
    { // Territory.updatePopulation because it updates internal state variables related
      // to production.
      //
      if (updateTerritories) t.updatePopulation(year);

      people += t.getPopulation(year);
      underfed += t.getUndernourished();
    }

    if (people == 0) throw new InvalidStateException("Region " + getName() + " has zero population.");

    // Region population in year 0.
    //
    population[year - Constant.FIRST_YEAR] = people;
    undernourished = underfed;
  }

  /**
   */
  public static Region createBookKeepingRegion(String name)
  {
    return new Region(name);
  }
}
