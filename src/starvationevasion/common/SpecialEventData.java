package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

import java.util.ArrayList;
import java.util.List;


public class SpecialEventData implements JSON
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

  public SpecialEventData (String name)
  {
    eventName = name;
  }


  public void setType (EnumSpecialEvent type)
  {
    this.type = type;
  }

  public void setYear (int year)
  {
    this.year = year;
  }

  public void setMonth(EnumMonth month) {this.month = month; }

  public void setDollarsInDamage (long dollarsInDamage)
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

  @Override
  public String toJSONString ()
  {
    return toJSON().toString();
  }

  @Override
  public JSONDocument toJSON ()
  {

    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setString("event-name", eventName);
    json.setNumber("latitude", latitude);
    json.setNumber("longitude", longitude);
    json.setNumber("severity", severity);
    json.setNumber("damage-in-dollars", dollarsInDamage);
    json.setString("type", type.toString());
    json.setNumber("year", year);
    json.setString("month", month.toString());
    json.setNumber("duration-in-months", durationInMonths);

    JSONDocument _locationArray = JSONDocument.createArray(locationList.size());
    for (int i = 0; i < locationList.size(); i++)
    {
      _locationArray.set(i, locationList.get(i).toJSON());
    }
    json.set("locationList", _locationArray);

    JSONDocument _regionsArray = JSONDocument.createArray(regions.size());
    for (int i = 0; i < regions.size(); i++)
    {
      _regionsArray.setString(i, regions.get(i).toString());
    }
    json.set("regions", _regionsArray);

    return json;
  }

  public SpecialEventData(JSONDocument json)
  {
    eventName = json.getString("event-name");
    latitude = (float) json.getNumber("latitude");
    longitude = (float) json.getNumber("longitude");
    severity = (float) json.getNumber("severity");
    dollarsInDamage = (long) json.getNumber("damage-in-dollars");
    type = EnumSpecialEvent.valueOf(json.getString("type"));
    year = (int) json.getNumber("year");
    month = EnumMonth.valueOf(json.getString("month"));
    durationInMonths = (int) json.getNumber("duration-in-months");

    List<Object> jLocParse = json.get("locationList").array();
    for (int i = 0; i < jLocParse.size(); i++)
      locationList.add(new MapPoint((JSONDocument) jLocParse.get(i)));

    List<Object> jRegionParse = json.get("regions").array();
    for (int i = 0; i < jRegionParse.size(); i++)
      regions.add(EnumRegion.valueOf((String)jRegionParse.get(i)));
  }
  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    if(!(o instanceof SpecialEventData))
      return false;
    SpecialEventData comp = (SpecialEventData) o;
    if(!comp.eventName.equals(this.eventName))
      return false;
    if(Float.compare(comp.latitude, this.latitude) != 0)
      return false;
    if(Float.compare(comp.longitude, this.longitude) != 0)
      return false;
    if(Float.compare(comp.severity, this.severity) != 0)
      return false;
    if(comp.dollarsInDamage != this.dollarsInDamage)
      return false;
    if(comp.type.ordinal()!= this.type.ordinal())
      return false;
    if(comp.year != this.year)
      return false;
    if(comp.month.ordinal()!= this.month.ordinal())
      return false;
    if(comp.durationInMonths != this.durationInMonths)
      return false;
    if(comp.locationList.size() != this.locationList.size())
      return false;
    for(int i = 0; i < this.locationList.size(); i++)
      if(!comp.locationList.get(i).equals(this.locationList.get(i)))
        return false;
    if(comp.regions.size() != this.regions.size())
      return false;
    for(int i = 0; i < this.regions.size(); i++)
      if(comp.regions.get(i).ordinal() != this.regions.get(i).ordinal())
        return false;
    return true;
  }
}
