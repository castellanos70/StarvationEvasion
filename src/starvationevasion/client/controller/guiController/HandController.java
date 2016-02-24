package starvationevasion.client.controller.guiController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import starvationevasion.client.controller.GameEngine;
import starvationevasion.common.EnumPolicy;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by c on 1/17/2016.
 */
public class HandController implements Initializable
{
  @FXML
  public ComboBox regionsComboBox;
  @FXML
  public TextField regionHDI, regionMoney, regionPopulation, regionFoodProduced, regionFoodConsumed;
  @FXML
  public TextField cropField1, cropField2, cropField3, cropField4, cropField5, cropField6, cropField7, cropField8, cropField9, cropField10, cropField11, cropField12;
  @FXML
  public TextFlow chatBox;
  @FXML
  public TextField chatField;
  @FXML
  ImageView cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven;

  private GameEngine gameEngine;

  private Map<ImageView, EnumPolicy> handMap;

  private EnumPolicy selectedCard;

  private void initHandMap()
  {
    handMap = new HashMap<ImageView, EnumPolicy>();
    handMap.put(cardOne, null);
    handMap.put(cardTwo, null);
    handMap.put(cardThree, null);
    handMap.put(cardFour, null);
    handMap.put(cardFive, null);
    handMap.put(cardSix, null);
    handMap.put(cardSeven, null);
  }

  private void initRegions()
  {
    ObservableList<String> regions =
      FXCollections.observableArrayList(
        "California",
        "Heartland",
        "Northern Plains",
        "Southeast",
        "Northern Crescent",
        "Southern Plains",
        "Mountain"
      );
    regionsComboBox.setItems(regions);
  }

  public void cardClicked(Event event)
  {
    ImageView card = (ImageView) event.getSource();
    selectedCard = handMap.get(card);
  }

  public EnumPolicy getSelectedCard()
  {
    return selectedCard;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    initHandMap();
    initRegions();
  }
}
