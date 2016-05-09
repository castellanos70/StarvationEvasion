package starvationevasion.sim.events;


import starvationevasion.common.*;
import starvationevasion.sim.*;


/**
 * A drought special event
 */
public class Drought extends AbstractEvent
{
  Region region;
  public Drought(Region landArea)
  {
    super(landArea, 4);
    region = landArea;
  }

  public void applyEffects()
  {
    reduceRainFall();
    super.applyEffects();
  }

  public MapPoint getLocation()
  {
    return region.getCenter();
  }
}
