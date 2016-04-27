package starvationevasion.communication.ClientTest.Popups;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import starvationevasion.common.EnumRegion;
import starvationevasion.communication.ClientTest.GUI;
import starvationevasion.communication.ClientTest.Graphs.Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GUI element which displays the graphs during the drafting phase
 */
public class GraphDisplay extends BorderPane
{
  GUI gui;
  ImageView leftArrow;
  ImageView rightArrow;

  ComboBox<EnumRegion> regionSelect;

  EnumRegion currentRegion;
  int graphIndex = 0;

  StackPane left;
  StackPane right;
  Graph graphDisplay;

  /**
   * Constructor for GraphDisplay
   * Takes a reference to the GUI which ones it
   * @param gui reference to gui
   */
  public GraphDisplay(GUI gui)
  {
    this.gui = gui;
    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("graphdisplay");
    this.setVisible(false);

    currentRegion = EnumRegion.USA_CALIFORNIA;//GUI.getAssignedRegion();
    graphDisplay = gui.getGraphManager().getGraphNodeGraph(currentRegion, graphIndex);

    initializeComboBox();
    initializeLeft();
    initializeRight();

    setAlignment(regionSelect, Pos.CENTER);
    setTop(regionSelect);
    setAlignment(left, Pos.CENTER);
    setLeft(left);
    setAlignment(right, Pos.CENTER);
    setRight(right);
    setAlignment(graphDisplay.getLineChart(),Pos.CENTER);
    setCenter(graphDisplay.getLineChart());
  }

  /**
   * Closes this display
   */
  public void open()
  {
    this.setVisible(true);
  }

  /**
   * Opens this display
   */
  public void close()
  {
    this.setVisible(false);
  }

  private void initializeComboBox()
  {
    ArrayList<EnumRegion> regions=new ArrayList<>(Arrays.asList(EnumRegion.values()));
    ObservableList<EnumRegion> regionList= FXCollections.observableArrayList(regions);
    regionSelect =new ComboBox<>(regionList);

    regionSelect.setValue(currentRegion);
    for (int i=  0; i < EnumRegion.values().length; ++i)
    {
      regionSelect.getItems().add(EnumRegion.values()[i]);
    }

    regionSelect.setOnAction((event) -> {
        currentRegion = regionSelect.getValue();
        graphDisplay = gui.getGraphManager().getGraphNodeGraph(currentRegion, graphIndex);
      setCenter(graphDisplay.getLineChart());
    });
  }

  private void initializeLeft()
  {
    left = new StackPane();
    leftArrow = new ImageView(gui.getImageGetter().getGraphLeftArrowBig());
    left.getChildren().add(leftArrow);

    left.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        moveLeft();
      }
    });
  }

  private void initializeRight()
  {
    right = new StackPane();
    rightArrow = new ImageView(gui.getImageGetter().getGraphRightArrowBig());
    right.getChildren().add(rightArrow);

    right.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        moveRight();
      }
    });
  }

  private EnumRegion stringToRegion(Object o)
  {
    String s = (String) o;

    if (s.equalsIgnoreCase(EnumRegion.USA_CALIFORNIA.toString()))
    {
      return EnumRegion.USA_CALIFORNIA;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_MOUNTAIN.toString()))
    {
      return EnumRegion.USA_MOUNTAIN;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_HEARTLAND.toString()))
    {
      return EnumRegion.USA_HEARTLAND;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_SOUTHEAST.toString()))
    {
      return EnumRegion.USA_SOUTHEAST;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_SOUTHERN_PLAINS.toString()))
    {
      return EnumRegion.USA_SOUTHERN_PLAINS;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_NORTHERN_PLAINS.toString()))
    {
      return EnumRegion.USA_NORTHERN_PLAINS;
    }
    else if (s.equalsIgnoreCase(EnumRegion.USA_NORTHERN_CRESCENT.toString()))
    {
      return EnumRegion.USA_NORTHERN_CRESCENT;
    }
    else
    {
      return EnumRegion.USA_CALIFORNIA;
    }
  }

  private void moveRight()
  {
    graphIndex += 1;
    if (graphIndex >= 3)
    {
      graphIndex = 0;
    }
    graphDisplay = gui.getGraphManager().getGraphNodeGraph(currentRegion, graphIndex);
    this.setCenter(graphDisplay.getLineChart());
  }

  private void moveLeft()
  {
    graphIndex -= 1;
    if (graphIndex < 0)
    {
      graphIndex = 2;
    }
    graphDisplay = gui.getGraphManager().getGraphNodeGraph(currentRegion, graphIndex);
    this.setCenter(graphDisplay.getLineChart());
  }
}
