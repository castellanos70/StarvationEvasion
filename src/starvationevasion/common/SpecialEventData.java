package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;

import java.io.Serializable;
import java.util.ArrayList;


public class SpecialEventData implements Serializable
{

  public enum EnumSpecialEvent
  {
    Blight, Drought, Fire, Flood, Freeze, Heat, Hurricane, Insects, War, Volcano, Earthquake, Tornado;
    public static final int SIZE = values().length;
  }

  public enum EnumMonth
  {
    January, February, March, April, May, June,
    July, August, September, October, November, December
  }


  public float latitude;
  public float longitude;

  /**
   * This value describes the severity of the event on a continuous
   * interval [0.0, 1.0]. The severity of the event increases approaching 1.0.
   */
  public float severity;
  public long dollarsInDamage;
  public EnumSpecialEvent type;
  public String eventName;
  public int year;
  public EnumMonth month;
  public int durationInMonths;
  public ArrayList<MapPoint> locationList = new ArrayList<>();
  public ArrayList<EnumRegion> regions = new ArrayList<>();

  public SpecialEventData(String name)
  {
    eventName = name;
  }


  public void setType(EnumSpecialEvent type)
  {
    this.type = type;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public void setDollarsInDamage(long dollarsInDamage)
  {
    this.dollarsInDamage = dollarsInDamage;
  }

  public void setSeverity(float severity)
  {
    this.severity = severity;
  }

  public void setDurationInMonths(int durationInMonths)
  {
    this.durationInMonths = durationInMonths;
  }

  public void setLatitude(float latitude)
  {
    this.latitude = latitude;
  }

  public void setLongitude(float longitude)
  {
    this.longitude = longitude;
  }

  public void addRegion(EnumRegion region)
  {
    regions.add(region);
  }

  public int regionsAffected()
  {
    return regions.size();
  }

  @Override
  public String toString()
  {
    String str = "";
    str += "Event " + eventName + "\n";
    str += "\tYear   : " + year + "\n";
    str += "\tActionType   : " + type.name() + "\n";
    str += "\tRegions: \n";
    for (EnumRegion region : regions)
    {
      str += "\t\t" + region.name() + "\n";
    }

    return str;
  }

  public JSONDocument toJSON()
  { //TODO Match all enum formats together
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setString("eventName", eventName);
    json.setNumber("latitude", latitude);
    json.setNumber("longitude", longitude);
    json.setNumber("severity", severity);
    json.setNumber("dollarsInDamage", dollarsInDamage);
    json.setNumber("enumSpecialEvent", type.ordinal());
    json.setNumber("year", year);
    json.setNumber("enumMonth", month.ordinal());
    json.setNumber("durationInMonths", durationInMonths);

    JSONDocument locationList = JSONDocument.createArray();
    locationList.set(0, locationList.get(0));
    json.set("locationList", locationList);

    JSONDocument regions = JSONDocument.createArray();
    regions.set(0, regions.get(0));
    json.set("regions", regions);

    //TODO Make clear JSON arrays work
    return json;
  }


}
