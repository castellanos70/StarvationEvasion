package starvationevasion.client.MegaMawile2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import starvationevasion.client.MegaMawile2.controller.ButtonOverlayController;

import java.io.IOException;

/**
 * Created by c on 12/7/2015.
 */
public class SliderOverlay
{
  private Pane sliderOverlayPane;

  public SliderOverlay(ButtonOverlayController controller)
  {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("popups/SliderOverlay.fxml"));
    loader.setController(controller);
    try
    {
      sliderOverlayPane = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public Pane getSliderOverlayPane()
  {
    return sliderOverlayPane;
  }
}
