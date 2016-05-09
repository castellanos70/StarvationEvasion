package starvationevasion.client.GUI.Graphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
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
  
  PieChart pieChart = new PieChart();;
  
  
  XYChart.Series series;
  ObservableList<XYChart.Data<Integer, Integer>> dataset;

  /**
   * Default constructor for a Graph
   */
  public Graph()
  {
    xAxis = new NumberAxis(2000, 2050, 1);
    yAxis = new NumberAxis();
    xAxis.setLabel("Year");

    initializeDataset();
    series = new XYChart.Series<Integer, Integer>();

    yAxis = new NumberAxis();
    yAxis.setAutoRanging(true);
    lineChart = new LineChart(xAxis, yAxis);

    lineChart.getStylesheets().add("/starvationevasion/client/GUI/Graphs/style.css");
    lineChart.getStyleClass().addAll("chart-title", "axis-label");
    lineChart.setLegendVisible(false);
    series.setData(dataset);
    lineChart.getData().add(series);
    
    
     pieChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/pieChartCSS.css").toExternalForm());
    
	 pieChart.setVisible(true);
	 pieChart.getData().clear();
	 pieChart.setTitle("Regional Crop Distribution");
	 ObservableList<Data> list = FXCollections.observableArrayList(
		 new PieChart.Data("Citrus Fruits", 3),
		 new PieChart.Data("Non-Citrus Fruits", 4),
		 new PieChart.Data("Nuts", 3),
		 new PieChart.Data("Grains", 13),
		 new PieChart.Data("Oil Crops", 6),
		 new PieChart.Data("Vegetables", 8),
		 new PieChart.Data("Specialty Crops", 1),
		 new PieChart.Data("Feed Crops", 20),
		 new PieChart.Data("Fish", 7),
		 new PieChart.Data("Meat Animals", 15),
		 new PieChart.Data("Poultry and Eggs", 11),
		 new PieChart.Data("Dairy Products", 9)
		 );
	 
	 pieChart.setData(list);
    
    
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

//  public PieChart getPieChart()
//  {
//	  return pieChart;
//  }
  
  /**
   * Returns the lineChart to display on the GUI
   * @return
   */
  public LineChart getLineChart()
  {
    return lineChart;
  }
}
