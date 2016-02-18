package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;

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
    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      regionData[i] = new RegionData(EnumRegion.values()[i]);
    }
  }


  /**
   * @return Data stored in this structure as a formatted String.
   */
  public String toString()
  {
    String msg = "WorldData[" + year + "] =====================================\n     price: [";
    for (EnumFood food : EnumFood.values())
    {
      msg += String.format("%s:%.0f", food, foodPrice[food.ordinal()]); if (food != EnumFood.DAIRY) msg += ", ";
    else msg += "]\n";
    }

    for (RegionData region : regionData)
    {
      msg += "     " + region + "\n";
    }

    for (SpecialEventData event : eventList)
    {
      msg += "     " + event + "\n";
    } return msg;
  }

  public JSONDocument toJSON()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setNumber("year", year);
    json.setNumber("seaLevel", seaLevel);

    JSONDocument jEventArray = JSONDocument.createArray();
    for(int i = 0; i < eventList.size(); i++) //event List iteration
      jEventArray.set(i, eventList.get(i).toJSON());
    json.set("eventList", jEventArray);
    //JSONDocument eventList = JSONDocument.createArray();
    //eventList.set(0, eventList.get(0));
    //json.set("eventList", eventList);


    JSONDocument jRegionArray =  JSONDocument.createArray();
    for(int i = 0; i < EnumRegion.SIZE; i++)
        jRegionArray.set(i, regionData[i].toJSON());
    json.set("regionData", jRegionArray);
    //JSONDocument regionData = JSONDocument.createArray();
    //regionData.set(0, regionData.get(0));
    //json.set("regionData", regionData);

    JSONDocument jPriceArray = JSONDocument.createArray();
    for(int i = 0; i < EnumFood.SIZE; i++) //food Price iteration
      jPriceArray.setNumber(i, foodPrice[i]);
    json.set("foodPrice", jPriceArray);
    //JSONDocument foodPrice = JSONDocument.createArray();
    //foodPrice.set(0, foodPrice.get(0));
    //json.set("foodPrice", foodPrice);

    //TODO Make clear JSON arrays work
    return json;
  }
}
