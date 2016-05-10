package starvationevasion.sim.events;

import starvationevasion.common.MapPoint;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

public class Earthquake extends AbstractEvent
{

  public Earthquake(Territory landArea, Region region, CropData cropData, int duration)
  {
    super(landArea, region, cropData, duration);
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
