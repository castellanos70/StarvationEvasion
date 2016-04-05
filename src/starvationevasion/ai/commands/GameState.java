package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.State;

public class GameState extends AbstractCommand
{

  private boolean sentReady = false;

  public GameState (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {

    if (this.getClient().getState() == null)
    {
      getClient().send(new Request((double) System.currentTimeMillis(), Endpoint.GAME_STATE));
      return true;
    }

    if (!sentReady && getClient().getState().equals(State.LOGIN))
    {
      sentReady = true;
      getClient().send(new Request((double) System.currentTimeMillis(), Endpoint.READY));
      return true;
    }

    if (getClient().getState().ordinal() > State.BEGINNING.ordinal())
    {
      getClient().getCommands().clear();
      getClient().getCommands().push(new Hand(getClient()));
      return false;
    }

    return false;

  }


}