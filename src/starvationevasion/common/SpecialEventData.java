package starvationevasion.common;
import java.io.Serializable;
import java.util.ArrayList;


public class SpecialEventData implements Serializable
{

  public enum EnumSpecialEvent
  {
    BLIGHT, DROUGHT, FIRE, FLOOD, HURRICANE, WAR
  }

  public enum EnumMonth
  { January, February, March, April, May, June,
    July, August, September, October, November, December
  }


  public EnumSpecialEvent type;
  public int year;
  public EnumMonth month;
  public int durationInMonths;
  public EnumRegion region;
  public ArrayList<MapPoint> locationList = new ArrayList<>();


  /**
   * @return Data stored in this structure as a formatted String.
   */
  public String toString()
  {
    String msg = type + "["+year+":"+month+" -> " + durationInMonths + "months]: " + region;
    return msg;
  }
}
