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

  public double getPenaltyValue(String territory)
  {
    return getPenaltyValue(getTerritory(territory));
  }

  public double getPenaltyValue(Territory territory)
  {
    double value;
    if (penaltyData.containsKey(territory))
    {
      value = penaltyData.get(territory);
    }
    else
    {
      double weightedSum = 0;
      double weightedTotal = 0;
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
        value = weightedSum / weightedTotal;
      }
    }
    return value;
  }

  public void setPenaltyData(String territory, double penaltyValue)
  {
    setPenaltyData(getTerritory(territory), penaltyValue);
  }

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
