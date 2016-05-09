package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Blight extends AbstractEvent
{

  public Blight(Territory landArea, int duration)
  {
    super(landArea, duration);
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }

  public void applyEffects()
  {
	  
	  
  }
  
  
}
