package starvationevasion.GuiTestCode;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * 
 * @author Christian Seely
 * 
 * Test program to demonstrate a possible way of visualizing data
 * for each region. 
 * This is the main entry point and launches a picture of the map with
 * a single regional icon. 
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {		
		try {
	    //The first screen will display the map. 
		Parent root = FXMLLoader.load(getClass().getResource("fxml/Map.fxml"));	
		String image = Main.class.getResource("/images/map.png").toExternalForm();
		root.setStyle("-fx-background-image: url('" + image + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	} catch(Exception e) {
		e.printStackTrace();
	}
	}
	public static void main(String[] args) {
		launch(args);
	}
}














































