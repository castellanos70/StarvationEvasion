package starvationevasion.client.MegaMawile2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import starvationevasion.client.MegaMawile2.controller.ButtonOverlayController;

import java.io.IOException;

/**
 * Created by c on 12/8/2015.
 */
public class DropDownOverlay
{
 private Pane dropDownPane;

  public DropDownOverlay(ButtonOverlayController controller)
  {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("popups/DropdownOverlay.fxml"));
    loader.setController(controller);
    try
    {
      dropDownPane = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public Pane getDropDownPane()
  {
    return dropDownPane;
  }
}
