package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Earthquake extends AbstractEvent
{

  public Earthquake(Territory landArea, int duration)
  {
    super(landArea, duration);
    // TODo Auto-generated constructor stub
  }

  @Override
  public MapPoint getLocation()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  public void applyEffects()
  {
    destroyInfrastructure();
    super.applyEffects();
  }

}
