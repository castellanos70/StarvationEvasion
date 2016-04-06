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
    super(EnumSpecialEvent.HURRICANE, landArea, 4);
  }

  public void applyEffects()
  {
    Territory landArea = getLandArea();
    for (LandTile tile : landArea.getLandTiles())
    {
      //tile.setRainfall(tile.getRainfall()*3);
    }
    super.applyEffects();
  }

  public MapPoint getLocation()
  {
    return getLandArea().getCapitolLocation();
  }
}
