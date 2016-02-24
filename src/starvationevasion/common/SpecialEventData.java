package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
<<<<<<< HEAD
=======
import starvationevasion.server.io.JSON;
>>>>>>> server

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SpecialEventData implements Serializable, JSON
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

  public void setDollarsInDamage (long dollarsInDamage)
  {
    this.dollarsInDamage = dollarsInDamage;
  }

  public void setSeverity (float severity)
  {
    this.severity = severity;
  }

  public void setDurationInMonths (int durationInMonths)
  {
    this.durationInMonths = durationInMonths;
  }

  public void setLatitude (float latitude)
  {
    this.latitude = latitude;
  }

  public void setLongitude (float longitude)
  {
    this.longitude = longitude;
  }

  public void addRegion (EnumRegion region)
  {
    regions.add(region);
  }

  public int regionsAffected ()
  {
    return regions.size();
  }

  @Override
  public String toString ()
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

<<<<<<< HEAD
  public JSONDocument toJSON()
  { //TODO Match all enum formats together
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setString("eventName", eventName);
    json.setNumber("latitude", latitude);
    json.setNumber("longitude", longitude);
    json.setNumber("severity", severity);
    json.setNumber("dollarsInDamage", dollarsInDamage);
    json.setNumber("enumType", type.ordinal());
    json.setNumber("year", year);
    json.setNumber("enumMonth", month.ordinal());
    json.setNumber("durationInMonths", durationInMonths);

    JSONDocument jLocArray =  JSONDocument.createArray();
    for(int i = 0; i < locationList.size(); i++)
      jLocArray.set(i, locationList.get(i).toJSON());
    json.set("locationList", jLocArray);

    JSONDocument jRegionArray = JSONDocument.createArray();
    for(int i = 0; i < regions.size(); i++)
      jRegionArray.setNumber(i, regions.get(i).ordinal());
    json.set("regions", jRegionArray);

    //TODO Make clear JSON arrays work
    return json;
  }

  public SpecialEventData(JSONDocument json)
=======
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

  public SpecialEventData (JSONDocument json)
>>>>>>> server
  {
    eventName = json.getString("eventName");
    latitude = (float) json.getNumber("latitude");
    longitude = (float) json.getNumber("longitude");
    severity = (float) json.getNumber("severity");
    dollarsInDamage = (long) json.getNumber("dollarsInDamage");
    type = EnumSpecialEvent.values()[(int) json.getNumber("enumType")];
    year = (int) json.getNumber("year");
    month = EnumMonth.values()[(int) json.getNumber("enumMonth")];
    durationInMonths = (int) json.getNumber("durationInMonths");

    List<Object> jLocParse = json.get("locationList").array();
    for (int i = 0; i < jLocParse.size(); i++)
<<<<<<< HEAD
      locationList.add(new MapPoint((JSONDocument) jLocParse.get(i)));
=======
    {
      locationList.add(new MapPoint((JSONDocument) jLocParse.get(i)));
    }
>>>>>>> server

    //This should produce a list of the ordinals  of EnumRegions for the region list
    List<Object> jRegionParse = json.get("regions").array();
    for (int i = 0; i < jRegionParse.size(); i++)
<<<<<<< HEAD
      regions.add(EnumRegion.values()[(int) jRegionParse.get(i)]);
=======
    {
      regions.add(EnumRegion.values()[(int) jRegionParse.get(i)]);
    }
>>>>>>> server
  }
}
