package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.State;

public class GameState extends AbstractCommand
{

  private boolean sentReady = false;

  public GameState(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
  {

    if (this.getClient().getState() == null)
    {
      getClient().getCommModule().send(Endpoint.GAME_STATE, null, null);
      // getClient().send(new Request((double) System.currentTimeMillis(),
      // Endpoint.GAME_STATE));
      return true;
    }

    if (!sentReady && getClient().getState().equals(State.LOGIN))
    {
      sentReady = true;
      getClient().getCommModule().send(Endpoint.READY, null, null);
      // getClient().send(new Request((double) System.currentTimeMillis(),
      // Endpoint.READY));
      return true;
    }

    return false;

  }

  @Override
  public String commandString()
  {
    // TODO Auto-generated method stub
    return "GameState";
  }

}
