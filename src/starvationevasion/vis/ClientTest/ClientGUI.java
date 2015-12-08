package starvationevasion.vis.ClientTest;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import starvationevasion.vis.controller.EarthViewer;


/**
 * Created by Tess Daughton on 11/15/15.
 * Class for testing purposes - simulate what a client's GUI might be like and ensure that our application
 * is function properly within their GUI
 */

public class ClientGUI extends Application implements EventHandler<KeyEvent> {

  private CustomLayout customLayout;
  private Scene scene;
  private Stage earthStage = new Stage();
  private final int MINI_EARTH_RADIUS = 100;
  private final int LARGE_EARTH_RADIUS = 300;
  private boolean fullEarthMode = false;
  private EarthViewer earthViewer = new EarthViewer(MINI_EARTH_RADIUS, LARGE_EARTH_RADIUS);


  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setWidth(800);
    primaryStage.setHeight(600);

    customLayout = new CustomLayout(earthViewer);
    scene = new Scene(customLayout);
    scene.setOnKeyPressed(this);
    primaryStage.setTitle("Starvation Evasion");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void handle(KeyEvent event)
  {
    switch (event.getCode())
    {
      //I have the Earth view toggling on TAB, but I'm not sure if this is a requirement
      //or if you can use a button or another key of your choice
      case TAB:
      {
        if (fullEarthMode)
        {
          fullEarthMode = false;
          earthStage.close();
        } else
        {
          fullEarthMode = true;
          Scene earthScene = new Scene(earthViewer.updateFull(), 1000, 700);
          earthViewer.addVisStyleSheet(earthScene);
          earthScene.setOnKeyPressed(this);
          earthStage.setScene(earthScene);
          earthStage.show();
        }
      }
//      case ENTER:
//      {
//        //World Data..
//        /* client passes in crop and temperature data, expecting MapPoints to be whole degrees */
//        HashMap<MapPoint, Float> tempData = new HashMap<>();
//        for (double lat = -90; lat < 90; lat += 1) {
//          for (double lon = -180; lon < 180; lon += 1) {
//            MapPoint p = new MapPoint(lat, lon);
//            /* temperature test data */
//            tempData.put(p, Util.rand.nextFloat() * 100);
//          }
//        }
//        earthViewer.updateTemperature(tempData);
//
//        HashMap<EnumRegion, int []> foodData = new HashMap<>();
//        for (EnumRegion e : EnumRegion.values())
//        {
//          int [] fData = new int[EnumFood.SIZE];
//          for (int i = 0; i < EnumFood.SIZE; i++)
//          {
//            fData[i] = Util.rand.nextInt(100);
//          }
//          foodData.put(e, fData);
//        }
//        earthViewer.updateFoodProduced(foodData);
//      }
//      case ENTER:
//      {
//        /* client passes in crop and temperature data, expecting MapPoints to be whole degrees */
//        ArrayList<LandTile> data = new ArrayList<>();
//        for (double lat = -90; lat < 90; lat += 1)
//        {
//          for (double lon = -180; lon < 180; lon += 1)
//          {
//            LandTile t = new LandTile(lon, lat);
//            t.elevation = Util.rand.nextFloat() * 100;
//            t.rainfall = Util.rand.nextFloat() * 20;
//
//            float max = Util.rand.nextFloat() * 100;
//            float min = max - 50;
//            float day = min + ((max - min) * Util.rand.nextFloat());
//            float night = min + ((max - min) * Util.rand.nextFloat());
//
//            t.maxAnnualTemp = max;
//            t.minAnnualTemp = min;
//            t.avgDayTemp = day;
//            t.avgNightTemp = night;
//            t.currCrop = EnumFood.values()[Util.rand.nextInt(EnumFood.SIZE)];
//            data.add(t);
//          }
//        }
//        earthViewer.updateLandTiles(data);


//        HashMap<MapPoint, Float> tempData = new HashMap<>();
//        for (double lat = -90; lat < 90; lat += 1) {
//          for (double lon = -180; lon < 180; lon += 1) {
//            MapPoint p = new MapPoint(lat, lon);
//            /* temperature test data */
//            tempData.put(p, Util.rand.nextFloat() * 100);
//          }
//        }
//        earthViewer.updateTemperature(tempData);
//
//        HashMap<EnumRegion, int[]> foodData = new HashMap<>();
//        for (EnumRegion e : EnumRegion.values()) {
//          int[] fData = new int[EnumFood.SIZE];
//          for (int i = 0; i < EnumFood.SIZE; i++) {
//            fData[i] = Util.rand.nextInt(100);
//          }
//          foodData.put(e, fData);
//        }
//        earthViewer.updateFoodProduced(foodData);
    }
  }


  public static void main(String[] args) {
    launch(args);
  }
}