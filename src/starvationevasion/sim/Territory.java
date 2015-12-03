package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.sim.util.EquirectangularConverter;
import starvationevasion.sim.util.MapConverter;
import starvationevasion.common.MapPoint;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;

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
public class Territory extends AbstractTerritory
{
  final public static MapConverter converter = new EquirectangularConverter();

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
    int income = 0;
    int production = 0;
    int land = 0;
    for (EnumFood crop : EnumFood.values())
    {
      land += landCrop[crop.ordinal()];
      production += cropProduction[crop.ordinal()];
      income += cropIncome[crop.ordinal()];
    }

    // If the total land is > 0 then this function has already been called.
    //
    //if (land == 0.)
    //{
      //if (income == 0. && production == 0.)
      //{ Logger.getGlobal().log(Level.INFO,
      //        "Territory {0} has no production or income. Faking it.", getName());

        // Assume they're getting $100 per acre.  Terrible guess, but it's just a
        // default for empty rows.
        //
      //  income = landTotal / 10; // $100 per acre / 1000.;

      //  for (int i = 0 ; i < EnumFood.SIZE ; i += 1) cropIncome[i] = income * EnumFood.SIZE;
      //}

      if (production == 0.)
      {
        for (EnumFood crop : EnumFood.values()) {
          // The current version of the CSV file doesn't have any production values.
          // Use the income values (if available) to estimate land per crop.
          //

          // Estimate production from the yield.
          //TODO: read data
          //cropProduction[crop.ordinal()] = (cropIncome[crop.ordinal()] / income) * landTotal;
          production += cropProduction[crop.ordinal()];
        }
      }

      for (EnumFood crop : EnumFood.values())
      {
        //TODO: read data
        //cropYield[crop.ordinal()] = cropProduction[crop.ordinal()] / landTotal;

        // Use the crop production to estimate land per crop.
        //
        //double p = cropProduction[crop.ordinal()] / production;

        // This is an initial naive estimate.  Per Joel there will eventually be a multiplier
        // applied that gives a more realistic estimate.
        //
        //landCrop[crop.ordinal()] =(int)( landTotal * p );// multiplier[food]

        land += landCrop[crop.ordinal()];
      }
    //}
  }

  /**
   * The loader loads 2014 data.  This function scales the data for 1981 given the scale factor.
   * @param factor The scaling factor.
   */
  public void scaleInitialStatistics(double factor)
  {
    int index = 0; // The 0th index is the start year.

    population[index] *= factor;
    medianAge *= factor;
    births *= factor;
    mortality *= factor;
    migration *= factor;
    undernourished *= factor;

    landTotal *= factor;

    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropYield[i] *= factor;
      cropNeedPerCapita[i] *= factor;
      cropProduction[i] *= factor;
      landCrop[i] *= factor;
    }

    for (int i = 0 ; i < EnumFarmMethod.values().length ; i += 1)
    {
      cultivationMethod[i] *= factor;
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
    medianAge = fromTerritory.medianAge;
    births = fromTerritory.births;
    mortality = fromTerritory.mortality;
    migration = fromTerritory.migration;
    undernourished = fromTerritory.undernourished;

    landTotal = fromTerritory.landTotal;

    for (EnumFood food : EnumFood.values())
    {
      cropYield[food.ordinal()] = fromTerritory.cropYield[food.ordinal()];
      cropNeedPerCapita[food.ordinal()] = fromTerritory.cropNeedPerCapita[food.ordinal()];
      cropIncome[food.ordinal()] = fromTerritory.cropIncome[food.ordinal()];
      cropProduction[food.ordinal()] = fromTerritory.cropProduction[food.ordinal()];
      landCrop[food.ordinal()] = fromTerritory.landCrop[food.ordinal()];
    }

    for (EnumFarmMethod method : EnumFarmMethod.values())
    {
      cultivationMethod[method.ordinal()] = fromTerritory.cultivationMethod[method.ordinal()];
    }

    while (index < Constant.LAST_YEAR - Constant.FIRST_YEAR)
    {
      population[index] = fromTerritory.population[index];
      index += 1;
    }
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




  public String toString()
  {
    return getClass().getSimpleName() + " " + name;
  }
}
