package starvationevasion.sim.events;

import java.util.ArrayList;

   

public class EventDriver 
{
 private ArrayList<AbstractEvent> EventList = new ArrayList<>();	

 public EventDriver()
 {
	 
	 
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
