package starvationevasion.client.GUI.Graphs;

import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Graph Manager which handles all of the graphs attached to the GUI
 * Has functions which allows the client to add data to a graph or methods for the graph node in the GUI to get
 * the linechart which needs to be displayed
 */
public class GraphManager
{
  private Label regionNameLabel = new Label();
  private Label currentYear = new Label();
	private ArrayList<Integer> past10YearsNetExports = new ArrayList<>();
	private ArrayList<Integer> past10YearsNetImports = new ArrayList<>();
	private ArrayList<Integer> past10YearsNetProduction = new ArrayList<>();
	private ArrayList<Integer> past10YearsNetDomesticConsumption = new ArrayList<>();
	private ArrayList<Integer> past10YearsBestCropProduction = new ArrayList<>();
 CategoryAxis xAxis = new CategoryAxis();
 NumberAxis yAxis = new NumberAxis();
 
 private boolean NOSTATS;
 PieChart regionalCropDistributionPieChart = new PieChart();
 ArrayList<long[]> annualRegionsCropDistributionStatistics =  new ArrayList<>();
 PieChart regionalCropExportPieChart = new PieChart();
 ArrayList<long[]> annualRegionsCropExportStatistics =  new ArrayList<>();
 PieChart regionalCropImportPieChart = new PieChart();
 ArrayList<long[]> annualRegionsCropImportStatistics =  new ArrayList<>();
 
 
 PieChart masterPieChart = new PieChart();
 

 
 
 private int REGION_NUM = 0;
 
	//Stacked Bar Chart looks better visually than a 
	//regular bar chart it has wider bars. 
 StackedBarChart<String, Number> barChart =
	        new StackedBarChart<String, Number>(xAxis, yAxis);

	//NOTE: These values below are all just for testing and 
	//will be obtained via the game flow once integrated. 
	private String regionName = "Pacific Northwest and Mountain States";
	String bestCropName = "Citrus";
	private int presentYear = 2010;
	private int currentYearlyBestCropProduction = 7_035_514;
	private int currentYearlyNetExports = 15_545_825;
	private int currentYearlyNetImports = 10_945_483;
	private int currentYearlyNetProduction = 8_517_987;
	private int currentYearlyNetDomesticConsumption = 3_917_645;
  GUI gui;
  ArrayList<Graph[]> graphs=new ArrayList<>();
  HashMap <EnumRegion,Graph[]> graphMap=new HashMap<>();
  Graph[] californiaGraphs;
  Graph[] mountainGraphs;
  Graph[] heartlandGraphs;
  Graph[] northernCresentGraphs;
  Graph[] southeastGraphs;
  Graph[] northernPlainsGraphs;
  Graph[] southernPlainsGraphs;

  Graph[] productBarGraphs = new Graph[12];

  final EnumFood[] PRODUCTS = {EnumFood.CITRUS, EnumFood.FRUIT, EnumFood.NUT, EnumFood.GRAIN, EnumFood.OIL, EnumFood.VEGGIES, EnumFood.SPECIAL,
    EnumFood.FEED, EnumFood.FISH, EnumFood.MEAT, EnumFood.POULTRY, EnumFood.DAIRY};

  /**
   * Constructor for the GraphManger
   * @param gui
   */
  public GraphManager(GUI gui)
  {
    this.gui = gui;
    barChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/barChartCSS.css").toExternalForm());
	barChart.applyCss();
	
	masterPieChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/pieChartCSS.css").toExternalForm());
	regionalCropExportPieChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/pieChartCSS.css").toExternalForm());
	regionalCropImportPieChart.getStylesheets().add(getClass().getResource("/starvationevasion/client/GUI/Graphs/pieChartCSS.css").toExternalForm());
	//Set initial region name (needs to be obtained from game once integrated)
	regionNameLabel.setText(regionName);
	regionNameLabel.setTextFill(Color.web("#FFFFFF")); //white
    initializeProductBarGraphs();
    initializeGraphNodeGraphs();
    
    initializeData();
    updateRegionalCropDistributionDisplay();
   // updateRegionalCropExportsDisplay();
    //updateRegionalCropImportsDisplay();
    
  }

  

  //Regional stats can only be displayed once the data has been collected,
  //so untill that happens 0's are inputted as the data. 
  public void initializeData()
  {
    NOSTATS=true;
    for(int i = 0; i < 18; i++)
    {
      annualRegionsCropDistributionStatistics.add(new long[] {0,1,0,0,0,0,0,0,0,0,0,0});
      annualRegionsCropExportStatistics.add(new long[] {0,0,0,0,0,1,0,0,0,1,0,0});
      annualRegionsCropImportStatistics.add(new long[] {0,1,0,0,0,0,1,0,1,0,0,0});
    }
  }
  
  
  
  public void updateRegionalCropDistributionNumbers(int year, ArrayList<long[]> updatedStats)
  {
    NOSTATS=false;
    System.out.println("size 2: " + updatedStats.size());
    annualRegionsCropDistributionStatistics = new ArrayList<>();
   // this.annualRegionsCropDistributionStatistics=updatedStats;
    for(long[] region: updatedStats)
    {
    
      annualRegionsCropDistributionStatistics.add(region);
    }
    System.out.println(Arrays.toString(updatedStats.get(8)));
    System.out.println("SIZE 1: " + annualRegionsCropDistributionStatistics.size());
  //  annualRegionsCropDistributionStatistics.clear();
    System.out.println("size 3: " + annualRegionsCropDistributionStatistics.size());
   // updateRegionalCropDistributionDisplay();
  }
  
  public void updateRegionalCropExportNumbers(int year,  ArrayList<long[]> updatedStats)
  {
    annualRegionsCropExportStatistics= new ArrayList<>();
    //this.annualRegionsCropExportStatistics=updatedStats;
    for(long[] region: updatedStats)
    {
      annualRegionsCropExportStatistics.add(region);
    }
    System.out.println("SIZE 2: " + annualRegionsCropExportStatistics.size());
   // annualRegionsCropExportStatistics.clear();
 //   updateRegionalCropExportsDisplay();
  }
  
  public void updateRegionalCropImportNumbers(int year,  ArrayList<long[]> updatedStats)
  {
    annualRegionsCropImportStatistics= new ArrayList<>();
    //this.annualRegionsCropImportStatistics=updatedStats;
    for(long[] region: updatedStats)
    {
      annualRegionsCropImportStatistics.add(region);
    }
    System.out.println("SIZE 3: " + annualRegionsCropImportStatistics.size());
    //annualRegionsCropImportStatistics.clear();
   // updateRegionalCropImportsDisplay();
  }
  
  
  public void setRegionNum(int num)
  {
    this.REGION_NUM=num;
  }
  
  /**
   * Method which adds a data point to the graphs in the food product bar
   * @param year
   * @param foodPrice
   */
  public void addData(int year, int[] foodPrice)
  {
    int p;
    for (int i = 0; i < 12; ++i)
    {
      p =   foodPrice[i];
      productBarGraphs[i].addDataPoint(year, p);
    }
  }

  public void addData(EnumRegion region, int graphNumber, int year, int dataValue)
  {
    Graph g;
    Graph[] regionGraphs=graphMap.get(region);
    regionGraphs[graphNumber].addDataPoint(year,dataValue);
//    switch (region)
//    {
//      case USA_CALIFORNIA:
//        g = californiaGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      case USA_MOUNTAIN:
//        g= mountainGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      case USA_HEARTLAND:
//        g = heartlandGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      case USA_NORTHERN_PLAINS:
//        g = northernPlainsGraphs[graphNumber];
//        break;
//      case USA_SOUTHERN_PLAINS:
//        g = southernPlainsGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      case USA_NORTHERN_CRESCENT:
//        g = northernCresentGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      case USA_SOUTHEAST:
//        g = southeastGraphs[graphNumber];
//        g.addDataPoint(year, dataValue);
//        break;
//      default:
//        break;
//    }
  }
  /*
  public void addData(EnumRegion region,  int year, int[] grossIncome, int[] foodProduced, int[] foodConsumed)
  {
    Graph g;

    switch (region)
    {
      case USA_CALIFORNIA:
        g = californiaGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_MOUNTAIN:
        g= mountainGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_HEARTLAND:
        g = heartlandGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_NORTHERN_PLAINS:
        g = northernPlainsGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_SOUTHERN_PLAINS:
        g = southernPlainsGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_NORTHERN_CRESCENT:
        g = northernCresentGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      case USA_SOUTHEAST:
        g = southeastGraphs[3];
        g.addDataPoint(year, grossIncome, foodProduced, foodConsumed);
        break;
      default:
        break;
    }
  }*/

  public boolean isPieChart(int number)
  {
	  if(number==1||number==2||number==3)
	  {
		  return true;
	  }
	  return false;

  }
  
  public PieChart getPieChart(int displayNumber)
  {
    switch(displayNumber)
    {
    case 1:
      updateRegionalCropDistributionDisplay();
	  return masterPieChart;
    case 2:
      updateRegionalCropExportsDisplay();
    return masterPieChart;
    case 3:
      updateRegionalCropImportsDisplay();
    return masterPieChart;
    default:
    return masterPieChart; //should not reach here
      
    }
  }
  
  public StackedBarChart getBarGraph()
  {
	  return barChart;
  }
  
  public BorderPane getGraph(int graphNum)
  {
	  return gui.getPopupManager().getPBDataDisplay().getProductData().get(graphNum-1).getGraph();
  }
  
  
  
  public void buildDisplay(int displayNum)
  {
//    switch(displayNum)
//	{
//	case 1:
//		displayRegionalCropDistribution();
//		break;
//	case 2:
//		displayRegionalHDIComparison();
//		break;
//	case 3:
//		displayBestCropChart();
//		break;
//	case 4:
//		displayExportsChart();
//		break;
//	case 5:
//		displayImportsChart();
//		break;
//	case 6:
//		displayProductionChart();
//		break;
//	case 7:
//		displayDomesticConsumptionChart();
//		break;
//	default:
//		break;
//	}
  }
  
//  public void updateCurrentRegionName(String regionClicked)
//  {
//		pieChart.setTitle(regionClicked + " Crop Distribution");
//		regionNameLabel.setText(regionClicked);
//		regionNameLabel.setTextFill(Color.web("#FFFFFF")); // white
//  }
  
  
  
  /**
   * Gets the requested graph
   * @param region graph of the region
   * @param number number of the graph you want to get
   * @return
   */
  public Graph getGraphNodeGraph(EnumRegion region, int number)
  {
    Graph g;
    Graph[] regionGraphs=graphMap.get(region);
    return regionGraphs[number];  
//    switch (region)
//    {
//      case USA_CALIFORNIA:
//        g = californiaGraphs[number];
//        break;
//      case USA_MOUNTAIN:
//        g= mountainGraphs[number];
//        break;
//      case USA_HEARTLAND:
//        g = heartlandGraphs[number];
//        break;
//      case USA_NORTHERN_PLAINS:
//        g = northernPlainsGraphs[number];
//        break;
//      case USA_SOUTHERN_PLAINS:
//        g = southernPlainsGraphs[number];
//        break;
//      case USA_NORTHERN_CRESCENT:
//        g = northernCresentGraphs[number];
//        break;
//      case USA_SOUTHEAST:
//        g = southeastGraphs[number];
//        break;
//      default:
//        g = null;
//        break;
//    }
//    return g;
  }

  
  /**
   * Set up the pie chart to display crop distribution for a region. 
   */
  public void updateRegionalCropDistributionDisplay()
  {
     long[] regionsCropDistribution = annualRegionsCropDistributionStatistics.get(REGION_NUM);
     System.out.println(Arrays.toString(regionsCropDistribution));
     regionalCropDistributionPieChart.setVisible(true);
  	 barChart.setVisible(false);
  	 currentYear.setVisible(false);
     masterPieChart.setLegendVisible(false);
  	 masterPieChart.getData().clear();
  	 masterPieChart.setTitle(regionName +" Crop Distribution");
  	 ObservableList<Data> list = FXCollections.observableArrayList(
  		 new PieChart.Data("Citrus Fruits", regionsCropDistribution[0]),
  		 new PieChart.Data("Non-Citrus Fruits", regionsCropDistribution[1]),
  		 new PieChart.Data("Nuts", regionsCropDistribution[2]),
  		 new PieChart.Data("Grains", regionsCropDistribution[3]),
  		 new PieChart.Data("Oil Crops", regionsCropDistribution[4]),
  		 new PieChart.Data("Vegetables", regionsCropDistribution[5]),
  		 new PieChart.Data("Specialty Crops", regionsCropDistribution[6]),
  		 new PieChart.Data("Feed Crops", regionsCropDistribution[7]),
  		 new PieChart.Data("Fish", regionsCropDistribution[8]),
  		 new PieChart.Data("Meat Animals", regionsCropDistribution[9]),
  		 new PieChart.Data("Poultry and Eggs", regionsCropDistribution[10]),
  		 new PieChart.Data("Dairy Products", regionsCropDistribution[11])
  		 );
  	 
  	 masterPieChart.setData(list);
  }
  
  public void updateRegionalCropExportsDisplay()
  {
     long[] regionsCropExports = annualRegionsCropExportStatistics.get(REGION_NUM);
     masterPieChart.setVisible(true);
     barChart.setVisible(false);
     currentYear.setVisible(false);
     masterPieChart.setLegendVisible(false);
     masterPieChart.getData().clear();
     masterPieChart.setTitle(regionName +" Crop Export Numbers");
     ObservableList<Data> list = FXCollections.observableArrayList(
       new PieChart.Data("Citrus Fruits", regionsCropExports[0]),
       new PieChart.Data("Non-Citrus Fruits", regionsCropExports[1]),
       new PieChart.Data("Nuts", regionsCropExports[2]),
       new PieChart.Data("Grains", regionsCropExports[3]),
       new PieChart.Data("Oil Crops", regionsCropExports[4]),
       new PieChart.Data("Vegetables", regionsCropExports[5]),
       new PieChart.Data("Specialty Crops", regionsCropExports[6]),
       new PieChart.Data("Feed Crops", regionsCropExports[7]),
       new PieChart.Data("Fish", regionsCropExports[8]),
       new PieChart.Data("Meat Animals", regionsCropExports[9]),
       new PieChart.Data("Poultry and Eggs", regionsCropExports[10]),
       new PieChart.Data("Dairy Products", regionsCropExports[11])
       );
     
     masterPieChart.setData(list);
  }
  
  
  public void updateRegionalCropImportsDisplay()
  {
    long[] regionsCropImports = annualRegionsCropImportStatistics.get(REGION_NUM);
    masterPieChart.setVisible(true);
     barChart.setVisible(false);
     currentYear.setVisible(false);
     masterPieChart.setLegendVisible(false);
     masterPieChart.getData().clear();
     masterPieChart.setTitle(regionName +" Crop Impoort Numbers");
     ObservableList<Data> list = FXCollections.observableArrayList(
       new PieChart.Data("Citrus Fruits", regionsCropImports[0]),
       new PieChart.Data("Non-Citrus Fruits", regionsCropImports[1]),
       new PieChart.Data("Nuts", regionsCropImports[2]),
       new PieChart.Data("Grains", regionsCropImports[3]),
       new PieChart.Data("Oil Crops", regionsCropImports[4]),
       new PieChart.Data("Vegetables", regionsCropImports[5]),
       new PieChart.Data("Specialty Crops", regionsCropImports[6]),
       new PieChart.Data("Feed Crops", regionsCropImports[7]),
       new PieChart.Data("Fish", regionsCropImports[8]),
       new PieChart.Data("Meat Animals", regionsCropImports[9]),
       new PieChart.Data("Poultry and Eggs", regionsCropImports[10]),
       new PieChart.Data("Dairy Products", regionsCropImports[11])
       );
     
     masterPieChart.setData(list);
  }
  /**
   * Set up pie chart to display a regional comparison of HDI of each
   * reigon. 
   */
//  public void displayRegionalHDIComparison()
//  {
//  	 pieChart.setVisible(true);
//  	 barChart.setVisible(false);
//  	 currentYear.setVisible(false);
//  	 pieChart.getData().clear();
//  	 pieChart.setTitle("Regional Human Development Index Comparison");
//  	 ObservableList<Data> list = FXCollections.observableArrayList(
//  			 new PieChart.Data("California", 7),
//  			 new PieChart.Data("Heartland", 9),
//  			 new PieChart.Data("Northern Plains", 15),
//  			 new PieChart.Data("Southeast", 10),
//  			 new PieChart.Data("Northern Crescent", 11),
//  			 new PieChart.Data("SP & Delta States", 13),
//  			 new PieChart.Data("PNW & MNT States", 35)
//  		 );	
//  	 pieChart.setData(list);
//  	 
//  }


//  /**
//   * Set up a stacked bar chart to display the production
//   * of the 'best' crop from the past 10 years (not in including current one)
//   * the current ones yearly to date information is displayed below the chart.
//   * Note the 'best' crop is hard coded in but again through integration
//   * it will be defined programatically.
//   */
//  public void displayBestCropChart()
//  {
//  	//Toggle what's visible (they are overlaid)
//  	 pieChart.setVisible(false);
//  	 barChart.setVisible(true);
//  	//Animation must be turned off unfortunately due to glitch 
//  	//see: https://bugs.openjdk.java.net/browse/JDK-8093151
//  	 barChart.setAnimated(false); 
//  	 barChart.setLegendVisible(false); //Useless in our case. 
//  	 barChart.getData().clear(); //Clear old data
//  	 barChart.setTitle("Yearly Production of " +bestCropName+" Over Past 10 Years");
//  	 //Set the values for testing (this information will obtained during 
//  	 //game flow once integrated. 
//  	 setTestValues1();
//  	      int index = 0;
//  	      //Create the series (each series is bar in our case) with
//  	      //the correct information. 
//  		  for(int i = (presentYear-11); i <(presentYear-1);i++)
//  		  {
//  			  XYChart.Series<String, Number> series =
//  				        new XYChart.Series<String, Number>();
//  			  series.getData().add(new XYChart.Data<String, Number>((presentYear-10+index)+"",past10YearsBestCropProduction.get(index)));
//  			  series.setName(presentYear-1-index+"");
//  			  barChart.getData().add(series);
//  			  ++index;
//  		  }
//      currentYear.setText("Current Yearly Production of "+ bestCropName+" to date: " + currentYearlyBestCropProduction);	
//      currentYear.setTextFill(Color.web("#FFFFFF"));
//      currentYear.setVisible(true);
//      barChart.setVisible(true);
//  }
//
//  /**
//   * Set up a stacked bar chart to display the regions net exports over
//   * the past 10 years. 
//   */
//  public void displayExportsChart()
//  {
//  	 pieChart.setVisible(false);
//  	 barChart.setVisible(true);
//  	 barChart.setLegendVisible(false);
//  	 barChart.setAnimated(true);
//  	 barChart.getData().clear();
//  	 barChart.setTitle("Net Yearly Regional Exports from the Past 10 Years");
//  	 //Set the values for testing (this information will obtained during 
//  	 //game flow once integrated. 
//  	 setTestValues2();
//  	      int index = 0;
//  	      //Create the series (each series is bar in our case) with
//  	      //the correct information. 
//  		  for(int i = (presentYear-11); i <(presentYear-1);i++)
//  		  {
//  			  XYChart.Series<String, Number> series =
//  				        new XYChart.Series<String, Number>();
//  			  series.getData().add(new XYChart.Data<String, Number>((presentYear-10+index)+"",past10YearsNetExports.get(index)));
//  			  series.setName(presentYear-1-index+"");
//  			  barChart.getData().add(series);
//  			  ++index;
//  		  }
//  	currentYear.setText("Current Yearly Net Exports thus far: " + currentYearlyNetExports );	
//      currentYear.setTextFill(Color.web("#FFFFFF"));
//      currentYear.setVisible(true);
//      barChart.setVisible(true);
//  }
//  /**
//   * Set up a stacked bar chart to display the regions net imports over
//   * the past 10 years. 
//   */
//  public void displayImportsChart()
//  {
//  	 pieChart.setVisible(false);
//  	 barChart.setVisible(true);
//  	 barChart.setLegendVisible(false);
//  	 barChart.setAnimated(true);
//  	 barChart.getData().clear();
//  	 barChart.setTitle("Net Yearly Regional Imports from the Past 10 Years");
//  	 //Set the values for testing (this information will obtained during 
//  	 //game flow once integrated. 
//  	 setTestValues3();
//  	      int index = 0;
//  	      //Create the series (each series is bar in our case) with
//  	      //the correct information. 
//  		  for(int i = (presentYear-11); i <(presentYear-1);i++)
//  		  {
//  			  XYChart.Series<String, Number> series =
//  				        new XYChart.Series<String, Number>();
//  			  series.getData().add(new XYChart.Data<String, Number>((presentYear-10+index)+"",past10YearsNetImports.get(index)));
//  			  series.setName(presentYear-1-index+"");
//  			  barChart.getData().add(series);
//  			  ++index;
//  		  }
//  	currentYear.setText("Current Yearly Net Imports thus far: " + currentYearlyNetImports );	
//  	currentYear.setTextFill(Color.web("#FFFFFF"));
//  	currentYear.setVisible(true);  
//      barChart.setVisible(true);
//  }
//  /**
//   * Set up a stacked bar chart to display the regions net production over
//   * the past 10 years. 
//   */
//  public void displayProductionChart()
//  {
//	  System.out.println("here");
//  	 pieChart.setVisible(false);
//  	 barChart.setVisible(true);
//  	 barChart.setLegendVisible(false);
//  	 barChart.setAnimated(true);
//  	 barChart.getData().clear();
//  	 barChart.setTitle("Net Yearly Regional Production from the Past 10 Years");
//  	 //Set the values for testing (this information will obtained during 
//  	 //game flow once integrated
//  	 setTestValues4();
//  	      int index = 0;
//  	      //Create the series (each series is bar in our case) with
//  	      //the correct information. 
//  		  for(int i = (presentYear-11); i <(presentYear-1);i++)
//  		  {
//  			  XYChart.Series<String, Number> series =
//  				        new XYChart.Series<String, Number>();
//  			  series.getData().add(new XYChart.Data<String, Number>((presentYear-10+index)+"",past10YearsNetProduction.get(index)));
//  			  series.setName(presentYear-1-index+"");
//  			  barChart.getData().add(series);
//  			  ++index;
//  		  }
//  	currentYear.setText("Current Yearly Net Production thus far: " + currentYearlyNetProduction );	
//  	currentYear.setTextFill(Color.web("#FFFFFF"));
//  	currentYear.setVisible(true);  		  
//      barChart.setVisible(true);
//  }
//  /**
//   * Set up a stacked bar chart to display the regions net domestic consumption over
//   * the past 10 years. 
//   */
//  public void displayDomesticConsumptionChart()
//  {
//  	 pieChart.setVisible(false);
//  	 barChart.setVisible(true);
//  	 barChart.setLegendVisible(false);
//  	 barChart.setAnimated(true);
//  	 barChart.getData().clear();
//  	 barChart.setTitle("Net Regional Domestic Consumption from the Past 10 Years");
//  	 //Set the values for testing (this information will obtained during 
//  	 //game flow once integrated
//  	 setTestValues5();
//  	      int index = 0;
//  	      //Create the series (each series is bar in our case) with
//  	      //the correct information. 
//  		  for(int i = (presentYear-11); i <(presentYear-1);i++)
//  		  {
//  			  XYChart.Series<String, Number> series =
//  				        new XYChart.Series<String, Number>();
//  			  series.getData().add(new XYChart.Data<String, Number>((presentYear-10+index)+"",past10YearsNetDomesticConsumption.get(index)));
//  			  series.setName(presentYear-1-index+"");
//  			  barChart.getData().add(series);
//  			  ++index;
//  		  }
//      currentYear.setText("Current Yearly Net Domestic Consumption thus far: " + currentYearlyNetDomesticConsumption );	
//  	currentYear.setTextFill(Color.web("#FFFFFF"));
//  	currentYear.setVisible(true); 		  
//      barChart.setVisible(true);
//  }

//  /**
//   * 
//   * @param Exit the program when exit button is clicked. 
//   */
//   @FXML
//   public void exit(ActionEvent event) {
//       Stage stage = (Stage) exitButton.getScene().getWindow();
//       stage.close();
//   }
   

  /**
   * Set values method not important ignore. 
   */
  public void setTestValues1()
  {
  	past10YearsBestCropProduction.add(1_436_483);
  	past10YearsBestCropProduction.add(2_426_786);
  	past10YearsBestCropProduction.add(2_245_518);
  	past10YearsBestCropProduction.add(3_551_454);
  	past10YearsBestCropProduction.add(6_155_152);
  	past10YearsBestCropProduction.add(4_255_458);
  	past10YearsBestCropProduction.add(5_258_152);
  	past10YearsBestCropProduction.add(7_155_241);
  	past10YearsBestCropProduction.add(8_155_151);
  	past10YearsBestCropProduction.add(9_123_152);

  }
  /**
   * Set values method not important ignore. 
   */
  public void setTestValues2()
  {
  	past10YearsNetExports.add(1_436_483);
  	past10YearsNetExports.add(2_426_786);
  	past10YearsNetExports.add(3_245_568);
  	past10YearsNetExports.add(5_541_424);
  	past10YearsNetExports.add(8_155_172);
  	past10YearsNetExports.add(4_255_158);
  	past10YearsNetExports.add(5_258_152);
  	past10YearsNetExports.add(13_165_741);
  	past10YearsNetExports.add(15_125_151);
  	past10YearsNetExports.add(17_123_152);

  }
  /**
   * Set values method not important ignore. 
   */
  public void setTestValues3()
  {
  	past10YearsNetImports.add(14_436_483);
  	past10YearsNetImports.add(13_426_786);
  	past10YearsNetImports.add(12_245_518);
  	past10YearsNetImports.add(6_551_454);
  	past10YearsNetImports.add(14_155_152);
  	past10YearsNetImports.add(5_255_458);
  	past10YearsNetImports.add(6_258_152);
  	past10YearsNetImports.add(3_155_241);
  	past10YearsNetImports.add(3_155_151);
  	past10YearsNetImports.add(2_123_152);

  }
  /**
   * Set values method not important ignore. 
   */
  public void setTestValues4()
  {
  	past10YearsNetProduction.add(1_436_483);
  	past10YearsNetProduction.add(2_426_786);
  	past10YearsNetProduction.add(2_245_518);
  	past10YearsNetProduction.add(3_551_454);
  	past10YearsNetProduction.add(6_155_152);
  	past10YearsNetProduction.add(4_255_458);
  	past10YearsNetProduction.add(5_258_152);
  	past10YearsNetProduction.add(7_155_241);
  	past10YearsNetProduction.add(8_155_151);
  	past10YearsNetProduction.add(9_123_152);

  }
  /**
   * Set values method not important ignore. 
   */
  public void setTestValues5()
  {

  	past10YearsNetDomesticConsumption.add(2_436_483);
  	past10YearsNetDomesticConsumption.add(3_426_786);
  	past10YearsNetDomesticConsumption.add(4_245_568);
  	past10YearsNetDomesticConsumption.add(6_541_424);
  	past10YearsNetDomesticConsumption.add(9_155_172);
  	past10YearsNetDomesticConsumption.add(5_255_158);
  	past10YearsNetDomesticConsumption.add(6_258_152);
  	past10YearsNetDomesticConsumption.add(14_165_741);
  	past10YearsNetDomesticConsumption.add(16_125_151);
  	past10YearsNetDomesticConsumption.add(18_123_152);

  }
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * Gets the request product bar graph
   * @param i index of the product bar graph you want to get
   * @return
   */
  public Graph getProductBarGraph(int i)
  {
    return productBarGraphs[i];
  }

  private void initializeProductBarGraphs()
  {
    for (int i = 0; i < 12; ++i)
    {
      productBarGraphs[i] = new GraphProductBar(PRODUCTS[i]);
    }
  }

  private void initializeGraphNodeGraphs()
  {
    for(EnumRegion region:EnumRegion.values())
    {
      Graph[] regionGraph=new Graph[3];

      regionGraph[0] = new GraphPopulation(region);
      regionGraph[1] = new GraphHDI(region);
      regionGraph[2] = new GraphFarmingBalance(region);
      graphMap.put(region, regionGraph);
    }
//    initCaliGraph();
//    initHrtGraph();
//    initMntGraph();
//    initNPGraph();
//    initSPGraph();
//    initSEGraph();
//    initNCGraph();
  }

  private void initCaliGraph()
  {
    californiaGraphs = new Graph[3];
    californiaGraphs[0] = new GraphPopulation(EnumRegion.USA_CALIFORNIA);
    californiaGraphs[1] = new GraphHDI(EnumRegion.USA_CALIFORNIA);
    californiaGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_CALIFORNIA);
    //californiaGraphs[3] = new GraphProductsDollars(EnumRegion.USA_CALIFORNIA);
  }

  private void initMntGraph()
  {
    mountainGraphs = new Graph[3];
    mountainGraphs[0] = new GraphPopulation(EnumRegion.USA_MOUNTAIN);
    mountainGraphs[1] = new GraphHDI(EnumRegion.USA_MOUNTAIN);
    mountainGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_MOUNTAIN);
    //mountainGraphs[3] = new GraphProductsDollars(EnumRegion.USA_MOUNTAIN);
  }

  private void initHrtGraph()
  {
    heartlandGraphs = new Graph[3];
    heartlandGraphs[0] = new GraphPopulation(EnumRegion.USA_HEARTLAND);
    heartlandGraphs[1] = new GraphHDI(EnumRegion.USA_HEARTLAND);
    heartlandGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_HEARTLAND);
    //heartlandGraphs[3] = new GraphProductsDollars(EnumRegion.USA_HEARTLAND);
  }

  private void initSEGraph()
  {
    southeastGraphs = new Graph[3];
    southeastGraphs[0] = new GraphPopulation(EnumRegion.USA_SOUTHEAST);
    southeastGraphs[1] = new GraphHDI(EnumRegion.USA_SOUTHEAST);
    southeastGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_SOUTHEAST);
    //southeastGraphs[3] = new GraphProductsDollars(EnumRegion.USA_SOUTHEAST);
  }

  private void initSPGraph()
  {
    southernPlainsGraphs = new Graph[3];
    southernPlainsGraphs[0] = new GraphPopulation(EnumRegion.USA_SOUTHERN_PLAINS);
    southernPlainsGraphs[1] = new GraphHDI(EnumRegion.USA_SOUTHERN_PLAINS);
    southernPlainsGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_SOUTHERN_PLAINS);
    //southernPlainsGraphs[3] = new GraphProductsDollars(EnumRegion.USA_SOUTHERN_PLAINS);
  }

  private void initNCGraph()
  {
    northernCresentGraphs = new Graph[3];
    northernCresentGraphs[0] = new GraphPopulation(EnumRegion.USA_NORTHERN_CRESCENT);
    northernCresentGraphs[1] = new GraphHDI(EnumRegion.USA_NORTHERN_CRESCENT);
    northernCresentGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_NORTHERN_CRESCENT);
    //northernCresentGraphs[3] = new GraphProductsDollars(EnumRegion.USA_NORTHERN_CRESCENT);
  }

  private void initNPGraph()
  {
    northernPlainsGraphs = new Graph[3];
    northernPlainsGraphs[0] = new GraphPopulation(EnumRegion.USA_NORTHERN_PLAINS);
    northernPlainsGraphs[1] = new GraphHDI(EnumRegion.USA_NORTHERN_PLAINS);
    northernPlainsGraphs[2] = new GraphFarmingBalance(EnumRegion.USA_NORTHERN_PLAINS);
    //northernPlainsGraphs[3] = new GraphProductsDollars(EnumRegion.USA_NORTHERN_PLAINS);
  }
}
