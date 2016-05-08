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
import starvationevasion.common.EnumFood;
/**
 * Graph Manager which handles all of the graphs attached to the GUI Has
 * functions which allows the client to add data to a graph or methods for the
 * graph node in the GUI to get the linechart which needs to be displayed
 */
public class GraphManager
{
  private Label regionNameLabel = new Label();
  private Label currentYear = new Label();
  CategoryAxis xAxis = new CategoryAxis();
  NumberAxis yAxis = new NumberAxis();

  ArrayList<long[]> annualRegionsCropDistributionStatistics = new ArrayList<>();
  ArrayList<long[]> annualRegionsCropExportStatistics = new ArrayList<>();
  ArrayList<long[]> annualRegionsCropImportStatistics = new ArrayList<>();

  
  ArrayList<Long> annualCitrusRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualFruitRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualNutRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualGrainRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualOilRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualVeggiesRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualSpecialRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualFeedRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualFishRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualMeatRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualPoultryRegionalProductionComparison =  new ArrayList<>();
  ArrayList<Long> annualDairyRegionalProductionComparison =  new ArrayList<>();
  
  private int[] populations = new int[18];
  
  
  ArrayList<ArrayList<Long>> listOfRegionalCropComparisonLists = new ArrayList<>();
  
  PieChart masterPieChart = new PieChart();

  private int REGION_NUM = 0;

  // Stacked Bar Chart looks better visually than a
  // regular bar chart it has wider bars.
  StackedBarChart<String, Number> barChart = new StackedBarChart<String, Number>(xAxis, yAxis);

  // NOTE: These values below are all just for testing and
  // will be obtained via the game flow once integrated.
  private String regionName = "";
  GUI gui;
  ArrayList<Graph[]> graphs = new ArrayList<>();
  HashMap<EnumRegion, Graph[]> graphMap = new HashMap<>();
  Graph[] californiaGraphs;
  Graph[] mountainGraphs;
  Graph[] heartlandGraphs;
  Graph[] northernCresentGraphs;
  Graph[] southeastGraphs;
  Graph[] northernPlainsGraphs;
  Graph[] southernPlainsGraphs;

  Graph[] productBarGraphs = new Graph[12];

  final EnumFood[] PRODUCTS =
  { EnumFood.CITRUS, EnumFood.FRUIT, EnumFood.NUT, EnumFood.GRAIN, EnumFood.OIL, EnumFood.VEGGIES, EnumFood.SPECIAL,
      EnumFood.FEED, EnumFood.FISH, EnumFood.MEAT, EnumFood.POULTRY, EnumFood.DAIRY };

  /**
   * Constructor for the GraphManger
   * 
   * @param gui
   */
  public GraphManager(GUI gui)
  {
    this.gui = gui;
    barChart.getStylesheets()
        .add(getClass().getResource("/starvationevasion/client/GUI/Graphs/barChartCSS.css").toExternalForm());
    barChart.applyCss();

    masterPieChart.getStylesheets()
        .add(getClass().getResource("/starvationevasion/client/GUI/Graphs/pieChartCSS.css").toExternalForm());
    // Set initial region name (needs to be obtained from game once integrated)
    initializeProductBarGraphs();
    initializeGraphNodeGraphs();


  }

  /**
   *  Update Regions Population
   * @param pop
   * @param index
   */
  public void updateRegionsPopulation(int pop, int index)
  {
    populations[index] = pop;
  }
  /**
   * Get Regions Population
   * @param region
   * @return
   */
  public int getPopulation(int region)
  {
    return populations[region];
  }
  
  /**
   * Update a regions crop production distribution numbers. 
   * @param year
   * @param updatedStats
   */
  public void updateRegionalCropDistributionNumbers(int year, ArrayList<long[]> updatedStats)
  {
    annualRegionsCropDistributionStatistics = new ArrayList<>();
    for (long[] region : updatedStats)
    {

      annualRegionsCropDistributionStatistics.add(region);
    }  
    updateRegionalCropComparison();
  }
  /**
   * Update a regions total crop export numbers. 
   * @param year
   * @param updatedStats
   */
  public void updateRegionalCropExportNumbers(int year,  ArrayList<long[]> updatedStats)
  {
    annualRegionsCropExportStatistics= new ArrayList<>();
    for(long[] region: updatedStats)
    {
      annualRegionsCropExportStatistics.add(region);
    }
  }
  /**
   * Update a regions total crop import numbers. 
   * @param year
   * @param updatedStats
   */
  public void updateRegionalCropImportNumbers(int year,  ArrayList<long[]> updatedStats)
  {
    annualRegionsCropImportStatistics= new ArrayList<>();
    for(long[] region: updatedStats)
    {
      annualRegionsCropImportStatistics.add(region);
    }
  }
  
  /**
   * Update Global Crop production comparison numbers. 
   */
  public void updateRegionalCropComparison()
  {
      for(long[] regionCropDist: annualRegionsCropDistributionStatistics)
      {
      annualCitrusRegionalProductionComparison.add(regionCropDist[0]);
      annualFruitRegionalProductionComparison.add(regionCropDist[1]);
      annualNutRegionalProductionComparison.add(regionCropDist[2]);
      annualGrainRegionalProductionComparison.add(regionCropDist[3]);
      annualOilRegionalProductionComparison.add(regionCropDist[4]);
      annualVeggiesRegionalProductionComparison.add(regionCropDist[5]);
      annualSpecialRegionalProductionComparison.add(regionCropDist[6]);
      annualFeedRegionalProductionComparison.add(regionCropDist[7]);
      annualFishRegionalProductionComparison.add(regionCropDist[8]);
      annualMeatRegionalProductionComparison.add(regionCropDist[9]);
      annualPoultryRegionalProductionComparison.add(regionCropDist[10]);
      annualDairyRegionalProductionComparison.add(regionCropDist[11]);
     // }
    }
      listOfRegionalCropComparisonLists.add(annualCitrusRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualFruitRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualNutRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualGrainRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualOilRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualVeggiesRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualSpecialRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualFeedRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualFishRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualMeatRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualPoultryRegionalProductionComparison);
      listOfRegionalCropComparisonLists.add(annualDairyRegionalProductionComparison);
      
  }
  
  
  public void setRegionNum(int num)
  {
    this.REGION_NUM=num;
  }
  
  /**
   * Method which adds a data point to the graphs in the food product bar
   * 
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
   * public void addData(EnumRegion region, int year, int[] grossIncome, int[]
   * foodProduced, int[] foodConsumed) { Graph g;
   * 
   * switch (region) { case USA_CALIFORNIA: g = californiaGraphs[3];
   * g.addDataPoint(year, grossIncome, foodProduced, foodConsumed); break; case
   * USA_MOUNTAIN: g= mountainGraphs[3]; g.addDataPoint(year, grossIncome,
   * foodProduced, foodConsumed); break; case USA_HEARTLAND: g =
   * heartlandGraphs[3]; g.addDataPoint(year, grossIncome, foodProduced,
   * foodConsumed); break; case USA_NORTHERN_PLAINS: g =
   * northernPlainsGraphs[3]; g.addDataPoint(year, grossIncome, foodProduced,
   * foodConsumed); break; case USA_SOUTHERN_PLAINS: g =
   * southernPlainsGraphs[3]; g.addDataPoint(year, grossIncome, foodProduced,
   * foodConsumed); break; case USA_NORTHERN_CRESCENT: g =
   * northernCresentGraphs[3]; g.addDataPoint(year, grossIncome, foodProduced,
   * foodConsumed); break; case USA_SOUTHEAST: g = southeastGraphs[3];
   * g.addDataPoint(year, grossIncome, foodProduced, foodConsumed); break;
   * default: break; } }
   */

  public boolean isPieChart(int number)
  {
    if (number == 1 || number == 2 || number == 3)
    {
      return true;
    }
    return false;

  }

  public PieChart getPieChart(int displayNumber)
  {
    switch (displayNumber)
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
      return masterPieChart; // should not reach here

    }
  }

  public StackedBarChart getBarGraph()
  {
    return barChart;
  }

  
  public void setRegionName(String regionName)
  {
    this.regionName = regionName;
  }
  
  

  /**
   * Gets the requested graph
   * 
   * @param region
   *          graph of the region
   * @param number
   *          number of the graph you want to get
   * @return
   */
  public Graph getGraphNodeGraph(EnumRegion region, int number)
  {
    Graph g;
    Graph[] regionGraphs = graphMap.get(region);
    return regionGraphs[number];
    // switch (region)
    // {
    // case USA_CALIFORNIA:
    // g = californiaGraphs[number];
    // break;
    // case USA_MOUNTAIN:
    // g= mountainGraphs[number];
    // break;
    // case USA_HEARTLAND:
    // g = heartlandGraphs[number];
    // break;
    // case USA_NORTHERN_PLAINS:
    // g = northernPlainsGraphs[number];
    // break;
    // case USA_SOUTHERN_PLAINS:
    // g = southernPlainsGraphs[number];
    // break;
    // case USA_NORTHERN_CRESCENT:
    // g = northernCresentGraphs[number];
    // break;
    // case USA_SOUTHEAST:
    // g = southeastGraphs[number];
    // break;
    // default:
    // g = null;
    // break;
    // }
    // return g;
  }

  /**
   * Update a crops global production comparison pie chart. 
   * @param crop
   * @return
   */
  public PieChart setCropProductionPieChart(int crop)
  {
    masterPieChart.setVisible(true);
    barChart.setVisible(false);
    currentYear.setVisible(false);
    masterPieChart.setLegendVisible(false);
    masterPieChart.getData().clear();
    masterPieChart.setTitle("Regional Production Comparison of " +PRODUCTS[crop-1].toString());
    ObservableList<Data> list = FXCollections.observableArrayList(
        new PieChart.Data("California", listOfRegionalCropComparisonLists.get(crop).get(0)),
        new PieChart.Data("HeartLands", listOfRegionalCropComparisonLists.get(crop).get(1)),
        new PieChart.Data("Northern Plains", listOfRegionalCropComparisonLists.get(crop).get(2)), 
        new PieChart.Data("SouthEast", listOfRegionalCropComparisonLists.get(crop).get(3)),
        new PieChart.Data("Northern Crescent", listOfRegionalCropComparisonLists.get(crop).get(4)),
        new PieChart.Data("Southern Plains", listOfRegionalCropComparisonLists.get(crop).get(5)),
        new PieChart.Data("Mountian", listOfRegionalCropComparisonLists.get(crop).get(6)),
        new PieChart.Data("Arctic America", listOfRegionalCropComparisonLists.get(crop).get(7)),
        new PieChart.Data("Middle America", listOfRegionalCropComparisonLists.get(crop).get(8)),
        new PieChart.Data("South America", listOfRegionalCropComparisonLists.get(crop).get(9)),
        new PieChart.Data("Europe", listOfRegionalCropComparisonLists.get(crop).get(10)),
        new PieChart.Data("Middle East", listOfRegionalCropComparisonLists.get(crop).get(11)),
        new PieChart.Data("Sub Saharan", listOfRegionalCropComparisonLists.get(crop).get(12)),
        new PieChart.Data("Russia", listOfRegionalCropComparisonLists.get(crop).get(13)),
        new PieChart.Data("Central Asia", listOfRegionalCropComparisonLists.get(crop).get(14)),
        new PieChart.Data("South Asia", listOfRegionalCropComparisonLists.get(crop).get(14)),
        new PieChart.Data("East Asia", listOfRegionalCropComparisonLists.get(crop).get(15)),
        new PieChart.Data("Oceania", listOfRegionalCropComparisonLists.get(crop).get(16)));

    masterPieChart.setData(list);
    
    return masterPieChart;
  }
  
  
  
  
  /**
   * Set up the pie chart to display crop distribution for a region.
   */
  public void updateRegionalCropDistributionDisplay()
  {
    long[] regionsCropDistribution = annualRegionsCropDistributionStatistics.get(REGION_NUM);
    masterPieChart.setVisible(true);
    barChart.setVisible(false);
    currentYear.setVisible(false);
    masterPieChart.setLegendVisible(false);
    masterPieChart.getData().clear();
    masterPieChart.setTitle(regionName + " Crop Production");
    ObservableList<Data> list = FXCollections.observableArrayList(
        new PieChart.Data("Citrus Fruits", regionsCropDistribution[0]),
        new PieChart.Data("Non-Citrus Fruits", regionsCropDistribution[1]),
        new PieChart.Data("Nuts", regionsCropDistribution[2]), new PieChart.Data("Grains", regionsCropDistribution[3]),
        new PieChart.Data("Oil Crops", regionsCropDistribution[4]),
        new PieChart.Data("Vegetables", regionsCropDistribution[5]),
        new PieChart.Data("Specialty Crops", regionsCropDistribution[6]),
        new PieChart.Data("Feed Crops", regionsCropDistribution[7]),
        new PieChart.Data("Fish", regionsCropDistribution[8]),
        new PieChart.Data("Meat Animals", regionsCropDistribution[9]),
        new PieChart.Data("Poultry and Eggs", regionsCropDistribution[10]),
        new PieChart.Data("Dairy Products", regionsCropDistribution[11]));

    masterPieChart.setData(list);
  }
  /**
   * Set up the pie chart to display crop exports for a region.
   */
  public void updateRegionalCropExportsDisplay()
  {
    long[] regionsCropExports = annualRegionsCropExportStatistics.get(REGION_NUM);
    masterPieChart.setVisible(true);
    barChart.setVisible(false);
    currentYear.setVisible(false);
    masterPieChart.setLegendVisible(false);
    masterPieChart.getData().clear();
    masterPieChart.setTitle(regionName + " Crop Export Numbers");
    ObservableList<Data> list = FXCollections.observableArrayList(
        new PieChart.Data("Citrus Fruits", regionsCropExports[0]),
        new PieChart.Data("Non-Citrus Fruits", regionsCropExports[1]), new PieChart.Data("Nuts", regionsCropExports[2]),
        new PieChart.Data("Grains", regionsCropExports[3]), new PieChart.Data("Oil Crops", regionsCropExports[4]),
        new PieChart.Data("Vegetables", regionsCropExports[5]),
        new PieChart.Data("Specialty Crops", regionsCropExports[6]),
        new PieChart.Data("Feed Crops", regionsCropExports[7]), new PieChart.Data("Fish", regionsCropExports[8]),
        new PieChart.Data("Meat Animals", regionsCropExports[9]),
        new PieChart.Data("Poultry and Eggs", regionsCropExports[10]),
        new PieChart.Data("Dairy Products", regionsCropExports[11]));

    masterPieChart.setData(list);
  }
  /**
   * Set up the pie chart to display crop imports for a region.
   */
  public void updateRegionalCropImportsDisplay()
  {
	
    long[] regionsCropImports = annualRegionsCropImportStatistics.get(REGION_NUM);
    masterPieChart.setVisible(true);
    barChart.setVisible(false);
    currentYear.setVisible(false);
    masterPieChart.setLegendVisible(false);
    masterPieChart.getData().clear();
    masterPieChart.setTitle(regionName + " Crop Import Numbers");
    ObservableList<Data> list = FXCollections.observableArrayList(
        new PieChart.Data("Citrus Fruits", regionsCropImports[0]),
        new PieChart.Data("Non-Citrus Fruits", regionsCropImports[1]), new PieChart.Data("Nuts", regionsCropImports[2]),
        new PieChart.Data("Grains", regionsCropImports[3]), new PieChart.Data("Oil Crops", regionsCropImports[4]),
        new PieChart.Data("Vegetables", regionsCropImports[5]),
        new PieChart.Data("Specialty Crops", regionsCropImports[6]),
        new PieChart.Data("Feed Crops", regionsCropImports[7]), new PieChart.Data("Fish", regionsCropImports[8]),
        new PieChart.Data("Meat Animals", regionsCropImports[9]),
        new PieChart.Data("Poultry and Eggs", regionsCropImports[10]),
        new PieChart.Data("Dairy Products", regionsCropImports[11]));

    masterPieChart.setData(list);
  }
 



  /**
   * Gets the request product bar graph
   * 
   * @param i
   *          index of the product bar graph you want to get
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
  public BorderPane getGraph(int graphNum)
  {
    return gui.getPopupManager().getPBDataDisplay().getProductData().get(graphNum-1).getGraph();
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
