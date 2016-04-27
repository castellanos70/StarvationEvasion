package starvationevasion.communication.ClientTest;

import javafx.application.Platform;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;
import starvationevasion.common.policies.EnumPolicy;
import starvationevasion.communication.ClientTest.Graphs.GraphManager;

import java.util.HashMap;

/**
 * Based off of the original LocalDataContainer with minor edits for the purposes of the client test.
 */
public class LocalDataContainer
{
  public HashMap<EnumRegion, RegionData> regionToData;
  public final int START_YEAR = 1981;
  private WorldData worldData;
  private EnumPolicy[] hand;
  private GUI gui;
  private int year;
  private Client client;
  /**
   * Constructs an instance of the LocalDataContainer class
   * @param client A reference to the client to construct to LocalDataContainer
   */
  public LocalDataContainer(Client client)
  {
    this.client = client;
    regionToData = new HashMap<>();
  }

  /**
   * Initialize the settings for the LocalDataContainer instance
   */
  public void init()
  {
    for(EnumRegion region : EnumRegion.values())
    {
      regionToData.put(region, new RegionData(region));
    }
  }

  /**
   * Gets the hand
   * @return The hand represented as an array of EnumPolicy
   */
  public EnumPolicy[] getHand()
  {
    return hand;
  }

  /**
   * Called once per turn, this method gives the Client the
   * contents of his new hand as well as all data that does
   * not pertain to the Visualizer.
   */
  public void updateGameState(WorldData worldData)
  {
    gui=client.getGUI();
    Platform.runLater(() ->
    {
      parseNewRoundData(worldData);
    });
  }

  private void parseNewRoundData(WorldData newData)
  {
    this.worldData = newData;
    synchronized (this)
    {
      this.year = newData.year;
      for (int i = 0; i < newData.regionData.length; i++)
      {
        EnumRegion curRegion = newData.regionData[i].region;
        regionToData.replace(curRegion, newData.regionData[i]);
      }
      sendDataToGraphs();
      sendDataToSummaryBar();
    }
  }

  private void sendDataToGraphs()
  {
    GraphManager manager =  gui.getGraphManager();
    regionToData.keySet().forEach(region ->{
      manager.addData(year,worldData.foodPrice);
    });
    regionToData.keySet().forEach(region ->
            manager.addData(region, 0, year, regionToData.get(region).population));
    regionToData.keySet().forEach(region ->
            manager.addData(region, 1, year, (int) regionToData.get(region).humanDevelopmentIndex));
    regionToData.keySet().forEach(region ->
            manager.addData(region, 2, year, regionToData.get(region).revenueBalance));
//      regionToData.keySet().forEach(region ->
//        manager.addData(region, year, regionToData.get(region).foodProduced,
//                                                              regionToData.get(region).foodExported,regionToData.get(region).foodIncome));
  }

  private void sendDataToSummaryBar()
  {
    SummaryBar summaryBar = gui.getDraftLayout().getSummaryBar();
    EnumRegion region = client.getRegion();
    for (EnumRegion enumRegion : regionToData.keySet())
    {
      if(region.equals(enumRegion))
      {
        RegionData regionData = regionToData.get(enumRegion);
        summaryBar.updateSummarybar(year, regionData.population, 0, (int) regionData.humanDevelopmentIndex, 0, regionData.revenueBalance);
      }
    }
  }
}
