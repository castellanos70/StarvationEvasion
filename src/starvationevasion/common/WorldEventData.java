package starvationevasion.common;
import java.io.Serializable;
import java.util.ArrayList;


public class WorldEventData implements Serializable
{

  public enum EnumWorldEvent
  {
    BLIGHT, DROUGHT, FIRE, FLOOD, HURRICANE, WAR
  }

  public EnumWorldEvent type;
  public int year;
  public EnumRegion region;
  public ArrayList<Location> locationList = new ArrayList<>();
}
