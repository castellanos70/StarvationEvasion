package GUIandDataVisTeamTestPackage;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author Christian Seely
 * 
 * Controller class associated with the Map.fxml 
 * Initializes and displays the map. 
 *
 */
public class MapController{
	@FXML AnchorPane pane;
	@FXML Button iconButton;
	//Get the images. 
	ImageView map = new ImageView(new Image(getClass().getResourceAsStream("/GUIandDataVisTeamTestPackage/map.png")));	
	ImageView pieChartIcon = new ImageView(new Image(getClass().getResourceAsStream("/GUIandDataVisTeamTestPackage/pieChartIcon.png")));
	
	/**
	 * You cannot have constructors inside of controller classes as
	 * the constructor is called prior to the FXML objects being 
	 * initialized. To fix this there is a method called initialize this
	 * gets called automatically if created and acts like a constructor
	 * but is called after the FXML objects are instantiated. This method
	 * initializes fields required for the map. 
	 */
	public void initialize()
	{
		//Make the button round.
		iconButton.setStyle(
	            "-fx-background-radius: 5em; " +
	                    "-fx-min-width: 3px; " +
	                    "-fx-min-height: 3px; " +
	                    "-fx-max-width: 3px; " +
	                    "-fx-max-height: 3px;"
	            );
		//Set the image. 
		iconButton.setGraphic(pieChartIcon);
 	
	}
	
	/**
	 * If the icon button is clicked then invoke the other stage
	 * that displays the data visualization. This method is called via
	 * the FXML onAction command for the icon button.
	 * 
	 *  So a problem with making a transparent scene is it takes
	 *  away the ability to exit (fixed) and move the screen (TODO)
	 */
	public void invokeOtherStage()
	{
		try {
			Stage newStage = new Stage();
			//Make the stage transparent. 
			newStage.initStyle(StageStyle.TRANSPARENT);			
			Parent root = FXMLLoader.load(getClass().getResource("/GUIandDataVisTeamTestPackage/DataVis.fxml"));
			Scene scene = new Scene(root);
			//Again needed for making the window
			//transparent. 
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newStage.setScene(scene);
			newStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	


}
