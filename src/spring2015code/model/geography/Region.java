package spring2015code.model.geography;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import spring2015code.common.AbstractAgriculturalUnit;
import spring2015code.common.EnumGrowMethod;
import spring2015code.model.CropOptimizer;
import spring2015code.model.MapPoint;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AgriculturalUnit class extends AbstractAgriculturalUnit, includes methods for accessing its
 * fields.
 */
public class Region extends AbstractAgriculturalUnit
{
  private static final int START_YEAR = Constant.FIRST_YEAR;
  private static final boolean VERBOSE = false;

  private MapPoint capitolLocation;
  private final Area area = new Area();
  private final Collection<AgriculturalUnit> entities = new ArrayList<>();

  /**
   * AgriculturalUnit constructor
   *
   * @param name country name
   */
  public Region(String name)
  {
    super(name);
  }

  /**
   * @return country's collection of agricultural units.
   */
  public Collection<AgriculturalUnit> getAgriculturalUnits()
  {
    return entities;
  }

  /**
   * @param tile AgriculturalUnit to add to country
   */
  public void addAgriculturalUnit(AgriculturalUnit tile)
  {
    entities.add(tile);
    area.add(tile.getArea());
  }

  public void optimizeCrops(int year)
  {
    for (AgriculturalUnit unit : entities) {
      CropOptimizer optimizer = new CropOptimizer(year, unit);
      optimizer.optimizeCrops();
    }
  }

  /**
   * returns the point representing the shipping location of that country.
   * <p/>
   * (!) note: this method can only be called after the AgriculturalUnit's regions have
   * been set.
   *
   * @return map point representing the lat and lon location of the AgriculturalUnit's
   * capitol.
   */
  public MapPoint getCapitolLocation()
  {
    if (capitolLocation == null)
    {
      capitolLocation = calCapitolLocation();
    }
    return capitolLocation;
  }

  /**
   * Used to link land tiles to a country.
   *
   * @param mapPoint mapPoint that is being testing for inclusing
   * @return true is mapPoint is found inside country.
   */
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (entities == null)
    {
      throw new RuntimeException("(!)REGIONS NOT SET YET");
    }

    for (AgriculturalUnit unit : entities)
    {
      if (unit.containsMapPoint(mapPoint)) return true;
    }
    return false;
  }

  // generate the capital by finding the center of the largest landmass.
  // this method can only be called after the AgriculturalUnit's regions have been set.
  private MapPoint calCapitolLocation()
  {
    if (entities == null) throw new RuntimeException("(!) regions not set!");
    if (entities.isEmpty()) throw new RuntimeException("(!) no regions !");

    int maxArea = 0;
    Area largest = null;

    for (AgriculturalUnit region : entities)
    {
      Area poly = region.getArea();
      int area = (int) (poly.getBounds().getWidth() * poly.getBounds().getHeight());
      if (area >= maxArea)
      {
        largest = poly;
        maxArea = area;
      }
    }

    int x = (int) largest.getBounds().getCenterX();
    int y = (int) largest.getBounds().getCenterY();

    return AgriculturalUnit.converter.pointToMapPoint(new Point(x, y));
  }

  /**
   * @param region region (i.e., area contained in vertices) to add to country
   */
  public void addRegion(AgriculturalUnit region)
  {
    entities.add(region);
  }

  /**
   * @return regions
   */
  public Collection<AgriculturalUnit> getRegions()
  {
    return entities;
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
      int perUnit = n / entities.size();
      int remainder = n % (entities.size() * perUnit);
      for (AgriculturalUnit unit : entities)
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
        System.err.println("Invalid argument for AgriculturalUnit.setPopulation method");
      }
    }
  }

  /**
   * Updates population for given year based on formula in spec
   *
   * @param year year for which to calculate population
   */
  public void updatePopulation(int year)
  {
    population[year - START_YEAR] = 0;

    for (AgriculturalUnit unit : entities)
    {
      unit.updatePopulation(year);
      population[year - START_YEAR] += unit.getPopulation(year);
    }
  }

  /**
   * Populate medianAge array with given age; assumes median age remains constant.
   *
   * @param years median age
   */
  public void setMedianAge(double years)
  {
    if (years >= 0)
    {
      for (int i = 0; i < medianAge.length; i++) medianAge[i] = years;
      for (AgriculturalUnit unit : entities)
      {
        unit.setMedianAge(years);
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setMedianAge method");
      }
    }
  }

  /**
   * Populate birthRate array with given rate; assumes rate remains constant.
   *
   * @param permille births/1000 people
   */
  public void setBirthRate(double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      for (int i = 0; i < birthRate.length; i++) birthRate[i] = permille;
      for (AgriculturalUnit unit : entities)
      {
        unit.setBirthRate(permille);
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setBirthRate method");
      }
    }
  }

  public void setMortalityRate(int year, double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      mortalityRate[year - START_YEAR] = permille;
      for (AgriculturalUnit unit : entities)
      {
        unit.setMortalityRate(year, permille);
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setMortalityRate method");
      }
    }
  }

  /**
   * Updates mortality rate for given year based on formula given in spec.
   *
   * @param year year for which we are updating mortality rate
   */
  public void updateMortalityRate(int year)
  {
    double mortalityNow = 0.;

    for (AgriculturalUnit unit : entities)
    {
      unit.updateMortalityRate(year);
      mortalityNow += unit.getMortalityRate(year);
    }

    mortalityNow /= entities.size();
    setMortalityRate(year, mortalityNow);
  }

  /**
   * Populate migrationRate array with given rate; assumes rate remains constant.
   *
   * @param permille migration/1000 people
   */
  public void setMigrationRate(double permille)
  {
    if (permille >= -1000 && permille <= 1000)
    {
      for (int i = 0; i < migrationRate.length; i++) migrationRate[i] = permille;
      for (AgriculturalUnit unit : entities)
      {
        unit.setMigrationRate(permille);
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setMigrationRate method");
      }
    }
  }

  /**
   * Sets undernourished percentage; see updateUndernourished method for calculating percentage.
   *
   * @param year     year to set
   * @param percentage percentage to set
   */
  public void setUndernourished(int year, double percentage)
  {
    if (percentage >= 0 && percentage <= 1)
    {
      undernourished[year - START_YEAR] = percentage;
      for (AgriculturalUnit unit : entities)
      {
        unit.setUndernourished(year, percentage);
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setUndernourished method");
      }
    }

  }

  /**
   * Update % undernourished using formula in spec.
   *
   * @param year
   */
  public void updateUndernourished(int year)
  {
    // TODO : PAB : This is not right.  We need to aggrgegate the new rates from all of the
    // contained regions.
    //
    double undernourished = 0.;

    for (AgriculturalUnit unit : entities)
    {
      unit.updateUndernourished(year);
      undernourished += unit.getUndernourished(year);
    }

    undernourished /= entities.size();
    setMortalityRate(year, undernourished);
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
      double perUnit = metTons / entities.size();
      double remainder = metTons % (entities.size() * perUnit);
      for (AgriculturalUnit unit : entities)
      {
        unit.setCropProduction(year, crop, perUnit + remainder);
        remainder = 0;
      }

      cropProduction[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setCropProduction method");
      }
    }
  }

  /**
   * @param year  year in question
   * @param crop  crop in question
   * @param metTons tons exported
   */
  public void setCropExport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
      // Divide it up amongst the units.
      //
      double perUnit = metTons / entities.size();
      double remainder = metTons % (entities.size() * perUnit);
      for (AgriculturalUnit unit : entities)
      {
        unit.setCropExport(year, crop, perUnit + remainder);
        remainder = 0;
      }

      cropExport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setCropExport method");
      }
    }
  }

  /**
   * @param year  year in question
   * @param crop  crop in question
   * @param metTons tons imported
   */
  public void setCropImport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
      // Divide it up amongst the units.
      //
      double perUnit = metTons / entities.size();
      double remainder = metTons % (entities.size() * perUnit);
      for (AgriculturalUnit unit : entities)
      {
        unit.setCropImport(year, crop, perUnit + remainder);
        remainder = 0;
      }

      cropImport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setCropImport method");
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
        System.err.println("Invalid argument for AgriculturalUnit.setLandTotal method");
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
        System.err.println("Invalid argument for AgriculturalUnit.setArableLand method for country " + getName());
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
        System.err.println("Invalid argument for AgriculturalUnit.setCropLand method for country " + getName() + " crop " + crop);
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

    for (AgriculturalUnit unit : entities)
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
      for (AgriculturalUnit unit : entities)
      {
        unit.setMethodPercentage(year, method, percentage);
      }

      cultivationMethod[method.ordinal()][year - START_YEAR] = percentage;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setMethodPercentage method");
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
    double perUnit = tonPerSqKilom / entities.size();
    double remainder = tonPerSqKilom % (entities.size() * perUnit);
    for (AgriculturalUnit unit : entities)
    {
      unit.setCropYield(year, crop, perUnit + remainder);
      remainder = 0;
    }

    cropYield[crop.ordinal()] = tonPerSqKilom;
  }

  /**
   * Method for calculating & setting crop need
   *
   * @param crop          EnumFood
   * @param tonsConsumed      2014 production + imports - exports
   * @param percentUndernourished 2014 % of population undernourished
   */
  final public void setCropNeedPerCapita(EnumFood crop, double tonsConsumed, double percentUndernourished)
  {
    for (AgriculturalUnit unit : entities)
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
    double perUnit = tonPerPerson / entities.size();
    double remainder = tonPerPerson % (entities.size() * perUnit);
    for (AgriculturalUnit unit : entities)
    {
      unit.setCropNeedPerCapita(crop, perUnit + remainder);
      remainder = 0;
    }

    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Calculate great circle distance from country's capitolLocation to another MapPoint.
   * Formula from http://www.gcmap.com/faq/gccalc
   *
   * @param otherCapitol
   * @return great circle distance in km
   */
  final public double getShippingDistance(MapPoint otherCapitol)
  {
    double radianConversion = (Math.PI) / 180;
    double lon1 = getCapitolLocation().getLon() * radianConversion;
    double lat1 = capitolLocation.getLat() * radianConversion;
    double lon2 = otherCapitol.getLon() * radianConversion;
    double lat2 = otherCapitol.getLat() * radianConversion;
    double theta = lon2 - lon1;
    double dist = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(theta));
    if (dist < 0) dist = dist + Math.PI;
    dist = dist * 6371.2;
    return dist;
  }
}
