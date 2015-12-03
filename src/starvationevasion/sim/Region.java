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
    for (Territory t : territories) t.estimateInitialYield();
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
}
