package starvationevasion.client.MegaMawile2.controller.guiController;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import starvationevasion.client.MegaMawile2.controller.GameEngine;
import starvationevasion.common.EnumPolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by c on 1/17/2016.
 */
public class HandController
{
  @FXML
  public ComboBox regionsComboBox;
  @FXML
  public TextField regionHDI, regionMoney, regionPopulation, regionFoodProduced, regionFoodConsumed;
  @FXML
  public TextField cropField1, cropField2, cropField3, cropField4, cropField5, cropField6, cropField7, cropField8, cropField9, cropField10, cropField11, cropField12;
  @FXML
  public TextArea chatBox;
  @FXML
  public TextField chatField;
  @FXML
  ImageView cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven;

  private GameEngine gameEngine;

  private Map<ImageView, EnumPolicy> handMap;

  private EnumPolicy selectedCard;

  HandController(GameEngine gameEngine)
  {
    this.gameEngine = gameEngine;
    initHandMap();
  }

  private void initHandMap()
  {
    handMap = new HashMap<>();
    handMap.put(cardOne, null);
    handMap.put(cardTwo, null);
    handMap.put(cardThree, null);
    handMap.put(cardFour, null);
    handMap.put(cardFive, null);
    handMap.put(cardSix, null);
    handMap.put(cardSeven, null);
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
}
