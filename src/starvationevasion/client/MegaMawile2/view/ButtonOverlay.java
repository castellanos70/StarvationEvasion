package starvationevasion.client.MegaMawile2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import starvationevasion.client.MegaMawile2.controller.ButtonOverlayController;

import java.io.IOException;

/**
 * Created by c on 12/6/2015.
 */
public class ButtonOverlay
{
  private Pane buttonOverlayPane;

  public ButtonOverlay(ButtonOverlayController controller)
  {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("popups/CardButtons.fxml"));
    loader.setController(controller);
    try
    {
      buttonOverlayPane = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public Pane getButtonOverlayPane()
  {
    return buttonOverlayPane;
  }
}
