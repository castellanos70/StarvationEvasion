package starvationevasion.client.Logic;

import javafx.application.Platform;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.Graphs.GraphManager;
import starvationevasion.client.GUI.SummaryBar;
import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.RegionData;
import starvationevasion.common.WorldData;
import starvationevasion.common.gamecards.EnumPolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * It basically takes data from the server and uses a run later to change data in the GUi accordingly.
 */
public class LocalDataContainer
{
  public HashMap<EnumRegion, RegionData> regionToData;
  public final int START_YEAR = 1981;
  private WorldData worldData;
  private EnumPolicy[] hand;
  private GUI gui;
  private int year;
  private ArrayList<long[]> annualCropDistributionStats = new ArrayList<>();
  private ArrayList<long[]> annualCropExportStats = new ArrayList<>();
  private ArrayList<long[]> annualCropImportStats = new ArrayList<>();
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
  public void updateGameState(WorldData worldData)
  {
    gui=client.getGui();
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
      
      //Uncomment to see the values of things being passed.
//      System.out.println("Region: " + region.toString() + "Year: " +year + "\n"
//          + "population " + regionToData.get(region).population + "\n"
//          + "HDI"+ regionToData.get(region).humanDevelopmentIndex +"\n" 
//          +"undernuished people" + regionToData.get(region).undernourished +"\n"
//          + "exports" +Arrays.toString(regionToData.get(region).foodExported) + "\n" 
//          +   "imports" + Arrays.toString(regionToData.get(region).foodImported) + "\n"
//           +"income" + Arrays.toString(regionToData.get(region).foodIncome) + "\n;"
//           + "food produced"+ Arrays.toString(regionToData.get(region).foodProduced) );
                 
    });
    //Clear previous stats
    annualCropDistributionStats.clear();
    annualCropExportStats.clear();
    annualCropImportStats.clear();
    //Update annual stats for each of the three categories for every region.
    regionToData.keySet().forEach(region -> annualCropDistributionStats.add(regionToData.get(region).foodProduced));
    regionToData.keySet().forEach(region -> annualCropExportStats.add(regionToData.get(region).foodProduced));
    regionToData.keySet().forEach(region -> annualCropImportStats.add(regionToData.get(region).foodProduced));
  
    //Now update the data.
    manager.updateRegionalCropDistributionNumbers(year, annualCropDistributionStats);
    manager.updateRegionalCropExportNumbers(year, annualCropExportStats);
    manager.updateRegionalCropImportNumbers(year, annualCropImportStats);
    //Update regions population data. 
      regionToData.keySet().forEach(region ->
      manager.updateRegionsPopulation(regionToData.get(region).population, region.ordinal()));   
      
     
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
