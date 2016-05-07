package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Flood extends AbstractEvent
{

  public Flood(EnumSpecialEvent eventType, Territory landArea, int duration)
  {
    super(eventType, landArea, duration);
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }

}
