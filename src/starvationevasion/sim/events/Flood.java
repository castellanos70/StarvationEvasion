package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

//Flood event wipes out land tiles.  can be created by a Hurricane event
public class Flood extends AbstractEvent
{

  public Flood(Territory landArea, Region region, CropData cropData, int duration)
  {
    super(landArea, region, cropData, duration);
    this.getEventSpread(0.5f, Util.rand.nextInt(10) + 1);

  }

  public void applyEffects()
  {
    wipeOutLandTiles();
    super.applyEffects();
  }

  @Override
  public MapPoint getLocation()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
