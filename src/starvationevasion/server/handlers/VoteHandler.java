package starvationevasion.server.handlers;

import starvationevasion.server.Worker;
import starvationevasion.server.Server;
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

    return false;
  }
}
