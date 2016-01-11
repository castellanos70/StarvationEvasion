/**
 * @author Mohammad R. Yousefi
 * Contains the crop data and chart data for the game.
 * @param TOP_ARRAY_SIZE The size of the array returned for top producers and consumers.
 * @param crop Final EnumFood associated with this data.
 */
package starvationevasion.client.Rayquaza.model.simulationData;

import javafx.scene.chart.XYChart;
import starvationevasion.common.*;
import starvationevasion.common.RegionData;
import starvationevasion.client.Rayquaza.engine.FoodManager;

import java.util.*;

public class Crop
{
  public static final int TOP_ARRAY_SIZE = 5;
  public final EnumFood crop;
  private final List<Double> price = new LinkedList<>();
  private static final Comparator<Tuple<EnumFood, RegionData>> topProducerComparator = (o1, o2) -> o1.b
      .foodProduced[o1.a.ordinal()] - o1.b.foodProduced[o1.a.ordinal()];
  private static final Comparator<Tuple<EnumFood, RegionData>> topConsumerComparator = (o1, o2) -> (o1.b
      .foodProduced[o1.a.ordinal()] - o1.b.foodExported[o1.a.ordinal()]) - (o2.b.foodProduced[o2.a.ordinal()] - o2.b
      .foodExported[o2.a.ordinal()]);

  private EnumRegion[] topProducers = new EnumRegion[TOP_ARRAY_SIZE];
  private EnumRegion[] topConsumers = new EnumRegion[TOP_ARRAY_SIZE];

  /* Dummy crop data. */
  public Crop(EnumFood crop, boolean dummy)
  {
    this(crop);

    if (dummy)
    {
      Random random = new Random();
      System.out.println("Creating Dummy Crop " + FoodManager.getDisplayName(crop));
      for (int i = 0; i < 10; i++) price.add(1 + random.nextDouble() * 50);
      for (int i = 0; i < TOP_ARRAY_SIZE; i++)
      {
        topProducers[i] = EnumRegion.values()[random.nextInt(EnumRegion.values().length)];
        topConsumers[i] = EnumRegion.values()[random.nextInt(EnumRegion.values().length)];
      }
    }

  }

  /**
   * The default constructor for the Crop.
   *
   * @param crop the EnumFood associated with this data.
   */
  public Crop(EnumFood crop)
  {
    this.crop = crop;
  }

  /**
   * Updates the data for this region. Each call of this method is considered an advancement of the time.
   *
   * @param data The data provided.
   */
  public void update(WorldData data)
  {
    price.add(data.foodPrice[crop.ordinal()]);
    processProducers(data);
    processConsumers(data);
  }

  /*Helper method for update.*/
  private void processProducers(WorldData data)
  {
    PriorityQueue<Tuple<EnumFood, RegionData>> topProducers = new PriorityQueue<>(topProducerComparator);
    for (RegionData regionData : data.regionData)
    {
      topProducers.add(new Tuple<>(crop, regionData));
    }
    this.topProducers = new EnumRegion[TOP_ARRAY_SIZE];
    int available = (TOP_ARRAY_SIZE <= topProducers.size() ? TOP_ARRAY_SIZE : topProducers.size());
    Iterator<Tuple<EnumFood, RegionData>> iterator = topProducers.iterator();
    for (int i = 0; i < available; i++)
    {
      this.topProducers[i] = iterator.next().b.region;
    }
  }

  /*Helper method for updates. Updated the top 5 list.*/
  private void processConsumers(WorldData data)
  {
    PriorityQueue<Tuple<EnumFood, RegionData>> topConsumers = new PriorityQueue<>(topConsumerComparator);
    for (RegionData regionData : data.regionData)
    {
      topConsumers.add(new Tuple<>(crop, regionData));
    }
    this.topProducers = new EnumRegion[TOP_ARRAY_SIZE];
    int available = (TOP_ARRAY_SIZE <= topConsumers.size() ? TOP_ARRAY_SIZE : topConsumers.size());
    Iterator<Tuple<EnumFood, RegionData>> iterator = topConsumers.iterator();
    for (int i = 0; i < available; i++)
    {
      this.topConsumers[i] = iterator.next().b.region;
    }
  }

  /**
   * Returns the top consumers of this crop.
   *
   * @return an array of top consumers. Returns an array of length 0 when no data is available.
   */
  public EnumRegion[] getTopConsumers()
  {
    return topConsumers;
  }

  /**
   * Returns the top consumers of this crop.
   *
   * @return an array of top consumers. Returns an array of length 0 when no data is available.
   */
  private EnumRegion[] getTopProducers()
  {
    return topProducers;
  }

  /**
   * Returns the price of the food in the current year.
   *
   * @return The price of the food for the food type or Double.MIN_VALUE if not set.
   */
  public double getPrice()
  {
    if (price.size() > 0) return price.get(price.size() - 1);
    else return Double.MIN_VALUE;
  }

  /**
   * Produces and XYChart.Series of the price data currently available.
   */
  public XYChart.Series getPriceChartSeries()
  {
    int[] year = {Constants.START_YEAR};
    int increment = 3;
    XYChart.Series<String, Double> values = new XYChart.Series<>();
    price.forEach(price -> {
      values.getData().add(new XYChart.Data("" + year[0], price));
      year[0] += increment;
    });
    return values;
  }
}