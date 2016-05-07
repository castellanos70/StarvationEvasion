package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Fire extends AbstractEvent
{

  public Fire(EnumSpecialEvent eventType, Territory landArea, int duration)
  {
    super(eventType, landArea, duration);
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }

}
