package starvationevasion.sim.events;

import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.CropData;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

import java.util.ArrayList;


// the Fire event destorys land tiles and also farm equitment 
public class Fire extends AbstractEvent
{
  Territory landArea;
  LandTile startTile;
  ArrayList <LandTile> tiles;
  public Fire(Territory landArea, Region region, CropData cropData, int duration)
  {
	 
    super(landArea, region, cropData, duration);
    this.landArea = landArea;
    //create event graph and populate the affected tiles
    this.getEventSpread(0.5f, Util.rand.nextInt(10) + 1);
    
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
