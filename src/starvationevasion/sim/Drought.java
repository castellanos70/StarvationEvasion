package starvationevasion.sim;


import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;


/**
 * A drought special event
 */
public class Drought extends AbstractEvent<Region>
{
  public Drought(Region landArea)
  {
    super(EnumSpecialEvent.DROUGHT, landArea, 3);
  }

  public void applyEffects()
  {
    super.applyEffects();
  }

  public MapPoint getLocation()
  {
    return new MapPoint(0, 0);
  }
}
