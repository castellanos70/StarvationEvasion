package starvationevasion.client.GUI;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;

/**
 * SummaryBar is the node which is displayed at the top of the GUI. It displays:
 *
 * Game's current year
 * Population of world and player region
 * HDI of world and player region
 * Current balance of the player's farming sector government revenue
 *
 */
public class SummaryBar extends GridPane
{
  private ArrayList<ColumnConstraints> colConstraintsList;
  private ArrayList<RowConstraints> rowConstraintsList;

  GUI gui;
  private EnumRegion region;
  static int year = 1981;

  static int regionPopulationData;
  static int worldPopulationData;

  static int regionHDIdata;
  static int worldHDIdata;

  static int farmingBalance;

  static Text regionTitle;
  static Text yearLabel;
  static Text balanceLabel;

  static Text regionHDI;
  static Text worldHDI;

  static Text regionPopulation;
  static Text worldPopulation;


  /**
   * Constructor for the SummaryBar
   * @param gui which owns the SummaryBar
   */
  public SummaryBar(GUI gui)
  {
    this.gui = gui;
    this.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, .45), null, null)));
    initializeGridSizes();
    this.getColumnConstraints().addAll(colConstraintsList);
    this.getRowConstraints().addAll(rowConstraintsList);

    //set assignedRegion to whatever the GUI was passed
    if (gui.assignedRegion == null)
    {
      region = EnumRegion.USA_CALIFORNIA;
    }
    else
    {
      region = gui.assignedRegion;
    }

    regionTitle = new Text();
    regionTitle.setFont(Font.font(null, FontWeight.BOLD, 14));
    regionTitle.setText(region.toString());

    yearLabel = new Text("" + year);
    yearLabel.setFont(Font.font(null, FontWeight.BOLD, 40));

    regionPopulation = new Text("Region Population: " + regionPopulationData);
    regionPopulation.setFont(Font.font(null, FontWeight.BOLD, 14));
    worldPopulation = new Text("World Population: " + worldPopulationData);
    worldPopulation.setFont(Font.font(null, FontWeight.BOLD, 14));

    regionHDI = new Text("Region HDI: " + regionHDIdata);
    regionHDI.setFont(Font.font(null, FontWeight.BOLD, 14));
    worldHDI = new Text("World HDI: " + worldHDIdata);
    worldHDI.setFont(Font.font(null, FontWeight.BOLD, 14));

    balanceLabel = new Text("Balance: $" + farmingBalance + " million");
    balanceLabel.setFont(Font.font(null, FontWeight.BOLD, 14));

    this.add(regionTitle, 2, 0, 1, 1);

   // this.setAlignment(Pos.CENTER);
   // this.add(yearLabel, 0, 0, 1, 4);

    //this.add(regionPopulation, 1, 1, 1, 1);
    //this.add(worldPopulation, 1, 2, 1, 1);

    //this.add(regionHDI, 3, 1, 1, 1);
    //this.add(worldHDI, 3, 2, 1, 1);

    //this.add(balanceLabel, 2,3,1,1);

    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("summarybar");
  }

  /**
   * Updates the summary bar information
   * @param newYear new year to update to
   * @param regPop new region population
   * @param worldPop new world population
   * @param regHDI new regionHDI
   * @param wHDI new worldHDI
   * @param newBalance new balance
   */
  public void updateSummarybar(int newYear, int regPop, int worldPop, int regHDI, int wHDI, int newBalance)
  {
    year = newYear;
    regionPopulationData = regPop;
    worldPopulationData = worldPop;
    regionHDIdata = regHDI;
    worldHDIdata = wHDI;
    farmingBalance = newBalance;

    yearLabel.setText("" + String.valueOf(year));
    regionPopulation.setText("Region Population: " + regionPopulationData);
    worldPopulation.setText("World Population: " + worldPopulationData);
    regionHDI.setText("Region HDI: " + regionHDIdata);
    worldHDI.setText("World HDI: " + worldHDIdata);
    balanceLabel.setText("Balance: $" + farmingBalance + " million");

    this.getChildren().removeAll();
    getChildren().clear();
    this.add(yearLabel, 0, 0, 1, 4);

    this.add(regionPopulation, 1, 1, 1, 1);
    this.add(worldPopulation, 1, 2, 1, 1);

    this.add(regionHDI, 3, 1, 1, 1);
    this.add(worldHDI, 3, 2, 1, 1);

    this.add(balanceLabel, 2,3,1,1);

  }
  public void setRegion(EnumRegion region){this.region=region;}
  /**
   * Updates the balance on the summarybar to the new balance
   * @param newBalance something
   */
  public void updateBalance(int newBalance)
  {
    farmingBalance = newBalance;
    balanceLabel.setText("Balance: $" + farmingBalance + " million");
  }

  private void initializeGridSizes()
  {
    colConstraintsList = new ArrayList<>();
    colConstraintsList.add(new ColumnConstraints());

    for (int i = 0; i < 5; ++i)
    {
      colConstraintsList.add(new ColumnConstraints());
      colConstraintsList.get(i).setPercentWidth(20);
    }

    rowConstraintsList = new ArrayList<>();
    for (int i = 0; i < 4; ++i)
    {
      rowConstraintsList.add(new RowConstraints());
      rowConstraintsList.get(i).setPercentHeight(25);
    }
  }

}
