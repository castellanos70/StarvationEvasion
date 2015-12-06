package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Region class extends AbstractAgriculturalUnit, includes methods for accessing its
 * fields.
 */
public class Region extends AbstractTerritory
{
  private static final int START_YEAR = Constant.FIRST_YEAR;
  private static final int PLAYER_START_REVENUE = 50; //million dollars
  private static final boolean VERBOSE = false;

  private EnumRegion region;
  private final Area area = new Area();
  private final Collection<Territory> territories = new ArrayList<>();

  private int revenue;

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
   * @param year year in question
   * @param n  population in that year
   */
  public void setPopulation(int year, int n)
  {
    if (n >= 0)
    {
      // Divide it up amongst the units.
      //
      int perUnit = n / territories.size();
      int remainder = n % (territories.size() * perUnit);
      for (Territory unit : territories)
      {
        unit.setPopulation(year, perUnit + remainder);
        remainder = 0;
      }

      population[year - START_YEAR] = n;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setPopulation method");
      }
    }
  }

  /**
   * Estimates the initial yield of all territories in the region.
   */
  public void estimateInitialYield()
  {
    long[] regionalIncome = new long[EnumFood.SIZE];
    for (Territory t : territories)
    {
      for (EnumFood food : EnumFood.values()) regionalIncome[food.ordinal()] += t.getCropIncome(food);
    }

    // Now we can take the ratio of icome to total income to set the initial production
    // for the region.
    //
    for (Territory t : territories)
    {
      for (EnumFood food : EnumFood.values())
      {
        double r = (double) t.getCropIncome(food) / regionalIncome[food.ordinal()];
        t.setCropProduction(food, (long) (r * initialProduction1981[food.ordinal()]));
        t.setCropExport(food, (long) (r * initialExports1981[food.ordinal()]));
        t.setCropImport(food, (long) (r * initialImports1981[food.ordinal()]));
      }
    }

    for (Territory t : territories) t.updateYield();
  }

  /**
   * Updates the yield of all territories in the region and aggregates the values for
   * the entire region.
   */
  public void updateYield()
  {
    for (Territory t : territories)
    { t.updateYield();
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

  /**
   * A region is a collection of one or more territories.
   */
  public void aggregateTerritoryFields(int year)
  {
    System.out.println("========> aggregateTerritoryFields("+year+")  territories="+ territories.size());

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
        if (part.cropProduction[i] > 0)
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

    double population = getPopulation(START_YEAR);
    double tonPerPerson = tonsConsumed / (population - 0.5 * percentUndernourished * population);
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
    // Divide it up amongst the units.
    //
    double perUnit = tonPerPerson / territories.size();
    double remainder = tonPerPerson % (territories.size() * perUnit);
    for (Territory unit : territories)
    {
      unit.setCropNeedPerCapita(crop, perUnit + remainder);
      remainder = 0;
    }

    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  public void updateCropNeed()
  {
    for (Territory t : getTerritories())
    {
      for (EnumFood crop : EnumFood.values())
      {
        t.setCropNeedPerCapita(crop, getInitialConsumption(crop, 1981) / (getPopulation(START_YEAR) * 1000));
      }
    }
  }

  /**
   */
  public static Region createBookKeepingRegion(String name)
  {
    return new Region(name);
  }
}
