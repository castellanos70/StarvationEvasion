package starvationevasion.client.MegaMawile2.controller;


import starvationevasion.client.MegaMawile2.controller.AbstractPlayerController;
import starvationevasion.client.MegaMawile2.model.GameOptions;
import starvationevasion.client.MegaMawile2.model.Player;
import starvationevasion.client.MegaMawile2.model.GameStateData;

public class HumanPlayerController extends AbstractPlayerController
{

  public HumanPlayerController(GameStateData gameState, GameOptions options)
  {
    super(gameState, new Player(), options);
  }

  @Override
  public void update(float deltaTime)
  {
    super.update(deltaTime);
    // work with the client
    //client;
  }

}
