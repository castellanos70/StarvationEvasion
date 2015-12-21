package starvationevasion.client.MegaMawile;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;


/**
 * Class is used to create charts from statistics that have been stored.
 * Methods in the class only need the specification and in turn a chart is
 * returned to the gui.
 */
public class ChartGraphics
{
  public XYChart.Series getRegionChartData(EnumRegion region, StatisticReadData.STAT_TYPE stat_type)
  {
    switch(region){

      case CALIFORNIA:
        return getRegionChartData(StatisticReadData.USREGION.CALIFORNIA, stat_type);
      case HEARTLAND:
        return getRegionChartData(StatisticReadData.USREGION.HEARTLAND, stat_type);
      case NORTHERN_PLAINS:
        return getRegionChartData(StatisticReadData.USREGION.N_PLAINS, stat_type);
      case SOUTHEAST:
        return getRegionChartData(StatisticReadData.USREGION.SE, stat_type);
      case NORTHERN_CRESCENT:
        return getRegionChartData(StatisticReadData.USREGION.N_CRESCENT, stat_type);
      case SOUTHERN_PLAINS:
        return getRegionChartData(StatisticReadData.USREGION.S_PLAINS_DS, stat_type);
      case MOUNTAIN:
        return getRegionChartData(StatisticReadData.USREGION.PACIFIC_NW_MS , stat_type);
      case ARCTIC_AMERICA:
        return getRegionChartData(StatisticReadData.WORLDREGION.ARTIC_AMERICA, stat_type);
      case MIDDLE_AMERICA:
        return getRegionChartData(StatisticReadData.WORLDREGION.MIDDLE_AMERICA, stat_type);
      case SOUTH_AMERICA:
        return getRegionChartData(StatisticReadData.WORLDREGION.SOUTH_AMERICA, stat_type);
      case EUROPE:
        return getRegionChartData(StatisticReadData.WORLDREGION.EUROPE, stat_type);
      case MIDDLE_EAST:
        return getRegionChartData(StatisticReadData.WORLDREGION.MIDDLE_EAST, stat_type);
      case SUB_SAHARAN:
        return getRegionChartData(StatisticReadData.WORLDREGION.SUB_SAHARAN_AFRICA, stat_type);
      case RUSSIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.RUSSIA_CAUCUSES, stat_type);
      case CENTRAL_ASIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.CENTRAL_ASIA, stat_type);
      case SOUTH_ASIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.SOUTH_ASIA, stat_type);
      case EAST_ASIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.EAST_ASIA, stat_type);
      case SOUTHEAST_ASIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.SOUTHEAST_ASIA, stat_type);
      case OCEANIA:
        return getRegionChartData(StatisticReadData.WORLDREGION.OCEANIA, stat_type);
    }
    return null;
  }

  private enum Y_AXIS{MONEY, PERCENT, NUMBER}
  public static ChartGraphics cg = null;

  public ChartGraphics ()
  {
    cg = this;
  }

  private XYChart.Series getAnnualChartData(String s, Y_AXIS type,
           ArrayList<Number> dataList)
  {
    XYChart.Series series = new XYChart.Series();
    series.setName(s);
    int year = 1980;
    for(int i = 0; i<dataList.size();i++)
    {
      XYChart.Data data = new XYChart.Data(""+year, dataList.get(i));
      data.setNode(new HoveredNode(dataList.get(i), type ));
      series.getData().add(data);
      year +=3;
    }
    return series;
  }

  /**
   * Class is used for when the user hovers over a node a value is displayed,
   * if they are not hovering then no value is displayed.
   * @author Mark Mitchell, Chris Wu, Evan King, Kiera Haskins, Javier Chavez
   */
  private class HoveredNode extends StackPane
  {
    HoveredNode(Number value, Y_AXIS type)
    {
      setPrefSize(10, 10);

      final Label label = createDataLabel(value, type);

      setOnMouseEntered(new EventHandler<MouseEvent>()
      {
        /**
         * When the mouse is over a node, the label comes to the front and the
         * cursor is invisible.
         * @param mouseEvent Mouse hovers over the node.
         */
        @Override
        public void handle(MouseEvent mouseEvent)
        {
          getChildren().setAll(label);
          setCursor(Cursor.NONE);
          toFront();
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent mouseEvent)
        {
          getChildren().clear();
          setCursor(Cursor.CROSSHAIR);
        }
      });
    }

    private Label createDataLabel(Number value, Y_AXIS type)
    {
      final Label label;
      if(type == Y_AXIS.MONEY)
      {
        label=new Label("$" + value + "M");
      }
      else
      {
        label = new Label(value+"");
      }
      label.getStyleClass().addAll("default-color2", "chart-line-symbol", "chart-series-line");
      label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
      label.setTextFill(Color.FORESTGREEN);

      label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
      return label;
    }
  }

  /**
   * Method produces the top regions for production/consumption for that
   * particular crop.
   * @param year Integer for the current year.
   * @param product The crop/farm product being examined.
   * @param statType Enum for whether the crops production or
   *                     consumption is being examined.
   * @return BarChart of the top five regions for production or consumption.
   */
  public XYChart.Series getTopFiveUS(
          int year, StatisticReadData.FARMPRODUCT product, StatisticReadData.STAT_TYPE statType)
  {
    XYChart.Series series = new XYChart.Series();
    ArrayList<StatisticReadData.USREGION> usregions =
            StatisticReadData.srd.getTopUSCropData(year, product, statType);
    int metricTons=0;
    for(StatisticReadData.USREGION region: usregions)
    {
      if(statType== StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION)
      {
        metricTons = StatisticReadData.srd.getRegionData(region).
                getAnnualData(year).getCropData(product).getProductionTons();
      }
      else
      {
        metricTons = StatisticReadData.srd.getRegionData(region).
                getAnnualData(year).getCropData(product).getConsumptionTons();
      }
      series.getData().add(new XYChart.Data(regionTypeToString(region),metricTons));
    }
    return series;
  }

  /**
   * Method produces the top regions for production/consumption for that
   * particular crop.
   * @param year Integer for the current year.
   * @param product The crop/farm product being examined.
   * @param statType Enum for whether the crops production or
   *                     consumption is being examined.
   * @return BarChart of the top five regions for production or consumption.
   */
  public XYChart.Series getTopFiveWorld(
          int year, StatisticReadData.FARMPRODUCT product, StatisticReadData.STAT_TYPE statType)
  {
    XYChart.Series series = new XYChart.Series();
    ArrayList<StatisticReadData.WORLDREGION> worldregions =
            StatisticReadData.srd.getTopWorldCropData(year, product, statType);
    int metricTons=0;
    for(StatisticReadData.WORLDREGION region: worldregions)
    {
      if(statType== StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION)
      {
        metricTons = StatisticReadData.srd.getRegionData(region).
                getAnnualData(year).getCropData(product).getProductionTons();
      }
      else
      {
        metricTons = StatisticReadData.srd.getRegionData(region).
                getAnnualData(year).getCropData(product).getConsumptionTons();
      }
      series.getData().add(new XYChart.Data(regionTypeToString(region), metricTons));
    }
    return series;
  }

  /**
   * Method used to build a LineChart for the world population starting at 1980
   * till the current turn.
   * @return LineChart with annual global population statistics
   */
  public XYChart.Series getGlobalPopulationChart()
  {
    return getAnnualChartData("Population over the years", Y_AXIS.NUMBER,
            StatisticReadData.srd.getGlobalPopulationList());
  }

  /**
   * Method used to build a LineChart for various crop prices starting at 1980
   * till the current year.
   * @return LineChart with annual crop prices for a particular product
   */
  public XYChart.Series getCropPriceChart(StatisticReadData.FARMPRODUCT product)
  {
    return getAnnualChartData("Prices over the years", Y_AXIS.NUMBER,
            StatisticReadData.srd.getCropPriceList(product));
  }

  /**
   * Method is used to make a line chart for a specific stat for a certain
   * region, because of the variables involved this method requires
   * the regionand the statistic type.  This is graphed over all past years.
   * @param region USREGION enum that specifies the region in question.
   * @param type STAT_TYPE enum that specifies the statistic being charted.
   * @return LineChart of the annual data for this particular statistic
   */
  public XYChart.Series getRegionChartData(StatisticReadData.USREGION region, StatisticReadData.STAT_TYPE type)
  {
    String regionName = regionTypeToString(region);
    if(StatisticReadData.srd.getRegionData(region)!=null)
    {
      StatisticReadData.RegionData dataSheet = StatisticReadData.srd.getRegionData(region);
      return helperChartData(dataSheet, type);
    }
    else
    {
      return null;
    }
  }

  /**
   * Method is used to make a line chart for a specific stat for a certain
   * region, because of the variables involved this method requires
   * the regionand the statistic type.  This is graphed over all past years.
   * @param region WORLDREGION enum that specifies the region in question.
   * @param type STAT_TYPE enum that specifies the statistic being charted.
   * @return LineChart of the annual data for this particular statistic
   */

  public XYChart.Series getRegionChartData(
          StatisticReadData.WORLDREGION region, StatisticReadData.STAT_TYPE type)
  {
    String regionName = regionTypeToString(region);
    if(StatisticReadData.srd.getRegionData(region)!=null)
    {
      StatisticReadData.RegionData dataSheet = StatisticReadData.srd.getRegionData(region);
      return helperChartData(dataSheet, type);
    }
    else
    {
      return null;
    }
  }


  private XYChart.Series helperChartData(StatisticReadData.RegionData dataSheet,
          StatisticReadData.STAT_TYPE type)
  {
    ArrayList<Number> dataList;
    switch(type)
    {
      case HDI:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.HDI);
        return getAnnualChartData("HDI Over the Years", Y_AXIS.NUMBER, dataList);
      case POPULATION:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.POPULATION);
        return getAnnualChartData("Regional Population Size", Y_AXIS.NUMBER,
                dataList);
      case REVENUE:
        dataList = dataSheet.getStatList(StatisticReadData.STAT_TYPE.REVENUE);
        return getAnnualChartData("Revenue over the Years", Y_AXIS.MONEY,
                dataList);
      case AVG_AGE:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.AVG_AGE);
        return getAnnualChartData("Average Age of the Population",
                Y_AXIS.NUMBER, dataList);
      case MALNOURISHED:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.MALNOURISHED);
        return getAnnualChartData("Malnourished per Thousand People",
                Y_AXIS.NUMBER, dataList);
      case BIRTH_RATE:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.BIRTH_RATE);
        return getAnnualChartData("Births per Thousand People", Y_AXIS.NUMBER,
                dataList);
      case NET_MIGRATION:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.NET_MIGRATION);
        return getAnnualChartData("Immigration - Emigration", Y_AXIS.NUMBER,
                dataList);
      case MORTALITY_RATE:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.MORTALITY_RATE);
        return getAnnualChartData("Deaths per Thousand People", Y_AXIS.NUMBER,
                dataList);
      case LIFE_EXPECTANCY:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.LIFE_EXPECTANCY);
        return getAnnualChartData("Life Expectancy in Years", Y_AXIS.NUMBER,
                dataList);
      case PER_GMO:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.PER_GMO);
        return getAnnualChartData("Percent of GMO Farming", Y_AXIS.PERCENT,
                dataList);
      case PER_ORG:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.PER_ORG);
        return getAnnualChartData("Percent of Organic Farming", Y_AXIS.PERCENT,
                dataList);
      case PER_CON:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.PER_CON);
        return getAnnualChartData("Percent of Conventional Farming",
                Y_AXIS.PERCENT, dataList);
      default:
        System.out.println("Stat type requires crop selection.");
        return null;
    }
  }

  /**
   * Method is used to make a line chart for a specific stat for a crop within
   * a certain region, because of the variables involved this method requires
   * the region, the farm product and the statistic type.  This is graphed over
   * all past years.
   * @param region USREGION enum that specifies the region in question.
   * @param type STAT_TYPE enum that specifies the statistic being charted.
   * @param product FARMPRODUCT enum is used for the product being charted.
   * @return LineChart of the annual data for this particular statistic
   */

  public XYChart.Series getRegionCropChart(
          StatisticReadData.USREGION region, StatisticReadData.STAT_TYPE type,
          StatisticReadData.FARMPRODUCT product)
  {
    String regionName = regionTypeToString(region);
    if(StatisticReadData.srd.getRegionData(region)!=null)
    {
      StatisticReadData.RegionData dataSheet = StatisticReadData.srd.getRegionData(region);
      return helperCropChartData( dataSheet, type, product);
    }
    else
    {
      return null;
    }
  }

  /**
   * Method is used to make a line chart for a specific stat for a crop within
   * a certain region, because of the variables involved this method requires
   * the region, the farm product and the statistic type.  This is graphed over
   * all past years.
   * @param region WORLDREGION enum that specifies the region in question.
   * @param type STAT_TYPE enum that specifies the statistic being charted.
   * @param product FARMPRODUCT enum is used for the product being charted.
   * @return LineChart of the annual data for this particular statistic
   */

  public XYChart.Series getRegionCropChart(
          StatisticReadData.WORLDREGION region, StatisticReadData.STAT_TYPE type,
          StatisticReadData.FARMPRODUCT product)
  {
    String regionName = regionTypeToString(region);
    if(StatisticReadData.srd.getRegionData(region)!=null)
    {
      StatisticReadData.RegionData dataSheet = StatisticReadData.srd.getRegionData(region);
      return helperCropChartData(dataSheet, type, product);
    }
    else
    {
      return null;
    }
  }


  private XYChart.Series helperCropChartData(
          StatisticReadData.RegionData dataSheet, StatisticReadData.STAT_TYPE type,
          StatisticReadData.FARMPRODUCT product)
  {
    ArrayList<Number> dataList;
    String title;
    switch (type)
    {
      case NET_FARM_INCOME:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.NET_FARM_INCOME, product);
        return getAnnualChartData("Total Farm Income", Y_AXIS.MONEY, dataList);
      case TOTAL_COST:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_COST, product);
        return getAnnualChartData("Total Cost", Y_AXIS.MONEY, dataList);
      case TOTAL_KM:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_KM, product);
        return getAnnualChartData("Square Kilometers of Farming", Y_AXIS.NUMBER, dataList);
      case TOTAL_PRODUCTION:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION, product);
        return getAnnualChartData("Production in Tons", Y_AXIS.NUMBER, dataList);
      case TOTAL_CONSUMPTION:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION, product);
        return getAnnualChartData("Consumption in Tons", Y_AXIS.NUMBER, dataList);
      case TOTAL_IMPORT:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_IMPORT, product);
        return getAnnualChartData("Imports in Tons", Y_AXIS.NUMBER, dataList);
      case TOTAL_EXPORT:
        dataList= dataSheet.getStatList(StatisticReadData.STAT_TYPE.TOTAL_EXPORT, product);
        return getAnnualChartData("Exports in Tons", Y_AXIS.NUMBER, dataList);
      default:
        System.out.println("Stat type not supported with crop selection.");
        return null;
    }
  }

  public String regionTypeToString(StatisticReadData.USREGION region)
  {
    switch (region)
    {
      case CALIFORNIA:
        return "California";
      case HEARTLAND:
        return "Heartland";
      case N_PLAINS:
        return "Northern Plains";
      case SE:
        return "Southeast";
      case N_CRESCENT:
        return "Northern Crescent";
      case S_PLAINS_DS:
        return "Southern Plains \n and Delta States";
      case PACIFIC_NW_MS:
        return "Pacific NW \n and Mountain States";
      default:
        return "";
    }
  }

  public StatisticReadData.USREGION stringToUsRegion(String s)
  {
    switch (s)
    {
      case "California":
        return StatisticReadData.USREGION.CALIFORNIA;
      case "Heartland":
        return StatisticReadData.USREGION.HEARTLAND;
      case "Northern Plains":
        return StatisticReadData.USREGION.N_PLAINS;
      case "Southeast":
        return StatisticReadData.USREGION.SE;
      case "Northern Crescent":
        return StatisticReadData.USREGION.N_CRESCENT;
      case "Southern Plains \n and Delta States":
        return StatisticReadData.USREGION.S_PLAINS_DS;
      case "Pacific NW \n and Mountain States":
        return StatisticReadData.USREGION.PACIFIC_NW_MS;
      default:
        return null;
    }
  }

  public String regionTypeToString(StatisticReadData.WORLDREGION region)
  {
    switch (region)
    {
      case ARTIC_AMERICA:
        return "Artic America";
      case MIDDLE_AMERICA:
        return "Middle America";
      case SOUTH_AMERICA:
        return "South America";
      case EUROPE:
        return "Europe";
      case MIDDLE_EAST:
        return "Middle East";
      case SUB_SAHARAN_AFRICA:
        return "Sub Saharan Africa";
      case RUSSIA_CAUCUSES:
        return "Russia and \n the Caucuses";
      case CENTRAL_ASIA:
        return "Central Asia";
      case SOUTH_ASIA:
        return "South Asia";
      case EAST_ASIA:
        return "East Asia";
      case SOUTHEAST_ASIA:
        return "Southeast Asia";
      case OCEANIA:
        return "Oceania";
      default:
        return "";
    }
  }

  public StatisticReadData.WORLDREGION stringToWorldRegion(String s)
  {
    switch (s)
    {
      case "Artic America":
        return StatisticReadData.WORLDREGION.ARTIC_AMERICA;
      case "Middle America":
        return StatisticReadData.WORLDREGION.MIDDLE_AMERICA;
      case "South America":
        return StatisticReadData.WORLDREGION.SOUTH_AMERICA;
      case "Europe":
        return StatisticReadData.WORLDREGION.EUROPE;
      case "Middle East":
        return StatisticReadData.WORLDREGION.MIDDLE_EAST;
      case "Sub Saharan Africa":
        return StatisticReadData.WORLDREGION.SUB_SAHARAN_AFRICA;
      case "Russia and \n the Caucuses":
        return StatisticReadData.WORLDREGION.RUSSIA_CAUCUSES;
      case "Central Asia":
        return StatisticReadData.WORLDREGION.CENTRAL_ASIA;
      case "South Asia":
        return StatisticReadData.WORLDREGION.SOUTH_ASIA;
      case "East Asia":
        return StatisticReadData.WORLDREGION.EAST_ASIA;
      case "Southeast Asia":
        return StatisticReadData.WORLDREGION.SOUTHEAST_ASIA;
      case "Oceania":
        return StatisticReadData.WORLDREGION.OCEANIA;
      default:
        return null;
    }
  }

  private String cropTypeToString(StatisticReadData.FARMPRODUCT crop)
  {
    switch(crop){
      case CITRUS:
        return "Citrus Fruit";
      case NON_CITRUS:
        return "Non-Citrus Fruit";
      case NUTS:
        return "Nuts";
      case GRAINS:
        return "Grains";
      case OIL_CROPS:
        return "Oil Crops";
      case VEGETABLES:
        return "Vegetables";
      case SPECIALTY_CROPS:
        return "Specialty Crops";
      case FEED_CROPS:
        return "Feed Crops";
      case FISH:
        return "Fish";
      case MEAT_ANIMALS:
        return "Meat Animals";
      case POULTRY_EGGS:
        return "Poultry & Eggs";
      case DAIRY:
        return "Dairy";
      default:
        return "";
    }
  }

}

