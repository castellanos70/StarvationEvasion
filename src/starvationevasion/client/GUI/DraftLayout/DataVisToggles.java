package starvationevasion.client.GUI.DraftLayout;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import starvationevasion.client.GUI.GUI;
import javafx.scene.layout.StackPane;
public class DataVisToggles extends HBox{
	GUI gui;
	StackPane mapPane;
	private int toggleRegionsCount = 0;
	/**
	 * 
	 * @param gui Control
	 * Create button to toggle region boarders on and off.
	 */
	DataVisToggles(GUI gui) 
	{
		this.gui = gui;
		Button toggleRegions = new Button("Toggle Region Boarders");
	    
	    toggleRegions.setOnAction(new EventHandler<ActionEvent>()
	    {
	    
	        @Override
	        public void handle(ActionEvent event)
	        {
	        
	        
	        if(toggleRegionsCount%2==0)
	        {
	            Image imageMap=gui.getImageGetter().getWorldMap();
	            ImageView worldMap=new ImageView(imageMap); 
	            mapPane.getChildren().add(worldMap);
	        }
	        else
	        {
	            Image imageMap=gui.getImageGetter().getRegionWorldMap();
	            ImageView worldMap=new ImageView(imageMap); 
	            mapPane.getChildren().add(worldMap);
	        }
	        ++toggleRegionsCount;

	        }   
	 });
	
	   //To be implemented:
	   // Button toggleClimateView = new Button ("Toggle Climate View");
	   // Button toggleMostImportantCropViewer = new Button ("Toggle Important Regional Crop Production Values");
	    
	    this.getChildren().addAll(toggleRegions);//,toggleClimateView,toggleMostImportantCropViewer);
	}
	

	/**
	 * 
	 * @param mapPane Obtain Pane containing world map. 
	 */
	public void setWorldPaneControl(StackPane mapPane)
	{
		this.mapPane = mapPane;
	}
	
	
	
	
}
