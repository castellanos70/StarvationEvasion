package starvationevasion.server.handlers;

import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

public class VoteHandler extends AbstractHandler
{
  public VoteHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.VOTE_UP))
    {
      return true;
    }
    else if (request.getDestination().equals(Endpoint.VOTE_DOWN))
    {
      return true;
    }
    return false;
  }
}
