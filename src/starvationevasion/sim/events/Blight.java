package starvationevasion.sim.events;

import starvationevasion.common.EnumFood;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

public class Blight extends AbstractEvent
{

  EnumFood crop;

  public Blight(Territory landArea, Region region, CropData cropData, int duration, EnumFood crop)

  {
    super(landArea, region, cropData, duration);
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
