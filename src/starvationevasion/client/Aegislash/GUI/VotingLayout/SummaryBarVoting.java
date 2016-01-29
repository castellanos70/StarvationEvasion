package starvationevasion.client.Aegislash.GUI.VotingLayout;

import starvationevasion.client.Aegislash.GUI.GUI;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by jmweisburd on 12/1/15.
 */
public class SummaryBarVoting extends StackPane
{
  GUI gui;
  Label temp;

  public SummaryBarVoting(GUI gui)
  {
    this.gui = gui;

    this.getStylesheets().add("/starvationevasion/client/Aegislash/GUI/VotingLayout/style.css");
    this.getStyleClass().add("summarybar");

    temp = new Label("SummaryBar");

    this.getChildren().add(temp);
  }
}
