package starvationevasion.client.GUIOrig.DraftLayout;

import starvationevasion.client.GUIOrig.Timer;
import javafx.scene.layout.StackPane;

/**
 * Class responsible for for displaying the Timer during the Drafting Phase
 */
public class DraftTimer extends StackPane
{
  public static Timer draftTimer = new Timer("draft");

  /**
   * Default constructor for the DraftTimer
   * Makes a Timer and puts it in the DraftTimer node
   */
  public DraftTimer()
  {
    this.getChildren().add(draftTimer);
  }

}
