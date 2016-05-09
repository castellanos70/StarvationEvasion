package starvationevasion.sim.events;

import starvationevasion.common.EnumFood;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Blight extends AbstractEvent
{

  EnumFood crop;
  public Blight(Territory landArea, int duration, EnumFood crop)
  {
    super(landArea, duration);
    this.crop = crop;
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }

  public void applyEffects()
  {	  
	  wipeOutCrop(crop);
	  super.applyEffects();
  }
  
  
}
