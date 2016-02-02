package starvationevasion.client.controller;


import starvationevasion.client.model.GameOptions;
import starvationevasion.client.model.Player;
import starvationevasion.client.model.GameStateData;

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
