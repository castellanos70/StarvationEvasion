package starvationevasion.client.MegaMawile2.view.popups;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import starvationevasion.client.MegaMawile2.ChartGraphics;
import starvationevasion.client.MegaMawile2.StatisticReadData;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Mark on 12/6/2015.
 */
public class PopUpController implements Initializable {

  @FXML
  BarChart<String, Number> t5CUs,t5CWo,t5PUs,t5PWo;
  @FXML
  LineChart<String, Number> prices;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if(location.toString().contains("Citrus")) {
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.CITRUS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Dairy")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.DAIRY));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.DAIRY,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.DAIRY,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.DAIRY,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.DAIRY,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Feed")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.FEED_CROPS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.FEED_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.FEED_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.FEED_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.FEED_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Fish")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.FISH));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.FISH,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.FISH,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.FISH,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.FISH,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Fruit")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.NON_CITRUS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.NON_CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.NON_CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.NON_CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.NON_CITRUS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Grains")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.GRAINS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.GRAINS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.GRAINS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.GRAINS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.GRAINS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Meat")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.MEAT_ANIMALS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.MEAT_ANIMALS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Nuts")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.NUTS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.NUTS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.NUTS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.NUTS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.NUTS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Oil")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.OIL_CROPS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.OIL_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.OIL_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.OIL_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.OIL_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Poultry")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.POULTRY_EGGS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.POULTRY_EGGS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.POULTRY_EGGS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.POULTRY_EGGS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.POULTRY_EGGS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Special")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.SPECIALTY_CROPS,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch (NullPointerException e){}
    }
    else if(location.toString().contains("Vegetables")){
      try {
        prices.getData().setAll(ChartGraphics.cg.getCropPriceChart(StatisticReadData.FARMPRODUCT.VEGETABLES));
        int year = StatisticReadData.srd.getCurYear();
        t5CUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.VEGETABLES,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5CWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.VEGETABLES,
                StatisticReadData.STAT_TYPE.TOTAL_CONSUMPTION));
        t5PUs.getData().setAll(ChartGraphics.cg.getTopFiveUS(year, StatisticReadData.FARMPRODUCT.VEGETABLES,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
        t5PWo.getData().setAll(ChartGraphics.cg.getTopFiveWorld(year, StatisticReadData.FARMPRODUCT.VEGETABLES,
                StatisticReadData.STAT_TYPE.TOTAL_PRODUCTION));
      }
      catch(NullPointerException e){}
    }
  }
}
