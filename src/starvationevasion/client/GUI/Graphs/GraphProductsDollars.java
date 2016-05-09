package starvationevasion.client.GUI.Graphs;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import starvationevasion.common.EnumRegion;

public class GraphProductsDollars extends Graph
{
  EnumRegion region;
  NumberAxis yAxisDollars;
  NumberAxis yAxisTons;

  LineChart dollarsChart;
  LineChart tonsChart;

  ObservableList<XYChart.Data<Integer, Integer>>[] foodIncome;
  ObservableList<XYChart.Data<Integer, Integer>>[] foodProduced;
  ObservableList<XYChart.Data<Integer, Integer>>[] foodExported;

  public GraphProductsDollars(EnumRegion region)
  {
    this.region = region;
    this.title = "Food Product Data for " + region.toString();

    yAxisDollars = new NumberAxis();
    yAxisTons = new NumberAxis();

    dollarsChart = new LineChart(xAxis, yAxisDollars);
    tonsChart = new LineChart(xAxis, yAxisTons);

    yAxis.setLabel("");
  }

  @Override
  public void addDataPoint(int year, int[] grossIncome, int[] foodProduced, int[] foodConsumed)
  {
    for (int i = 0; i < 12; ++i)
    {
      XYChart.Data<Integer, Integer> data = new XYChart.Data<>(year, grossIncome[i]);
      data.setNode(new HoveredNode(grossIncome[i]));
      this.foodIncome[i].add(data);
    }

    for (int i = 0; i < 12; ++i)
    {
      XYChart.Data<Integer, Integer> data = new XYChart.Data<>(year, foodProduced[i]);
      data.setNode(new HoveredNode(foodProduced[i]));
      this.foodProduced[i].add(data);
    }

    for (int i = 0; i < 12; ++i)
    {
      XYChart.Data<Integer, Integer> data = new XYChart.Data<>(year, foodConsumed[i]);
      data.setNode(new HoveredNode(foodProduced[i]));
      this.foodProduced[i].add(data);
    }
  }

  private void initDataArrays()
  {
    foodIncome = new ObservableList[12];
    foodProduced = new ObservableList[12];
    foodExported = new ObservableList[12];
    for (int i = 0; i < 12; ++i)
    {

    }
  }
}
