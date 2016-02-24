package starvationevasion.client.controller.guiController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by MohammadR on 2/3/2016.
 */
public class FrameController extends GuiController
{
  @FXML
  public StackPane mainStage;
  @FXML
  public Button backButton;

  public FrameController(FXMLLoader loader)
  {
    super(loader);
    this.loader.setController(this);
  }

  public void initialize()
  {
    setBackButtonEventHandler();
  }

  private void setBackButtonEventHandler()
  {
    throw new NotImplementedException();
  }

  public void putOnMainStage(Node node)
  {
    mainStage.getChildren().add(node);
  }

  public void popOffMainStage()
  {
    if (mainStage.getChildren().size() > 1) mainStage.getChildren().remove(mainStage.getChildren().size() - 1);
  }

  public void clearMainStage()
  {
    mainStage.getChildren().remove(1, mainStage.getChildren().size() - 1);
  }
}
