package starvationevasion.common;
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
  { January, February, March, April, May, June,
    July, August, September, October, November, December
  }


  public float latitude;
  public float longitude;

  /**
   *  This value describes the severity of the event on a continuous
   *  interval [0.0, 1.0]. The severity of the event increases approaching 1.0.
   *
   */
  public float severity;
  public long   dollarsInDamage;
  public EnumSpecialEvent type;
  public String eventName;
  public int year;
  public EnumMonth month;
  public int durationInMonths;
  public ArrayList<MapPoint> locationList = new ArrayList<>();
  public ArrayList<EnumRegion> regions    = new ArrayList<>();

  public SpecialEventData(String name)
  {
    eventName = name;
  }

  /**
   * @return Data stored in this structure as a formatted String.
   */
  /*
  public String toString()
  {
    String msg = type + "["+year+":"+month+" -> " + durationInMonths + "months]: " + regions.toString();
    return msg;
  }*/

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
    str += "Event "+ eventName + "\n";
    str += "\tYear   : "+year+"\n";
    str += "\tType   : "+type.name()+"\n";
    str += "\tRegions: \n";
    for (EnumRegion region : regions)
    {
      str += "\t\t" + region.name() + "\n";
    }

    return str;
  }
}
