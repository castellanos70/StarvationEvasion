package starvationevasion.sim;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Data structure for holding penalty function data.
 */
public class PenaltyData
{
  private HashMap<Territory, Double> penaltyData;
  private List<Territory> territories;

  public PenaltyData(List<Territory> territories)
  {
    penaltyData = new HashMap<>();
    this.territories = territories;
  }

  /**
   * Get the penalty function for a territory by territory name
   *
   * @param territory  name of the territory
   * @return penalty function of the territory
   */
  public double getPenaltyValue(String territory)
  {
    return getPenaltyValue(getTerritory(territory));
  }

  /**
   * Get the penalty function for a territory object
   *
   * @param territory  territory object
   * @return penalty function of the territory object
   */
  public double getPenaltyValue(Territory territory)
  {
    double value;
    if (penaltyData.containsKey(territory))
    {
      value = penaltyData.get(territory);
    }
    else
    {
      // the penalty function for this country is not available in the data
      // take a weighted average of the penalty functions for other territories
      // in the same region
      double weightedSum = 0;
      double weightedTotal = 0;

      // only get territories in the region which have a penalty function
      for (Territory t : getRegionTerritories(territory))
      {
        weightedSum += getPenaltyValue(t)*t.getPopulation(33);
        weightedTotal += t.getPopulation(33);
      }

      if (weightedTotal == 0)
      {
        value = 0;
      }
      else
      {
        // penalty function is only calculated once when it is needed
        value = weightedSum / weightedTotal;
        penaltyData.put(territory, value);
      }
    }
    return value;
  }

  /**
   * Sets the penalty function for a territory with the given name
   *
   * @param territory  name of the territory
   * @param penaltyValue  value of the penalty function
   */
  public void setPenaltyData(String territory, double penaltyValue)
  {
    setPenaltyData(getTerritory(territory), penaltyValue);
  }

  /**
   * Sets the penalty function for a territory for a given territory object
   *
   * @param territory territory object
   * @param penaltyValue penalty function value
   */
  public void setPenaltyData(Territory territory, double penaltyValue)
  {
    penaltyData.put(territory, penaltyValue);
  }

  @Override
  public String toString()
  {
    String string = "";

    for (Territory territory : penaltyData.keySet())
    {
      string += territory.getName() + ": " + getPenaltyValue(territory) + "\n";
    }
    return string;
  }

  /**
   * Get a territory based on its name
   */
  private Territory getTerritory(String name)
  {
    for (Territory territory : territories)
    {
      if (territory.getName().equals(name))
      {
        return territory;
      }
    }
    return null;
  }

  /**
   * Get the territories in the same region as territory who have a valid
   * penalty function
   */
  private List<Territory> getRegionTerritories(Territory territory)
  {
    List<Territory> regionTerritories = new ArrayList<>();
    for (Territory t : territories)
    {
      if (penaltyData.containsKey(territory) && t.getGameRegion() == territory.getGameRegion())
      {
        regionTerritories.add(t);
      }
    }
    return regionTerritories;
  }
}
