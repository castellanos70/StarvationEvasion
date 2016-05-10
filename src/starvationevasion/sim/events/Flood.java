package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
<<<<<<< HEAD
import starvationevasion.common.Util;
=======
>>>>>>> 40f41cfe4934958536978f099d51e12aa4c3b952
import starvationevasion.sim.CropData;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

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
