package starvationevasion.sim.events;

import java.util.ArrayList;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.SpecialEventData.EnumSpecialEvent;
import starvationevasion.common.Util;
import starvationevasion.sim.Model;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;
import starvationevasion.sim.events.AbstractEvent.EventGraph.EventNode;

   

public class EventDriver 
{
 public static  ArrayList<AbstractEvent> eventList = new ArrayList<>();	
 private Model model;
 
 public EventDriver(Model model)
 {
  this.model=model;	 
	 
 }
 
 private void initEvents()
 {
	 
 }
 
 
 
 private void addRandomEvents(int numberOfRandomEvents)
 { 
	 float eventChoice;
	 
	 int regionChoice;
	 int terriotyChoice;
	 int eventIndex;
	 
	 
	 Region tmpRegion; 
	 Territory tmpTerritory;
	 EnumRegion tmpEnumRegion;
	 int counter = 0;
	 while(counter != numberOfRandomEvents)
	 {
		 eventChoice=Util.rand.nextFloat();
		 
		 EnumRegion[] regionEnums = EnumRegion.values();
		 regionChoice = Util.rand.nextInt(EnumRegion.SIZE);
		 tmpEnumRegion = regionEnums[regionChoice];
		 tmpRegion = model.getRegion(tmpEnumRegion);
		 
		 terriotyChoice = Util.rand.nextInt(tmpRegion.getTerritoryList().size());
		 tmpTerritory = tmpRegion.getTerritoryList().get(terriotyChoice);
		 
		 eventIndex = (int) (eventChoice * 10);
		 
		 
		// determine what events to add to EventsList
		 switch(eventIndex)
		 {
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
