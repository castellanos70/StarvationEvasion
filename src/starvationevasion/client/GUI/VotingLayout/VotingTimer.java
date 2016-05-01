package starvationevasion.client.GUI.VotingLayout;

import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.ResizeStrategy;
import starvationevasion.client.GUI.Clock;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by jmweisburd on 12/1/15.
 */
public class VotingTimer extends StackPane
{
  GUI gui;
  Label temp;
  public Clock voteTimer = new Clock(this, new ResizeStrategy(0, 0, 1, 1));

  public VotingTimer(GUI gui)
  {

    this.getChildren().add(voteTimer);
  }

}
