package starvationevasion.sim.events;

import java.util.ArrayList;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.CropData;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;


// the Fire event destorys land tiles and also farm equitment 
public class Fire extends AbstractEvent
{
  Territory landArea;
  LandTile startTile;
  ArrayList <LandTile> tiles;
  public Fire(Territory landArea, Region region, CropData cropData, int duration, String eventType)
  {
	 
    super(landArea, region, cropData, duration, eventType);
    this.landArea = landArea;
    //create event graph and populate the affected tiles
    getEventSpread(0.9f, Util.rand.nextInt(10) + 1);
    
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }
  
  public void applyEffects()
  {
	  destroyFarmEquipment();
	  wipeOutLandTiles();
	  super.applyEffects();	 
	  
  }
  
  
}
