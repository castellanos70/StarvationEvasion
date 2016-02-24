package starvationevasion.client.model.simulationData;

import javafx.scene.chart.XYChart;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.WorldData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MohammadR on 1/17/2016.
 * Contains the world data simulated over time.
 */
public class WorldSimulationData
{
  public final int startYear;
  public final int period;
  private int updateCount;

  private final List<SimulationAnnualData> worldData;

  /**
   * Creates a new instance of the class.
   *
   * @param startYear starting year for the data provided.
   * @param period    the span of time covered by each entry.
   */
  public WorldSimulationData(int startYear, int period)
  {
    this.startYear = startYear;
    this.period = period;
    this.worldData = new ArrayList<SimulationAnnualData>();
    updateCount = 0;
  }

  public void updateWorldData(WorldData periodicData)
  {
    int expectation = startYear + updateCount * period;
    if (expectation != periodicData.year) throw new DataYearMismatchException(expectation, periodicData.year);
    updateCount++;
    worldData.add(new SimulationAnnualData(periodicData));
  }

  public SimulationAnnualData getLastEntry()
  {
    return worldData.get(updateCount - 1);
  }


  public EnumRegion[] getTopProducers(int year, EnumFood crop, int length)
  {
    return worldData.get((year - startYear) / period).getTopProducers(crop, length);
  }

  public EnumRegion[] getTopConsumers(int year, EnumFood crop, int length)
  {
    return worldData.get((year - startYear) / period).getTopConsumers(crop, length);
  }

  public XYChart.Series<String, Integer> getRevenueChart(EnumRegion region)
  {
    XYChart.Series<String, Integer> dataSeries = new XYChart.Series<String, Integer>();
    int year = startYear;
    for (int i = 0; i < updateCount; i++)
    {
      assert year == worldData.get(i).year;
      SimulationAnnualData data = worldData.get(i);
      dataSeries.getData().add(new XYChart.Data<String, Integer>(String.format("%d", year), data.getRegionData(region)
          .revenueBalance));
      year += i * period;
    }
    return dataSeries;
  }

  public XYChart.Series<String, Integer> getPopulationChart(EnumRegion region)
  {
    XYChart.Series<String, Integer> dataSeries = new XYChart.Series<String, Integer>();
    int year = startYear;
    for (int i = 0; i < updateCount; i++)
    {
      assert year == worldData.get(i).year;
      SimulationAnnualData data = worldData.get(i);
      dataSeries.getData().add(new XYChart.Data<String, Integer>(String.format("%d", year), data.getRegionData(region).population));
      year += i * period;
    }
    return dataSeries;
  }

  public XYChart.Series<String, Double> getHumanDevelopementIndexChart(EnumRegion region)
  {
    XYChart.Series<String, Double> dataSeries = new XYChart.Series<String, Double>();
    int year = startYear;
    for (int i = 0; i < updateCount; i++)
    {
      assert year == worldData.get(i).year;
      SimulationAnnualData data = worldData.get(i);
      dataSeries.getData().add(new XYChart.Data<String, Double>(String.format("%d", year), data.getRegionData(region)
          .humanDevelopmentIndex));
      year += i * period;
    }
    return dataSeries;
  }

  public XYChart.Series<String, Double> getUndernourishedChart(EnumRegion region)
  {
    XYChart.Series<String, Double> dataSeries = new XYChart.Series<String, Double>();
    int year = startYear;
    for (int i = 0; i < updateCount; i++)
    {
      assert year == worldData.get(i).year;
      SimulationAnnualData data = worldData.get(i);
      dataSeries.getData().add(new XYChart.Data<String, Double>(String.format("%d", year), data.getRegionData(region)
          .undernourished));
      year += i * period;
    }
    return dataSeries;
  }

  public XYChart.Series<String, Integer> getFoodIncome(EnumRegion region, EnumFood crop)
  {
    XYChart.Series<String, Integer> dataSeries = new XYChart.Series<String, Integer>();
    int year = startYear;
    for (int i = 0; i < updateCount; i++)
    {
      assert year == worldData.get(i).year;
      dataSeries.getData().add(new XYChart.Data<String, Integer>(String.format("%d", year), worldData.get(i).getRegionData(region)
          .foodIncome[crop.ordinal()]));
      year += i * period;
    }
    return dataSeries;
  }
}
