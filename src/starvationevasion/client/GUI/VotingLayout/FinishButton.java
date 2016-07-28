package starvationevasion.client.GUI.VotingLayout;

import starvationevasion.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 * Created by jmweisburd on 12/1/15.
 */
public class FinishButton extends StackPane
{
  GUI gui;
  Button finishButton;

  public FinishButton(GUI gui)
  {
    this.gui = gui;

    finishButton = new Button("Finish Voting");
    finishButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        if(!gui.getTesting())gui.getClient().done();
        else gui.switchScenes();
      }
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().add(finishButton);
  }

}
