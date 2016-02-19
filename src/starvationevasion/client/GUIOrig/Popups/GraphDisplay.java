package starvationevasion.client.GUIOrig.Popups;

import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.Graphs.Graph;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import starvationevasion.common.EnumRegion;

/**
 * GUIOrig element which displays the graphs during the drafting phase
 */
public class GraphDisplay extends BorderPane
{
  GUI gui;
  ImageView leftArrow;
  ImageView rightArrow;

  ComboBox regionSelect;

  EnumRegion currentRegion;
  int graphIndex = 0;

  StackPane left;
  StackPane right;
  Graph graphDisplay;

  /**
   * Constructor for GraphDisplay
   * Takes a reference to the GUIOrig which ones it
   * @param gui
   */
  public GraphDisplay(GUI gui)
  {
    this.gui = gui;
    this.getStylesheets().add("/starvationevasion/client/GUIOrig/DraftLayout/style.css");
    this.getStyleClass().add("graphdisplay");
    this.setVisible(false);

    currentRegion = EnumRegion.CALIFORNIA;//GUIOrig.getAssignedRegion();
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
    regionSelect = new ComboBox();
    regionSelect.setValue(currentRegion.toString());
    for (int i=  0; i < EnumRegion.US_REGIONS.length; ++i)
    {
      regionSelect.getItems().add(EnumRegion.US_REGIONS[i].toString());
    }

    regionSelect.setOnAction((event) -> {
        currentRegion = stringToRegion(regionSelect.getSelectionModel().getSelectedItem());
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

    if (s.equalsIgnoreCase(EnumRegion.CALIFORNIA.toString()))
    {
      return EnumRegion.CALIFORNIA;
    }
    else if (s.equalsIgnoreCase(EnumRegion.MOUNTAIN.toString()))
    {
      return EnumRegion.MOUNTAIN;
    }
    else if (s.equalsIgnoreCase(EnumRegion.HEARTLAND.toString()))
    {
      return EnumRegion.HEARTLAND;
    }
    else if (s.equalsIgnoreCase(EnumRegion.SOUTHEAST.toString()))
    {
      return EnumRegion.SOUTHEAST;
    }
    else if (s.equalsIgnoreCase(EnumRegion.SOUTHERN_PLAINS.toString()))
    {
      return EnumRegion.SOUTHERN_PLAINS;
    }
    else if (s.equalsIgnoreCase(EnumRegion.NORTHERN_PLAINS.toString()))
    {
      return EnumRegion.NORTHERN_PLAINS;
    }
    else if (s.equalsIgnoreCase(EnumRegion.NORTHERN_CRESCENT.toString()))
    {
      return EnumRegion.NORTHERN_CRESCENT;
    }
    else
    {
      return EnumRegion.CALIFORNIA;
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
