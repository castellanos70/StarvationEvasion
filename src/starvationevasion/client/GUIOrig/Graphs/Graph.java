package starvationevasion.client.GUIOrig.Graphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * The class Graph is the class from which all implemented classes are extended
 * It holds a series, a dataset a lineChart, two axis and a title which the graph will display
 */
public class Graph
{
  String title;

  LineChart lineChart;
  NumberAxis xAxis;
  NumberAxis yAxis;
  XYChart.Series series;
  ObservableList<XYChart.Data<Integer, Integer>> dataset;

  /**
   * Default constructor for a Graph
   */
  public Graph()
  {
    xAxis = new NumberAxis(1980, 2050, 1);
    yAxis = new NumberAxis();
    xAxis.setLabel("Year");

    initializeDataset();
    series = new XYChart.Series<Integer, Integer>();

    yAxis = new NumberAxis();
    yAxis.setAutoRanging(true);
    lineChart = new LineChart(xAxis, yAxis);

    lineChart.getStylesheets().add("/starvationevasion/client/GUIOrig/Graphs/style.css");
    lineChart.getStyleClass().addAll("chart-title", "axis-label");
    lineChart.setLegendVisible(false);

    series.setData(dataset);
    lineChart.getData().add(series);
  }

  /**
   * Addes a data point to the graph for food production/consumption graph
   * @param year
   * @param grossIncome
   * @param foodProduced
   * @param foodConsumed
   */
  public void addDataPoint(int year, int[] grossIncome, int[] foodProduced, int[] foodConsumed)
  {
  }

  /**
   * Adds a data point to the graph at a given year and a value
   * @param year year to add (x value of the data point)
   * @param value y value of the data point
   */
  public void addDataPoint(int year, int value)
  {
    XYChart.Data<Integer, Integer> data = new XYChart.Data<>(year, value);
    data.setNode(new HoveredNode(value));
    dataset.add(data);
  }

  /**
   * Initializes the dataset so there's no null pointers
   * @return
   */
  public ObservableList<XYChart.Data<Integer, Integer>> initializeDataset()
  {
    dataset = FXCollections.observableArrayList();
    return dataset;
  }


  /**
   * Returns the lineChart to display on the GUIOrig
   * @return
   */
  public LineChart getLineChart()
  {
    return lineChart;
  }
}
