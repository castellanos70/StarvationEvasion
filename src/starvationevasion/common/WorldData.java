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

  /**
   * Current simulation year. All other data in this structure valid only for this current year.
   */
  public int year;

  /**
   * Rise (+) or fall (-) in the average global sea level from Constant.FIRST_YEAR as measured
   * at the start of the current simulation year.;
   */
  public double seaLevel;


  /**
   * List of special world events (if any) that occurred during the past turn (3 years).
   */
  public ArrayList<SpecialEventData> eventList = new ArrayList<>();


  /**
   * Data for each of the player and non-player world regions.
   */
  public RegionData[] regionData = new RegionData[EnumRegion.SIZE];

  /**
   * Sell foodPrice in US Dollars for one metric ton (1000 kg) of each food category on
   * the world market at the start of the current year.
   */
  public double[] foodPrice = new double[EnumFood.SIZE];


  public WorldData()
  {
    for (int i=0; i<EnumRegion.SIZE; i++)
    {
      regionData[i] = new RegionData(EnumRegion.values()[i]);
    }
  }




  /**
   * @return Data stored in this structure as a formatted String.
   */
  public String toString()
  {
    String msg = "WorldData[" + year + "] =====================================\n     Price: [";
    for (EnumFood food :EnumFood.values())
    {
      msg += String.format("%s:%f.0", food, foodPrice[food.ordinal()]);
      if (food != EnumFood.DAIRY) msg += ", "; else msg += "]\n";
    }

    for (RegionData region : regionData)
    {
      msg += "     " + region + "\n";
    }

    for (SpecialEventData event : eventList)
    {
      msg += "     " + event + "\n";
    }
    return msg;
  }
}
