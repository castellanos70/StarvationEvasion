package starvationevasion.sim.events;

import java.util.ArrayList;

import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;
import starvationevasion.common.Util;
import starvationevasion.sim.LandTile;
import starvationevasion.sim.Territory;

public class Fire extends AbstractEvent
{
  Territory landArea;
  LandTile startTile;
  ArrayList <LandTile> tiles;
  public Fire(Territory landArea, int duration)
  {
	 
    super(landArea, duration);
    this.landArea = landArea;
    this.getEventSpread(0.5f, 1);
    
  }

  @Override
  public MapPoint getLocation()
  {
    return null;
  }
  
  public void applyEffects()
  {
	  destroyFarmEquipment();
	  wipeOutLandTiles(0.7);
	  super.applyEffects();
	  
	  // do something from the abstract class
	  
  }
}
