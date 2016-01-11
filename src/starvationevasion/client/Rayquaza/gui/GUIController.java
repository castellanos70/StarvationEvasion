package starvationevasion.client.Rayquaza.gui;

import starvationevasion.client.Rayquaza.engine.Engine;

/**
 * Controller to update the GUI. Communicates with Engine.
 */
public class GUIController
{
  Engine engine;
  GUI gui;

  public GUIController(Engine engine)
  {
    this.engine = engine;
    gui = new GUI();
  }
}
