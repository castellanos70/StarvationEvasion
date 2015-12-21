package starvationevasion.client.MegaMawile.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import starvationevasion.client.MegaMawile.ChartGraphics;
import starvationevasion.client.MegaMawile.StatisticReadData;

import java.util.Random;

public class GameOverMain extends Application{

  protected Scene gameFailed;
  protected Scene gameWin;
  protected Scene gamePlace;
  public static void main(String[] args) {
        launch(args);
    }

  @Override
  public void start(Stage primaryStage) throws Exception{
    //chart testing
    new StatisticReadData();
    new ChartGraphics();
    int year=1981;
    int stat;
    Random rand = new Random();
    for(StatisticReadData.USREGION region: StatisticReadData.USREGION.values())
    {
      year = 1981;
      while (year <= 2050)
      {
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.POPULATION, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.HDI, year, (double)stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.REVENUE, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.MALNOURISHED, year, (double)stat);
        year += 3;
      }
    }
    for(StatisticReadData.WORLDREGION region: StatisticReadData.WORLDREGION.values())
    {
      year = 1981;
      while (year <= 2050)
      {
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.POPULATION, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.HDI, year, (double)stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.REVENUE, year, stat);
        stat = rand.nextInt(100);
        StatisticReadData.srd.populateStats(region,
                StatisticReadData.STAT_TYPE.MALNOURISHED, year, (double)stat);
        year += 3;
      }
    }
    //
    Parent root = FXMLLoader.load(getClass().getResource("WorldFamine.fxml"));
    gameFailed = new Scene(root, 600, 400);
    primaryStage.setScene(gameFailed);
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.show();
    Stage newStage=new Stage();
    root = FXMLLoader.load(getClass().getResource("FirstPlace.fxml"));
    gameWin = new Scene(root, 600, 400);
    newStage.setScene(gameWin);
    newStage.setTitle("Starvation Evasion");
    newStage.show();
    Stage newerStage= new Stage();
    root = FXMLLoader.load(getClass().getResource("OtherPlacing.fxml"));
    gamePlace= new Scene(root, 600, 400);
    newerStage.setScene(gamePlace);
    newerStage.setTitle("Starvation Evasion");
    newerStage.show();
  }

}
