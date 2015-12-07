package starvationevasion.vis.ClientTest;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import starvationevasion.vis.controller.EarthViewer;

/**
 * Created by Tess Daughton on 11/23/15.
 * This is my CustomLayout class but it is not necessary for embedding the Earth to work.
 * One thing that you will need is some sort of Layout Manager, an instance of EarthViewer, and
 * a method to switch between showing the Earth in full mode or mini mode
 */
public class CustomLayout extends BorderPane
{
  private GridPane topBar = new GridPane();
  private GridPane leftBarGrid = new GridPane();
  private GridPane rightBarGrid = new GridPane();
  private GridPane centerGrid = new GridPane();

  public  Label title = new Label("Year, World/Region \nPopulation and HDI");
  private Label toggleEarthMode = new Label("Press tab to toggle between Earth sizes");

  private EarthViewer earthViewer;

  public CustomLayout(EarthViewer earthViewer)
  {
    //Earth Viewer takes two parameters, one is the desired radius of your mini Earth
    //and one is the desired radius of your large Earth
    //This was done so that each client could easily size the Earth to fit in with their GUI
    //Start rotate will put the earthViewer object in an automatic and continuous rotation (this is for the mini view)
    this.earthViewer = earthViewer;
    initTopBar();
    initLeftBar();

    this.setTop(topBar);
    this.setLeft(leftBarGrid);
    this.setRight(rightBarGrid);
    this.setCenter(centerGrid);
  }

//The rest of these functions are layout things and not important

  private void initTopBar()
  {
    title.setId("title");
    title.setWrapText(true);
    toggleEarthMode.setId("button");
    title.setTextAlignment(TextAlignment.CENTER);
    toggleEarthMode.setTextAlignment(TextAlignment.CENTER);
    toggleEarthMode.setWrapText(true);
    topBar.setHgap(7);
    topBar.add(title, 15, 0);
    topBar.add(toggleEarthMode, 10, 10);
  }

  private void initLeftBar()
  {
    leftBarGrid.setHgap(10);
    leftBarGrid.setVgap(10);
    leftBarGrid.setPadding(new Insets(0, 0, 0, 0));
    leftBarGrid.add(earthViewer.updateMini(), 0, 0);
  }
}