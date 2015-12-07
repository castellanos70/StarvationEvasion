package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.sim.util.EquirectangularConverter;
import starvationevasion.sim.util.MapConverter;
import starvationevasion.common.MapPoint;

import java.awt.geom.Area;
import java.util.*;

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

  private double[] land1981 = new double[EnumFood.SIZE];
  private double[] yield1981 = new double[EnumFood.SIZE];
  private long[] cropBudget = new long[EnumFood.SIZE];

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
  public void updateYield()
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

      if (landCrop[crop.ordinal()] != 0)
      { cropYield[crop.ordinal()] = cropProduction[crop.ordinal()] / landCrop[crop.ordinal()];
      }
      else cropYield[crop.ordinal()] = 0;
    }
  }

  public void setCropBudget(EnumFood food, long budget)
  {
    cropBudget[food.ordinal()] = budget;
  }

  /**
   * Get the budget for the type of food
   *
   * @param food EnumFood
   * @return current budget of the food
   */
  public long getCropBudget(EnumFood food)
  {
    return cropBudget[food.ordinal()];
  }

  /**
   * Get the total budget for all crops
   *
   * @return total budget of all crops for the territory
   */
  public long getCropBudget()
  {
    long budget = 0;
    for (int i = 0; i < cropBudget.length; i++)
    {
      budget += cropBudget[i];
    }
    return budget;
  }

  public void setLand1981(EnumFood food, double land)
  {
    land1981[food.ordinal()] = land;
  }

  /**
   * Get the land area for the type of food
   *
   * @param food EnumFood
   * @return current land area of the food
   */
  public double getLand1981(EnumFood food)
  {
    return land1981[food.ordinal()];
  }

  /**
   * Get the total land area for all crops
   *
   * @return total land area of all crops for the territory
   */
  public double getLand1981()
  {
    double land = 0;
    for (int i = 0; i < land1981.length; i++)
    {
      land += land1981[i];
    }
    return land;
  }

  public void setYield1981(EnumFood food, double yield)
  {
    yield1981[food.ordinal()] = yield;
  }

  /**
   * Get the yield for the type of food
   *
   * @param food EnumFood
   * @return current yield of the food
   */
  public double getYield1981(EnumFood food)
  {
    return yield1981[food.ordinal()];
  }

  /**
   * Get the total yield for all crops
   *
   * @return total yield of all crops for the territory
   */
  public double getYield1981()
  {
    double yield = 0;
    for (int i = 0; i < yield1981.length; i++)
    {
      yield += yield1981[i];
    }
    return yield;
  }

  /**
   * The loader loads 2014 data.  This function scales the data for 1981 given the scale factor.
   * @param factor The scaling factor.
   */
  public void scaleCropData(double factor)
  {
    for (int i = 0 ; i < EnumFood.values().length ; i += 1)
    {
      cropIncome[i] *= factor;
      cropProduction[i] *= factor;
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

  public void updatePopulation(int year)
  {
    int index = year - Constant.FIRST_YEAR;

    // Population data is stored in a fixed array.
    //
    int netChange = 0;
    if (index > 0) netChange = population[index] - population[index - 1];

    // TODO: We need a way to take the net change in population and back that
    // number out to birth rate, mortality rate, and undernourishment. This is
    // the Spring code to update undernourishment, based on the Spring 2015
    // spec. :
    //
    double numUndernourished;
    double population = getPopulation(year);
    double[] netCropsAvail = new double[EnumFood.SIZE];
    int numCropsAvail = 0;
    for (EnumFood crop : EnumFood.values())
    {
      double netAvail = getNetCropAvailable(crop);
      netCropsAvail[crop.ordinal()] = netAvail;
      if (netAvail >= 0) numCropsAvail++;
    }

    if (numCropsAvail == EnumFood.SIZE)
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

    setUndernourished((int) numUndernourished);
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

  public int compareTo(String territoryName)
  {
    return territoryName.compareTo(name);
  }

  /**
   * Parses the geographic data and generates a unified set of Territory objects from the
   * list of cartagraphic regions.
   * @return collection of countries created form the given regions.
   */
  public static Territory[] parseTerritories(List<GeographicArea> geography)
  {
    Collections.sort(geography, new Comparator<GeographicArea>() {
      @Override
      public int compare(GeographicArea a1, GeographicArea a2) {
        return a1.getName().compareTo(a2.getName());
      }
    });

    ArrayList<Territory> territoryList = new ArrayList<>(geography.size());
    Territory territory = null;
    for (GeographicArea region : geography)
    {
      if (territory != null && territory.getName().equals(region.getName()))
      {
        region.setTerritory(territory);
        territory.addRegion(region);
      }
      else {
        territory = new Territory(region.getName());
        territory.addRegion(region);
        territoryList.add(territory);
      }
    }

    // We may not have a territory for the United States.
    //
    Territory us = new Territory("United States");
    Collections.sort(territoryList);
    if (Collections.binarySearch(territoryList, us) < 0) territoryList.add(us);

    return territoryList.toArray(new Territory[territoryList.size()]);
  }
}
