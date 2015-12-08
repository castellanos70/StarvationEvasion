package starvationevasion.sim;


import starvationevasion.common.*;


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
      for (LandTile tile : territory.getLandTiles())
      {
        tile.setRainfall(tile.getRainfall() / 3);
      }
    }

    super.applyEffects();
  }

  public MapPoint getLocation()
  {
    // This should be made more accurate
    for (Territory t : landArea.getTerritories())
    {
      return t.getCapitolLocation();
    }
    return new MapPoint(0, 0);
  }
}
