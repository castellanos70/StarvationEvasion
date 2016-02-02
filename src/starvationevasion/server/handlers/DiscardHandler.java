package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.Request;

public class DiscardHandler extends AbstractHandler
{
  public DiscardHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    return false;
  }
}
