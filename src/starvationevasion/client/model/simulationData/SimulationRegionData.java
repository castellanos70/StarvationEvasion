package starvationevasion.client.model.simulationData;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;

/**
 */
public class SimulationRegionData
{
  public final EnumRegion region;
  public int revenueBalance;
  public int population;
  public double undernourished;
  public double humanDevelopmentIndex;
  public int[] foodProduced = new int[EnumFood.values().length];
  public int[] foodIncome = new int[EnumFood.values().length];
  public int[] foodExported = new int[EnumFood.values().length];
  public int ethanolProducerTaxCredit;
  public int[] farmArea = new int[EnumFood.values().length];
  
  public SimulationRegionData(EnumRegion region, WorldData data)
  {
    this.region = region;
    this.revenueBalance = data.regionData[region.ordinal()].revenueBalance;
    this.population = data.regionData[region.ordinal()].population;
    this.undernourished = data.regionData[region.ordinal()].undernourished;
    this.humanDevelopmentIndex = data.regionData[region.ordinal()].humanDevelopmentIndex;
    this.ethanolProducerTaxCredit = data.regionData[region.ordinal()].ethanolProducerTaxCredit;
    for (int i = 0; i < EnumFood.values().length; i++)
    {
      foodProduced[i] = data.regionData[region.ordinal()].foodProduced[i];
      foodExported[i] = data.regionData[region.ordinal()].foodExported[i];
      foodProduced[i] = data.regionData[region.ordinal()].foodProduced[i];
      farmArea[i] = data.regionData[region.ordinal()].farmArea[i];
    }
  }
}
