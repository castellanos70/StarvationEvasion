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
    medianAge[index] = 0;
    births[index] = 0;
    mortality[index] = 0;
    migration[index] = 0;
    undernourished[index] = 0;

    landTotal[index] = 0;
    landArable[index] = 0;

    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropYield[i] = 0;
      cropNeedPerCapita[i] = 0;
      cropIncome[i][index] = 0;
      cropProduction[i][index] = 0;
      landCrop[i][index] = 0;
    }

    for (int i = 0 ; i < EnumGrowMethod.values().length ; i += 1)
    {
      cultivationMethod[i][index] = 0;
    }

    // Sum.
	//
    for (Territory t : territories)
    {
      medianAge[index] += t.getMedianAge(year);
      births[index] += t.getBirths(year);
      mortality[index] += t.getMortality(year);
      migration[index] += t.getMigration(year);
      undernourished[index] += t.getUndernourished(year);

      landTotal[index] += t.getLandTotal(year);
      landArable[index] += t.getLandArable(year);

      for (EnumFood food : EnumFood.values())
      {
        cropYield[food.ordinal()] += t.getCropYield(year, food);
        cropNeedPerCapita[food.ordinal()] += t.getCropNeedPerCapita(food);
        cropIncome[food.ordinal()][index] += t.getCropIncome(year, food);
        cropProduction[food.ordinal()][index] += t.getCropProduction(year, food);
        landCrop[food.ordinal()][index] += t.getCropLand(year, food); // Yes, they named it backwards.
      }

      for (EnumGrowMethod method : EnumGrowMethod.values())
      {
        cultivationMethod[method.ordinal()][index] += t.getMethodPercentage(year, method);
      }
    }

    // Update average values.
	//
    medianAge[index] /= territories.size();
  }

  /**
   * @param year  year in question
   * @param crop  crop in question
   * @param metTons tons produced
   */
  public void setCropProduction(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
      // Divide it up amongst the units.
      //
      double perUnit = metTons / territories.size();
      double remainder = metTons % (territories.size() * perUnit);
      for (Territory t : territories)
      {
        t.setCropProduction(year, crop, perUnit + remainder);
        remainder = 0;
      }

      cropProduction[crop.ordinal()][year - START_YEAR] = metTons;
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
   * @param year
   * @param kilomsq total land area
   */
  private void setLandTotal(int year, double kilomsq)
  {
    if (kilomsq > 0)
    {
      // TODO : PAB : We should probably add the unit's total land
      //
      for (int i = 0; i < (YEARS_OF_SIM); i++) landTotal[i] = kilomsq;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setLandTotal method");
      }
    }
  }

  /**
   * @param year
   * @param kilomsq area of arable land
   */
  private void setArableLand(int year, double kilomsq)
  {
    if (kilomsq >= 0)
    {
      for (int i = 0; i < (YEARS_OF_SIM); i++) landArable[i] = kilomsq;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setArableLand method for country " + getName());
      }
    }
  }


  /**
   * Set crop land value; use this method when initializing
   *
   * @param year  year in question
   * @param crop  crop in question
   * @param kilomsq area to set
   */
  private void setCropLand(int year, EnumFood crop, double kilomsq)
  {
    if (kilomsq >= 0 && kilomsq <= getArableLand(year))
    {
      for (int i = 0; i < (YEARS_OF_SIM); i++)
        landCrop[crop.ordinal()][i] = kilomsq;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setCropLand method for country " + getName() + " crop " + crop);
      }
    }
  }

  /**
   * Sets area to be planted with given crop in given year based on user input
   *
   * @param year  year in question
   * @param crop  crop in question
   * @param kilomsq number square km user wants to plant with that crop
   */
  public void updateCropLand(int year, EnumFood crop, double kilomsq)
  {
    double cropLand = 0.;

    for (Territory unit : territories)
    {
      unit.updateCropLand(year, crop, kilomsq);
      cropLand += unit.getCropLand(year, crop);
    }

    setCropLand(year, crop, cropLand);
  }

  /**
   * @param year     year in question
   * @param method   cultivation method
   * @param percentage % land cultivated by method
   */
  public void setMethodPercentage(int year, EnumGrowMethod method, double percentage)
  {
    if (percentage >= 0)
    {
      for (Territory unit : territories)
      {
        unit.setMethodPercentage(year, method, percentage);
      }

      cultivationMethod[method.ordinal()][year - START_YEAR] = percentage;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setMethodPercentage method");
      }
    }
  }

  /**
   * @param year      (passing year might be useful in the next milestone?)
   * @param crop
   * @param tonPerSqKilom yield for crop
   */
  public void setCropYield(int year, EnumFood crop, double tonPerSqKilom)
  {
    // Divide it up amongst the units.
    //
    double perUnit = tonPerSqKilom / territories.size();
    double remainder = tonPerSqKilom % (territories.size() * perUnit);
    for (Territory unit : territories)
    {
      unit.setCropYield(year, crop, perUnit + remainder);
      remainder = 0;
    }

    cropYield[crop.ordinal()] = tonPerSqKilom;
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
