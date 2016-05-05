package starvationevasion.client.GUI.DraftLayout;

import javafx.scene.layout.StackPane;
import starvationevasion.client.GUI.Clock;

/**
 * Class responsible for for displaying the Timer during the Drafting Phase
 */
public class DraftTimer extends StackPane
{
  public Clock draftTimer = new Clock();

  /**
   * Default constructor for the DraftTimer
   * Makes a Timer and puts it in the DraftTimer node
   */
  public DraftTimer()
  {
    this.getChildren().add(draftTimer);
  }

}
