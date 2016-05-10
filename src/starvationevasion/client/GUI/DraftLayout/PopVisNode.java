package starvationevasion.client.GUI.DraftLayout;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import starvationevasion.client.GUI.GUI;

public class PopVisNode extends BorderPane{

	  Text title;
	  GUI gui;

	  public PopVisNode(GUI gui)
	  {
	    this.gui = gui;
	    title = new Text("Populations");
	    title.setFont(Font.font(null, FontWeight.BOLD, 15));

	    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
	    this.getStyleClass().add("popVisNode");	    
	 //   this.getChildren().add(new ImageView(gui.getImageGetter().getCoin()));
	  //  this.getChildren().add(tempText);
	    this.setCenter(title);
	    //this.setLeft(new ImageView(gui.getImageGetter().getCoin()));
	    ImageView pop = new ImageView(gui.getImageGetter().getPopIcon());
	    this.setAlignment(pop, Pos.CENTER_RIGHT);
	    this.setRight(pop);
	    
	  //  this.getChildren().add(1,new ImageView(gui.getImageGetter().getCoin()));
	    //this.getChildren().
//	    
	    this.setOnMouseClicked(new EventHandler<MouseEvent>()
	    {
	      @Override
	      public void handle(MouseEvent event)
	      {
	        if(gui.getPopupManager().isOpen()==null)
	        {
	    	gui.getDraftLayout().getGraphDisplay().setDataVisMode(3);
	        gui.getPopupManager().toggleGraphDisplay();
	        }
	      }
	    });
	  
	  }
}
