package starvationevasion.client.GUI.VotingLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import starvationevasion.client.GUI.Clock;
import starvationevasion.client.GUI.GUI;

/**
 * Created by jmweisburd on 12/1/15.
 */
public class VotingTimer extends StackPane
{
  GUI gui;
  Label temp;
  public Clock voteTimer = new Clock();

  public VotingTimer(GUI gui)
  {

    this.getChildren().add(voteTimer);
  }

}
