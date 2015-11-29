package starvationevasion.common;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * This structure is populated and returned by the
 * {@link starvationevasion.sim.Simulator#nextTurn(ArrayList)} method.
 * It contains all world and region data to be returned to the client after each turn.
 * It does not contain high resolution location data possibly needed by the visualizer.
 */
public class WorldData implements Serializable
{
  public int year;
  public double seaLevel;
  public ArrayList<WorldEventData> eventList = new ArrayList<>();
  RegionData[] regionData = new RegionData[EnumRegion.SIZE];

  public WorldData()
  {
    for (int i=0; i<EnumRegion.SIZE; i++)
    {
      regionData[i] = new RegionData(EnumRegion.values()[i]);
    }
  }
}
