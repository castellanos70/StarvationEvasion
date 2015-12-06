package starvationevasion.io;


import starvationevasion.common.EnumRegion;
import starvationevasion.sim.Territory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


/**
 * Loads Trade and Food penalty function into a map
 */
public class PenaltyCSVLoader
{
  private final static Logger LOGGER = Logger.getLogger(FertilizerCSVLoader.class.getName());
  private static final String PATH = "/sim/WorldData/Food_Trade_Penalty_Function.csv";

  public PenaltyCSVLoader(Territory[] territories) throws FileNotFoundException
  {
    CSVReader fileReader = new CSVReader(PATH, 2);

    String[] fieldList;
    while ((fieldList = fileReader.readRecord(2)) != null)
    {
      String name = fieldList[0];
      double value = Double.parseDouble(fieldList[1]);

      Territory tmp = new Territory(name);
      int idx = Arrays.binarySearch(territories, tmp);
      if (idx < 0)
      {
        LOGGER.severe("**ERROR** Reading " + PATH+
            "Territory="+name + ", Not found in territory list.");
        return;
      }
      Territory territory = territories[idx];
      territory.setPenaltyValue(value);
    }

    // add missing penalties
    addPenaltyToRegions(territories);
  }

  /**
   * Estimate penalties for territories there is no data for
   */
  private void addPenaltyToRegions(Territory[] territories)
  {
    HashMap<EnumRegion, Double> regionPenalies = getRegionPenalties(territories);

    for (int i = 0; i < territories.length; i++)
    {
      Territory territory = territories[i];
      if (territory.getPenaltyValue() == -1)
      {
        territory.setPenaltyValue(regionPenalies.get(territory.getGameRegion()));
      }
    }
  }

  /**
   * Creates a map of a region to a weighted average of penalites in that region
   */
  private HashMap<EnumRegion, Double> getRegionPenalties(Territory[] territories)
  {
    HashMap<EnumRegion, Double> regionPenalties = new HashMap<>();
    for (EnumRegion region : EnumRegion.values())
    {
      // the penalty function for this country is not available in the data
      // take a weighted average of the penalty functions for other territories
      // in the same region
      double weightedSum = 0;
      double weightedTotal = 0;
      double value;

      // only get territories in the region which have a penalty function
      for (Territory t : getTerritoriesInRegion(region, territories))
      {
        if (t.getPenaltyValue() != -1)
        {
          // use 2014 population data
          weightedSum += t.getPenaltyValue() * t.getPopulation(2014);
          weightedTotal += t.getPopulation(2014);
        }
      }

      if (weightedTotal == 0)
      {
        value = 0;
      }
      else
      {
        value = weightedSum / weightedTotal;
      }

      regionPenalties.put(region, value);
    }
    return regionPenalties;
  }

  /**
   * Get all of the territories in a region
   */
  private List<Territory> getTerritoriesInRegion(EnumRegion region, Territory[] territories)
  {
    List<Territory> regionTerritories = new ArrayList<>();

    for (int i = 0; i < territories.length; i++)
    {
      Territory t = territories[i];
      if (t.getGameRegion() == region)
      {
        regionTerritories.add(t);
      }
    }
    return regionTerritories;
  }
}
