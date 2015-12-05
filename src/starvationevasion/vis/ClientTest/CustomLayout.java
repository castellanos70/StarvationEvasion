package starvationevasion.vis.ClientTest;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import starvationevasion.vis.visuals.*;

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

  private final ImageView image0 = new ImageView();
  private final ImageView image1 = new ImageView();
  private Image coldImage = new Image(  getClass().getClassLoader().getResourceAsStream("visResources/snowflake.png"));
  private Image hotImage = new Image(getClass().getClassLoader().getResourceAsStream("visResources/hot.png"));

  public  Label title = new Label("Year, World/Region \nPopulation and HDI");
  private Label toggleEarthMode = new Label("Press tab to toggle between Earth sizes");

  private EarthViewer earthViewer;
  //boolean to keep track of whether we are in Full Earth Mode or Mini Earth Mode
  private boolean fullEarth = false;

  public CustomLayout()
  {
    //Earth Viewer takes two parameters, one is the desired radius of your mini Earth
    //and one is the desired radius of your large Earth
    //This was done so that each client could easily size the Earth to fit in with their GUI
    earthViewer = new EarthViewer(70, 250, this);
    //Start rotate will put the earthViewer object in an automatic and continuous rotation (this is for the mini view)
    earthViewer.startRotate();

    initTopBar();
    initLeftBar();

    this.setTop(topBar);
    this.setLeft(leftBarGrid);
    this.setRight(rightBarGrid);
    this.setCenter(centerGrid);
  }

  public synchronized void switchEarthView()
  {
    //This method will switch between Earth "modes", the modes being either Mini or Large
    //Needs to be synchronized, since it will be removing and adding nodes while the program is running
    //In my example, I am using BorderLayout and I switching between displaying the mini Earth in the
    //left pane and the large Earth in the center.
    //The large Earth responds to scrolling, the mini Earth does not and should simply rotate continuously.
    if (fullEarth)
    {
      //nodes to remove
      centerGrid.getChildren().remove(earthViewer.getLargeEarth());
      rightBarGrid.getChildren().remove(image0);
      rightBarGrid.getChildren().remove(image1);
      //update text to not show the last selected state
      title.setText(" ");
      //nodes to add
      leftBarGrid.getChildren().add(earthViewer.getMiniEarth());

      fullEarth = false;
    }
    else
    {
      //nodes to remove
      leftBarGrid.getChildren().remove(earthViewer.getMiniEarth());
      //nodes to add
      image0.setImage(coldImage);
      image1.setImage(hotImage);
      rightBarGrid.add(image0, 0, 0);
      rightBarGrid.add(image1, 0, 128);
      centerGrid.getChildren().add((earthViewer.getLargeEarth()));
      //display large earth
      earthViewer.startEarth();

      fullEarth = true;
    }
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
    leftBarGrid.add(earthViewer.getMiniEarth(), 0, 0);
  }

  private void initRightBar()
  {
    rightBarGrid.setAlignment(Pos.TOP_RIGHT);
    rightBarGrid.add(image0,0,0);
    rightBarGrid.add(image1,0,128);
  }


  private void initCenter()
  {
    centerGrid.setAlignment(Pos.TOP_CENTER);
    centerGrid.setPadding(new Insets(30, 0, 0, 0));
  }
}