package starvationevasion.sim.events;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Territory;


/**
 * A hurricane special event
 */
public class Hurricane extends AbstractEvent
{
  public Hurricane(Territory landArea)
  {
    super(landArea, 1);
  }

  public void applyEffects()
  {
    super.applyEffects();
    this.causeFlood();
    
  }

  public MapPoint getLocation()
  {
    return getLandArea().getCenter();
  }
}
