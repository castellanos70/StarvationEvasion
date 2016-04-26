package starvationevasion.communication.commands;

import starvationevasion.ai.AI;
import starvationevasion.communication.AITest;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.State;

public class GameState extends AbstractCommand
{

  private boolean sentReady = false;

  public GameState (AITest client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {

    if (this.getClient().getState() == null)
    {
      getClient().getCommModule().send(Endpoint.GAME_STATE, null, null);
      //getClient().send(new Request((double) System.currentTimeMillis(), Endpoint.GAME_STATE));
      return true;
    }

    if (!sentReady && getClient().getState().equals(State.LOGIN))
    {
      sentReady = true;
      getClient().getCommModule().send(Endpoint.READY, null, null);
      //getClient().send(new Request((double) System.currentTimeMillis(), Endpoint.READY));
      return true;
    }

    return false;

  }


}