package starvationevasion.client.MegaMawile.controller;


import starvationevasion.client.MegaMawile.model.GameOptions;
import starvationevasion.client.MegaMawile.model.Player;
import starvationevasion.client.MegaMawile.model.GameStateData;

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
