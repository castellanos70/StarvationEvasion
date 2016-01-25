package starvationevasion.server.handlers;

import starvationevasion.server.ClientWorker;
import starvationevasion.server.Request;
import starvationevasion.server.ServerMaster;
import starvationevasion.sim.Simulator;

public class VoteHandler extends AbstractHandler
{
  public VoteHandler (ServerMaster server, ClientWorker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    return false;
  }
}
