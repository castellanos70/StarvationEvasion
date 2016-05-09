package starvationevasion.sim.events;


import java.util.ArrayList;
import java.util.List;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Territory;


/**
 * Defines a special event. Takes a land area type and applies effects to the land region based on event.
 *
 * T is the type of area the event effects (territory, region, etc...)
 */
public abstract class AbstractEvent
{
  private EnumSpecialEvent eventType;
  private int duration;
  private Territory landArea;

  /**
   * Creates a special event object
   *
   * @param eventType ActionType of event this will be
   * @param landArea The land area (Territory, Region, etc...) this event effects
   * @param duration How many simulator years this event lasts
   */
  public AbstractEvent(EnumSpecialEvent eventType, Territory landArea, int duration)
  {
    this.eventType = eventType;
    this.landArea = landArea;
    this.duration = duration;
  }

  /**
   * Applies effects of the event to the to the landArea. This assumes the event lasts one year (due to the
   * resolution of the simulator). When the effect is applied then the duration is decreased by 1 year.
   */
  public void applyEffects()
  {
    duration -= 1;
  }

  /**
   * Return the longitue and latitude location of the event. This will usually be the center of the land area.
   *
   * @return latitude and longitude stored in a MapPoint.
   */
  public abstract MapPoint getLocation();

  public EnumSpecialEvent getEventType()
  {
    return eventType;
  }

  public int getDuration()
  {
    return duration;
  }

  public Territory getLandArea()
  {
    return landArea;
  }

  private void destroyFarmEquipment()
  {
    for(LandTile tile : landArea.getLandTiles())
    {
      if(tile.getProductionMultiplier() >= 0.3)
      {
        tile.setProductionMultiplier(tile.getProductionMultiplier() - 0.3);
      }
      else tile.setProductionMultiplier(0);
    }
  }
  
  private void destroyInfrastrucure()
  {
    for(LandTile tile : landArea.getLandTiles())
    {
      if(tile.getProductionMultiplier() >= 0.3)
      {
        tile.setProductionMultiplier(tile.getProductionMultiplier() - 0.3);
      }
      else tile.setProductionMultiplier(0);
    }
  }
  
  private void causeFlood()
  {
    Flood flood = new Flood(eventType, landArea, duration);
    //TODO: add to events list.
  }
  
  private void wipeOutLandTiles(double percentToWipe)
  {
    List<LandTile> tilesToWipe = landArea.getLandTiles();
    int amtToWipe = (int) (tilesToWipe.size() * percentToWipe);
    int numWiped = 0;
    while(numWiped < amtToWipe)
    {
      tilesToWipe.remove(Util.rand.nextInt(tilesToWipe.size())).setProductionMultiplier(0);
    }
    
  }
  
  private void wipeOutCrop(EnumFood crop)
  {
    for(LandTile tile : landArea.getLandTiles())
    {
      if(tile.getClass().equals(crop))
      {
        tile.setProductionMultiplier(0);
      }
    }
    
  }
  
  private void reduceRainFall()
  {
    
  }
}
