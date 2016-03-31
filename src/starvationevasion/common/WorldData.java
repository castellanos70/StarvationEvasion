package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * This structure is populated and returned by the
 * {@link starvationevasion.sim.Simulator#nextTurn(ArrayList)} method.
 * It contains all world and region data to be returned to the client after each turn.
 * It does not contain high resolution location data possibly needed by the visualizer.
 */
public class WorldData implements JSON
{

  /**
   * Year to which this WorldData applies.
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
      msg += String.format("%s:%.0f", food, foodPrice[food.ordinal()]);
      if (food != EnumFood.DAIRY)
      {
        msg += ", ";
      }
      else
      {
        msg += "]\n";
      }
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


  @Override
  public String toJSONString()
  {
    return toJSON().toString();
  }

  @Override
  public JSONDocument toJSON()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setNumber("year", year);
    json.setNumber("sealevel", seaLevel);

    JSONDocument _eventArray = JSONDocument.createArray(eventList.size());
    for (int i = 0; i < eventList.size(); i++)
    {
      _eventArray.set(i, eventList.get(i).toJSON());
    }

    JSONDocument _regionArray = JSONDocument.createArray(regionData.length);
    for (int i = 0; i < regionData.length; i++)
    {
      _regionArray.set(i, regionData[i].toJSON());
    }

    JSONDocument _foodPriceArray = JSONDocument.createArray(foodPrice.length);
    for (int i = 0; i < foodPrice.length; i++)
    {
      _foodPriceArray.setNumber(i, foodPrice[i]);
    }


    json.set("events", _eventArray);
    json.set("food-prices", _foodPriceArray);
    json.set("regions", _regionArray);


    return json;
  }

  public WorldData(JSONDocument json)
  {
    year = (int) json.getNumber("year");
    seaLevel = (double) json.getNumber("sealevel");

    List<Object> eventArray = json.get("events").array();
    for (int i = 0; i < eventArray.size(); i++)
    { eventList.add(new SpecialEventData((JSONDocument) eventArray.get(i))); }

    List<Object> foodPriceArray = json.get("food-prices").array();
    for (int i = 0; i < foodPriceArray.size(); i++)
    { foodPrice[i] = (double) foodPriceArray.get(i); }

    List<Object> regionArray = json.get("regions").array();
    for (int i = 0; i < regionArray.size(); i++)
    { regionData[i] = new RegionData((JSONDocument) regionArray.get(i)); }
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof WorldData)) return false;
    WorldData comp = (WorldData) o;
    if (comp.year != this.year) return false;
    if (Double.compare(comp.seaLevel, this.seaLevel) != 0) return false;
    if (comp.eventList.size() != this.eventList.size()) return false;
    for (int i = 0; i < eventList.size(); i++)
    {
      if (!comp.eventList.get(i).equals(this.eventList.get(i))) return false;
    }
    for (int i = 0; i < foodPrice.length; i++)
    {
      if (Double.compare(comp.foodPrice[i], this.foodPrice[i]) != 0) return false;
    }
    for (int i = 0; i < regionData.length; i++)
    {
      if (!comp.regionData[i].equals(this.regionData[i])) return false;
    }
    return true;
  }
}
