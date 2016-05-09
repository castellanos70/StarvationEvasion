package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

public class Flood extends AbstractEvent
{

  public Flood(Territory landArea, Region region, CropData cropData, int duration)
  {
    super(landArea, region, cropData, duration);
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }

  public void applyEffects()
  {
    wipeOutLandTiles(0.5);
    super.applyEffects();
  }
}
