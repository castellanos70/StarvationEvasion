package starvationevasion.client.GUI.DraftLayout;

import starvationevasion.client.GUI.GUI;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * VotingGraphNode is the GUI element responsible for allowing the player to view graphs about the selected region
 * Graphs able for view for a selected region:
 * Population
 * HDI
 * Balance of farming sector government revenue
 * Total Cost and Total Revenue of each of the 12 farm products
 *
 * Currently the Total Cost and Total Revenue Graph for each of the 12 farm products are not
 */
public class GraphNode extends StackPane
{
  Text tempText;
  GUI gui;

  public GraphNode(GUI gui)
  {
    this.gui = gui;
    tempText = new Text("Global Statistics");
    tempText.setFont(Font.font(null, FontWeight.BOLD, 15));

    this.getStylesheets().add("/starvationevasion/client/GUI/DraftLayout/style.css");
    this.getStyleClass().add("graphnode");
    this.getChildren().add(tempText);

    this.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        if(gui.getPopupManager().isOpen()==null)
        {
          if(gui.getPopupManager().isOpen()==null)
          {
          gui.getDraftLayout().getGraphDisplay().setDataVisMode(1);
          gui.getPopupManager().toggleGraphDisplay();
          }
        }
      }
    });
  }

}
