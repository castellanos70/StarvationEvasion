package starvationevasion.sim;


import java.util.HashMap;


/**
 * Data structure for holding penalty function data.
 */
public class PenaltyData
{
  private HashMap<String, Double> penaltyData;

  public PenaltyData()
  {
    penaltyData = new HashMap<>();
  }

  public double getPenaltyValue(String territory)
  {
    double penaltyValue;
    if (territory.startsWith("US-"))
    {
      penaltyValue = penaltyData.get("United States of America");
    }
    else
    {
      penaltyValue = penaltyData.get(territory);
    }
    return penaltyValue;
  }

  public double getPenaltyValue(Territory territory)
  {
    return getPenaltyValue(territory.getName());
  }

  public void setPenaltyData(String territory, double penaltyValue)
  {
    penaltyData.put(territory, penaltyValue);
  }

  public void setPenaltyData(Territory territory, double penaltyValue)
  {
    setPenaltyData(territory.getName(), penaltyValue);
  }

  @Override
  public String toString()
  {
    String string = "";

    for (String territory : penaltyData.keySet())
    {
      string += territory + ": " + getPenaltyValue(territory) + "\n";
    }
    return string;
  }
}
