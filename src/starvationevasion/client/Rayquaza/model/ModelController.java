package starvationevasion.client.Rayquaza.model;

import starvationevasion.client.Rayquaza.engine.Engine;

/**
 * Created by c on 1/10/2016.
 */
public class ModelController
{
  Engine engine;
  Model model;

  public ModelController(Engine engine)
  {
    this.engine = engine;
    model = new Model();
  }
}
