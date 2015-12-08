package starvationevasion.sim.events;


import starvationevasion.common.*;
import starvationevasion.sim.*;


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
    for (Territory territory : landArea.getTerritories())
    {
      for (starvationevasion.sim.LandTile tile : territory.getLandTiles())
      {
        tile.setRainfall(tile.getRainfall() / 3);
      }
    }
    super.applyEffects();
  }

  public MapPoint getLocation()
  {
    return landArea.getCenter();
  }
}
