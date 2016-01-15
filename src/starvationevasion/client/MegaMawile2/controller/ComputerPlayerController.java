package starvationevasion.client.MegaMawile2.controller;

import starvationevasion.client.MegaMawile2.model.GameOptions;
import starvationevasion.client.MegaMawile2.model.Player;
import starvationevasion.client.MegaMawile2.model.GameStateData;
import starvationevasion.server.ServerState;

/**
 * ComputerPlayerController updates an AI brain to make decisions based on the current round.
 */
public class ComputerPlayerController extends starvationevasion.client.MegaMawile2.controller.AbstractPlayerController
{

  /**
   * Creates a new ComputerPlayerController with access to the current {@link GameStateData} as well as necessary
   * {@link GameOptions} for connecting to a server.
   *
   * @param gameState the GameState to update based on (relevant phase/connectivity, etc is retrieved from GameState).
   * @param options the GameOptions to use when connecting this ComputerPlayerController to the server.
   * @param player the Player model to control.
   */
  public ComputerPlayerController(GameStateData gameState, GameOptions options, Player player)
  {
    super(gameState, player, options);
  }

  /**
   * Updates based upon current GameState - drafting/voting/etc.
   *
   * @param deltaTime current deltaTime from the game engine.
   */
  @Override
  public void update(float deltaTime)
  {
    super.update(deltaTime);
  }

}
