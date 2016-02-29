package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Request;

public class CardHandler extends AbstractHandler
{
  public CardHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    return false;
  }
}
