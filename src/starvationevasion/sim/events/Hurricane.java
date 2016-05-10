package starvationevasion.sim.events;

import starvationevasion.common.MapPoint;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;


/**
 * A hurricane special event
 */
public class Hurricane extends AbstractEvent
{
  private boolean isFirstYear = true;
  
  public Hurricane(Territory landArea, Region region, CropData cropData, int duration)
  {
    super(landArea, region, cropData, duration);
  }

  public void applyEffects()
  {
    if(isFirstYear) 
    {
      causeFlood();
      isFirstYear = false;
    }
    destroyInfrastructure();
    super.applyEffects();    
  }

  public MapPoint getLocation()
  {
    return getLandArea().getCenter();
  }
}
