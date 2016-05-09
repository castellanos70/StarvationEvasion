package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.Territory;

public class Flood extends AbstractEvent
{

  public Flood(Territory landArea, int duration)
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
    wipeOutLandTiles(0.5);
    super.applyEffects();
  }
}
