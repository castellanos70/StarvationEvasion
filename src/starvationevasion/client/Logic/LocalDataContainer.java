package starvationevasion.client.Logic;

import starvationevasion.client.GUIOrig.GUI;
import starvationevasion.client.GUIOrig.Graphs.GraphManager;
import starvationevasion.client.GUIOrig.SummaryBar;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;

import java.util.HashMap;

//import starvationevasion.common.messages.ActionResponse;
//import starvationevasion.common.messages.GameState;

/**
 * It basically takes data from the server and uses a run later to change data in the GUi accordingly.
 */
public class LocalDataContainer
{
  public HashMap<EnumRegion, RegionData> regionToData;
  public final int START_YEAR = 1981;
  private WorldData worldData;
  private EnumPolicy[] hand;
  private Client client;
  private GUI gui;
  private int year;

  /**
   * Constructs an instance of the LocalDataContainer class
   * @param client A reference to the client to construct to LocalDataContainer
   */
  public LocalDataContainer(Client client)
  {
    this.client = client;
    this.gui = client.gui;
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
    synchronized (this)
    {
      return hand;
    }
  }

//  public void parseActionResponse(ActionResponse response)
//  {
//    //TODO: Not sure if we want to do something with Server's response?
//    System.out.println(response.responseMessage);
//  }

  /**
   * Called once per turn, this method gives the Client the
   * contents of his new hand as well as all data that does
   * not pertain to the Visualizer.
   */
//  public void updateGameState(GameState newGameState)
//  {
//    Platform.runLater(() ->
//    {
//      gui.getDraftLayout().getHand().setHand(newGameState.hand);
//      parseNewRoundData(newGameState.worldData);
//    });
//  }

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
    regionToData.keySet().forEach(region ->
        manager.addData(region, 0, year, regionToData.get(region).population));
      regionToData.keySet().forEach(region ->
        manager.addData(region, 1, year, (int) regionToData.get(region).humanDevelopmentIndex));
      regionToData.keySet().forEach(region ->
        manager.addData(region, 2, year, regionToData.get(region).revenueBalance));
      //regionToData.keySet().forEach(region ->
        //manager.addData(region, year, regionToData.get(region).foodProduced,
                                                              //regionToData.get(region).foodExported,regionToData.get(region).foodIncome));
  }

  private void sendDataToSummaryBar()
  {
    SummaryBar summaryBar = gui.getDraftLayout().getSummaryBar();
    EnumRegion region = client.getAssignedRegion();
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
