package starvationevasion.client.Aegislash.GUI.VotingLayout;

import starvationevasion.client.Aegislash.GUI.GUI;
import starvationevasion.client.Aegislash.GUI.Timer;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by jmweisburd on 12/1/15.
 */
public class VotingTimer extends StackPane
{
  GUI gui;
  Label temp;
  public static Timer voteTimer = new Timer("vote");

  public VotingTimer(GUI gui)
  {

    this.getChildren().add(voteTimer);
  }

}
