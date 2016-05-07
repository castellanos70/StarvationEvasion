package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Earthquake extends AbstractEvent
{

  public Earthquake(EnumSpecialEvent eventType, Territory landArea, int duration)
  {
    super(eventType, landArea, duration);
    // TODO Auto-generated constructor stub
  }

  @Override
  public MapPoint getLocation()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
