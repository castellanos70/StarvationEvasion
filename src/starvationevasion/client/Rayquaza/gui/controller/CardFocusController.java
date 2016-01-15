package starvationevasion.client.Rayquaza.gui.controller;

import javafx.scene.image.ImageView;
import starvationevasion.client.Rayquaza.engine.Engine;

/**
 * Created by c on 1/10/2016.
 */
public class CardFocusController
{
  Engine engine;
  ImageView currentCard;

  public void CardFocusController(Engine engine)
  {
    this.engine = engine;
  }

  public void setCurrentCard(ImageView imageView)
  {
    currentCard = imageView;
  }

  public ImageView getCurrentCard()
  {
    return currentCard;
  }
}
