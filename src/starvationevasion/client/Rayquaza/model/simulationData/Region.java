/**
 * @author Mohammad R. Yousefi
 * Contains the region data and chart data for the game.
 * <p>
 * All current values are returned as Integer.MIN_VALUE or Double.MIN_VALUE if the data set for the requested value
 * is empty.
 * @param region Final EnumRegion associated with this data.
 */
package starvationevasion.client.Rayquaza.model.simulationData;

import javafx.scene.chart.XYChart;
import starvationevasion.common.Constant;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.client.Rayquaza.engine.MapManager;

import java.util.*;

public class Region
{
  /**
   * Available chart data.
   */
  public enum RegionChartDataType
  {
    REGION_REVENUE,
    REGION_POPULATION,
    REGION_HDI,
    CITRUS_REVENUE,
    DAIRY_REVENUE,
    FEED_REVENUE,
    FISH_REVENUE,
    FRUIT_REVENUE,
    GRAIN_REVENUE,
    MEAT_REVENUE,
    NUT_REVENUE,
    OIL_REVENUE,
    POULTRY_REVENUE,
    SPECIAL_REVENUE,
    VEGGIES_REVENUE
  }

  public final EnumRegion region;
  private int lastUpdatedYear;
  private List<Integer> revenueBalance = new LinkedList<>();
  private List<Integer> population = new LinkedList<>();
  private List<Double> undernourished = new LinkedList<>();
  private List<Double> humanDevelopmentIndex = new LinkedList<>();
  private Map<EnumFood, List<Integer>> foodIncome = new HashMap<>();
  private Map<EnumFood, List<Integer>> foodProduced = new HashMap<>();
  private Map<EnumFood, List<Integer>> foodExported = new HashMap<>();
  private Map<EnumFood, List<Integer>> farmArea = new HashMap<>();

  /* Dummy data constructor*/
  public Region(EnumRegion region, boolean dummy)
  {
    this(region);
    if (dummy)
    {
      System.out.println("Creating Dummy Region " + MapManager.getDisplayName(region));
      lastUpdatedYear = Constant.FIRST_YEAR;
      Random random = new Random();
      for (int i = 0; i < 10; i++)
      {
        revenueBalance.add(1 + random.nextInt(100));
        population.add(1 + random.nextInt(50));
        undernourished.add(random.nextDouble() * 0.5);
        humanDevelopmentIndex.add(random.nextDouble());
        for (EnumFood food : EnumFood.values())
        {
          foodIncome.get(food).add(1 + random.nextInt(100));
          foodProduced.get(food).add(1 + random.nextInt(100));
          foodExported.get(food).add(1 + random.nextInt(100));
          farmArea.get(food).add(1 + random.nextInt(100));
        }
      }
    }

  }

  /**
   * Produces and XYChart.Series of the data currently available.
   *
   * @param type The type from which the series is created.
   */
  public XYChart.Series getChartData(RegionChartDataType type)
  {
    switch (type)
    {
      case REGION_REVENUE:
        return getChartSeries(revenueBalance);
      case REGION_POPULATION:
        return getChartSeries(population);
      case REGION_HDI:
        return getChartSeries(humanDevelopmentIndex);
      case CITRUS_REVENUE:
        return getFoodIncomeSeries(EnumFood.CITRUS);
      case DAIRY_REVENUE:
        return getFoodIncomeSeries(EnumFood.DAIRY);
      case FEED_REVENUE:
        return getFoodIncomeSeries(EnumFood.FEED);
      case FISH_REVENUE:
        return getFoodIncomeSeries(EnumFood.FISH);
      case FRUIT_REVENUE:
        return getFoodIncomeSeries(EnumFood.FRUIT);
      case GRAIN_REVENUE:
        return getFoodIncomeSeries(EnumFood.GRAIN);
      case MEAT_REVENUE:
        return getFoodIncomeSeries(EnumFood.MEAT);
      case NUT_REVENUE:
        return getFoodIncomeSeries(EnumFood.NUT);
      case OIL_REVENUE:
        return getFoodIncomeSeries(EnumFood.OIL);
      case POULTRY_REVENUE:
        return getFoodIncomeSeries(EnumFood.POULTRY);
      case SPECIAL_REVENUE:
        return getFoodIncomeSeries(EnumFood.SPECIAL);
      case VEGGIES_REVENUE:
        return getFoodIncomeSeries(EnumFood.VEGGIES);
      default:
        return null;
    }
  }

  /* Helper class for getChartData*/
  private XYChart.Series getFoodIncomeSeries(EnumFood food)
  {
    List values = foodIncome.get(food);
    return getChartSeries(values);
  }

  /* Helper class for getChartData*/
  private XYChart.Series getChartSeries(List valueList)
  {
    int[] year = {Constants.START_YEAR};
    int increment = 3;
    XYChart.Series<String, Double> values = new XYChart.Series<>();
    valueList.forEach(value -> {
      values.getData().add(new XYChart.Data("" + year[0], value));
      year[0] += increment;
    });
    return values;
  }

  /**
   * Returns the revenue for the current year.
   *
   * @return The current revenue balance or Integer.MIN_VALUE if not set.
   */
  public int getRevenueBalance()
  {
    if (revenueBalance.size() > 0) return revenueBalance.get(revenueBalance.size() - 1);
    else return Integer.MIN_VALUE;
  }

  /**
   * Returns the population for the current year.
   *
   * @return The current population or Integer.MIN_VALUE if not set.
   */
  public int getPopulation()
  {
    if (population.size() > 0) return population.get(population.size() - 1);
    else return Integer.MIN_VALUE;
  }

  /**
   * Returns the undernourished population percentage for the current year.
   *
   * @return The current undernourished population percentage or Double.MIN_VALUE if not set.
   */
  public double getUndernourished()
  {
    if (undernourished.size() > 0) return undernourished.get(undernourished.size() - 1);
    else return Double.MIN_VALUE;
  }

  /**
   * Returns the human development index for the current year.
   *
   * @return The current human development index percentage or Double.MIN_VALUE if not set.
   */
  public double getHumanDevelopmentIndex()
  {
    if (humanDevelopmentIndex.size() > 0) return humanDevelopmentIndex.get(humanDevelopmentIndex.size() - 1);
    else return Double.MIN_VALUE;
  }

  /**
   * Returns the consumed food in the current year.
   *
   * @param food The food type.
   * @return The consumed food amount for the food type or Integer.MIN_VALUE if not set.
   */
  public int getFoodConsumed(EnumFood food)
  {
    if (foodProduced.get(food).size() > 0 && foodExported.get(food).size() > 0)
      return getFoodProduced(food) - getFoodExported(food);
    else return Integer.MIN_VALUE;
  }

  /**
   * Returns the produced food in the current year.
   *
   * @param food The food type.
   * @return The produced food amount for the food type or Integer.MIN_VALUE if not set.
   */
  public int getFoodProduced(EnumFood food)
  {
    if (foodProduced.get(food).size() > 0) return foodProduced.get(food).get(foodProduced.get(food).size() - 1);
    else return Integer.MIN_VALUE;
  }

  /**
   * Returns the exported amount of food in the current year.
   *
   * @param food The food type.
   * @return The exported food amount for the food type or Integer.MIN_VALUE if not set.
   */
  public int getFoodExported(EnumFood food)
  {
    if (foodExported.get(food).size() > 0) return foodExported.get(food).get(foodExported.get(food).size() - 1);
    else return Integer.MIN_VALUE;
  }

  /**
   * Returns the farm land used for food in the current year.
   *
   * @param food The food type.
   * @return The exported food amount for the food type or Integer.MIN_VALUE if not set.
   */
  public int getFarmArea(EnumFood food)
  {
    if (farmArea.get(food).size() > 0) return farmArea.get(food).get(farmArea.get(food).size() - 1);
    else return Integer.MIN_VALUE;
  }

  /**
   * Constructs a Region data without any values.
   *
   * @param region The associated region for this instance.
   */
  public Region(EnumRegion region)
  {
    this.region = region;
    lastUpdatedYear = Integer.MIN_VALUE;
    initMaps();
  }

  /**
   * Returns the last updated year for the region.
   *
   * @return The last updated year or Integer.MIN_VALUE if not set.
   */
  public int getLastUpdatedYear()
  {
    return lastUpdatedYear;
  }

  /*Helper for the constructor.*/
  private void initMaps()
  {
    for (EnumFood food : EnumFood.values())
    {
      foodIncome.put(food, new LinkedList<>());
      foodProduced.put(food, new LinkedList<>());
      foodExported.put(food, new LinkedList<>());
      farmArea.put(food, new LinkedList<>());
    }
  }

  /**
   * Updates the data for this region. Each call of this method is considered an advancement of the time.
   *
   * @param data The data provided.
   */
  public void update(starvationevasion.common.WorldData data)
  {
    lastUpdatedYear = data.year;
    starvationevasion.common.RegionData regionData = data.regionData[region.ordinal()];
    revenueBalance.add(regionData.revenueBalance);
    population.add(regionData.population);
    undernourished.add(regionData.undernourished);
    humanDevelopmentIndex.add(regionData.humanDevelopmentIndex);
    for (EnumFood food : EnumFood.values())
    {
      foodIncome.get(food).add(regionData.foodIncome[food.ordinal()]);
      foodProduced.get(food).add(regionData.foodProduced[food.ordinal()]);
      foodExported.get(food).add(regionData.foodExported[food.ordinal()]);
      farmArea.get(food).add(regionData.farmArea[food.ordinal()]);
    }
  }

  @Override
  public boolean equals(Object o)
  {
    if (o instanceof Region) return ((Region) o).region.equals(region);
    return false;
  }
}