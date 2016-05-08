package starvationevasion.sim.events;


import java.util.ArrayList;

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
	
  private LandTile startTile;
  private ArrayList <LandTile> tiles;	
  private EnumSpecialEvent eventType;
  private int duration;
  private Territory landArea;
  private ArrayList<LandTile> effectedTiles;
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
    this.landArea = landArea;
    tiles = landArea.getLandTiles();
    startTile = tiles.get(Util.rand.nextInt(tiles.size()));
    effectedTiles .add(startTile);
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
    
  }
  
  private void destroyInfrastrucure()
  {
    
  }
  
  private void causeFlood()
  {
    
  }
  
  private void wipeOutLandTiles(double percentToWipe)
  {
    
  }
  
  private void reduceRainFall()
  {
    
  }
  
  //graph data structure to keep track of event spreading through a territory
  
  class EventGraph
  {
	  
	  private ArrayList <LandTile> notVisted;
	  private LandTile root;
	  private ArrayList<EventNode> graph;
	  private ArrayList<LandTile> tiles;
	  
	  public EventGraph(ArrayList<LandTile> tiles)
	  {
		 this.tiles = tiles;
		 for(LandTile tile : tiles)
		 {
			 graph.add(new EventNode(tile));
		 }
	  }
	  
	  private void assignNeighbors()
	  {
		  ArrayList<EventNode> tmpNieghbors = new ArrayList<EventNode>();
		  EventNode curNode; 
		  for(EventNode node : graph)
		  {
			  for(int i = 0; i< graph.size(); i++)
			  {
				  curNode= graph.get(i);
				  LandTile tmp =curNode.landTile;
				  if(tmp != node.landTile)
				  {
					  float tmpLat =tmp.getLatitude();
					  float tmpLong =tmp.getLongitude();
					  
					  float tileLat = node.landTile.getLatitude();
					  float tileLong = node.landTile.getLongitude();
					 
					  // need to change 100 to actual size of landtile
					  if(distanceBetweenLandTiles(tileLat, tileLong, tmpLat,tmpLong) <= 100 )
					  {
						 
						  tmpNieghbors.add(curNode);
						  
					  }
					 
				  }
			  }
			  node.setNieghbors(tmpNieghbors);
			  tmpNieghbors.clear();
			  
		  }
		  
	  }
	  
	  /*
	   * This method returns the distance in kilometers between the centers of landtiles.
	   * this method uses the Haversine Formula to calculate distance. 
	   * 
	   * this is where i found the formula  http://andrew.hedges.name/experiments/haversine/
	   * 
	   * 
	   * 
	   * */
	  
	  private double distanceBetweenLandTiles(float lat1,float long1, float lat2 , float long2)
	  {
		  double distance = 0;
		  double R = 6373.0; //radius of the world 
		  
		  float dlon = long2 - long1 ;
		  float dlat = lat2 - lat1 ;
	   	  double a =  (Math.sin(dlat/2)) * (Math.sin(dlat/2))  + Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon/2)) * (Math.sin(dlon/2));
		  double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) ) ;
		  distance = R * c ;
		  
		  return distance;
		  
	  }
	  private void traverseAdjacent()
	  {
		 
		  
	  }
	  
	  class EventNode
	  {
         LandTile landTile;
		 ArrayList <EventNode> nieghbors;
		 boolean effected=false;
		 
		 public EventNode(LandTile landTile)
		 {
			this.landTile = landTile;
		 }
		 public void setNieghbors(ArrayList <EventNode> nieghbors)
		 {
			 this.nieghbors= nieghbors;
			
		 }
		 public void setEffected(boolean state)
		 {
			 effected= state;
		 }
		 
		 public void spreadEvent()
		 {
			 
		 }
		 
	  }
	  
  }
}
