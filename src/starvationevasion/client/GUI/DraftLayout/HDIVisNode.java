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

public class HDIVisNode extends BorderPane {
	  Text title;
	  GUI gui;

	  public HDIVisNode(GUI gui)
	  {
	    this.gui = gui;
	    title = new Text("Regional HDI");
	    title.setFont(Font.font(null, FontWeight.BOLD, 15));

	    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
	    this.getStyleClass().add("hdi");	    
	 //   this.getChildren().add(new ImageView(gui.getImageGetter().getCoin()));
	  //  this.getChildren().add(tempText);
	    this.setCenter(title);
	    //this.setLeft(new ImageView(gui.getImageGetter().getCoin()));
	    ImageView hdi = new ImageView(gui.getImageGetter().getHDIIcon());
	    this.setAlignment(hdi, Pos.CENTER_RIGHT);
	    this.setRight(hdi);
	    
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
	    	gui.getDraftLayout().getGraphDisplay().setDataVisMode(4);
	        gui.getPopupManager().toggleGraphDisplay();
	        }
	      }
	    });
	  
	  }
}
