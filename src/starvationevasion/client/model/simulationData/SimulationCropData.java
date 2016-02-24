/**
 * @author Mohammad R. Yousefi
 * Contains the crop data and chart data for the game.
 * @param DEFAULT_TOP_ARRAY_SIZE The size of the array returned for top producers and consumers.
 * @param crop Final EnumFood associated with this data.
 */
package starvationevasion.client.model.simulationData;

import starvationevasion.common.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class SimulationCropData
{
  public static final int DEFAULT_TOP_ARRAY_SIZE = 5;
  public final EnumFood crop;
  public final double price;

  private final EnumRegion[] sortedProducers;
  private final EnumRegion[] sortedConsumers;

  /**
   * The default constructor for the Crop.
   *
   * @param crop the EnumFood associated with this data.
   */
  public SimulationCropData(EnumFood crop, WorldData data)
  {
    this.crop = crop;
    this.price = data.foodPrice[crop.ordinal()];
    sortedProducers = new EnumRegion[EnumRegion.values().length];
    sortedConsumers = new EnumRegion[EnumRegion.values().length];
    processData(data);
  }

  private void processData(WorldData worldData)
  {
    throw new NotImplementedException();

//    LinkedList<SortingData> cropList = new LinkedList<>();
//    for (int i = 0; i < EnumRegion.values().length; i++)
//    {
//      cropList.add(new SortingData(EnumRegion.values()[i], worldData.regionData[i].foodProduced[crop.ordinal()],
//          worldData.regionData[i].foodExported[crop.ordinal()]));
//    }
//    Collections.sort(cropList, Comparator.comparing(SortingData::getProduction).reversed());
//    Iterator<SortingData> iterator = cropList.iterator();
//    for (int i = 0; i < EnumRegion.values().length; i++)
//    {
//      sortedProducers[i] = iterator.next().region;
//    }
//
//    Collections.sort(cropList, Comparator.comparing(SortingData::getConsumption).reversed());
//    iterator = cropList.iterator();
//    for (int i = 0; i < EnumRegion.values().length; i++)
//    {
//      sortedProducers[i] = iterator.next().region;
//    }
  }

  /**
   * Returns the top consumers of this crop.
   *
   * @return an array of top consumers.
   */
  public EnumRegion[] getTopConsumers(int length)
  {
    if (length < 1 || length > sortedConsumers.length)
      throw new IllegalArgumentException("Length must be greater than 0 and less than " + sortedConsumers.length + ".");
    return Arrays.copyOfRange(sortedConsumers, 0, length - 1);
  }

  /**
   * Returns the top consumers of this crop.
   *
   * @return an array of top consumers.
   */
  public EnumRegion[] getTopProducers(int length)
  {
    if (length < 1 || length > sortedConsumers.length)
      throw new IllegalArgumentException("Length must be greater than 0 and less than " + sortedProducers.length + ".");
    return Arrays.copyOfRange(sortedProducers, 0, length - 1);
  }

  /**
   * Returns the price of the food in the current year.
   *
   * @return The price of the food for the food type or Double.MIN_VALUE if not set.
   */
  public double getPrice()
  {
    return price;
  }

  private class SortingData
  {
    public final EnumRegion region;
    public final double production;
    public final double consumption;

    public SortingData(EnumRegion region, double production, double export)
    {
      this.region = region;
      this.production = production;
      this.consumption = production - export;
    }

    public double getProduction()
    {
      return production;
    }

    public double getConsumption()
    {
      return consumption;
    }
  }

}