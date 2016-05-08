package starvationevasion.sim.events;

import java.util.ArrayList;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.SpecialEventData.EnumSpecialEvent;
import starvationevasion.common.Util;
import starvationevasion.sim.Model;
import starvationevasion.sim.Region;
import starvationevasion.sim.Territory;

   

public class EventDriver 
{
 private ArrayList<AbstractEvent> EventList = new ArrayList<>();	
 private Model model;
 
 public EventDriver(Model model)
 {
  this.model=model;	 
	 
 }
 
 private void initRandomEvents()
 {
	 float eventChoice;
	 
	 int regionChoice;
	 int terriotyChoice;
	 int event;
	 
	 
	 Region tmpRegion; 
	 Territory tmpTerrioty;
	 EnumRegion tmpEnumRegion;
	 EnumRegion[] regionEnums = EnumRegion.values();
	 while((eventChoice=Util.rand.nextFloat()) < 0.8)
	 {
		
		 regionChoice = Util.rand.nextInt(EnumRegion.SIZE);
		 tmpEnumRegion = regionEnums[regionChoice];
		 tmpRegion = model.getRegion(tmpEnumRegion);
		 
		 terriotyChoice = Util.rand.nextInt(tmpRegion.getTerritoryList().size());
		 tmpTerrioty = tmpRegion.getTerritoryList().get(terriotyChoice);
		 
		 event = (int) (eventChoice * 10);
		 switch(event)
		 {
		 	// determine what events to add to Eventslist
		 }
	 }
  
 }
 
 
 
 
 private void addEvent(AbstractEvent event)
 {
	 EventList.add(event);
 }
 
 
 public void applyEvents()
 {
	 for (AbstractEvent event : EventList)
	 {
	   if(event.getDuration()> 0)
	   {
		 event.applyEffects();
	   }
	   else
		   EventList.remove(event);
	 }
 }
 
 
 
 
}
