package starvationevasion.client.controller.guiController;

import javafx.fxml.FXMLLoader;

/**
 * Created by MohammadR on 2/5/2016.
 */
public abstract class GuiController
{
  final protected FXMLLoader loader;
  public GuiController(FXMLLoader loader)
  {
    this.loader = loader;
  }
}
