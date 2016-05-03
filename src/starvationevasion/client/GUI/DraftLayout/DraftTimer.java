package starvationevasion.client.GUI.DraftLayout;

import starvationevasion.client.GUI.Clock;
import starvationevasion.client.GUI.ResizeStrategy;
import javafx.scene.layout.StackPane;

/**
 * Class responsible for for displaying the Timer during the Drafting Phase
 */
public class DraftTimer extends StackPane
{
  public Clock draftTimer = new Clock(this, new ResizeStrategy(0, 0, 1, 1));

  /**
   * Default constructor for the DraftTimer
   * Makes a Timer and puts it in the DraftTimer node
   */
  public DraftTimer()
  {
    this.getChildren().add(draftTimer);
  }

}
