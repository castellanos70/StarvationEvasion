package starvationevasion.sim.events;

import java.util.ArrayList;
import java.util.List;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.sim.CropData;
import starvationevasion.sim.Model;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

   

public class EventDriver 
{
 public static  ArrayList<AbstractEvent> eventList = new ArrayList<>();	
 private Model model;
 private CropData cropData;
 
 public EventDriver(Model model)
 {
  this.model=model;	 
	this.cropData = model.getCropData();
 }
 
 public void initEvents(int numEvents)
 {
	 addRandomEvents(numEvents);
 }
 
 
 
 private void addRandomEvents(int numberOfRandomEvents)
 { 
	 
	 
	 int regionChoice;
	 int territoryChoice;
	 int eventIndex;
	 
	 
	 Region tmpRegion; 
	 Territory tmpTerritory;
	 EnumRegion tmpEnumRegion;
	 int counter = 0;
	 while(counter != numberOfRandomEvents)
	 {
		
		 
		 EnumRegion[] regionEnums = EnumRegion.values();
		 regionChoice = Util.rand.nextInt(EnumRegion.SIZE);
		 tmpEnumRegion = regionEnums[regionChoice];
		 tmpRegion = model.getRegion(tmpEnumRegion);
		 
		 territoryChoice = Util.rand.nextInt(tmpRegion.getTerritoryList().size());
		 tmpTerritory = tmpRegion.getTerritoryList().get(territoryChoice);
		 
		 eventIndex = Util.rand.nextInt(6);
		 
		 
		// determine what events to add to EventsList
		 switch(eventIndex)
		 {
<<<<<<< HEAD
		 case 0: addEvent(new Drought(tmpTerritory, tmpRegion, cropData, Util.rand.nextInt(5))); break;
		 case 1: addEvent(new Fire(tmpTerritory,null, null, 1)); break;
		 case 2: addEvent(new Hurricane(tmpTerritory, null, null, 3));break;
		 case 3: addEvent(new Earthquake(tmpTerritory, null, null, 5));break;
		 case 4:
		   Region[] regionList = model.getRegionList();
		   List<Territory> allTerritories = new ArrayList<>();
		   List<Territory> possibleBlights = new ArrayList<>();
		   for(int i = 0; i < regionList.length; i++)
		   {
		     allTerritories.addAll(regionList[i].getTerritoryList());
		   }
		   for(int i = 0; i < 20; i++)
		   {
		     possibleBlights.add(allTerritories.remove(Util.rand.nextInt(allTerritories.size())));
		   }
		   for(Territory territory : possibleBlights)
		   {
		     int[] mostPlanted = territory.getMostPlantedCrop();
		     if(mostPlanted[1] / territory.getLandTiles().size() > .7)
		     {
		       EnumFood crop = EnumFood.values()[mostPlanted[0]];
		       addEvent(new Blight(territory, null, cropData, 2, crop));
		     }
		   }		   
		   break;
		 case 5: addEvent(new Flood(tmpTerritory, tmpRegion, cropData, 1));
		
=======
		 case 0: addEvent(new Drought(tmpRegion, tmpRegion, null, counter)); break;
		 case 1: addEvent(new Fire(tmpTerritory,tmpRegion, null, 1)); break;
		 case 2: addEvent(new Hurricane(tmpTerritory, tmpRegion, null, counter));break;
		 case 3: addEvent(new Earthquake(tmpTerritory,tmpRegion, null, 2));break;
		 case 4: addEvent(new Blight(tmpTerritory,tmpRegion, null, 2));break;
		 case 5: break;
		 case 6: break;
		 case 7: break;
		 case 8: break;
		 case 9: break;
>>>>>>> 40f41cfe4934958536978f099d51e12aa4c3b952
		
		 
		 }
		 counter++;	
		 
		 	
		 
		 
	 }
  
 }
 
 
 private void addEvent(AbstractEvent event)
 {
	 eventList.add(event);
 }
  
 public void applyEvents()
 {
	 for (AbstractEvent event : eventList)
	 {
	   if(event.getDuration()> 0)
	   {
		 event.applyEffects();
	   }
	   else
		   eventList.remove(event);
	 }
 }
 
 
 
 
 
}
