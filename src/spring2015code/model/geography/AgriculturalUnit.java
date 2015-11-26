package spring2015code.model.geography;

import starvationevasion.common.EnumFood;
import spring2015code.common.AbstractAgriculturalUnit;
import spring2015code.common.AbstractScenario;
import spring2015code.common.EnumGrowMethod;
import spring2015code.model.LandTile;
import spring2015code.model.MapPoint;
import spring2015code.util.EquirectangularConverter;
import spring2015code.util.MapConverter;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AgriculturalUnit is basically the former Country class, and extends AbstractAgriculturalUnit.
 * It includes methods for accessing its fields.
 * Created by winston on 3/9/15.
 * Edited by Jessica on 3/14/15: getName, setLandTotal, methodPercentage methods
 *
 * @version 22-Mar-2015
 */
public class AgriculturalUnit extends AbstractAgriculturalUnit
{
  private static final boolean VERBOSE = false;
  final public static MapConverter converter = new EquirectangularConverter();

  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;

  private MapPoint capitolLocation;
  private final Area area = new Area();
  private final Collection<GeographicArea> regions = new ArrayList<>();
  private Collection<LandTile> landTiles;

  /**
   * AgriculturalUnit constructor
   *
   * @param name country name
   */
  public AgriculturalUnit(String name)
  {
    super(name);
    this.landTiles = new ArrayList<>();
  }

  /**
   * @return country's collection of 100km2 tiles
   */
  final public Area getArea()
  {
    return area;
  }

  // generate the capital by finding the center of the largest landmass.
  // this method can only be called after the AgriculturalUnit's regions have been set.
  private MapPoint calCapitolLocation()
  {
    if (regions == null) throw new RuntimeException("(!) regions not set!");
    if (regions.isEmpty()) throw new RuntimeException("(!) no regions !");

    int maxArea = 0;
    Area largest = null;

    for (GeographicArea region : regions)
    {
      Area poly = new Area(GeographicArea.mapConverter.regionToPolygon(region));
      int area = (int) (poly.getBounds().getWidth() * poly.getBounds().getHeight());
      if (area >= maxArea)
      {
        largest = poly;
        maxArea = area;
      }
    }

    if (largest == null) throw new IllegalStateException("Internal error computing largest region");

    int x = (int) largest.getBounds().getCenterX();
    int y = (int) largest.getBounds().getCenterY();

    return AgriculturalUnit.converter.pointToMapPoint(new Point(x, y));
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
   * @return country's collection of 100km2 tiles
   */
  final public Collection<LandTile> getLandTiles()
  {
    return landTiles;
  }

  /**
   * @param tile LandTile to add to country
   */
  final public void addLandTile(LandTile tile)
  {
    landTiles.add(tile);
  }

  /**
   * Used to link land tiles to a country.
   *
   * @param mapPoint mapPoint that is being testing for inclusing
   * @return true is mapPoint is found inside country.
   */
  final public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (regions == null)
    {
      throw new RuntimeException("(!)REGIONS NOT SET YET");
    }

    for (GeographicArea region : regions)
    {
      if (region.containsMapPoint(mapPoint)) return true;
    }
    return false;
  }

  /**
   * @param region region (i.e., area contained in vertices) to add to country
   */
  final public void addRegion(GeographicArea region)
  {
    regions.add(region);
    area.add(new Area(converter.regionToPolygon(region)));
  }

  /**
   * @return regions
   */
  final public Collection<GeographicArea> getRegions()
  {
    return regions;
  }
  /**
   * @param year year in question
   * @param n    population in that year
   */
  final public void setPopulation(int year, int n)
  {
    if (n >= 0)
    {
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
  final public void updatePopulation(int year)
  {
    population[year - START_YEAR] = 0;

    int priorPop = population[year - START_YEAR - 1];
    double changePer1K = birthRate[year - START_YEAR] + migrationRate[year - START_YEAR] - mortalityRate[year - START_YEAR];
    Double popNow = priorPop + changePer1K * priorPop / 1000;
    int popInt = popNow.intValue();
    population[year - START_YEAR] = popInt;
  }

  /**
   * Populate medianAge array with given age; assumes median age remains constant.
   *
   * @param years median age
   */
  final public void setMedianAge(double years)
  {
    if (years >= 0)
    {
      for (int i = 0; i < medianAge.length; i++) medianAge[i] = years;
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
  final public void setBirthRate(double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      for (int i = 0; i < birthRate.length; i++) birthRate[i] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for AgriculturalUnit.setBirthRate method");
      }
    }
  }

  final public void setMortalityRate(int year, double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      mortalityRate[year - START_YEAR] = permille;
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
  final public void updateMortalityRate(int year)
  {
    double mortalityNow;
    double hungryStart = undernourished[0] * population[0];
    double mortalityStart = mortalityRate[0];
    int popNow = population[year - START_YEAR - 1];
    double hungryNow = popNow * undernourished[year - START_YEAR - 1];
    if (hungryNow <= hungryStart)
    {
      mortalityNow = mortalityStart;
    }
    else
    {
      double hungryChange = hungryNow - hungryStart;
      mortalityNow = (mortalityStart + 0.2 * hungryChange) / (popNow / 1000);
    }
    setMortalityRate(year, mortalityNow);
  }

  /**
   * Populate migrationRate array with given rate; assumes rate remains constant.
   *
   * @param permille migration/1000 people
   */
  final public void setMigrationRate(double permille)
  {
    if (permille >= -1000 && permille <= 1000)
    {
      for (int i = 0; i < migrationRate.length; i++) migrationRate[i] = permille;
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
   * @param year       year to set
   * @param percentage percentage to set
   */
  final public void setUndernourished(int year, double percentage)
  {
    if (percentage >= 0 && percentage <= 1)
    {
      undernourished[year - START_YEAR] = percentage;
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
   * @param year       year to update
   */
  final public void updateUndernourished(int year)
  {
    double numUndernourished;
    double population = getPopulation(year);
    double[] netCropsAvail = new double[EnumFood.SIZE];
    int numCropsAvail = 0;
    for (EnumFood crop : EnumFood.values())
    {
      double netAvail = getNetCropAvailable(year, crop);
      netCropsAvail[crop.ordinal()] = netAvail;
      if (netAvail >= 0) numCropsAvail++;
    }
    if (numCropsAvail == 5)
    {
      numUndernourished = 0;
    }
    else
    {
      double maxResult = 0;
      for (EnumFood crop : EnumFood.values())
      {
        double need = getCropNeedPerCapita(crop);
        double result = (netCropsAvail[crop.ordinal()]) / (0.5 * need * population);
        if (result > maxResult) maxResult = result;
      }
      numUndernourished = Math.min(population, maxResult);
    }
    setUndernourished(year, numUndernourished / population);
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons produced
   */
  final public void setCropProduction(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
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
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons exported
   */
  final public void setCropExport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
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
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons imported
   */
  final public void setCropImport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
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
   * @param year       year to set
   * @param kilomsq total land area
   */
  final public void setLandTotal(int year, double kilomsq)
  {
    if (kilomsq > 0)
    {
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
   * @param year       year to set
   * @param kilomsq area of arable land
   */
  final public void setArableLand(int year, double kilomsq)
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
   * @param year    year in question
   * @param crop    crop in question
   * @param kilomsq area to set
   */
  final public void setCropLand(int year, EnumFood crop, double kilomsq)
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
   * @param year    year in question
   * @param crop    crop in question
   * @param kilomsq number square km user wants to plant with that crop
   */
  final public void updateCropLand(int year, EnumFood crop, double kilomsq)
  {
    double unused = getArableLandUnused(year);
    double currCropLand = getCropLand(year, crop);
    double delta = kilomsq - currCropLand;
    double valueToSet;

    // if trying to decrease beyond 0, set to 0
    if ((currCropLand + delta) < 0)
    {
      valueToSet = 0;
    }
    // else if trying to increase by amount greater than available, set to current + available
    else if (delta > unused)
    {
      valueToSet = currCropLand + unused;
    }
    // else set to curr + delta
    else
    {
      valueToSet = currCropLand + delta;
    }
    for (int i = year - START_YEAR; i < YEARS_OF_SIM; i++)
      landCrop[crop.ordinal()][i] = valueToSet;
  }

  /**
   * @param year       year in question
   * @param method     cultivation method
   * @param percentage % land cultivated by method
   */
  final public void setMethodPercentage(int year, EnumGrowMethod method, double percentage)
  {
    if (percentage >= 0)
    {
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
   * Method for calculating & setting crop need
   *
   * @param crop                  EnumFood
   * @param tonsConsumed          2014 production + imports - exports
   * @param percentUndernourished 2014 % of population undernourished
   */
  final public void setCropNeedPerCapita(EnumFood crop, double tonsConsumed, double percentUndernourished)
  {
    double population = getPopulation(START_YEAR);
    double tonPerPerson = tonsConsumed / (population - 0.5 * percentUndernourished * population);
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Method for setting crop need when already known (e.g., when copying).
   *
   * @param crop         EnumFood
   * @param tonPerPerson 2014 ton/person
   */
  final public void setCropNeedPerCapita(EnumFood crop, double tonPerPerson)
  {
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * @param year          (passing year might be useful in the next milestone?)
   * @param crop
   * @param tonPerSqKilom yield for crop
   */
  final public void setCropYield(int year, EnumFood crop, double tonPerSqKilom)
  {
    cropYield[crop.ordinal()] = tonPerSqKilom;
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
