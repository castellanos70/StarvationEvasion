package starvationevasion.client.MegaMawile.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import starvationevasion.client.MegaMawile.ChartGraphics;
import starvationevasion.client.MegaMawile.StatisticReadData;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameOverController implements Initializable
{
  @FXML
  Label usStatLabel;
  @FXML
  Label worldStatLabel;
  @FXML
  ComboBox<String> usStatSelect;
  @FXML
  ComboBox<String> worldStatSelect;
  @FXML
  TableView<TriStat> tableView1;
  @FXML
  TableColumn usCol1;
  @FXML
  TableColumn usCol2;
  @FXML
  TableColumn usCol3;
  @FXML
  TableView<TriStat> tableView2;
  @FXML
  TableColumn worldCol1;
  @FXML
  TableColumn worldCol2;
  @FXML
  TableColumn worldCol3;
  @FXML
  LineChart<String, Number> worldChart;
  @FXML
  LineChart<String, Number> usChart;
  @FXML
  Button exit1;
  @FXML
  Button exit2;
  @FXML
  Button newGame1;
  @FXML
  Button newGame2;

  StatisticReadData.USREGION usRegion;
  StatisticReadData.WORLDREGION worldRegion;

  private ObservableList<TriStat> data;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    //should start with players region as USregion
    usRegion = StatisticReadData.USREGION.CALIFORNIA;
    worldRegion = StatisticReadData.WORLDREGION.MIDDLE_AMERICA;
    //

    usStatSelect.getItems().setAll("HDI Statistics", "Revenue Statistics",
            "Population Statistics", "Malnourished Statistics");
    usStatSelect.setValue("HDI Statistics");
    usStatLabel.textProperty().bind(usStatSelect.getSelectionModel().selectedItemProperty());
    usStatSelect.valueProperty().addListener(new ChangeListener<String>() {
      @Override public void changed(ObservableValue ov, String t, String t1) {
        tableView1.setItems(getNewUSData(t1));
      }
    });

    worldStatSelect.getItems().setAll("HDI Statistics", "Revenue Statistics",
            "Population Statistics", "Malnourished Statistics");
    worldStatSelect.setValue("HDI Statistics");
    worldStatLabel.textProperty().bind(worldStatSelect.getSelectionModel().selectedItemProperty());
    worldStatSelect.valueProperty().addListener(new ChangeListener<String>() {
      @Override public void changed(ObservableValue ov, String t, String t1) {
        tableView2.setItems(getNewWorldData(t1));
      }
    });

    exit1.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e)
      {
        Node  source = (Node) e.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
      }
    });
    exit2.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e)
      {
        Node  source = (Node) e.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
      }
    });
    newGame1.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle (ActionEvent e)
      {
        Node  source = (Node) e.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        newGame(stage);
      }

      private void newGame (Stage stage)
      {
        try
        {
          Parent root = FXMLLoader.load(getClass().getResource("EnterUserName.fxml"));
          Scene gameFailed = new Scene(root, 600, 400);
          stage.setScene(gameFailed);
          stage.setTitle("Starvation Evasion");
          stage.show();
        }
        catch (Exception e){}

      }
    });
    newGame2.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e)
      {
        Node  source = (Node) e.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        newGame(stage);
      }

      private void newGame (Stage stage)
      {
        try
        {
          Parent root = FXMLLoader.load(getClass().getResource("EnterUserName.fxml"));
          Scene gameFailed = new Scene(root, 600, 400);
          stage.setScene(gameFailed);
          stage.setTitle("Starvation Evasion");
          stage.show();
        }
        catch (Exception e){}

      }
    });

    tableView1.setItems(getNewUSData("HDI Statistics"));
    usCol1.setCellValueFactory(new PropertyValueFactory("rank"));
    usCol2.setCellValueFactory(new PropertyValueFactory("name"));
    usCol3.setCellValueFactory(new PropertyValueFactory("stat"));
    tableView1.getColumns().setAll(usCol1, usCol2, usCol3);
    tableView1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      try
      {
        usRegion = ChartGraphics.cg.stringToUsRegion(newSelection.name);
        String s = usStatSelect.getValue();
        getNewUSData(s);
      }
      catch(NullPointerException e){}
    });

    worldCol1.setCellValueFactory(new PropertyValueFactory("rank"));
    worldCol2.setCellValueFactory(new PropertyValueFactory("name"));
    worldCol3.setCellValueFactory(new PropertyValueFactory("stat"));
    tableView2.setItems(getNewWorldData("HDI Statistics"));
    tableView2.getColumns().setAll(worldCol1, worldCol2, worldCol3);
    tableView2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      try
      {
        worldRegion = ChartGraphics.cg.stringToWorldRegion(newSelection.name);
        String s = worldStatSelect.getValue();
        getNewWorldData(s);
      }
      catch (NullPointerException e){}
    });
  }

  private ObservableList getNewUSData(String statType){
    List list = new ArrayList();
    switch (statType){
      case "Population Statistics":
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usRegion,
                StatisticReadData.STAT_TYPE.POPULATION));
        list.addAll(triStatMakerUs(StatisticReadData.STAT_TYPE.POPULATION));
        break;
      case "HDI Statistics":
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usRegion,
                StatisticReadData.STAT_TYPE.HDI));
        list.addAll(triStatMakerUs(StatisticReadData.STAT_TYPE.HDI));
        break;
      case "Revenue Statistics":
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usRegion,
                StatisticReadData.STAT_TYPE.REVENUE));
        list.addAll(triStatMakerUs(StatisticReadData.STAT_TYPE.REVENUE));
        break;
      case "Malnourished Statistics":
        usChart.getData().setAll(ChartGraphics.cg.getRegionChartData(usRegion,
                StatisticReadData.STAT_TYPE.MALNOURISHED));
        list.addAll(triStatMakerUs(StatisticReadData.STAT_TYPE.MALNOURISHED));
        break;
    }
    ObservableList data = FXCollections.observableList(list);
    return data;
  }

  private List<TriStat> triStatMakerUs(StatisticReadData.STAT_TYPE statType)
  {
    List<TriStat> statList = new ArrayList<>();
    ArrayList<StatisticReadData.USREGION> rankings = new ArrayList<>();
    switch (statType){
      case POPULATION:
        rankings = StatisticReadData.srd.getTopUSData(
                StatisticReadData.STAT_TYPE.POPULATION);
        break;
      case HDI:
        rankings = StatisticReadData.srd.getTopUSData(
                StatisticReadData.STAT_TYPE.HDI);
        break;
      case MALNOURISHED:
        rankings = StatisticReadData.srd.getTopUSData(
                StatisticReadData.STAT_TYPE.MALNOURISHED);
        break;
      case REVENUE:
        rankings = StatisticReadData.srd.getTopUSData(
                StatisticReadData.STAT_TYPE.REVENUE);
        break;
    }
    int i=1;
    for(StatisticReadData.USREGION region: rankings)
    {
      StatisticReadData.RegionData rd = StatisticReadData.srd.getRegionData(region);
      statList.add(new TriStat(i, ChartGraphics.cg.regionTypeToString(region),
              rd.getAnnualData(StatisticReadData.srd.getCurYear()).getAnyStat(statType)));
      i++;
    }
    return statList;
  }

  private ObservableList getNewWorldData(String statType) {
    List list = new ArrayList();
    switch (statType){
      case "Population Statistics":
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(
                worldRegion, StatisticReadData.STAT_TYPE.POPULATION));
        list.addAll(triStatMakerWorld(StatisticReadData.STAT_TYPE.POPULATION));
        break;
      case "HDI Statistics":
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(
                worldRegion, StatisticReadData.STAT_TYPE.HDI));
        list.addAll(triStatMakerWorld(StatisticReadData.STAT_TYPE.HDI));
        break;
      case "Revenue Statistics":
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(
                worldRegion, StatisticReadData.STAT_TYPE.REVENUE));
        list.addAll(triStatMakerWorld(StatisticReadData.STAT_TYPE.REVENUE));
        break;
      case "Malnourished Statistics":
        worldChart.getData().setAll(ChartGraphics.cg.getRegionChartData(
                worldRegion, StatisticReadData.STAT_TYPE.MALNOURISHED));
        list.addAll(triStatMakerWorld(StatisticReadData.STAT_TYPE.MALNOURISHED));
        break;
    }
    ObservableList data = FXCollections.observableList(list);
    return data;
  }

  private List<TriStat> triStatMakerWorld(StatisticReadData.STAT_TYPE statType)
  {
    List<TriStat> statList = new ArrayList<>();
    ArrayList<StatisticReadData.WORLDREGION> rankings = new ArrayList<>();
    switch (statType){
      case POPULATION:
        rankings = StatisticReadData.srd.getTopWorldData(
                StatisticReadData.STAT_TYPE.POPULATION);
        break;
      case HDI:
        rankings = StatisticReadData.srd.getTopWorldData(
                StatisticReadData.STAT_TYPE.HDI);
        break;
      case MALNOURISHED:
        rankings = StatisticReadData.srd.getTopWorldData(
                StatisticReadData.STAT_TYPE.MALNOURISHED);
        break;
      case REVENUE:
        rankings = StatisticReadData.srd.getTopWorldData(
                StatisticReadData.STAT_TYPE.REVENUE);
        break;
    }
    int i=1;
    for(StatisticReadData.WORLDREGION region: rankings)
    {
      StatisticReadData.RegionData rd = StatisticReadData.srd.getRegionData(region);
      statList.add(new TriStat(i, ChartGraphics.cg.regionTypeToString(region),
              rd.getAnnualData(StatisticReadData.srd.getCurYear()).getAnyStat(statType)));
      i++;
    }
    return statList;
  }

  public class TriStat
  {
    private int rank;
    private String name;
    private int stat;

    public TriStat(int rank, String name, int stat)
    {
      this.rank = rank;
      this.name = name;
      this.stat = stat;
    }

    public int getRank()
    {
      return rank;
    }

    public String getName()
    {
      return name;
    }

    public int getStat()
    {
      return stat;
    }
  }
}
