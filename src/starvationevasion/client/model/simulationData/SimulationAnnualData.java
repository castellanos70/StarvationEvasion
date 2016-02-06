package starvationevasion.client.model.simulationData;

import starvationevasion.common.*;

import java.util.LinkedList;

/**
 * Created by Mohammad R on 1/17/2016.
 */
public class SimulationAnnualData
{
  public final int year;
  private final SimulationCropData[] cropData;
  private final SimulationRegionData[] regionData;
  private LinkedList<SpecialEventData> specialEvents;

  public SimulationAnnualData(int year)
  {
    this.year = year;
    cropData = new SimulationCropData[EnumFood.SIZE];
    regionData = new SimulationRegionData[EnumRegion.SIZE];
  }

  public SimulationAnnualData(WorldData data)
  {
    this(data.year);
    updateAnnualData(data);
  }


  private void updateAnnualData(WorldData data)
  {
    updateRegions(data);
    updateCrops(data);
  }

  private void updateRegions(WorldData data)
  {
    for (int i = 0; i < EnumRegion.values().length; i++)
    {
      regionData[i] = new SimulationRegionData(EnumRegion.values()[i], data);
    }
  }

  private void updateCrops(WorldData data)
  {
    for (int i = 0; i < EnumFood.values().length; i++)
    {
      cropData[i] = new SimulationCropData(EnumFood.values()[i], data);
    }
  }

  /***
   * Replaces the current data for the food category with the given data.
   *
   * @param data Reference pointer to the new data.
   */
  public void replaceData(SimulationCropData data)
  {
    int index = data.crop.ordinal();
    cropData[index] = data;
  }

  /***
   * Replaces the current data for the region with the given data.
   *
   * @param data Reference pointer to the new data.
   */
  public void replaceData(SimulationRegionData data)
  {
    int index = data.region.ordinal();
    regionData[index] = data;
  }

  public EnumRegion[] getTopProducers(EnumFood crop, int length)
  {
    return cropData[crop.ordinal()].getTopProducers(length);
  }

  public EnumRegion[] getTopConsumers(EnumFood crop, int length)
  {
    return cropData[crop.ordinal()].getTopConsumers(length);
  }

  public SimulationRegionData getRegionData(EnumRegion region)
  {
    return regionData[region.ordinal()];
  }

  public SimulationCropData getCropData(EnumFood crop)
  {
    return cropData[crop.ordinal()];
  }
}
