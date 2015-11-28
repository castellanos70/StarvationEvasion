package spring2015code.model.geography;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import spring2015code.common.AbstractAgriculturalUnit;
import spring2015code.common.AbstractScenario;
import spring2015code.common.EnumGrowMethod;
import starvationevasion.common.EnumRegion;
import starvationevasion.geography.GeographicArea;
import starvationevasion.geography.LandTile;
import starvationevasion.geography.MapPoint;
import starvationevasion.util.EquirectangularConverter;
import starvationevasion.util.MapConverter;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Territory is the former Country class, and extends AbstractAgriculturalUnit.
 * This class is an abstraction subclassed by the USState and Country classes, but is
 * also directly instantiated by the CSV parser as a data structure used to store
 * temporary data.
 * Created by winston on 3/9/15.
 * Edited by Jessica on 3/14/15: getName, setLandTotal, methodPercentage methods
 * Edited by Peter November 2015 to make the class more abstract.
 *
 * @version 22-Mar-2015
 */
public class Territory extends AbstractAgriculturalUnit
{
  // Spring 2015 data items.
  //
  private static final boolean VERBOSE = false;
  final public static MapConverter converter = new EquirectangularConverter();

  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;

  private MapPoint capitolLocation;
  private final Area area = new Area();
  private final Collection<GeographicArea> regions = new ArrayList<>();
  private Collection<LandTile> landTiles;

  /* The region to which this country belongs.
   */
  private EnumRegion region;

  /**
   * Territory constructor
   *
   * @param name country name
   */
  public Territory(String name)
  {
    super(name);

    landTiles = new ArrayList<>();
  }

  /**
   * @return country's collection of 100km2 tiles
   */
  final public Area getArea()
  {
    return area;
  }

  /**
   * Sets the game region for this unit.
   */
  public void setGameRegion(EnumRegion region)
  {
    this.region = region;
  }

  /**
   * @return Sets the game region for this unit.
   */
  public EnumRegion getGameRegion()
  {
    return region;
  }

  /**
   * Estimates the initial land used per food type, and the yield.
   */
  public void estimateInitialYield()
  {
    double income = 0.;
    double production = 0.;
    double land = 0.;
    for (EnumFood crop : EnumFood.values())
    {
      land += landCrop[crop.ordinal()][0];
      production += cropProduction[crop.ordinal()][0];
      income += cropIncome[crop.ordinal()][0];
    }

    // If the total land is > 0 then this function has already been called.
    //
    if (land == 0.)
    {
      if (income == 0. && production == 0.)
      { Logger.getGlobal().log(Level.INFO,
              "Territory {0} has no production or income. Faking it.", getName());

        // Assume they're getting $100 per acre.  Terrible guess, but it's just a
        // default for empty rows.
        //
        income = landTotal[0] / 10.; // $100 per acre / 1000.;
        double p = 1. / EnumFood.SIZE;
        for (int i = 0 ; i < EnumFood.SIZE ; i += 1) cropIncome[i][0] = income * p;
      }

      if (production == 0.)
      {
        for (EnumFood crop : EnumFood.values()) {
          if (production == 0.) {
            // The current version of the CSV file doesn't have any production values.
            // Use the income values (if available) to estimate land per crop.
            //

            // Estimate production from the yield.
            //
            cropProduction[crop.ordinal()][0] = (cropIncome[crop.ordinal()][0] / income) * landTotal[0];
            production += cropProduction[crop.ordinal()][0];
          }
        }
      }

      for (EnumFood crop : EnumFood.values())
      {
        cropYield[crop.ordinal()] = cropProduction[crop.ordinal()][0] / landTotal[0];

        // Use the crop production to estimate land per crop.
        //
        double p = cropProduction[crop.ordinal()][0] / production;

        // This is an initial naive estimate.  Per Joel there will eventually be a multiplier
        // applied that gives a more realistic estimate.
        //
        landCrop[crop.ordinal()][0] = landTotal[0] * p /* * multiplier[food] */;

        land += landCrop[crop.ordinal()][0];
      }
    }
  }

  /**
   * The loader loads 2014 data.  This function scales the data for 1981 given the scale factor.
   * @param factor The scaling factor.
   */
  public void scaleInitialStatistics(double factor)
  {
    int index = 0; // The 0th index is the start year.

    population[index] *= factor;
    medianAge[index] *= factor;
    births[index] *= factor;
    mortality[index] *= factor;
    migration[index] *= factor;
    undernourished[index] *= factor;

    landTotal[index] *= factor;
    landArable[index] *= factor;

    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropYield[i] *= factor;
      cropNeedPerCapita[i] *= factor;
      cropProduction[i][index] *= factor;
      landCrop[i][index] *= factor;
    }

    for (int i = 0 ; i < EnumGrowMethod.values().length ; i += 1)
    {
      cultivationMethod[i][index] *= factor;
    }
  }

  /**
   * Copies initial data from another territory to this territory.  This is primarily only
   * done when unifying XML and CSV data.
   *
   * @param fromTerritory The territory from which to copy data.
   */
  public void copyInitialValuesFrom(final Territory fromTerritory)
  {
    int index = 0;
    medianAge[index] = fromTerritory.medianAge[index];
    births[index] = fromTerritory.births[index];
    mortality[index] = fromTerritory.mortality[index];
    migration[index] = fromTerritory.migration[index];
    undernourished[index] = fromTerritory.undernourished[index];

    landTotal[index] = fromTerritory.landTotal[index];
    landArable[index] = fromTerritory.landArable[index];

    for (EnumFood food : EnumFood.values())
    {
      cropYield[food.ordinal()] = fromTerritory.cropYield[food.ordinal()];
      cropNeedPerCapita[food.ordinal()] = fromTerritory.cropNeedPerCapita[food.ordinal()];
      cropIncome[food.ordinal()][index] = fromTerritory.cropIncome[food.ordinal()][index];
      cropProduction[food.ordinal()][index] = fromTerritory.cropProduction[food.ordinal()][index];
      landCrop[food.ordinal()][index] = fromTerritory.landCrop[food.ordinal()][index];
    }

    for (EnumGrowMethod method : EnumGrowMethod.values())
    {
      cultivationMethod[method.ordinal()][index] = fromTerritory.cultivationMethod[method.ordinal()][index];
    }

    while (index < Constant.LAST_YEAR - Constant.FIRST_YEAR)
    {
      population[index] = fromTerritory.population[index];
      index += 1;
    }
  }

  // generate the capital by finding the center of the largest landmass.
  // this method can only be called after the Territory's regions have been set.
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

    return Territory.converter.pointToMapPoint(new Point(x, y));
  }

  /**
   * returns the point representing the shipping location of that country.
   * <p/>
   * (!) note: this method can only be called after the Territory's regions have
   * been set.
   *
   * @return map point representing the lat and lon location of the Territory's
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
        System.err.println("Invalid argument for Territory.setPopulation method");
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
    double changePer1K = births[year - START_YEAR] + migration[year - START_YEAR] - mortality[year - START_YEAR];
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
        System.err.println("Invalid argument for Territory.setMedianAge method");
      }
    }
  }

  /**
   * Populate medianAge array with given age; assumes median age remains constant.
   *
   * @param years median age
   */
  final public void setMedianAge(int year, double years)
  {
    if (years >= 0)
    {
      medianAge[year - START_YEAR] = years;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setMedianAge method");
      }
    }
  }

  /**
   * Populate births array with given rate; assumes rate remains constant.
   *
   * @param permille births/1000 people
   */
  final public void setBirths(double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      for (int i = 0; i < births.length; i++) births[i] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setBirths method");
      }
    }
  }
  /**
   * Populate births array with given rate; assumes rate remains constant.
   *
   * @param permille births/1000 people
   */
  final public void setBirths(int year, double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      births[year - START_YEAR] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setBirths method");
      }
    }
  }

  final public void setMortality(int year, double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      mortality[year - START_YEAR] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setMortality method");
      }
    }
  }

  /**
   * Updates mortality rate for given year based on formula given in spec.
   *
   * @param year year for which we are updating mortality rate
   */
  final public void updateMortality(int year)
  {
    double mortalityNow;
    double hungryStart = undernourished[0] * population[0];
    double mortalityStart = mortality[0];
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
    setMortality(year, mortalityNow);
  }

  /**
   * Populate migration array with given rate; assumes rate remains constant.
   *
   * @param permille migration/1000 people
   */
  final public void setMigration(double permille)
  {
    if (permille >= -1000 && permille <= 1000)
    {
      for (int i = 0; i < migration.length; i++) migration[i] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setMigration method");
      }
    }
  }

  /**
   * Populate migration array with given rate; assumes rate remains constant.
   *
   * @param permille migration/1000 people
   */
  final public void setMigration(int year, double permille)
  {
    if (permille >= -1000 && permille <= 1000)
    {
      migration[year - START_YEAR] = permille;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setMigration method");
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
        System.err.println("Invalid argument for Territory.setUndernourished method");
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
        System.err.println("Invalid argument for Territory.setCropProduction method");
      }
    }
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param value Income in $1,000
   */
  final public void setCropIncome(int year, EnumFood crop, double value)
  {
    if (value >= 0)
    {
      cropIncome[crop.ordinal()][year - START_YEAR] = value;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setCropIncome method");
      }
    }
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons exported
   */
  @Deprecated
  final private void setCropExport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
      throw new UnsupportedOperationException("Fall 2015 doesn't used crop import or export values.");
      // cropExport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setCropExport method");
      }
    }
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons imported
   */
  @Deprecated
  final private void setCropImport(int year, EnumFood crop, double metTons)
  {
    if (metTons >= 0)
    {
      throw new UnsupportedOperationException("Fall 2015 doesn't used crop import or export values.");
      // cropImport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Territory.setCropImport method");
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
        System.err.println("Invalid argument for Territory.setLandTotal method");
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
        System.err.println("Invalid argument for Territory.setArableLand method for country " + getName());
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
        System.err.println("Invalid argument for Territory.setCropLand method for country " + getName() + " crop " + crop);
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
        System.err.println("Invalid argument for Territory.setMethodPercentage method");
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

  public String toString()
  {
    return getClass().getSimpleName() + " " + name;
  }
}
