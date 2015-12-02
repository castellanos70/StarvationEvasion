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
public class Region extends AbstractAgriculturalUnit
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
   * @return country's collection of agricultural units.
   */
  public Collection<Territory> getAgriculturalUnits()
  {
    return territories;
  }

  /**
   * @param tile Territory to add to country
   */
  public void addTerritory(Territory tile)
  {
    territories.add(tile);
    area.add(tile.getArea());

    for (int i = Constant.FIRST_YEAR ; i < Constant.LAST_YEAR ; i += 1)
      {
        population[i - Constant.FIRST_YEAR] += tile.getPopulation(i);
    }
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
   * The loader loads 2014 data.  This function scales the data for 1981 given the scale factor.
   * @param factor The scaling factor.
   */
  public void scaleInitialStatistics(double factor)
  {
    for (Territory t : territories) t.scaleInitialStatistics(factor);
  }

  /**
   * Updates population for given year based on formula in spec
   *
   * @param year Year index for which to calculate population (e.g.2014)
   */
  public void updateStatistics(int year)
  {
    int index = year - START_YEAR;

    // Re-initialize to zero.
	//
    medianAge = 0;
    births = 0;
    mortality = 0;
    migration = 0;
    undernourished = 0;

    landTotal= 0;

    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropYield[i] = 0;
      cropNeedPerCapita[i] = 0;
      cropIncome[i]= 0;
      cropProduction[i] = 0;
      landCrop[i] = 0;
    }

    for (int i = 0 ; i < EnumGrowMethod.values().length ; i += 1)
    {
      cultivationMethod[i] = 0;
    }

    // Sum.
	//
    for (Territory t : territories)
    {
      medianAge += t.getMedianAge();
      births += t.getBirths();
      mortality += t.getMortality();
      migration += t.getMigration();
      undernourished += t.getUndernourished();

      landTotal += t.getLandTotal();

      for (EnumFood food : EnumFood.values())
      {
        cropYield[food.ordinal()] += t.getCropYield(food);
        cropNeedPerCapita[food.ordinal()] += t.getCropNeedPerCapita(food);
        cropIncome[food.ordinal()] += t.getCropIncome(food);
        cropProduction[food.ordinal()] += t.getCropProduction(food);
        landCrop[food.ordinal()] += t.getCropLand(food); // Yes, they named it backwards.
      }

      for (EnumGrowMethod method : EnumGrowMethod.values())
      {
        cultivationMethod[method.ordinal()] += t.getMethodPercentage(method);
      }
    }

    // Update average values.
	//
    medianAge /= territories.size();
  }

  /**
   * @param crop  crop in question
   * @param metTons tons produced
   */
  public void setCropProduction(EnumFood crop, int metTons)
  {
    if (metTons >= 0)
    {
      // Divide it up amongst the units.
      //
      int perUnit = metTons / territories.size();
      int remainder = metTons % (territories.size() * perUnit);
      for (Territory t : territories)
      {
        t.setCropProduction(crop, perUnit + remainder);
        remainder = 0;
      }

      cropProduction[crop.ordinal()] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setCropProduction method");
      }
    }
  }









  /**
   * Method for calculating and setting crop need
   *
   * @param crop          EnumFood
   * @param tonsConsumed      2014 production + imports - exports
   * @param percentUndernourished 2014 percent of population undernourished
   */
  final public void setCropNeedPerCapita(EnumFood crop, double tonsConsumed, double percentUndernourished)
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
  final public void setCropNeedPerCapita(EnumFood crop, double tonPerPerson)
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
