package starvationevasion.client.MegaMawile.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import starvationevasion.client.MegaMawile.model.GUIDraftDrawDiscard;
import starvationevasion.client.MegaMawile.view.ButtonOverlay;
import starvationevasion.client.MegaMawile.view.DropDownOverlay;
import starvationevasion.client.MegaMawile.view.SliderOverlay;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by chris wu on 12/6/2015.
 */
public class ButtonOverlayController implements Initializable, GameController
{
  private final ButtonOverlay buttonOverlay;
  private final SliderOverlay sliderOverlay;
  private final DropDownOverlay dropDownOverlay;
  private final GameEngine gameEngine;

  private PolicyCard current;

  private final GUIDraftDrawDiscard guiDraftDrawDiscard;

  /**
   * FXML injector fields used in conjunction with the SliderOverlay and the DropdownOverlay
   */
  @FXML
  private Button draftButton, discardDraftButton, discardButton, cancelButton, lockinButtonSlider, cancelButtonSlider, toDropdown;
  @FXML
  private Button lockinButtonDropdown, cancelButtonDropdown, toSlider;
  @FXML
  private Slider xSlider, ySlider, zSlider;
  @FXML
  private ComboBox regionBox, foodBox;

  public ButtonOverlayController(GUIDraftDrawDiscard guiDraftDrawDiscard, GameEngine gameEngine)
  {
    this.guiDraftDrawDiscard = guiDraftDrawDiscard;
    this.gameEngine = gameEngine;
    buttonOverlay = new ButtonOverlay(this);
    sliderOverlay = new SliderOverlay(this);
    dropDownOverlay = new DropDownOverlay(this);
  }

  @Override
  public void update(float deltaTime)
  {

  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {

  }

  /**
   * ActionEvent which is linked to a button on the first overlay that appears on a card
   * when clicked.
   *
   * @param event
   */
  @FXML
  protected void draftCard(ActionEvent event)
  {
    StackPane sp = (StackPane) buttonOverlay.getButtonOverlayPane().getParent();
    sp.getChildren().remove(1);
    sp.getChildren().add(sliderOverlay.getSliderOverlayPane());
    setSlidersToEnabled();
    populateTargets();
//    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.NONE) guiDraftDrawDiscard.setCurrentMove(GUIDraftDrawDiscard.MoveType.DRAFT);
//    if(guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.DRAFT) guiDraftDrawDiscard.addToActionTable(current);
  }

  /**
   * Checks to see which variables; X, Y, and or Z are needed for the given PolicyCard and sets
   * the corresponding sliders as either enabled or disabled.
   */
  private void setSlidersToEnabled()
  {
    PolicyCard.EnumVariableUnit testX = current.getRequiredVariables(PolicyCard.EnumVariable.X);
    PolicyCard.EnumVariableUnit testY = current.getRequiredVariables(PolicyCard.EnumVariable.Y);
    PolicyCard.EnumVariableUnit testZ = current.getRequiredVariables(PolicyCard.EnumVariable.Z);
    if (testX != null)
    {
      xSlider.setDisable(false);
      switch (testX)
      {
        case PERCENT:
          xSlider.setMin(PolicyCard.MIN_PERCENT);
          xSlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case MILLION_DOLLAR:
          xSlider.setMin(PolicyCard.MIN_PERCENT);
          xSlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case UNIT:
          xSlider.setMin(2);
          xSlider.setMax(7);
          xSlider.setSnapToTicks(true);
          xSlider.setMajorTickUnit(5);
          break;
      }
    }
    else
    {
      xSlider.setDisable(true);
    }
    if (testY != null)
    {
      ySlider.setDisable(false);
      switch (testY)
      {
        case PERCENT:
          ySlider.setMin(PolicyCard.MIN_PERCENT);
          ySlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case MILLION_DOLLAR:
          ySlider.setMin(PolicyCard.MIN_PERCENT);
          ySlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case UNIT:
          ySlider.setMin(2);
          ySlider.setMax(7);
          ySlider.setSnapToTicks(true);
          ySlider.setMajorTickUnit(5);
          break;
      }
    }
    else
    {
      ySlider.setDisable(true);
    }
    if (testZ != null)
    {
      zSlider.setDisable(false);
      switch (testZ)
      {
        case PERCENT:
          zSlider.setMin(PolicyCard.MIN_PERCENT);
          zSlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case MILLION_DOLLAR:
          zSlider.setMin(PolicyCard.MIN_PERCENT);
          zSlider.setMax(PolicyCard.MAX_PERCENT);
          break;
        case UNIT:
          zSlider.setMin(2);
          zSlider.setMax(7);
          zSlider.setSnapToTicks(true);
          zSlider.setMajorTickUnit(5);
          break;
      }
    }
    else
    {
      zSlider.setDisable(true);
    }
  }

  private void populateTargets()
  {
    EnumRegion[] regions = current.getValidTargetRegions();
    EnumFood[] foods = current.getValidTargetFoods();
    if (regions != null) regionBox.setItems(new ObservableListWrapper(Arrays.asList(regions)));
    else regionBox.setDisable(true);
    if (foods != null) foodBox.setItems(new ObservableListWrapper(Arrays.asList(foods)));
    else foodBox.setDisable(true);
  }

  /**
   * AbstractHandler to discard and draw cards.
   *
   * @param event
   */
  @FXML
  protected void discardDrawCard(ActionEvent event)
  {
    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.NONE) guiDraftDrawDiscard.setCurrentMove(GUIDraftDrawDiscard.MoveType.DISCARDDRAW);
    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.DISCARDDRAW) guiDraftDrawDiscard.addToActionTable(current);
  }

  /**
   * AbstractHandler used to just discard a card.
   *
   * @param event
   */
  @FXML
  protected void discardCard(ActionEvent event)
  {
    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.NONE) guiDraftDrawDiscard.setCurrentMove(GUIDraftDrawDiscard.MoveType.DISCARD);
    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.DISCARD) guiDraftDrawDiscard.addToActionTable(current);
  }

  /**
   * AbstractHandler to cancel a selected card.
   *
   * @param event
   */
  @FXML
  protected void cancelCard(ActionEvent event)
  {
    StackPane sp = (StackPane) buttonOverlay.getButtonOverlayPane().getParent();
    sp.getChildren().remove(1);
  }

  /**
   * AbstractHandler used to submit selected values for the given card before passing it to the
   * server.
   *
   * @param event
   */
  @FXML
  protected void submitDraft(ActionEvent event)
  {
    if (!xSlider.isDisabled()) current.setX((int) xSlider.getValue());
    if (!ySlider.isDisabled()) current.setY((int) ySlider.getValue());
    if (!zSlider.isDisabled()) current.setZ((int) zSlider.getValue());

    if (current.getValidTargetFoods() != null) current.setTargetFood((EnumFood) foodBox.getValue());
    if (current.getValidTargetRegions() != null) current.setTargetRegion((EnumRegion) regionBox.getValue());

    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.NONE) guiDraftDrawDiscard.setCurrentMove(GUIDraftDrawDiscard.MoveType.DRAFT);
    if (guiDraftDrawDiscard.getCurrentMove() == GUIDraftDrawDiscard.MoveType.DRAFT) guiDraftDrawDiscard.addToActionTable(current);

    if (sliderOverlay.getSliderOverlayPane().getParent() != null)
    {
      removeSliderOverlay();
    }
    if (dropDownOverlay.getDropDownPane().getParent() != null)
    {
      removeDropdownOverlay();
    }
  }

  /**
   * AbstractHandler used to cancel the draft for the slider overlay.
   *
   * @param event
   */
  @FXML
  protected void cancelDraftSlider(ActionEvent event)
  {
    removeSliderOverlay();
  }

  /**
   * AbstractHandler used to cancel the draft for the dropdown overlay.
   *
   * @param event
   */
  @FXML
  protected void cancelDraftDropdown(ActionEvent event)
  {
    removeDropdownOverlay();
  }

  /**
   * AbstractHandler used to switch from the slider to the dropdown overlay.
   *
   * @param event
   */
  @FXML
  protected void switchToDropdown(ActionEvent event)
  {
    StackPane sp = (StackPane) sliderOverlay.getSliderOverlayPane().getParent();
    sp.getChildren().remove(1);
    sp.getChildren().add(dropDownOverlay.getDropDownPane());
  }

  /**
   * AbstractHandler used to switch from the dropdown to the slider overlay.
   *
   * @param event
   */
  @FXML
  protected void switchToSlider(ActionEvent event)
  {
    StackPane sp = (StackPane) dropDownOverlay.getDropDownPane().getParent();
    sp.getChildren().remove(1);
    sp.getChildren().add(sliderOverlay.getSliderOverlayPane());
  }

  private void removeSliderOverlay()
  {
    StackPane sp = (StackPane) sliderOverlay.getSliderOverlayPane().getParent();
    sp.getChildren().remove(1);
  }

  private void removeDropdownOverlay()
  {
    StackPane sp = (StackPane) dropDownOverlay.getDropDownPane().getParent();
    sp.getChildren().remove(1);
  }

  /**
   * Getter for the Pane of the given overlay.
   *
   * @return Pane view
   */
  public Pane getView()
  {
    return buttonOverlay.getButtonOverlayPane();
  }

  /**
   * Sets the current PolicyCard.
   *
   * @param current
   */
  public void setCurrent(PolicyCard current)
  {
    this.current = current;
  }

  /**
   * Gets the currently selected PolicyCard.
   *
   * @return PolicyCard current
   */
  public PolicyCard getCurrent()
  {
    return current;
  }
}
